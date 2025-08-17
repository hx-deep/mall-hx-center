package com.hx.practice.async;



import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.Thread.sleep;

public class CompletableFutureTest {
    public static final ThreadPoolExecutor EXECUTOR_SERVICE =
            new ThreadPoolExecutor(5,
                    10,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(100),
                    new ThreadPoolExecutor.CallerRunsPolicy());


    /**
     * supplyAsync:可以得到返回值
     * runAsync:不需要返回值
     */
    CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() ->{
        System.out.println(Thread.currentThread().getName());
        return "task1";
    }, EXECUTOR_SERVICE);

    CompletableFuture<Void> task2 = CompletableFuture.runAsync(()->{
        System.out.println(Thread.currentThread().getName());
        System.out.println("逻辑处理");
    });




    /**
     * 定义任务
     * @param args
//     */
//    Supplier<String> renWu1 = ()->{
//        System.out.println(Thread.currentThread().getName());
//        return "renWu1";
//    };
//
//    Supplier<String> renWu2 = () ->{
//        System.out.println(Thread.currentThread().getName());
//        return "renWu2";
//    };
//    CompletableFuture<String> future = CompletableFuture.supplyAsync(renWu1, EXECUTOR_SERVICE);
//    CompletableFuture test = future.thenApply(renWu2);




    public static void main(String[] args) throws InterruptedException {
//        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() ->{
//            System.out.println(Thread.currentThread().getName());
//            return "task1";
//        }, EXECUTOR_SERVICE);
//        task1.thenApplyAsync(result->{
//            System.out.println(Thread.currentThread().getName());
//            System.out.println(result+"+task2");
//            return "task2"+result;
//        }, EXECUTOR_SERVICE);
//        task1.thenApply(result ->{
//            System.out.println(Thread.currentThread().getName());
//            System.out.println(result+"+task3");
//            return "task3"+result;
//        },EXECUTOR_SERVICE);
        Supplier<String> renWu1 = () -> {
            System.out.println(Thread.currentThread().getName());
            return "renWu1";
        };

        Function<String, String> renWu2 = s -> {
            System.out.println(Thread.currentThread().getName());
            return "renWu2";
        };
        CompletableFuture<String> future = CompletableFuture.supplyAsync(renWu1, EXECUTOR_SERVICE);
        sleep(1);
        future.thenApplyAsync(renWu2, EXECUTOR_SERVICE);
    }





}
