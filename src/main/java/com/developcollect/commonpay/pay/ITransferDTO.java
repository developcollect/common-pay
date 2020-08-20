package com.developcollect.commonpay.pay;

import cn.hutool.core.util.StrUtil;


/**
 * 转账对象
 *
 * @author zak
 * @since 1.0.0
 */
public interface ITransferDTO<SOURCE> extends IExtDto {


    /**
     * 转账编号
     *
     * @return 转账编号
     * @author zak
     * @since 1.0.0
     */
    String getOutTransferNo();

    /**
     * 支付平台转账编号
     *
     * @return 转账编号
     * @author zak
     * @since 1.0.0
     */
    default String getTransferNo() {
        return null;
    }


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

}
