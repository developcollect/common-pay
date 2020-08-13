package com.developcollect.commonpay.pay.wxpay;

import com.developcollect.commonpay.PayPlatform;
import com.developcollect.commonpay.config.GlobalConfig;
import com.developcollect.commonpay.config.WxPayConfig;
import com.developcollect.commonpay.pay.wxpay.sdk.WXPayConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.InputStream;

/**
 * 微信sdk配置
 * @author zak
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultWXPayConfig extends WXPayConfig {


    private String appId;

    private String mchId;

    private String key;


    @Override
    public String getAppID() {
        return appId;
    }

    @Override
    public String getMchID() {
        return mchId;
    }

    @Override
    public String getKey() {
        return key;
    }


    /**
     * 微信支付证书
     *
     * @return 流
     * @author zak
     * @since 1.0.0
     */
    @Override
    public InputStream getCertStream() {
        // 将证书配置通过配置传入
        WxPayConfig payConfig = GlobalConfig.getPayConfig(PayPlatform.WX_PAY);
        return payConfig.getCertInputStreamSupplier().get();
    }
}

