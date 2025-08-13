//package com.hx.service;
//
//import com.alibaba.fastjson.JSONObject;
//import io.grpc.stub.StreamObserver;
//import lombok.extern.slf4j.Slf4j;
//import net.devh.boot.grpc.server.service.GrpcService;
//import com.hx.proto.SendInfoServiceGrpc;
//import com.hx.proto.ReqProto;
//import com.hx.proto.ResponseMsgDto;
///**
// * @Package: com.hx.service
// * @ClassName: InvokeMessage.java
// * @Description:
// * @author: zjg
// * @date: 2024/7/18
// */
//@Slf4j
//@GrpcService
//public class InvokeMessage extends SendInfoServiceGrpc.SendInfoServiceImplBase {
//
//
//    @Override
//    public void getDateInfo(ReqProto reqProto, StreamObserver<ResponseMsgDto> responseObserver){
//        String applyCode = reqProto.getApplyCode();
//        int stockCode = reqProto.getStockCode();
//        int market = reqProto.getMarket();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("applyCode",applyCode);
//        jsonObject.put("stockCode",stockCode);
//        jsonObject.put("market",market);
//        log.info("grpc远程过程调用参数：{}",jsonObject.toJSONString());
//
//        ResponseMsgDto.Builder builder = ResponseMsgDto.newBuilder();
//        builder.setStartDate(jsonObject.toJSONString());
//        builder.setListType(1);
//        responseObserver.onNext(builder.build());
//        responseObserver.onCompleted();
//    }
//
//}