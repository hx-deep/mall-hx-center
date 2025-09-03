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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayPalCaptureResponse {
    private String orderId;
    private String captureId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String createTime;
}
