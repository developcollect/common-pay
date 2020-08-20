package com.developcollect.commonpay.pay;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/8/20 11:03
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Data
public class DefaultPayDTO extends BaseDTO implements IPayDTO {

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 支付平台订单号
     */
    private String tradeNo;

    /**
     * 支付金额
     */
    private Long totalFee;

    /**
     * 订单开始时间
     * (非必填)
     */
    private LocalDateTime timeStart;

    /**
     * 订单过期时间
     * (非必填, 在需要显式指定订单有效期时需要)
     */
    private LocalDateTime timeExpire;

    /**
     * 支付平台
     */
    private int payPlatform;

    /**
     * 原始订单对象
     */
    private Object source;
}
