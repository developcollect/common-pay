package com.developcollect.commonpay.pay;

import cn.hutool.core.date.DateUtil;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.developcollect.commonpay.PayPlatform;
import com.developcollect.commonpay.pay.alipay.bean.AliPayDTO;
import com.developcollect.commonpay.pay.wxpay.bean.WxPayDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付结果
 *
 * @author zak
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class PayResponse implements Serializable {

    /**
     * 支付平台
     */
    private int payPlatform;

    /**
     * 是否支付成功
     */
    private boolean success;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误代码描述
     */
    private String errCodeDes;

    /**
     * 交易凭证号
     */
    private String tradeNo;

    /**
     * 原支付请求的商户订单号
     */
    private String outTradeNo;

    /**
     * 支付完成实际
     */
    private LocalDateTime payTime;

    /**
     * 各平台的原始通知信息
     * 注意： 同一平台的异步推送信息和主动查询信息的对象可能不同
     *       不能只靠payPlatform值来区分是什么对象
     */
    private Serializable rawObj;


    public static PayResponse of(AliPayDTO alipayDTO) {
        PayResponse payResponse = new PayResponse();
        payResponse
                .setSuccess(true)
                .setRawObj(alipayDTO)
                .setPayPlatform(PayPlatform.ALI_PAY)
                .setTradeNo(alipayDTO.getTradeNo())
                .setPayTime(alipayDTO.getNotifyTime())
                .setOutTradeNo(alipayDTO.getOutTradeNo());
        return payResponse;
    }


    public static PayResponse of(WxPayDTO wxPayDTO) {
        PayResponse payResponse = new PayResponse();
        payResponse
                .setSuccess(true)
                .setRawObj(wxPayDTO)
                .setPayPlatform(PayPlatform.WX_PAY)
                .setPayTime(DateUtil.parseLocalDateTime(wxPayDTO.getTimeEnd(), "yyyyMMddHHmmss"))
                .setTradeNo(wxPayDTO.getTransactionId())
                .setOutTradeNo(wxPayDTO.getOutTradeNo());
        return payResponse;
    }

    public static PayResponse of(AlipayTradeQueryResponse alipayTradeQueryResponse) {

        //交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、
        //         TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
        //         TRADE_SUCCESS（交易支付成功）、
        //         TRADE_FINISHED（交易结束，不可退款）

        PayResponse payResponse = new PayResponse();
        payResponse
                .setSuccess(
                        "TRADE_SUCCESS".equals(alipayTradeQueryResponse.getTradeStatus())
                                || "TRADE_FINISHED".equals(alipayTradeQueryResponse.getTradeStatus())
                )
                .setRawObj(alipayTradeQueryResponse)
                .setPayPlatform(PayPlatform.ALI_PAY)
                .setTradeNo(alipayTradeQueryResponse.getTradeNo())
                .setPayTime(DateUtil.toLocalDateTime(alipayTradeQueryResponse.getSendPayDate()))
                .setOutTradeNo(alipayTradeQueryResponse.getOutTradeNo());

        return payResponse;
    }
}
