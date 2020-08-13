package com.developcollect.commonpay;

/**
 * 支付平台常量枚举
 * 之所以不使用枚举类型， 是为了方便扩展自定义支付平台
 * 如：
 * QQ支付，京东支付，龙支付，个人支付等等
 * @author zak
 * @since 1.0.0
 */
public interface PayPlatform {

    /**
     * 支付宝
     */
    int ALI_PAY = 1;

    /**
     * 微信
     */
    int WX_PAY = 2;


    /**
     * 不存在的平台
     */
    int NO_PAY = 0x80000000;

}
