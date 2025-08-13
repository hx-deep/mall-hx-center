package com.hx.service;

import com.alibaba.fastjson.JSON;
import com.hx.dto.CountDto;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class StreamMain {

    public final String tempDir;

    public StreamMain(String tempDir) {
        this.tempDir = tempDir;
    }

    public static void main(String[] args) {
//        String dirName = "diadddd";
//        String suffix = "pdf";
//        String savePath = String.join(File.separator, null, dirName, dirName + "." + suffix);
//        // 创建多个CompletableFuture
//        System.out.println(savePath);
        String ossurl = "https://hw-scanner-test.oss-cn-beijing.aliyuncs.com/91534ce2c99968487e328bed42c2514.jpg?x-oss-credential=LTAI5tCD52Qh7KCj6f5dp2ia%2F20250804%2Fcn-beijing%2Foss%2Faliyun_v4_request&x-oss-date=20250804T021722Z&x-oss-expires=3600&x-oss-signature-version=OSS4-HMAC-SHA256&x-oss-signature=35d99d96e71f1d22571561fff12bdd2bf645e2ad8f11201ed62e7ac5ebb40004";
        String fileNameFromUrl = getFileNameFromUrl(ossurl);
        log.info("获取的文件名为：{}",fileNameFromUrl);


    }


    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static long getFileSizeFromUrl(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        conn.connect();
        String s = Optional.ofNullable(url).map(JSON::toJSONString).orElse("");
        long length = conn.getContentLengthLong();
        conn.disconnect();

        return length;
    }
}


