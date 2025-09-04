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
public class PayPalOrderDTO {

    /**
     * 系统订单id
     */
    private String orderId;
    /**
     * 订单金额
     */
    private BigDecimal amount;
    /**
     * 单位
     */
    private String currency;
    /**
     * 描述
     */
    private String description;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 品牌名
     */
    private String brandName;
    /**
     * 支付成功回调地址
     */
    private String returnUrl;
    /**
     * 取消支付的回调地址
     */
    private String cancelUrl;
    /**
     * 用户的userId
     */
    private String userId;
}
