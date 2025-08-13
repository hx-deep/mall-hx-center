package com.hx.service;

import com.hx.dto.CountDto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class ThreadFulter {


        public static void main(String[] args) {
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "结果3";
            });

            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "结果2");
            CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
                try {

                  for(int i = 0;i<=100;i++){
                      log.info("打印处理的线程：{},线程名称：{}",i,Thread.currentThread().getName());
                  }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "结果3";
            });
            List<CompletableFuture<String>> futures = new ArrayList<>();
            futures.add(future1);
            futures.add(future2);
            futures.add(future3);
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            try {
                // 现在可以安全地获取各个future的结果
                System.out.println(future1.get()); // 输出: 结果1

                System.out.println(future2.get()); // 输出: 结果2
                System.out.println(future3.get()); // 输出: 结果3
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }


}
