package com.developcollect.commonpay.pay.wxpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 微信退款通知数据封装
 * https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_16&index=11
 *
 * @author zak
 * @since 1.0.0
 */
@Data
@ToString
@XStreamAlias("xml")
public class WxRefundDTO implements Serializable {


    /**
     * 返回状态码
     */
    @XStreamAlias("return_code")
    private String returnCode;

    /**
     * 返回信息
     */
    @XStreamAlias("return_msg")
    private String returnMsg;

    /**
     * 公众账号ID
     */
    @XStreamAlias("appid")
    private String appId;

    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    private String mchId;

    /**
     *
     */
    @XStreamAlias("nonce_str")
    private String nonceStr;


    /**
     * 加密信息
     */
    @XStreamAlias("req_info")
    private String reqInfo;


    @Data
    @ToString
    @XStreamAlias("xml")
    public static class WxRefundNotifyData {
        /**
         * 微信订单号
         */
        @XStreamAlias("transaction_id")
        private String transactionId;

        /**
         * 商户系统内部的订单号
         */
        @XStreamAlias("out_trade_no")
        private String outTradeNo;

        /**
         * 微信退款单号
         */
        @XStreamAlias("refund_id")
        private String refundId;

        /**
         * 商户退款单号
         */
        @XStreamAlias("out_refund_no")
        private String outRefundNo;

        /**
         * 订单总金额，单位为分，只能为整数，
         * 详见https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=4_2
         */
        @XStreamAlias("total_fee")
        private String totalFee;

        /**
         * 当该订单有使用非充值券时，返回此字段。
         * 应结订单金额=订单金额-非充值代金券金额，
         * 应结订单金额<=订单金额。
         */
        @XStreamAlias("settlement_total_fee")
        private String settlementTotalFee;

        /**
         * 退款总金额,单位为分
         */
        @XStreamAlias("refund_fee")
        private String refundFee;

        /**
         * 退款金额=申请退款金额-非充值代金券退款金额，
         * 退款金额<=申请退款金额
         */
        @XStreamAlias("settlement_refund_fee")
        private String settlementRefundFee;

        /**
         * SUCCESS-退款成功
         * CHANGE-退款异常
         * REFUNDCLOSE—退款关闭
         */
        @XStreamAlias("refund_status")
        private String refundStatus;

        /**
         * 资金退款至用户帐号的时间，格式2017-12-15 09:46:01
         */
        @XStreamAlias("success_time")
        private String successTime;

        /**
         * 取当前退款单的退款入账方
         * 1）退回银行卡：
         * {银行名称}{卡类型}{卡尾号}
         * <p>
         * 2）退回支付用户零钱:
         * 支付用户零钱
         * <p>
         * 3）退还商户:
         * 商户基本账户
         * 商户结算银行账户
         * <p>
         * 4）退回支付用户零钱通:
         * 支付用户零钱通
         */
        @XStreamAlias("refund_recv_accout")
        private String refundRecvAccout;

        /**
         * REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户
         * REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款
         */
        @XStreamAlias("refund_account")
        private String refundAccount;

        /**
         * API接口
         * VENDOR_PLATFORM商户平台
         */
        @XStreamAlias("refund_request_source")
        private String refundRequestSource;
    }
}
