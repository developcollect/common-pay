package com.developcollect.commonpay.pay;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 微信JSBridge支付返回值
 *
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/8/15 13:50
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Data
public class PayWxJsResult implements Serializable {

    /**
     * 公众号id
     */
    private String appId;

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


    public static PayWxJsResult of(Map<String, String> wxJsPayMap) {
        PayWxJsResult wxJsPayResult = new PayWxJsResult();
        wxJsPayResult.setAppId(wxJsPayMap.get("appid"));
        wxJsPayResult.setTimeStamp(wxJsPayMap.get("timeStamp"));
        wxJsPayResult.setNonceStr(wxJsPayMap.get("nonce_str"));
        wxJsPayResult.setPackage0(wxJsPayMap.get("package"));
        wxJsPayResult.setPrepayId(wxJsPayMap.get("prepay_id"));
        wxJsPayResult.setSignType(wxJsPayMap.get("sign_type"));
        wxJsPayResult.setPaySign(wxJsPayMap.get("sign"));

        return wxJsPayResult;
    }
}
