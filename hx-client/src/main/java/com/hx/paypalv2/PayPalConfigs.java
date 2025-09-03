package com.hx.paypalv2;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author zhang jiagu
 * @date 2025/9/3
 **/
// 1. PayPal配置类
@Configuration
@ConfigurationProperties(prefix = "paypal")
@Data
public class PayPalConfigs {
    private String clientId;
    private String clientSecret;
    private String mode; // sandbox 或 live
    private String webhookId;

    @Bean
    public PayPalHttpClient payPalHttpClient() {
        PayPalEnvironment environment;
        if ("live".equals(mode)) {
            environment = new PayPalEnvironment.Live(clientId, clientSecret);
        } else {
            environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
        }
        return new PayPalHttpClient(environment);
    }
}