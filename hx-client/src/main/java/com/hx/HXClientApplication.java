package com.hx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Package: com.hx
 * @ClassName: HXClientApplication.java
 * @Description:
 * @author: zjg
 * @date: 2024/7/18 
 */
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@SpringBootApplication
public class HXClientApplication {


    public static void main(String[] args) {
        SpringApplication.run(HXClientApplication.class, args);
    }
}