package com.developcollect.commonpay.pay;

/**
 * 支付抽象接口
 *
 * @author zak
 * @since 1.0.0
 */
public interface Pay {

    /**
     * 支付
     * 通过付款二维码支付
     *
     * @param order 支付订单
     * @return String 二维码值
     * @author zak
     * @since 1.0.0
     */
    String payQrCode(IOrder order);


    /**
     * 通过跳转到第三方的支付PC表单实现支付
     *
     * @param order 支付订单
     * @return String html代码片段
     * @author zak
     * @since 1.0.0
     */
    String payPcForm(IOrder order);

    /**
     * 通过跳转到第三方的支付WAP表单实现支付
     *
     * @param order
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2020/8/15 10:55
     */
    String payWapForm(IOrder order);


    /**
     * 在微信浏览器里面使用WeixinJSBridge打开H5网页中执行JS调起支付
     * 仅微信支持
     *
     * @param order
     * @return void
     * @author Zhu Kaixiao
     * @date 2020/8/15 13:49
     */
    PayWxJsResult payWxJs(IOrder order);

    /**
     * 同步调用支付
     * 直接返回支付结果，不使用异步通知的方式
     *
     * @author zak
     * @since 1.0.0
     */
    PayResponse paySync(IOrder order);


    /**
     * 支付结果查询
     *
     * @author zak
     * @since 1.0.0
     */
    PayResponse payQuery(IOrder order);

    /**
     * 退款
     *
     * @param refund 退款信息
     * @return 退款结果
     * @author zak
     * @since 1.0.0
     */
    RefundResponse refundSync(IOrder order, IRefund refund);


    /**
     * 转账
     *
     * @param transfer 转账信息
     * @return 转账结果
     * @author zak
     * @since 1.0.0
     */
    TransferResponse transferSync(ITransfer transfer);

}
