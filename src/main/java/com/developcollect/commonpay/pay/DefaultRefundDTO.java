package com.developcollect.commonpay.pay;

import lombok.Data;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/8/20 11:06
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Data
public class DefaultRefundDTO extends BaseDTO implements IRefundDTO {

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 支付平台退款单号
     */
    private String refundNo;

    /**
     * 退款金额
     */
    private Long refundFee;

    /**
     * 支付平台
     */
    private int payPlatform;

    /**
     * 原始退款对象
     */
    private Object source;

}
