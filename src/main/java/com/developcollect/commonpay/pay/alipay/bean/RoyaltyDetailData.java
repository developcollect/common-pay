package com.developcollect.commonpay.pay.alipay.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝退分账明细信息
 *
 * @author zak
 * @since 1.0.0
 */
@Data
public class RoyaltyDetailData implements Serializable {

    /**
     * 必填,收入方账户。如果收入方账户类型为userId，本参数为收入方的支付宝账号对应的支付宝唯一用户号，
     * 以2088开头的纯16位数字；如果收入方类型为cardAliasNo，本参数为收入方在支付宝绑定的卡编号；
     * 如果收入方类型为loginName，本参数为收入方的支付宝登录号；
     */
    @JSONField(name = "trans_in")
    private String transIn;

    /**
     * 可选, 分账类型.
     * 普通分账为：transfer;
     * 补差为：replenish;
     * 为空默认为分账transfer;
     */
    @JSONField(name = "royalty_type")
    private String royaltyType;

    /**
     * 可选, 支出方账户。如果支出方账户类型为userId，本参数为支出方的支付宝账号对应的支付宝唯一用户号，
     * 以2088开头的纯16位数字；如果支出方类型为loginName，本参数为支出方的支付宝登录号；
     */
    @JSONField(name = "trans_out")
    private String transOut;

    /**
     * 可选, 支出方账户类型。userId表示是支付宝账号对应的支付宝唯一用户号;loginName表示是支付宝登录号；
     */
    @JSONField(name = "trans_out_type")
    private String transOutType;

    /**
     * 可选, 收入方账户类型。userId表示是支付宝账号对应的支付宝唯一用户号;
     * cardAliasNo表示是卡编号;loginName表示是支付宝登录号；
     */
    @JSONField(name = "trans_in_type")
    private String transInType;


    /**
     * 可选, 分账的金额，单位为元
     */
    @JSONField(name = "amount")
    private String amount;


    /**
     * 可选, 分账信息中分账百分比。取值范围为大于0，少于或等于100的整数。
     */
    @JSONField(name = "amount_percentage")
    private Integer amountPercentage;

    /**
     * 可选, 分账描述
     */
    @JSONField(name = "desc")
    private Integer desc;
}
