package com.developcollect.commonpay.pay;

import lombok.Data;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/8/20 11:17
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Data
public class DefaultTransferDTO extends BaseDTO implements ITransferDTO {

    /**
     * 商户转账单号
     */
    private String outTransferNo;

    /**
     * 支付平台转账单号
     */
    private String transferNo;

    /**
     * 转账到的账号
     */
    private String account;

    /**
     * 转账金额(单位:分)
     */
    private Long amount;

    /**
     * 收款用户姓名
     */
    private String reUserName;

    /**
     * 转账描述
     */
    private String description;

    /**
     * 原始转账对象
     */
    private Object source;

    /**
     * 支付平台
     */
    private int payPlatform;


}
