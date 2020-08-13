package com.developcollect.commonpay.pay;

/**
 * 退款抽象接口
 *
 * @author zak
 * @since 1.0.0
 */
public interface IRefund<SOURCE> {

    /**
     * 退款单号
     *
     * @return 退款单号
     * @author zak
     * @since 1.0.0
     */
    String getOutRefundNo();

    /**
     * 退款金额 单位：分
     *
     * @return 退款金额
     * @author zak
     * @since 1.0.0
     */
    Long getRefundFee();

    /**
     * 支付平台
     *
     * @return 支付平台
     * @author zak
     * @since 1.0.0
     */
    int getPayPlatform();

    /**
     * 原始退款对象
     *
     * @return 原始退款对象
     * @author zak
     * @since 1.0.0
     */
    SOURCE getSource();
}
