package com.developcollect.commonpay.config;


import com.developcollect.commonpay.PayPlatform;
import com.developcollect.commonpay.exception.ConfigException;
import com.developcollect.commonpay.pay.Pay;
import com.developcollect.commonpay.pay.alipay.Alipay;
import com.developcollect.commonpay.pay.nopay.NoPay;
import com.developcollect.commonpay.pay.wxpay.WxPay;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付对象创建工厂
 *
 * @author zak
 * @since 1.0.0
 */
public class DefaultPayFactory implements IPayFactory {

    protected final Map<Integer, Pay> payMap = new ConcurrentHashMap<>();


    /**
     * 根本支付枚举创建支付方式对象
     *
     * @param payPlatform 支付平台
     * @return Pay 支付对象
     * @author zak
     * @since 1.0.0
     */
    @Override
    public Pay createPay(int payPlatform) {
        if (!GlobalConfig.payPlatformVerify(payPlatform)) {
            throw new ConfigException("未找到支付平台[{}]的配置", payPlatform);
        }
        return payMap.computeIfAbsent(payPlatform, k -> {
            switch (k) {
                case PayPlatform.ALI_PAY:
                    return new Alipay();
                case PayPlatform.WX_PAY:
                    return new WxPay();
                default:
                    return new NoPay();
            }
        });
    }
}
