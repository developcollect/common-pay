package com.developcollect.commonpay.config;


import com.developcollect.commonpay.pay.Pay;

/**
 * 支付对象工厂接口
 *
 * @author zak
 * @since 1.0.0
 */
@FunctionalInterface
public interface IPayFactory {

    /**
     * 根据支付平台创建相应的支付对象
     * 支付平台取值见{@link com.developcollect.commonpay.PayPlatform}
     *
     * @param payPlatform 支付平台
     * @return 支付对象
     * @author zak
     * @since 1.0.0
     */
    Pay createPay(int payPlatform);
}
