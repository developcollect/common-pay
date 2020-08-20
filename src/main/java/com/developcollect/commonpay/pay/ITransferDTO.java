package com.developcollect.commonpay.pay;

import cn.hutool.core.util.StrUtil;

/**
 * 转账对象
 *
 * @author zak
 * @since 1.0.0
 */
public interface ITransferDTO<SOURCE> {


    /**
     * 转账编号
     *
     * @return 转账编号
     * @author zak
     * @since 1.0.0
     */
    String getOutTransferNo();


    /**
     * 转账到的账号
     *
     * @return 转账到的账号
     * @author zak
     * @since 1.0.0
     */
    String getAccount();


    /**
     * 获取转账总额
     *
     * @return 获取转账总额
     * @author zak
     * @since 1.0.0
     */
    Long getAmount();


    /**
     * 是否校验用户姓名
     *
     * @return 是否校验用户姓名
     * @author zak
     * @since 1.0.0
     */
    default boolean needCheckName() {
        return StrUtil.isBlank(getReUserName());
    }


    /**
     * 收款用户姓名
     *
     * @return 收款用户姓名
     * @author zak
     * @since 1.0.0
     */
    String getReUserName();

    /**
     * 转账描述
     *
     * @return 转账描述
     * @author zak
     * @since 1.0.0
     */
    String getDescription();


    /**
     * 原始转账对象
     *
     * @return 原始转账对象
     * @author zak
     * @since 1.0.0
     */
    SOURCE getSource();


    /**
     * 支付平台
     *
     * @return 支付平台
     * @author zak
     * @since 1.0.0
     */
    int getPayPlatform();


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
