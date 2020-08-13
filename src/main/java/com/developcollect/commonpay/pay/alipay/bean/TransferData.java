package com.developcollect.commonpay.pay.alipay.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.developcollect.commonpay.pay.ITransfer;
import com.developcollect.commonpay.utils.UnitUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝转账参数封装
 *
 * @author zak
 * @since 1.0.0
 */
@Data
public class TransferData implements Serializable {

    /**
     * 商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯一。
     * 必选
     */
    @JSONField(name = "out_biz_no")
    private String outBizNo;

    /**
     * 订单总金额，单位为元，精确到小数点后两位，
     * STD_RED_PACKET产品取值范围[0.01,100000000]；
     * TRANS_ACCOUNT_NO_PWD产品取值范围[0.1,100000000]
     * 必选
     */
    @JSONField(name = "trans_amount")
    private String transAmount;

    /**
     * 业务产品码
     * 收发现金红包固定为：STD_RED_PACKET；
     * 单笔无密转账到支付宝账户固定为：TRANS_ACCOUNT_NO_PWD；
     * 单笔无密转账到银行卡固定为：TRANS_BANKCARD_NO_PWD
     * 必选
     */
    @JSONField(name = "product_code")
    private String productCode;

    /**
     * 描述特定的业务场景，可传的参数如下：
     * PERSONAL_COLLECTION：C2C现金红包-领红包；
     * DIRECT_TRANSFER：B2C现金红包、单笔无密转账到支付宝/银行卡
     * 可选
     */
    @JSONField(name = "biz_scene")
    private String bizScene;

    /**
     * 转账业务的标题，用于在支付宝用户的账单里显示
     * 可选
     */
    @JSONField(name = "order_title")
    private String orderTitle;

    /**
     * 原支付宝业务单号
     * C2C现金红包-红包领取时，传红包支付时返回的支付宝单号；B2C现金红包、单笔无密转账到支付宝/银行卡不需要该参数。
     * 可选
     */
    @JSONField(name = "original_order_id")
    private String originalOrderId;

    /**
     * 收款方信息
     * 必选
     */
    @JSONField(name = "payee_info")
    private Payee payeeInfo;

    /**
     * 业务备注
     * 可选
     */
    @JSONField(name = "remark")
    private String remark;

    /**
     * 转账业务请求的扩展参数，支持传入的扩展参数如下：
     * 1、sub_biz_scene 子业务场景，红包业务必传，取值REDPACKET，C2C现金红包、B2C现金红包均需传入；
     * 2、withdraw_timeliness为转账到银行卡的预期到账时间，可选（不传入则默认为T1），T0表示预期T+0到账，T1表示预期T+1到账，到账时效受银行机构处理影响，支付宝无法保证一定是T0或者T1到账；
     * 可选
     */
    @JSONField(name = "business_params")
    private String businessParams;


    public static TransferData of(ITransfer transfer) {
        TransferData transferData = new TransferData();
        transferData.setBizScene("DIRECT_TRANSFER");
        transferData.setOutBizNo(transfer.getOutTransferNo());
        transferData.setOrderTitle(transfer.getDescription());
        transferData.setProductCode("TRANS_ACCOUNT_NO_PWD");
        transferData.setTransAmount(UnitUtil.convertFenToYuanStr(transfer.getAmount()));
        Payee payee = new Payee();
        payee.setIdentity(transfer.getAccount());
        payee.setIdentityType("ALIPAY_LOGON_ID");
        payee.setName(transfer.getReUserName());
        if (transfer.needCheckName()) {
            payee.setName(transfer.getReUserName());
        }
        transferData.setPayeeInfo(payee);
        return transferData;
    }
}
