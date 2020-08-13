package com.developcollect.commonpay.config;

import com.developcollect.commonpay.pay.IOrder;
import com.developcollect.commonpay.pay.IRefund;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * 支付配置基类
 *
 * @author zak
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public abstract class AbstractPayConfig {

    /**
     * 是否启用debug模式
     * 有些支付平台会提供测试环境， 比如支付宝有提供沙箱环境
     * 如果支付平台本身不支持测试环境， 那么这个值设置了也没用
     */
    private boolean debug = false;

    /**
     * 退款时的回调地址生成器
     */
    protected BiFunction<IOrder, IRefund, String> refundNotifyUrlGenerator;

    /**
     * 支付时的回调地址生成器
     */
    protected Function<IOrder, String> payNotifyUrlGenerator;

    /**
     * 支付完成跳转地址生成器
     */
    protected Function<IOrder, String> returnUrlGenerator;

    /**
     * 支付二维码访问链接生成器
     */
    protected BiFunction<IOrder, String, String> payQrCodeAccessUrlGenerator;

    /**
     * 支付页面访问链接生成器
     */
    protected BiFunction<IOrder, String, String> payFormHtmlAccessUrlGenerator;


    /**
     * 二维码宽度
     */
    private int qrCodeWidth = 300;

    /**
     * 二维码高度
     */
    private int qrCodeHeight = 300;

}
