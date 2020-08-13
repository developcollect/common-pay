package com.developcollect.commonpay.notice;

import com.developcollect.commonpay.pay.TransferResponse;

/**
 * 转账结果广播器
 *
 * @author zak
 * @since 1.0.0
 */
@FunctionalInterface
public interface ITransferBroadcaster {

    /**
     * 发送广播
     *
     * @param transferResponse 退款结果
     * @return boolean 是否发送成功
     * @author zak
     * @since 1.0.0
     */
    boolean broadcast(TransferResponse transferResponse);
}
