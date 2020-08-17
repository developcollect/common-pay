package com.developcollect.commonpay;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.developcollect.commonpay.config.AbstractPayConfig;
import com.developcollect.commonpay.config.GlobalConfig;
import com.developcollect.commonpay.exception.PayException;
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
     * 不使用订单中的支付平台, 而是用指定的支付平台支付(二维码)
     * 返回的是二维码的文本值, 可根据该文本值生成二维码图片
     *
     * @param payPlatform 支付平台
     * @param order       订单
     * @return java.lang.String 二维码值
     * @author zak
     * @since 1.8.6
     */
    public static String payQrCode(int payPlatform, IOrder order) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return payQrCode(rePayPlatformOrder);
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
     * 不使用订单中的支付平台, 而是用指定的支付平台支付(二维码)
     * 返回的是一张png格式的二维码图片的base64字符串
     *
     * @param payPlatform 支付平台
     * @param order       订单
     * @return java.lang.String 二维码图片转base64后的字符串
     * @author zak
     * @since 1.8.6
     */
    public static String payQrCodeBase64(int payPlatform, IOrder order) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return payQrCodeBase64(rePayPlatformOrder);
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
     * 不使用订单中的支付平台, 而是用指定的支付平台支付(二维码)
     * 返回的是二维码图片的访问链接
     * 这个链接是通过配置的链接生成器生成的
     *
     * @param order 订单
     * @return java.lang.String  二维码图片的访问链接
     * @author zak
     * @since 1.8.6
     */
    public static String payQrCodeAccessUrl(int payPlatform, IOrder order) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return payQrCodeAccessUrl(rePayPlatformOrder);
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
     * 不使用订单中的支付平台, 而是用指定的支付平台支付(PC页面跳转方式)
     * 返回的是一段html代码
     *
     * @param order 订单
     * @return html代码段
     * @author zak
     * @since 1.8.6
     */
    public static String payPcForm(int payPlatform, IOrder order) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return payPcForm(rePayPlatformOrder);
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
     * 不使用订单中的支付平台, 而是用指定的支付平台支付(PC页面跳转方式)
     * 返回的是一个页面链接
     *
     * @param payPlatform 支付平台
     * @param order       订单
     * @return 支付页面链接
     * @author zak
     * @since 1.8.6
     */
    public static String payPcFormAccessUrl(int payPlatform, IOrder order) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return payPcFormAccessUrl(rePayPlatformOrder);
    }


    /**
     * 支付(WAP页面跳转方式)
     * 返回的是一段html代码
     *
     * @param order 订单
     * @return html代码段
     * @author zak
     * @since 1.0.0
     */
    public static String payWapForm(IOrder order) {
        if (order.getPayPlatform() == PayPlatform.WX_PAY) {
            // 微信是直接返回的url, 没有返回html代码的接口
            throw new PayException("暂不支持返回WAP页面代码");
        }
        Pay pay = GlobalConfig.payFactory().createPay(order.getPayPlatform());
        String form = pay.payWapForm(order);
        return form;
    }

    /**
     * 不使用订单中的支付平台, 而是用指定的支付平台支付(WAP页面跳转方式)
     * 返回的是一段html代码
     *
     * @param payPlatform 支付平台
     * @param order       订单
     * @return html代码段
     * @author zak
     * @since 1.8.6
     */
    public static String payWapForm(int payPlatform, IOrder order) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return payWapForm(rePayPlatformOrder);
    }

    /**
     * 在微信浏览器里面使用WeixinJSBridge打开H5网页中执行JS调起支付
     * 仅微信支持
     *
     * @param order 订单
     * @param openId 微信用户标识
     * @return WxJsPayResult
     */
    public static PayWxJsResult payWxJs(IOrder order, String openId) {
        Pay pay = GlobalConfig.payFactory().createPay(order.getPayPlatform());
        PayWxJsResult payWxJsResult = pay.payWxJs(order, openId);
        return payWxJsResult;
    }

    /**
     * 不使用订单中的支付平台, 而是用指定的支付平台在微信浏览器里面使用WeixinJSBridge打开H5网页中执行JS调起支付
     * 仅微信支持
     *
     * @param payPlatform 支付平台
     * @param order       订单
     * @param openId      微信用户标识
     * @return WxJsPayResult
     * @author zak
     * @since 1.8.6
     */
    public static PayWxJsResult payWxJs(int payPlatform, IOrder order, String openId) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return payWxJs(rePayPlatformOrder, openId);
    }

    /**
     * 支付(WAP页面跳转方式)
     * 返回的是一个页面链接
     *
     * @param order 订单
     * @return 支付页面链接
     * @author zak
     * @since 1.0.0
     */
    public static String payWapFormAccessUrl(IOrder order) {
        if (order.getPayPlatform() == PayPlatform.WX_PAY) {
            // 微信是直接返回的url, 不需要通过访问链接生成器生成
            Pay pay = GlobalConfig.payFactory().createPay(order.getPayPlatform());
            String form = pay.payWapForm(order);
            return form;
        }

        String form = payWapForm(order);
        String accessUrl = GlobalConfig
                .getPayConfig(order.getPayPlatform())
                .getWapPayFormHtmlAccessUrlGenerator()
                .apply(order, form);
        return accessUrl;
    }

    /**
     * 不使用订单中的支付平台, 而是用指定的支付平台支付(WAP页面跳转方式)
     * 返回的是一个页面链接
     *
     * @param payPlatform 支付平台
     * @param order       订单
     * @return 支付页面链接
     * @author zak
     * @since 1.8.6
     */
    public static String payWapFormAccessUrl(int payPlatform, IOrder order) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return payWapFormAccessUrl(rePayPlatformOrder);
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
     * 不使用订单中的支付平台, 而是用指定的支付平台支付(同步支付)
     * 直接返回支付结果,而不是通过异步通知的形式
     *
     * @param payPlatform 支付平台
     * @param order       订单
     * @return 支付结果
     * @author zak
     * @since 1.8.6
     */
    public static PayResponse paySync(int payPlatform, IOrder order) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return paySync(rePayPlatformOrder);
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
     * 不使用订单中的支付平台, 而是用指定的支付平台查询订单支付结果
     * 查询出错时返回null
     *
     * @param payPlatform 支付平台
     * @param order       订单
     * @return 支付结果
     * @author zak
     * @since 1.8.6
     */
    public static PayResponse payQuery(int payPlatform, IOrder order) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        return payQuery(rePayPlatformOrder);
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
        if (GlobalConfig.refundBroadcaster() != null) {
            // 如果广播失败了不会重试
            boolean broadcast = GlobalConfig.refundBroadcaster().broadcast(refundResponse);
            if (!broadcast) {
                log.error("退款[{}]结果广播失败", refund.getOutRefundNo());
            }
        }
        return refundResponse;
    }

    /**
     * 不使用退款对象中的支付平台, 而是用指定的支付平台退款(同步方法)
     * 直接返回退款结果,而不是通过异步通知的形式
     *
     * @param payPlatform 支付平台
     * @param order       订单
     * @param refund      退款对象
     * @return 退款结果
     * @author zak
     * @since 1.8.6
     */
    public static RefundResponse refundSync(int payPlatform, IOrder order, IRefund refund) {
        RePayPlatformOrder rePayPlatformOrder = new RePayPlatformOrder(payPlatform, order);
        RePayPlatformRefund rePayPlatformRefund = new RePayPlatformRefund(payPlatform, refund);
        return refundSync(rePayPlatformOrder, rePayPlatformRefund);
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

        if (GlobalConfig.transferBroadcaster() != null) {
            // 如果广播失败了不会重试
            boolean broadcast = GlobalConfig.transferBroadcaster().broadcast(transferResponse);
            if (!broadcast) {
                log.error("转账[{}]结果广播失败", transfer.getOutTransferNo());
            }
        }
        return transferResponse;
    }


    /**
     * 不使用转账对象中的支付平台, 而是用指定的支付平台转账(同步方法)
     * 直接返回转账结果,而不是通过异步通知的形式
     *
     * @param payPlatform 支付平台
     * @param transfer    转账对象
     * @return TransferResponse 转账结果
     * @author zak
     * @since 1.8.6
     */
    public static TransferResponse transferSync(int payPlatform, ITransfer transfer) {
        RePayPlatformTransfer rePayPlatformTransfer = new RePayPlatformTransfer(payPlatform, transfer);
        return transferSync(rePayPlatformTransfer);
    }
}
