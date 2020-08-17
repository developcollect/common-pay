package com.developcollect.commonpay;

import com.developcollect.commonpay.pay.IRefund;
import lombok.RequiredArgsConstructor;


/**
 * 重新指定支付平台订单
 *
 * @author zak
 * @since 1.8.6
 */
@RequiredArgsConstructor
public class RePayPlatformRefund implements IRefund {

    private final int payPlatform;
    private final IRefund refund;


    @Override
    public String getOutRefundNo() {
        return refund.getOutRefundNo();
    }

    @Override
    public Long getRefundFee() {
        return refund.getRefundFee();
    }

    @Override
    public int getPayPlatform() {
        return payPlatform;
    }

    @Override
    public Object getSource() {
        return refund.getSource();
    }
}
