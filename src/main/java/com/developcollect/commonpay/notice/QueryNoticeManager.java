package com.developcollect.commonpay.notice;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.developcollect.commonpay.config.GlobalConfig;
import com.developcollect.commonpay.pay.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 主动查询通知管理器
 * 在这里有设置定时任务去主动查询支付结果， 并将支付结果通过广播器广播出去
 * @author zak
 * @since 1.0.0
 */
@Slf4j
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
        executor.scheduleWithFixedDelay(this::refundQueryTask, 0, GlobalConfig.queryNoticeDelay(), TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(this::transferQueryTask, 0, GlobalConfig.queryNoticeDelay(), TimeUnit.MILLISECONDS);
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

        Page<IPayDTO> page = new Page<>(1, 50);

        do {
            page = unconfirmedOrderFetcher.getUnconfirmedOrders(page);

            for (IPayDTO payDTO : page.getRecords()) {
                try {
                    Pay pay = GlobalConfig.payFactory().createPay(payDTO.getPayPlatform());
                    PayResponse payResponse = pay.payQuery(payDTO);

                    if (payResponse != null) {
                        GlobalConfig.payBroadcaster().broadcast(payResponse);
                    }
                } catch (Exception e) {
                    log.info("查询订单状态失败", e);
                }
            }

        } while (page.hasNext());
    }


    /**
     * 转账结果查询任务
     *
     * @author zak
     * @since 1.0.0
     */
    protected void refundQueryTask() {

        IUnconfirmedRefundFetcher unconfirmedRefundFetcher = GlobalConfig.unconfirmedRefundFetcher();

        if (unconfirmedRefundFetcher == null) {
            return;
        }

        Page<IRefundDTO> page = new Page<>(1, 50);

        do {
            page = unconfirmedRefundFetcher.getUnconfirmedRefunds(page);

            for (IRefundDTO refundDTO : page.getRecords()) {
                try {
                    Pay pay = GlobalConfig.payFactory().createPay(refundDTO.getPayPlatform());
                    RefundResponse refundResponse = pay.refundQuery(refundDTO);

                    if (refundResponse != null) {
                        GlobalConfig.refundBroadcaster().broadcast(refundResponse);
                    }
                } catch (Exception e) {
                    // 当前订单失败不影响下一个
                    log.info("查询退款单状态失败", e);
                }
            }
        } while (page.hasNext());
    }

    /**
     * 转账结果查询任务
     *
     * @author zak
     * @since 1.0.0
     */
    protected void transferQueryTask() {

        IUnconfirmedTransferFetcher unconfirmedTransferFetcher = GlobalConfig.unconfirmedTransferFetcher();

        if (unconfirmedTransferFetcher == null) {
            return;
        }

        Page<ITransferDTO> page = new Page<>(1, 50);

        do {
            page = unconfirmedTransferFetcher.getUnconfirmedTransfers(page);

            for (ITransferDTO transferDTO : page.getRecords()) {
                try {
                    Pay pay = GlobalConfig.payFactory().createPay(transferDTO.getPayPlatform());
                    TransferResponse transferResponse = pay.transferQuery(transferDTO);

                    if (transferResponse != null) {
                        GlobalConfig.transferBroadcaster().broadcast(transferResponse);
                    }
                } catch (Exception e) {
                    log.info("查询转账单状态失败", e);
                }
            }
        } while (page.hasNext());
    }

}
