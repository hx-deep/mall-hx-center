package com.hx.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author zhang jiagu
 * @date 2025/9/3
 **/
// 4. 数据传输对象(DTO)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayPalOrderRequest {


    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String productName;
    private String brandName;
    private String returnUrl;
    private String cancelUrl;

    private String userId;
}
