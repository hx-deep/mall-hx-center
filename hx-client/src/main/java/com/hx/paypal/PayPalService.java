//package com.hx.paypal;
//
//import cn.hutool.core.lang.UUID;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Scanner;
//
///**
// * TODO
// *
// * @author zhang jiagu
// * @date 2025/9/3
// **/
//
//public class PayPalService {
//
//
//    @Test
//    public void order() throws IOException {
//        URL url = new URL("https://api-m.sandbox.paypal.com/v2/checkout/orders");
//        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//        httpConn.setRequestMethod("POST");
//
//        httpConn.setRequestProperty("Content-Type", "application/json");
//        httpConn.setRequestProperty("PayPal-Request-Id", "7b92603e-77ed-4896-8e78-5dea2050476kk");
//        // 设置 accessToken
//        httpConn.setRequestProperty("Authorization", "Bearer A21AAKn5cnLgrxyUuxaQiGeuuA1aQWPZuxyZLBJbXK04-BNm0FJIJZjXef_VFumB9RLCVurmrPh9DXn2KaLwVEau7NqsLTcNA");
//
//        // 金额
//        JSONObject amount = new JSONObject();
//        amount.put("currency_code", "USD");
//        amount.put("value", "10.00");
//
//        JSONObject address = new JSONObject();
//        address.put("address_line_1", "虹桥镇");
//        address.put("address_line_2", "茅台路168号");
//        address.put("admin_area_1", "上海市");
//        address.put("admin_area_2", "闵行区");
//        address.put("postal_code", "20000");
//        address.put("country_code", "CN");
//
//        // 客户姓名
//        JSONObject name = new JSONObject();
//        name.put("full_name", "Jacky");
//        // 客户信息机地址
//        JSONObject shipping = new JSONObject();
//        shipping.put("name", name);
//        shipping.put("address", address);
//
//        // 购买人信息，数组，最多可以同时传10个
//        JSONObject purchaseUnit = new JSONObject();
//        purchaseUnit.put("reference_id", UUID.randomUUID().toString());
//        purchaseUnit.put("custom_id", UUID.randomUUID().toString());
//        purchaseUnit.put("amount", amount);
//        purchaseUnit.put("description", "测试支付");
//        purchaseUnit.put("shipping", shipping);
//        JSONArray puprchase_units = new JSONArray();
//        puprchase_units.add(purchaseUnit);
//        // 订单上下文信息，取消地址、返回地址设置
//        JSONObject applicationContext = new JSONObject();
//        applicationContext.put("cancel_url", "http://localhost/paypalv2/cancel");
//        applicationContext.put("return_url", "http://localhost/paypalv2/back");
//        JSONObject json = new JSONObject();
//        json.put("intent", "CAPTURE");// 用户付款了，商户立即收款
//        json.put("purchase_units", puprchase_units);
//        json.put("application_context", applicationContext);
//        httpConn.setDoOutput(true);
//        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
//        writer.write(json.toJSONString());
//        writer.flush();
//        writer.close();
//        httpConn.getOutputStream().close();
//
//        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
//                ? httpConn.getInputStream()
//                : httpConn.getErrorStream();
//        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
//        String response = s.hasNext() ? s.next() : "";
//        System.out.println(response);
//    }
//}
