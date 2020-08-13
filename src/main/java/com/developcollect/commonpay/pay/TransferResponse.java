package com.developcollect.commonpay.pay;



import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 转账结果
 *
 * @author zak
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class TransferResponse implements Serializable {

    /**
     * 成功
     */
    public static final int STATUS_SUCCESS = 1;

    /**
     * 处理中
     */
    public static final int STATUS_PROCESSING = 2;

    /**
     * 失败
     */
    public static final int STATUS_FAIL = 3;

    /**
     * 支付平台
     */
    private int payPlatform;

    /**
     * 支付平台转账单号
     */
    private String transferNo;

    /**
     * 商户转账单号
     */
    private String outTransferNo;

    /**
     * 付款成功时间
     */
    private LocalDateTime paymentTime;

    /**
     * 状态
     * 1 成功
     * 2 处理中
     * 3 失败
     */
    private Integer status;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误原因
     */
    private String errorDesc;


    /**
     * 各平台的原始通知信息
     * 注意： 同一平台的异步推送信息和主动查询信息的对象可能不同
     * 不能只靠payPlatform值来区分是什么对象
     */
    private Serializable rawObj;
}
