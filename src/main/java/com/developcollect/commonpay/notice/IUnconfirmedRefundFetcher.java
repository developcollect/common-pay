package com.developcollect.commonpay.notice;

import com.developcollect.commonpay.pay.IRefundDTO;

/**
 * 未确认退款单提取器
 * 用来提取未得到确认的退款单， 用于主动去支付平台查询退款单状态
 *
 * @author zak
 * @since 1.0.0
 */
public interface IUnconfirmedRefundFetcher {

    /**
     * 获取未确认的退款单
     *
     * @param page 分页参数
     * @return 未确认的退款单
     * @author zak
     * @since 1.0.0
     */
    Page<IRefundDTO> getUnconfirmedRefunds(Page<IRefundDTO> page);
}
