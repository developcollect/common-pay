package com.developcollect.commonpay;

import com.developcollect.commonpay.pay.ITransferDTO;
import lombok.RequiredArgsConstructor;


/**
 * 重新指定支付平台订单
 *
 * @author zak
 * @since 1.8.6
 */
@RequiredArgsConstructor
public class RePayPlatformTransferDTO implements ITransferDTO {

    private final int payPlatform;
    private final ITransferDTO transfer;


    @Override
    public String getOutTransferNo() {
        return transfer.getOutTransferNo();
    }

    @Override
    public String getAccount() {
        return transfer.getAccount();
    }

    @Override
    public Long getAmount() {
        return transfer.getAmount();
    }

    @Override
    public boolean needCheckName() {
        return transfer.needCheckName();
    }

    @Override
    public String getReUserName() {
        return transfer.getReUserName();
    }

    @Override
    public String getDescription() {
        return transfer.getDescription();
    }

    @Override
    public Object getSource() {
        return transfer.getSource();
    }

    @Override
    public int getPayPlatform() {
        return payPlatform;
    }

    @Override
    public Object getExt(String key) {
        return transfer.getExt(key);
    }

    @Override
    public void putExt(String key, Object extParameter) {
        transfer.putExt(key, extParameter);
    }
}
