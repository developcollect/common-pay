package com.developcollect.commonpay.pay;

import com.developcollect.commonpay.config.AbstractPayConfig;
import com.developcollect.commonpay.config.GlobalConfig;
import com.developcollect.commonpay.exception.PayException;

/**
 * 支付基类
 *
 * @author zak
 * @since 1.0.0
 */
public abstract class AbstractPay implements Pay {

    protected abstract int getPlatform();

    protected <T extends AbstractPayConfig> T getPayConfig() {
        return GlobalConfig.getPayConfig(getPlatform());
    }

    @Override
    public PayAppResult payApp(IOrder order) {
        throw new PayException("暂不支持App支付");
    }

    @Override
    public String payQrCode(IOrder order) {
        throw new PayException("暂不支持二维码支付");
    }

    @Override
    public String payPcForm(IOrder order) {
        throw new PayException("暂不支持跳转表单支付");
    }

    @Override
    public String payWapForm(IOrder order) {
        throw new PayException("暂不支持跳转WAP表单支付");
    }

    @Override
    public PayWxJsResult payWxJs(IOrder order, String openId) {
        throw new PayException("暂不支持微信JS支付");
    }

    @Override
    public PayResponse paySync(IOrder order) {
        throw new PayException("暂不支持同步调用支付");
    }

    @Override
    public PayResponse payQuery(IOrder order) {
        throw new PayException("暂不支持支付结果查询");
    }

    @Override
    public RefundResponse refundSync(IOrder order, IRefund refund) {
        throw new PayException("暂不支持退款");
    }

    @Override
    public TransferResponse transferSync(ITransfer transfer) {
        throw new PayException("暂不支持转账");
    }
}
