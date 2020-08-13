package com.developcollect.commonpay.notice;


import com.developcollect.commonpay.pay.PayResponse;

/**
 * 支付结果广播器接口
 *
 * @author zak
 * @since 1.0.0
 */
@FunctionalInterface
public interface IPayBroadcaster {


    /**
     * 发送广播
     *
     * @param payResponse 支付结果
     * @return boolean 是否发送成功
     * @author zak
     * @since 1.0.0
     */
    boolean broadcast(PayResponse payResponse);

}
