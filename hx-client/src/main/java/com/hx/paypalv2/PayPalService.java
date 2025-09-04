package com.hx.paypalv2;

import com.alibaba.fastjson.JSON;
import com.hx.domain.*;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.payments.CapturesRefundRequest;
import com.paypal.payments.RefundRequest;
import com.paypal.payments.Refund;
import com.paypal.payments.Money;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author zhang jiagu
 * @date 2025/9/3
 **/
//2. PayPal服务类
@Service
@Slf4j
public class PayPalService {

    @Autowired
    private PayPalHttpClient payPalHttpClient;

    @Autowired
    private PayPalConfigs payPalConfig;

    /**
     * 创建订单
     */
    public PayPalOrderResponse createOrder(PayPalOrderRequest request) {
        try {
            // 构建订单请求
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.checkoutPaymentIntent("CAPTURE");// 用户付款了，商户立即收款

            // 设置金额
            AmountWithBreakdown amountBreakdown = new AmountWithBreakdown()
                    .currencyCode(request.getCurrency())
                    .value(request.getAmount().toString());

            // 设置购买单元
            PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                    .referenceId(request.getOrderId())
                    .description(request.getDescription())
                    .customId(request.getUserId())
                    .softDescriptor(request.getProductName())
                    .amountWithBreakdown(amountBreakdown);

            orderRequest.purchaseUnits(Arrays.asList(purchaseUnitRequest));

            // 设置应用上下文（回调URL等）
            ApplicationContext applicationContext = new ApplicationContext()
                    .brandName(request.getBrandName())
                    .locale("zh-CN")
                    .landingPage("BILLING")
                    .shippingPreference("NO_SHIPPING")
                    .userAction("PAY_NOW")
                    .returnUrl(request.getReturnUrl())
                    .cancelUrl(request.getCancelUrl());

            orderRequest.applicationContext(applicationContext);

            // 创建订单请求
            OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest();
            ordersCreateRequest.prefer("return=representation");
            ordersCreateRequest.requestBody(orderRequest);

            // 执行请求
            HttpResponse<Order> response = payPalHttpClient.execute(ordersCreateRequest);
            Order order = response.result();

            // 获取支付链接
            String approvalUrl = order.links().stream()
                    .filter(link -> "approve".equals(link.rel()))
                    .map(LinkDescription::href)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("未找到PayPal支付链接"));

            return PayPalOrderResponse.builder()
                    .orderId(order.id())
                    .status(order.status())
                    .approvalUrl(approvalUrl)
                    .build();

        } catch (Exception e) {
            log.error("创建PayPal订单失败", e);
            throw new RuntimeException("创建PayPal订单失败: " + e.getMessage());
        }
    }

    /**
     * 用户授权支付成功，进行扣款操作
     *      用户通过CreateOrder生成 approveUrl 跳转paypal支付成功后，只是授权，并没有将用户的钱打入我们的paypal账户，
     *      我们需要通过 CaptureOrder接口，将钱打入我的PayPal账户
     *
     */
    public PayPalCaptureResponse captureOrder(String orderId) {
        try {
            OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
            request.prefer("return=representation");
            request.requestBody(new OrderActionRequest());

            HttpResponse<Order> response = payPalHttpClient.execute(request);
            Order order = response.result();

            // 获取捕获详情
            Capture capture = order.purchaseUnits().get(0)
                    .payments().captures().get(0);

            return PayPalCaptureResponse.builder()
                    .orderId(order.id())
                    .captureId(capture.id())
                    .status(capture.status())
                    .amount(new BigDecimal(capture.amount().value()))
                    .currency(capture.amount().currencyCode())
                    .createTime(capture.createTime())
                    .build();

        } catch (Exception e) {
            log.error("捕获PayPal订单支付失败, orderId: {}", orderId, e);
            throw new RuntimeException("捕获PayPal订单支付失败: " + e.getMessage());
        }
    }

    /**
     * 退款
     */
    public PayPalRefundResponse refundPayment(PayPalRefundRequest request) {
        try {
            CapturesRefundRequest refundRequest = new CapturesRefundRequest(request.getCaptureId());

            Money money = new Money()
                    .currencyCode(request.getCurrency()).value(request.getAmount().toString());

            RefundRequest refundBody = new RefundRequest()
                    .amount(money)
                    .noteToPayer(request.getNote());

            refundRequest.requestBody(refundBody);
            refundRequest.prefer("return=representation");

            HttpResponse<Refund> response = payPalHttpClient.execute(refundRequest);
            Refund refund = response.result();

            return PayPalRefundResponse.builder()
                    .refundId(refund.id())
                    .status(refund.status())
                    .amount(new BigDecimal(refund.amount().value()))
                    .currency(refund.amount().currencyCode())
                    .createTime(refund.createTime())
                    .build();

        } catch (Exception e) {
            log.error("PayPal退款失败, captureId: {}", request.getCaptureId(), e);
            throw new RuntimeException("PayPal退款失败: " + e.getMessage());
        }
    }

    /**
     * 验证Webhook签名
     */
    public boolean verifyWebhookSignature(String payload, Map<String, String> headers) {
        try {
            log.info("webhook验签请求头信息：{}", headers.toString());
            String transmissionId = headers.get("PAYPAL-TRANSMISSION-ID");
            String certId = headers.get("PAYPAL-CERT-ID");
            String transmissionSig = headers.get("PAYPAL-TRANSMISSION-SIG");
            String transmissionTime = headers.get("PAYPAL-TRANSMISSION-TIME");
            String authAlgo = headers.get("PAYPAL-AUTH-ALGO");

            if (transmissionId == null || certId == null || transmissionSig == null
                    || transmissionTime == null || authAlgo == null) {
                log.warn("缺少必要的PayPal Webhook验证头信息");
                return false;
            }

            // 构建验证字符串
            String expectedSig = buildExpectedSignature(
                    transmissionId, transmissionTime, payPalConfig.getWebhookId(),
                    payload, certId, authAlgo
            );

            // 验证签名
            return verifyWebhookSignature(payload,transmissionId,transmissionTime,certId,authAlgo,expectedSig);
//            return verifySignature(transmissionSig, expectedSig, authAlgo);

        } catch (Exception e) {
            log.error("验证PayPal Webhook签名失败", e);
            return false;
        }
    }


    // 3. Webhook 签名验证
    private boolean verifyWebhookSignature(String payload, String transmissionId, String transmissionTime,
                                           String certUrl, String authAlgo, String transmissionSig) throws Exception {
        String expectedSignature = calculateSignature(payload, transmissionId, transmissionTime, payPalConfig.getWebhookId());
        URL url = new URL(certUrl);
        String certContent = new BufferedReader(new InputStreamReader(url.openStream())).lines().collect(Collectors.joining("\n"));

        // 加载 PayPal 证书
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certContent.getBytes(StandardCharsets.UTF_8)));

        // 验证签名
        Signature sig = Signature.getInstance(authAlgo);
        sig.initVerify(cert.getPublicKey());
        sig.update((transmissionId + "|" + transmissionTime + "|" + payPalConfig.getWebhookId() + "|" + payload).getBytes(StandardCharsets.UTF_8));
        return sig.verify(Base64.getDecoder().decode(transmissionSig));
    }

    private String calculateSignature(String payload, String transmissionId, String transmissionTime, String webhookId) throws Exception {
        String data = transmissionId + "|" + transmissionTime + "|" + webhookId + "|" + payload;
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(payPalConfig.getClientSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    private String buildExpectedSignature(String transmissionId, String transmissionTime,
                                          String webhookId, String payload, String certId,
                                          String authAlgo) {
        // 构建待签名字符串
        String signatureString = transmissionId + "|" + transmissionTime + "|" +
                webhookId + "|" + Base64.getEncoder().encodeToString(payload.getBytes());

        return signatureString;
    }

    private boolean verifySignature(String receivedSig, String expectedSig, String authAlgo) {
        try {
            // 这里需要获取PayPal的公钥来验证签名
            // 实际项目中需要从PayPal获取证书并验证
            // 简化版本：比较字符串（不推荐生产环境使用）
            log.info("验证签名 - 接收到的签名: {}", receivedSig);
            log.info("预期签名: {}", expectedSig);

            // TODO: 实现真正的RSA签名验证
            return true; // 临时返回，实际需要实现RSA验证

        } catch (Exception e) {
            log.error("签名验证失败", e);
            return false;
        }
    }

    /**
     * 获取订单详情
     */
    public PayPalOrderDetails getOrderDetails(String orderId) {
        try {
            OrdersGetRequest request = new OrdersGetRequest(orderId);
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            Order order = response.result();

            return PayPalOrderDetails.builder()
                    .orderId(order.id())
                    .status(order.status())
                    .createTime(order.createTime())
                    .updateTime(order.updateTime())
                    .intent(order.checkoutPaymentIntent())
                    .build();

        } catch (Exception e) {
            log.error("获取PayPal订单详情失败, orderId: {}", orderId, e);
            throw new RuntimeException("获取PayPal订单详情失败: " + e.getMessage());
        }
    }

    /**
     *  买家批准了这个订单：商户需要调用捕获api,将钱打入我的PayPal账户
     * @param eventJson
     */
    public void handleOrderApproved(JSONObject eventJson) {
        log.info("买家批准了这个订单支付回调信息：{}", eventJson);
        JSONObject resource = eventJson.getJSONObject("resource");
        String orderId = resource.getString("id");
        //捕获订单支付
        PayPalCaptureResponse payPalCaptureResponse = captureOrder(orderId);

        log.info("PayPal订单已批准 - 订单ID: {},捕获订单支付详情：{}", orderId, JSON.toJSONString(payPalCaptureResponse));
        //需要在订单表中存储这个id,为后续的退款做准备
        String captureId = payPalCaptureResponse.getCaptureId();

        // 可以在这里更新订单状态或执行其他业务逻辑
        // orderService.updateOrderStatus(orderId, OrderStatus.APPROVED);
    }




    // Webhook事件处理方法
    private void handlePaymentCaptured(JSONObject eventJson) {
        log.info("支付成功回调信息：{}", eventJson.toString());
        JSONObject resource = eventJson.getJSONObject("resource");
        String captureId = resource.getString("id");

        // 从supplementary_data中获取order_id（如果存在）
        String orderId = null;
        if (resource.has("supplementary_data") &&
                resource.getJSONObject("supplementary_data").has("related_ids") &&
                resource.getJSONObject("supplementary_data").getJSONObject("related_ids").has("order_id")) {
            orderId = resource.getJSONObject("supplementary_data")
                    .getJSONObject("related_ids").getString("order_id");
        }
        log.info("PayPal支付完成 - 订单ID: {}, 捕获ID: {}", orderId, captureId);
        PayPalOrderDetails orderDetails = getOrderDetails(orderId);
        log.info("获取的订单详情：{}", JSON.toJSONString(orderDetails));

        // 更新订单状态为已支付
        // orderService.updateOrderStatus(orderId, OrderStatus.PAID);
    }
}

