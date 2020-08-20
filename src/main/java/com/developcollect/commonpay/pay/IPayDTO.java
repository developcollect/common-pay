package com.developcollect.commonpay.pay;

import java.time.LocalDateTime;

/**
 * 支付参数
 *
 * @author zak
 * @since 1.0.0
 */
public interface IPayDTO<SOURCE> {

    /**
     * 商户订单号
     */
    String getOutTradeNo();

    /**
     * 支付平台订单号
     */
    String getTradeNo();

    /**
     * 获取支付金额
     */
    Long getTotalFee();

    /**
     * 订单开始时间
     */
    default LocalDateTime getTimeStart() {
        return null;
    }

    /**
     * 订单过期时间
     */
    default LocalDateTime getTimeExpire() {
        return null;
    }

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

    /**
     * 获取扩展参数
     * 比如自定义余额支付可能需要支付密码
     * 或者自定义网盾支付又需要什么秘钥等等
     *
     * @param key 扩展参数key
     * @return T 扩展参数值
     */
    default <T> T getExt(String key) {
        return null;
    }

    /**
     * 放入扩展参数
     * 默认就是什么都不干, 如果有需要, 那就重写这个方法
     *
     * @param key          扩展参数key
     * @param extParameter 扩展参数值
     */
    default void putExt(String key, Object extParameter) {
        // do nothing
        // 留给子类去重写
    }
}
