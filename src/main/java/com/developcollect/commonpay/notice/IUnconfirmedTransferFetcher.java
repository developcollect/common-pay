package com.developcollect.commonpay.notice;

import com.developcollect.commonpay.pay.ITransferDTO;

/**
 * 未确认提现单提取器
 * 用来提取未得到确认的提现单， 用于主动去支付平台查询提现单状态
 *
 * @author zak
 * @since 1.0.0
 */
public interface IUnconfirmedTransferFetcher {


    /**
     * 获取未确认的提现单
     *
     * @param page 分页参数
     * @return 未确认的提现单
     * @author zak
     * @since 1.0.0
     */
    Page<ITransferDTO> getUnconfirmedTransfers(Page<ITransferDTO> page);
}
