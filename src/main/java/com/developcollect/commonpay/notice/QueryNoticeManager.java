package com.developcollect.commonpay.notice;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.developcollect.commonpay.config.GlobalConfig;
import com.developcollect.commonpay.pay.IPayDTO;
import com.developcollect.commonpay.pay.Pay;
import com.developcollect.commonpay.pay.PayResponse;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 主动查询通知管理器
 * 在这里有设置定时任务去主动查询支付结果， 并将支付结果通过广播器广播出去
 * @author zak
 * @since 1.0.0
 */
public class QueryNoticeManager {

    /**
     * 定时器
     */
    protected ScheduledThreadPoolExecutor executor;


    /**
     * 初始化方法
     *
     * @author zak
     * @since 1.0.0
     */
    public synchronized void init() {
        if (executor != null) {
            executor.shutdownNow();
        }

        executor = new ScheduledThreadPoolExecutor(
                2,
                ThreadFactoryBuilder.create().setNamePrefix("COMMON-PAY-QUERY-NOTICE-").build()
        );

        executor.scheduleWithFixedDelay(this::payQueryTask, 0, GlobalConfig.queryNoticeDelay(), TimeUnit.MILLISECONDS);
    }


    /**
     * 支付结果查询任务
     * @author zak
     * @since 1.0.0
     */
    protected void payQueryTask() {

        IUnconfirmedOrderFetcher unconfirmedOrderFetcher = GlobalConfig.unconfirmedOrderFetcher();
        if (unconfirmedOrderFetcher == null) {
            return;
        }

        Page<IPayDTO> page = new Page<>(1, 1);

        do {
            page = unconfirmedOrderFetcher.getUnconfirmedOrders(page);

            for (IPayDTO payDTO : page.getRecords()) {
                Pay pay = GlobalConfig.payFactory().createPay(payDTO.getPayPlatform());
                PayResponse payResponse = pay.payQuery(payDTO);

                if (payResponse != null) {
                    GlobalConfig.payBroadcaster().broadcast(payResponse);
                }
            }

        } while (page.hasNext());
    }


}
