package com.developcollect.commonpay;

import com.developcollect.commonpay.pay.IPayDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 重新指定支付平台支付信息
 *
 * @author zak
 * @since 1.8.6
 */
@RequiredArgsConstructor
public class RePayPlatformPayDTO implements IPayDTO {

    private final int payPlatform;
    private final IPayDTO payDTO;


    @Override
    public String getOutTradeNo() {
        return payDTO.getOutTradeNo();
    }

    @Override
    public String getTradeNo() {
        return payDTO.getTradeNo();
    }

    @Override
    public Long getTotalFee() {
        return payDTO.getTotalFee();
    }

    @Override
    public LocalDateTime getTimeStart() {
        return payDTO.getTimeStart();
    }

    @Override
    public LocalDateTime getTimeExpire() {
        return payDTO.getTimeExpire();
    }

    @Override
    public int getPayPlatform() {
        return payPlatform;
    }

    @Override
    public Object getSource() {
        return payDTO.getSource();
    }

    @Override
    public Object getExt(String key) {
        return payDTO.getExt(key);
    }

    @Override
    public void putExt(String key, Object extParameter) {
        payDTO.putExt(key, extParameter);
    }

    public IPayDTO getOriginPayDTO() {
        return payDTO;
    }
}
