package com.developcollect.commonpay.pay.alipay.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.developcollect.commonpay.pay.IOrder;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝订单状态主动查询参数封装
 *
 * @author zak
 * @since 1.0.0
 */
@Data
public class PayQueryData implements Serializable {

    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    @JSONField(name = "trade_no")
    private String tradeNo;


    public static PayQueryData of(IOrder order) {
        PayQueryData payQueryData = new PayQueryData();
        payQueryData.setOutTradeNo(order.getOutTradeNo());
        payQueryData.setTradeNo(order.getTradeNo());
        return payQueryData;
    }
}
