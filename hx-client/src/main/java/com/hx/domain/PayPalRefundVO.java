package com.hx.domain;

/**
 * TODO
 *
 * @author zhang jiagu
 * @date 2025/9/4
 **/
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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayPalRefundVO {
    private String refundId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String createTime;
}

