package com.developcollect.commonpay.notice;

import com.developcollect.commonpay.pay.IPayDTO;


/**
 * 未确认订单提取器
 * 用来提取未得到支付确认的订单， 用于主动去支付平台查询订单状态
 *
 * @author zak
 * @since 1.0.0
 */
public interface IUnconfirmedOrderFetcher {

    /**
     * 获取未确认的订单
     *
     * @param page 分页参数
     * @return 未确认的订单
     * @author zak
     * @since 1.0.0
     */
    Page<IPayDTO> getUnconfirmedOrders(Page page);
}
