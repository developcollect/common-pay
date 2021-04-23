package com.developcollect.commonpay.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * 支付宝支付配置
 *
 * @author zak
 * @since 1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AliPayConfig extends AbstractPayConfig {

    /**
     * 支付宝appid
     */
    private String appId;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 编码集，支持 GBK/UTF-8
     */
    private String charset = "UTF-8";

    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2
     */
    private String signType = "RSA2";


    /**
     * 应用公钥证书内容提供器
     */
    private Supplier<String> appCertContentSupplier;


    /**
     * 支付宝公钥证书文件内容提供器
     */
    private Supplier<String> alipayCertContentSupplier;


    /**
     * 支付宝 CA 根证书文件内容提供器
     */
    private Supplier<String> alipayRootCertContentSupplier;


    public boolean hasCert() {
        return this.getAppCertContentSupplier() != null
                && this.getAlipayCertContentSupplier() != null
                && this.getAlipayRootCertContentSupplier() != null;
    }
}
