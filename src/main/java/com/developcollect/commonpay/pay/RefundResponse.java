package com.developcollect.commonpay.pay;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 退款响应结果
 *
 * @author zak
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class RefundResponse implements Serializable {

    /**
     * 退款成功
     */
    public static final int SUCCESS = 1;

    /**
     * 退款失败
     */
    public static final int FAIL = 2;

    /**
     * 退款中
     */
    public static final int PROCESSING = 3;

    /**
     * 退款状态
     * 1 退款成功
     * 2 退款失败
     * 3 退款中
     */
    private int status;

    /**
     * 支付平台
     */
    private int payPlatform;

    /**
     * 支付平台退款单号
     */
    private String refundNo;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 各平台的原始通知信息
     * 注意： 同一平台的异步推送信息和主动查询信息的对象可能不同
     * 不能只靠payPlatform值来区分是什么对象
     */
    private Serializable rawObj;


    /**
     * 退款是否成功
     * 注意： 如果返回false则有可能还在退款中
     */
    public boolean success() {
        return status == SUCCESS;
    }
}
