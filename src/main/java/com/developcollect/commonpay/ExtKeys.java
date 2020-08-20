package com.developcollect.commonpay;

/**
 * 扩展参数key枚举
 *
 * @author zak
 * @since 1.0.0
 */
public interface ExtKeys {

    /**
     * 付款码支付中的付款码在{@link com.developcollect.commonpay.pay.IPayDTO}中的扩展参数key
     */
    String PAY_SCAN_AUTH_CODE = "PAY_SCAN_AUTH_CODE";

    /**
     * 微信JS支付时微信用户标识在{@link com.developcollect.commonpay.pay.IPayDTO}中的扩展参数key
     */
    String PAY_WXJS_OPENID = "PAY_WXJS_OPENID";
}
