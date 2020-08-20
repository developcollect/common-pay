package com.developcollect.commonpay;

import com.developcollect.commonpay.pay.IPayDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 重新指定支付平台订单
 *
 * @author zak
 * @since 1.8.6
 */
@RequiredArgsConstructor
public class RePayPlatformPayDTO implements IPayDTO {

    private final int payPlatform;
    private final IPayDTO order;


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

    @Override
    public Object getExt(String key) {
        return order.getExt(key);
    }

    @Override
    public void putExt(String key, Object extParameter) {
        order.putExt(key, extParameter);
    }
}
