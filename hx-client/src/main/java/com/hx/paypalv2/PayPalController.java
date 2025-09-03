package com.hx.paypalv2;


import com.hx.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author zhang jiagu
 * @date 2025/9/3
 **/
// 3. 控制器类

@RestController
@RequestMapping("/api/paypal")
@Slf4j
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    /**
     * 创建支付订单
     */
    @PostMapping("/create-order")
    public ResponseEntity<PayPalOrderResponse> createOrder(@RequestBody PayPalOrderRequest request) {
        try {
            PayPalOrderResponse response = payPalService.createOrder(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("创建PayPal订单失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PayPalOrderResponse.builder()
                            .orderId(null)
                            .status("ERROR")
                            .approvalUrl(null)
                            .build());
        }
    }

    /**
     * 支付成功回调
     */
    @GetMapping("/success")
    public ResponseEntity<PayPalCallbackResponse> paymentSuccess(
            @RequestParam String orderId,
            @RequestParam(required = false) String PayerID) {
        try {
            // 捕获订单支付
            PayPalCaptureResponse capture = payPalService.captureOrder(orderId);

            log.info("PayPal支付成功，订单ID: {}, 捕获ID: {}",
                    capture.getOrderId(), capture.getCaptureId());

            return ResponseEntity.ok(PayPalCallbackResponse.builder()
                    .success(true)
                    .message("支付成功")
                    .orderId(capture.getOrderId())
                    .captureId(capture.getCaptureId())
                    .amount(capture.getAmount())
                    .currency(capture.getCurrency())
                    .build());

        } catch (Exception e) {
            log.error("处理PayPal支付成功回调失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PayPalCallbackResponse.builder()
                            .success(false)
                            .message("处理支付回调失败: " + e.getMessage())
                            .build());
        }
    }

    /**
     * 支付取消回调
     */
    @GetMapping("/cancel")
    public ResponseEntity<PayPalCallbackResponse> paymentCancel(@RequestParam String token) {
        log.info("PayPal支付已取消，订单ID: {}", token);
        return ResponseEntity.ok(PayPalCallbackResponse.builder()
                .success(false)
                .message("支付已取消")
                .orderId(token)
                .build());
    }

    /**
     * Webhook回调处理
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                HttpServletRequest request) {
        try {
            // 获取请求头
            Map<String, String> headers = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.put(headerName.toUpperCase(), request.getHeader(headerName));
            }

            // 验证Webhook签名
            if (!payPalService.verifyWebhookSignature(payload, headers)) {
                log.warn("PayPal Webhook签名验证失败");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("签名验证失败");
            }

            // 解析Webhook事件
            JSONObject eventJson = new JSONObject(payload);
            String eventType = eventJson.getString("event_type");

            log.info("收到PayPal Webhook事件: {}", eventType);

            // 处理不同类型的事件
            switch (eventType) {
                case "PAYMENT.CAPTURE.COMPLETED":
                    handlePaymentCaptured(eventJson);
                    break;
                case "PAYMENT.CAPTURE.DENIED":
                    handlePaymentDenied(eventJson);
                    break;
                case "PAYMENT.CAPTURE.REFUNDED":
                    handlePaymentRefunded(eventJson);
                    break;
                case "CHECKOUT.ORDER.APPROVED":
                    handleOrderApproved(eventJson);
                    break;
                default:
                    log.info("未处理的PayPal事件类型: {}", eventType);
            }

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            log.error("处理PayPal Webhook失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("处理失败");
        }
    }

    /**
     * 退款接口
     */
    @PostMapping("/refund")
    public ResponseEntity<PayPalRefundResponse> refundPayment(@RequestBody PayPalRefundRequest request) {
        try {
            PayPalRefundResponse response = payPalService.refundPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("PayPal退款失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PayPalRefundResponse.builder()
                            .refundId(null)
                            .status("ERROR")
                            .build());
        }
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PayPalOrderDetails> getOrderDetails(@PathVariable String orderId) {
        try {
            PayPalOrderDetails details = payPalService.getOrderDetails(orderId);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            log.error("获取订单详情失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Webhook事件处理方法
    private void handlePaymentCaptured(JSONObject eventJson) {
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

        // 更新订单状态为已支付
        // orderService.updateOrderStatus(orderId, OrderStatus.PAID);
    }

    private void handlePaymentDenied(JSONObject eventJson) {
        JSONObject resource = eventJson.getJSONObject("resource");
        String captureId = resource.getString("id");

        log.warn("PayPal支付被拒绝 - 捕获ID: {}", captureId);

        // 更新订单状态为支付失败
        // orderService.updateOrderStatus(orderId, OrderStatus.PAYMENT_FAILED);
    }

    private void handlePaymentRefunded(JSONObject eventJson) {
        JSONObject resource = eventJson.getJSONObject("resource");
        String refundId = resource.getString("id");

        log.info("PayPal退款完成 - 退款ID: {}", refundId);

        // 更新订单状态为已退款
        // orderService.updateOrderStatus(orderId, OrderStatus.REFUNDED);
    }

    private void handleOrderApproved(JSONObject eventJson) {
        JSONObject resource = eventJson.getJSONObject("resource");
        String orderId = resource.getString("id");

        log.info("PayPal订单已批准 - 订单ID: {}", orderId);

        // 可以在这里更新订单状态或执行其他业务逻辑
        // orderService.updateOrderStatus(orderId, OrderStatus.APPROVED);
    }
}
