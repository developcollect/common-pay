package com.developcollect.commonpay.pay.alipay.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.developcollect.commonpay.pay.IOrder;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝统一下单数据包装
 *
 * @author zak
 * @since 1.0.0
 */
@Data
public class PayData implements Serializable {

    /**
     * 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 销售产品码，与支付宝签约的产品码名称。注：目前仅支持FAST_INSTANT_TRADE_PAY
     */
    @JSONField(name = "product_code")
    private String productCode;

    /**
     * 订单总金额(单位:分)
     */
    @JSONField(serialize = false)
    private Long totalAmount;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 订单描述
     */
    private String body;

    /**
     * 绝对超时时间，格式为yyyy-MM-dd HH:mm:ss
     */
    @JSONField(name = "time_expire")
    private String timeExpire;

    /**
     * 公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付宝只会在同步返回（包括跳转回商户网站）和异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝。
     */
    @JSONField(name = "passback_params")
    private String passbackParams;


    public String getTotal_amount() {
        return String.valueOf(totalAmount / 100.0);
    }


    public static PayData of(IOrder order) {
        PayData payData = new PayData();
        payData.setOutTradeNo(order.getOutTradeNo());

        payData.setSubject("商品_" + order.getOutTradeNo());
        payData.setBody("商品_" + order.getOutTradeNo());
        payData.setTotalAmount(order.getTotalFee());
        // FIXME: 2020/1/9 沙箱环境加了这个参数后导致   订单信息无法识别,建议联系卖家。错误码:INVALID_PARAMETER
        // payData.setTimeExpire(DateUtil.format(order.getTimeExpire()));
        return payData;
    }

    public static void main(String[] args) {
        PayData payData = new PayData();
        payData.setOutTradeNo("201978458585");
        payData.setSubject("iphone 11 pro");
        payData.setBody("iphone 11 pro");
        payData.setTotalAmount(1544L);
        String s = JSONObject.toJSONString(payData);
        System.out.println(s);
    }
}
