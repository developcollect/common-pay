package com.developcollect.commonpay.pay.alipay.bean;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.developcollect.commonpay.PayPlatform;
import com.developcollect.commonpay.config.GlobalConfig;
import com.developcollect.commonpay.pay.IPayDTO;
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

    @JSONField(name = "auth_code")
    private String authCode;


    public String getTotal_amount() {
        return String.valueOf(totalAmount / 100.0);
    }


    public static PayData of(IPayDTO payDTO) {
        PayData payData = new PayData();
        payData.setOutTradeNo(payDTO.getOutTradeNo());

        payData.setSubject("商品_" + payDTO.getOutTradeNo());
        payData.setBody("商品_" + payDTO.getOutTradeNo());
        payData.setTotalAmount(payDTO.getTotalFee());
        // FIXME: 2020/1/9 沙箱环境加了这个参数后导致   订单信息无法识别,建议联系卖家。错误码:INVALID_PARAMETER
        if (GlobalConfig.getPayConfig(PayPlatform.ALI_PAY).isDebug() == false) {
            payData.setTimeExpire(DateUtil.format(payDTO.getTimeExpire(), "yyyy-MM-dd HH:mm:ss"));
        }
        return payData;
    }
}
