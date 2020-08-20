package com.developcollect.commonpay.pay.nopay;

import com.developcollect.commonpay.PayPlatform;
import com.developcollect.commonpay.exception.PayException;
import com.developcollect.commonpay.pay.*;

/**
 * 支付方式不存在
 *
 * @author zak
 * @since 1.0.0
 */
public class NoPay extends AbstractPay {

    @Override
    public String payPcForm(IPayDTO payDTO) {
        throw new PayException("支付方式不存在");
    }

    @Override
    public String payQrCode(IPayDTO payDTO) {
        throw new PayException("支付方式不存在");
    }

    @Override
    public TransferResponse transferSync(ITransferDTO transferDTO) {
        throw new PayException("支付方式不存在");
    }

    @Override
    public RefundResponse refundSync(IPayDTO payDTO, IRefundDTO refundDTO) {
        throw new PayException("支付方式不存在");
    }

    @Override
    protected int getPlatform() {
        return PayPlatform.NO_PAY;
    }
}
