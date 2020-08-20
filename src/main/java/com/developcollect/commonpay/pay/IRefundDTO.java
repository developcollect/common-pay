package com.developcollect.commonpay.pay;

/**
 * 退款抽象接口
 *
 * @author zak
 * @since 1.0.0
 */
public interface IRefundDTO<SOURCE> {

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
