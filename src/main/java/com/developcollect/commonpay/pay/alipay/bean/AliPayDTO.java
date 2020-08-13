package com.developcollect.commonpay.pay.alipay.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付宝异步调用数据对象
 * @author zak
 * @since 1.0.0
 */
@Data
public class AliPayDTO implements Serializable {


    /**
     * 通知的发送时间。格式为yyyy-MM-dd HH:mm:ss
     */
    @JSONField(name = "notify_time")
    private LocalDateTime notifyTime;

    /**
     * 通知类型
     */
    @JSONField(name = "notify_type")
    private String notifyType;

    /**
     * 通知校验ID
     */
    @JSONField(name = "notify_id")
    private String notifyId;

    /**
     * 编码格式，如utf-8、gbk、gb2312等
     */
    private String charset;

    /**
     * 接口版本, 固定为：1.0
     */
    private String version;

    /**
     * 签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
     */
    @JSONField(name = "sign_type")
    private String signType;

    /**
     * 签名
     */
    private String sign;

    /**
     * 授权方的appid，由于本接口暂不开放第三方应用授权，因此auth_app_id=app_id
     */
    @JSONField(name = "auth_app_id")
    private String authAppId;


    /**
     * 支付宝交易凭证号
     */
    @JSONField(name = "trade_no")
    private String tradeNo;


    /**
     * 开发者的app_id
     */
    @JSONField(name = "app_id")
    private String appId;


    /**
     * 原支付请求的商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;


}
