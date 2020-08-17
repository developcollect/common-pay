package com.developcollect.commonpay;

import com.developcollect.commonpay.pay.IOrder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 重新指定支付平台订单
 *
 * @author zak
 * @since 1.8.6
 */
@RequiredArgsConstructor
public class RePayPlatformOrder implements IOrder {

    private final int payPlatform;
    private final IOrder order;


    @Override
    public String getOutTradeNo() {
        return order.getOutTradeNo();
    }

    @Override
    public String getTradeNo() {
        return order.getTradeNo();
    }

    @Override
    public Long getTotalFee() {
        return order.getTotalFee();
    }

    @Override
    public LocalDateTime getTimeStart() {
        return order.getTimeStart();
    }

    @Override
    public LocalDateTime getTimeExpire() {
        return order.getTimeExpire();
    }

    @Override
    public int getPayPlatform() {
        return payPlatform;
    }

    @Override
    public Object getSource() {
        return order.getSource();
    }
}
