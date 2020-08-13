package com.developcollect.commonpay.pay.wxpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * 微信支付通知数据封装
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
 *
 * @author zak
 * @since 1.0.0
 */
@Data
@ToString
@XStreamAlias("xml")
public class WxPayDTO implements Serializable {

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
    @XStreamAlias("sub_mch_id")
    private String subMchId;

    /**
     * 设备号
     */
    @XStreamAlias("device_info")
    private String deviceInfo;

    /**
     * 随机字符串
     */
    @XStreamAlias("nonce_str")
    private String nonceStr;

    /**
     * 签名
     */
    @XStreamAlias("sign")
    private String sign;

    /**
     * 签名类型
     */
    @XStreamAlias("sign_type")
    private String signType;

    /**
     * 业务结果
     */
    @XStreamAlias("result_code")
    private String resultCode;

    /**
     * 错误代码
     */
    @XStreamAlias("err_code")
    private String errCode;

    /**
     * 错误代码描述
     */
    @XStreamAlias("err_code_des")
    private String errCodeDes;

    /**
     * 用户标识
     */
    @XStreamAlias("openid")
    private String openid;

    /**
     * 是否关注公众账号
     */
    @XStreamAlias("is_subscribe")
    private String isSubscribe;

    /**
     * 交易类型
     */
    @XStreamAlias("trade_type")
    private String tradeType;

    /**
     * 付款银行
     */
    @XStreamAlias("bank_type")
    private String bankType;

    /**
     * 订单总金额，单位为分
     */
    @XStreamAlias("total_fee")
    private String totalFee;

    /**
     * 应结订单金额
     */
    @XStreamAlias("settlement_total_fee")
    private String settlementTotalFee;

    /**
     * 货币种类
     */
    @XStreamAlias("fee_type")
    private String feeType;

    /**
     * 现金支付金额
     */
    @XStreamAlias("cash_fee")
    private String cashFee;

    /**
     * 现金支付货币类型
     */
    @XStreamAlias("cash_fee_type")
    private String cashFeeType;

    /**
     * 总代金券金额
     */
    @XStreamAlias("coupon_fee")
    private String couponFee;

    /**
     * 代金券使用数量
     */
    @XStreamAlias("coupon_count")
    private String couponCount;

    /**
     * 微信支付订单号
     */
    @XStreamAlias("transaction_id")
    private String transactionId;

    /**
     * 商户订单号
     */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    /**
     * 商家数据包
     */
    @XStreamAlias("attach")
    private String attach;

    /**
     * 支付完成时间,格式为yyyyMMddHHmmss
     */
    @XStreamAlias("time_end")
    private String timeEnd;

//    private String coupon_type_$n;
//    private String coupon_id_$n;
//    private String coupon_fee_$n;

//    public static void main(String[] args) {
//        WxpayNotify wxpayNotify = SerializeUtil.xmlToBean("<xml>\n" +
//                "  <appid><![CDATA[wx2421b1c4370ec43b]]></appid>\n" +
//                "  <attach><![CDATA[支付测试]]></attach>\n" +
//                "  <bank_type><![CDATA[CFT]]></bank_type>\n" +
//                "  <fee_type><![CDATA[CNY]]></fee_type>\n" +
//                "  <is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
//                "  <mch_id><![CDATA[10000100]]></mch_id>\n" +
//                "  <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>\n" +
//                "  <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>\n" +
//                "  <out_trade_no><![CDATA[1409811653]]></out_trade_no>\n" +
//                "  <result_code><![CDATA[SUCCESS]]></result_code>\n" +
//                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
//                "  <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>\n" +
//                "  <sub_mch_id><![CDATA[10000100]]></sub_mch_id>\n" +
//                "  <time_end><![CDATA[20140903131540]]></time_end>\n" +
//                "  <total_fee>1</total_fee>\n" +
//                "  <trade_type><![CDATA[JSAPI]]></trade_type>\n" +
//                "  <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>\n" +
//                "</xml>", WxpayNotify.class);
//
//        System.out.println(1);
//    }


}
