package com.developcollect.commonpay.pay;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * app支付返回值
 * 目前只有微信app支付需要后台预下单, 所以返回值都是按微信的要求加的
 */
@Data
public class PayAppResult implements Serializable {

    /**
     * appid
     */
    private String appId;

    /**
     * 商户id
     */
    private String partnerId;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 订单详情扩展字符串
     */
    @JSONField(name = "package")
    private String package0;

    /**
     * 预支付交易会话标识
     */
    private String prepayId;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 签名
     */
    private String paySign;

}
