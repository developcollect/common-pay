package com.developcollect.commonpay.notice;


import com.developcollect.commonpay.pay.RefundResponse;

/**
 * 退款结果广播器接口
 *
 * @author zak
 * @since 1.0.0
 */
@FunctionalInterface
public interface IRefundBroadcaster {


    /**
     * 发送广播
     *
     * @param refundResponse 退款结果
     * @return boolean 是否发送成功
     * @author zak
     * @since 1.0.0
     */
    boolean broadcast(RefundResponse refundResponse);
}
