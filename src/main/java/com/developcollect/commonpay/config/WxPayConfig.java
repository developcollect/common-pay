package com.developcollect.commonpay.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * 微信支付配置
 *
 * @author zak
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WxPayConfig extends AbstractPayConfig {

    /**
     * appid
     */
    private String appId;

    /**
     * 商户id
     */
    private String mchId;

    /**
     * 微信支付key
     */
    private String key;

    /**
     * 证书
     */
    private Supplier<InputStream> certInputStreamSupplier;

}
