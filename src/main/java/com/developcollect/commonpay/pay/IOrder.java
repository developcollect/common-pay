package com.developcollect.commonpay.pay;

import java.time.LocalDateTime;

/**
 * 订单抽象接口
 *
 * @author zak
 * @since 1.0.0
 */
public interface IOrder<SOURCE> {

    /**
     * 商户订单号
     */
    String getOutTradeNo();

    /**
     * 支付平台订单号
     */
    String getTradeNo();

    /**
     * 获取订单总额
     */
    Long getTotalFee();

    /**
     * 订单开始时间
     */
    LocalDateTime getTimeStart();

    /**
     * 订单过期时间
     */
    LocalDateTime getTimeExpire();

    /**
     * 支付平台
     */
    int getPayPlatform();

    /**
     * 原始订单对象
     *
     * @return 原始订单对象
     * @author zak
     * @since 1.0.0
     */
    SOURCE getSource();

}
