package com.developcollect.commonpay.pay;

import java.io.Serializable;

/**
 * 支付抽象接口
 *
 * @author zak
 * @since 1.0.0
 */
public interface Pay extends Serializable {


    /**
     * 扫用户付款码支付
     *
     * @param payDTO    支付参数
     */
    PayResponse payScan(IPayDTO payDTO);

    /**
     * app支付
     * 有些平台(如: 微信)在app支付时需要后台预下单,然后app根据预下单结果进行支付
     * 有的(如: 支付宝)不需要,只要app直接调用平台支付接口就行
     */
    PayAppResult payApp(IPayDTO payDTO);


    /**
     * 支付
     * 通过付款二维码支付
     *
     * @param payDTO 支付参数
     * @return String 二维码值
     * @author zak
     * @since 1.0.0
     */
    String payQrCode(IPayDTO payDTO);


    /**
     * 通过跳转到第三方的支付PC表单实现支付
     *
     * @param payDTO 支付参数
     * @return String html代码片段
     * @author zak
     * @since 1.0.0
     */
    String payPcForm(IPayDTO payDTO);

    /**
     * 通过跳转到第三方的支付WAP表单实现支付
     *
     * @param payDTO
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2020/8/15 10:55
     */
    String payWapForm(IPayDTO payDTO);


    /**
     * 在微信浏览器里面使用WeixinJSBridge打开H5网页中执行JS调起支付
     * 仅微信支持
     *
     * @param payDTO 支付参数
     * @return PayWxJsResult
     * @author Zhu Kaixiao
     * @date 2020/8/15 13:49
     */
    PayWxJsResult payWxJs(IPayDTO payDTO);

    /**
     * 同步调用支付
     * 直接返回支付结果，不使用异步通知的方式
     *
     * @author zak
     * @since 1.0.0
     */
    PayResponse paySync(IPayDTO payDTO);


    /**
     * 支付结果查询
     *
     * @author zak
     * @since 1.0.0
     */
    PayResponse payQuery(IPayDTO payDTO);

    /**
     * 退款
     *
     * @param refundDTO 退款信息
     * @return 退款结果
     * @author zak
     * @since 1.0.0
     */
    RefundResponse refundSync(IPayDTO payDTO, IRefundDTO refundDTO);


    /**
     * 转账
     *
     * @param transferDTO 转账信息
     * @return 转账结果
     * @author zak
     * @since 1.0.0
     */
    TransferResponse transferSync(ITransferDTO transferDTO);

}
