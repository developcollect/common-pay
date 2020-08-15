package com.developcollect.commonpay;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.developcollect.commonpay.config.AbstractPayConfig;
import com.developcollect.commonpay.config.GlobalConfig;
import com.developcollect.commonpay.pay.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付工具类
 * @author zak
 * @since 1.0.0
 */
@Slf4j
public class PayUtil {

    /**
     * 支付(二维码)
     * 返回的是二维码的文本值, 可根据该文本值生成二维码图片
     *
     * @param order 订单
     * @return java.lang.String 二维码值
     * @author zak
     * @since 1.0.0
     */
    public static String payQrCode(IOrder order) {
        Pay pay = GlobalConfig.payFactory().createPay(order.getPayPlatform());
        String code = pay.payQrCode(order);
        return code;
    }

    /**
     * 支付(二维码)
     * 返回的是一张png格式的二维码图片的base64字符串
     *
     * @param order 订单
     * @return java.lang.String 二维码图片转base64后的字符串
     * @author zak
     * @since 1.0.0
     */
    public static String payQrCodeBase64(IOrder order) {
        String code = payQrCode(order);
        AbstractPayConfig payConfig = GlobalConfig.getPayConfig(order.getPayPlatform());
        int qrCodeWidth = payConfig.getQrCodeWidth();
        int qrCodeHeight = payConfig.getQrCodeHeight();
        byte[] qrCodeBytes = QrCodeUtil.generatePng(code, qrCodeWidth, qrCodeHeight);
        String qrCodeBase64 = Base64.encode(qrCodeBytes);
        return qrCodeBase64;
    }

    /**
     * 支付(二维码)
     * 返回的是二维码图片的访问链接
     * 这个链接是通过配置的链接生成器生成的
     *
     * @param order   订单
     * @return java.lang.String  二维码图片的访问链接
     * @author zak
     * @since 1.0.0
     */
    public static String payQrCodeAccessUrl(IOrder order) {
        String code = payQrCode(order);
        String accessUrl = GlobalConfig
                .getPayConfig(order.getPayPlatform())
                .getPayQrCodeAccessUrlGenerator()
                .apply(order, code);
        return accessUrl;
    }

    /**
     * 支付(PC页面跳转方式)
     * 返回的是一段html代码
     *
     * @param order 订单
     * @return html代码段
     * @author zak
     * @since 1.0.0
     */
    public static String payPcForm(IOrder order) {
        Pay pay = GlobalConfig.payFactory().createPay(order.getPayPlatform());
        String form = pay.payPcForm(order);
        return form;
    }


    /**
     * 支付(PC页面跳转方式)
     * 返回的是一个页面链接
     *
     * @param order 订单
     * @return 支付页面链接
     * @author zak
     * @since 1.0.0
     */
    public static String payPcFormAccessUrl(IOrder order) {
        String form = payPcForm(order);
        String accessUrl = GlobalConfig
                .getPayConfig(order.getPayPlatform())
                .getPcPayFormHtmlAccessUrlGenerator()
                .apply(order, form);
        return accessUrl;
    }


    /**
     * 支付(PC页面跳转方式)
     * 返回的是一段html代码
     *
     * @param order 订单
     * @return html代码段
     * @author zak
     * @since 1.0.0
     */
    public static String payWapForm(IOrder order) {
        Pay pay = GlobalConfig.payFactory().createPay(order.getPayPlatform());
        String form = pay.payWapForm(order);
        return form;
    }


    /**
     * 支付(PC页面跳转方式)
     * 返回的是一个页面链接
     *
     * @param order 订单
     * @return 支付页面链接
     * @author zak
     * @since 1.0.0
     */
    public static String payWapFormAccessUrl(IOrder order) {
        String form = payWapForm(order);
        String accessUrl = GlobalConfig
                .getPayConfig(order.getPayPlatform())
                .getWapPayFormHtmlAccessUrlGenerator()
                .apply(order, form);
        return accessUrl;
    }

    /**
     * 支付(同步支付)
     * 直接返回支付结果,而不是通过异步通知的形式
     *
     * @param order 订单
     * @return PayResponse 支付结果
     * @author zak
     * @since 1.0.0
     */
    public static PayResponse paySync(IOrder order) {
        Pay pay = GlobalConfig.payFactory().createPay(order.getPayPlatform());
        PayResponse payResponse = pay.paySync(order);
        // 如果广播失败了不会重试
        boolean broadcast = GlobalConfig.payBroadcaster().broadcast(payResponse);
        if (!broadcast) {
            log.error("订单[{}]支付结果广播失败", order.getOutTradeNo());
        }
        return payResponse;
    }

    /**
     * 查询订单支付结果
     * 查询出错时返回null
     *
     * @param order 订单
     * @return 订单支付结果
     * @author zak
     * @since 1.0.0
     */
    public static PayResponse payQuery(IOrder order) {
        Pay pay = GlobalConfig.payFactory().createPay(order.getPayPlatform());
        PayResponse payResponse = pay.payQuery(order);
        return payResponse;
    }


    /**
     * 退款(同步方法)
     * 直接返回退款结果,而不是通过异步通知的形式
     *
     * @param order 订单对象
     * @param refund 退款对象
     * @return RefundResponse 退款结果
     * @author zak
     * @since 1.0.0
     */
    public static RefundResponse refundSync(IOrder order, IRefund refund) {
        Pay pay = GlobalConfig.payFactory().createPay(refund.getPayPlatform());
        RefundResponse refundResponse = pay.refundSync(order, refund);
        return refundResponse;
    }


    /**
     * 转账(同步方法)
     * 直接返回转账结果,而不是通过异步通知的形式
     *
     * @param transfer 转账对象
     * @return TransferResponse 转账结果
     * @author zak
     * @since 1.0.0
     */
    public static TransferResponse transferSync(ITransfer transfer) {
        Pay pay = GlobalConfig.payFactory().createPay(transfer.getPayPlatform());
        TransferResponse transferResponse = pay.transferSync(transfer);
        return transferResponse;
    }


}
