package com.developcollect.commonpay;

import com.developcollect.commonpay.pay.IRefundDTO;
import lombok.RequiredArgsConstructor;


/**
 * 重新指定支付平台订单
 *
 * @author zak
 * @since 1.8.6
 */
@RequiredArgsConstructor
public class RePayPlatformRefundDTO implements IRefundDTO {

    private final int payPlatform;
    private final IRefundDTO refund;


    @Override
    public String getOutRefundNo() {
        return refund.getOutRefundNo();
    }

    @Override
    public String getRefundNo() {
        return refund.getRefundNo();
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

    @Override
    public Object getExt(String key) {
        return refund.getExt(key);
    }

    @Override
    public void putExt(String key, Object extParameter) {
        refund.putExt(key, extParameter);
    }
}
