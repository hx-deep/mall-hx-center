package com.hx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DataStructureApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataStructureApplication.class, args);
        log.info("启动成功！");
    }
}