package com.hx;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Package: com.hx
 * @ClassName: HXServiceApplication.java
 * @Description:
 * @author: zjg
 * @date: 2024/7/18 
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HXServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(HXServiceApplication.class, args);
    }
}