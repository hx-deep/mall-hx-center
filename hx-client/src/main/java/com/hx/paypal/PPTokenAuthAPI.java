//package com.hx.paypal;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.http.HttpResponse;
//import cn.hutool.http.HttpUtil;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.binary.Base64;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * TODO
// *
// * @author zhang jiagu
// * @date 2025/9/2
// **/
//@Slf4j
//@Service
//public class PPTokenAuthAPI {
//
//
//    public static final String clientId = "ARZD1uMKTYNi85tBF4uIj-4Chlc0ehvn5vcKS4O2tXjYY84J8_Hs_Stg9iyak1XNJ--IhH5BWE1tN8jh";
//
//
//    public static final String clientSecret = "EO3TZBT0jXoYs2d5rPzP82foLwou0QO0DGLpkPAitQJ1Gd6RSumP6rOSibLQOa_Jj_nzvKEhoF3Yq13F";
//
//    public static final String token_url = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
//
//    public static String body = "grant_type=client_credentials";
//
//    public static String accessToken = "A21AALTnd35nnCB9Y1uGh_9v-Fv5gB5Vp8wKT6OZeJmsAa2AAm_h6MBwx5_gGs87tYAiqaaVfFaFTVwzEnWLO3o59CAPO47TQ";
//
//    public static String create_order_url = "https://api-m.sandbox.paypal.com/v2/checkout/orders";
//
//
//    public  String buildParam(String clientId, String clientSecret) {
//        String auth = clientId + ":" + clientSecret;
//        String encodedAuth = Base64.encodeBase64String(auth.getBytes());
//        return encodedAuth;
//    }
//
//
//
//    public  String execute(String encodeAuth){
//        HttpResponse response = HttpUtil.createPost(token_url)
//                .body(body)
//                .header("Authorization", "Basic " + encodeAuth)
//                .contentType("application/x-www-form-urlencoded")
//                .execute();
//        log.info("Authorization response: " + response.body());
//        return response.body();
//
//    }
//
//
//    public  Map<String, String> createRequestHeaders(String requestId, String accessToken) {
//        Map<String, String> headerMap = new HashMap<>();
//        headerMap.put("Authorization", "Bearer " + accessToken);
//        headerMap.put("Content-Type", "application/json");
//        if (!StrUtil.isEmpty(requestId)) {
//            headerMap.put("PayPal-Request-Id", requestId);
//        }
//        return headerMap;
//    }
//
//
//    public  String buildCreateOrderParam(JSONObject jsonObject) {
//        jsonObject.put("intent", "CAPTURE");
//        JSONArray jsonArray = new JSONArray();
//        JSONObject amount = new JSONObject();
//        amount.put("currency_code", "USD");
//        amount.put("value", "100.00");
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("amount", amount);
//        jsonArray.add(jsonObject1);
//        jsonObject.put("purchase_units", jsonArray);
//        log.info("获取到的json:{}", jsonObject.toJSONString());
//        return jsonObject.toJSONString();
//    }
//
//    public  String executeCreateOrder(String json,Map header) {
//        log.info("获取到请求头参数：{}", header.toString());
//        HttpResponse execute = HttpUtil.createPost(create_order_url).addHeaders(header).body(json).execute();
//        log.info("创建订单返回：{}", execute.body());
//        return execute.body();
//    }
//
//
//
//
////    public static void main(String[] args) {
////        String encodeAuth  = buildParam(clientId, clientSecret);
////        String execute = execute(encodeAuth, encodeAuth);
////        String body = buildCreateOrderParam(new JSONObject());
////        Map<String, String> headers = createRequestHeaders(null, accessToken);
////        String s = executeCreateOrder(body, headers);
////    }
//
//}
