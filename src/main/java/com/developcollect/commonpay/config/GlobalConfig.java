package com.developcollect.commonpay.config;

import com.developcollect.commonpay.notice.*;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


/**
 * 全局配置
 * 当前项目中所有的配置都集中在这个类中
 * @author zak
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class GlobalConfig {

    /**
     * 支付配置生成器
     */
    private Map<Integer, Supplier<? extends AbstractPayConfig>> payConfigSupplierMap = new ConcurrentHashMap<>();

    /**
     * pay工厂
     */
    private IPayFactory payFactory = new DefaultPayFactory();

    /**
     * 支付结果广播器
     */
    private IPayBroadcaster payBroadcaster;

    /**
     * 退款结果广播器
     */
    private IRefundBroadcaster refundBroadcaster;

    /**
     * 转账结果广播器
     */
    private ITransferBroadcaster transferBroadcaster;

    /**
     * 未确认订单提取器
     */
    private IUnconfirmedOrderFetcher unconfirmedOrderFetcher;

    /**
     * 主动查询通知管理器
     */
    private QueryNoticeManager queryNoticeManager = new QueryNoticeManager();

    /**
     * 查询通知间隔， 单位毫秒
     */
    private long queryNoticeDelay = 10 * 60 * 1000;



    /**
     * 持有实例
     */
    private static GlobalConfig GLOBAL_CONFIG;

    @Resource
    private GlobalConfig globalConfig;

    public GlobalConfig() {
    }

    /**
     * 配置初始化方法
     * 在配置设置好后要调用该方法进行初始化
     * 之所以设置为私有类型是为了防止其他地方误调用， 所以要调用该方法的话需要反射调用
     *
     * @author zak
     * @since 1.0.0
     */
    private void init() {
        GLOBAL_CONFIG = globalConfig;
        this.getQueryNoticeManager().init();
    }

    /**
     * 获取当前全局配置实例
     * @return 全局配置
     * @author zak
     * @since 1.0.0
     */
    private static GlobalConfig getInstance() {
        return GLOBAL_CONFIG;
    }


    /**
     * 根据支付平台获取相应的支付配置
     * @param payPlatform 支付平台 取值见{@link com.zakwz.commonpay.PayPlatform}
     * @param <T> 支付配置的类型
     * @return 支付配置
     * @author zak
     * @since 1.0.0
     */
    public static <T extends AbstractPayConfig> T getPayConfig(int payPlatform) {
        AbstractPayConfig payConfig = getInstance()
                .getPayConfigSupplierMap()
                .get(payPlatform)
                .get();
        return (T) payConfig;
    }

    /**
     * 获取当前配置的支付工厂
     * @return 支付工厂
     * @author zak
     * @since 1.0.0
     */
    public static IPayFactory payFactory() {
        return getInstance().getPayFactory();
    }

    /**
     * 获取当前配置的未确认订单提取器
     * @return 未确认订单提取器
     * @author zak
     * @since 1.0.0
     */
    public static IUnconfirmedOrderFetcher unconfirmedOrderFetcher() {
        return getInstance().getUnconfirmedOrderFetcher();
    }

    /**
     * 获取当前配置的支付结果广播器
     * @return 支付结果广播器
     * @author zak
     * @since 1.0.0
     */
    public static IPayBroadcaster payBroadcaster() {
        return getInstance().getPayBroadcaster();
    }

    /**
     * 获取当前配置的退款结果广播器
     *
     * @return 退款结果广播器
     * @author zak
     * @since 1.0.0
     */
    public static IRefundBroadcaster refundBroadcaster() {
        return getInstance().getRefundBroadcaster();
    }

    /**
     * 获取当前配置的转账结果广播器
     *
     * @return 转账结果广播器
     * @author zak
     * @since 1.0.0
     */
    public static ITransferBroadcaster transferBroadcaster() {
        return getInstance().getTransferBroadcaster();
    }

    /**
     * 校验指定的支付平台是否有支付配置
     * @param payPlatform 支付平台
     * @return 有支付配置返回true， 否则返回false
     * @author zak
     * @since 1.0.0
     */
    public static boolean payPlatformVerify(int payPlatform) {
        return getInstance().getPayConfigSupplierMap().containsKey(payPlatform);
    }

    /**
     * 获取主动查询的时间间隔
     * 主动查询订单支付状态的定时器时间间隔就是用的这个值
     * @return 主动查询的时间间隔
     * @author zak
     * @since 1.0.0
     */
    public static long queryNoticeDelay() {
        return getInstance().getQueryNoticeDelay();
    }


    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GlobalConfig)) {
            return false;
        }
        final GlobalConfig other = (GlobalConfig) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$payConfigSupplierMap = this.getPayConfigSupplierMap();
        final Object other$payConfigSupplierMap = other.getPayConfigSupplierMap();
        if (this$payConfigSupplierMap == null ? other$payConfigSupplierMap != null : !this$payConfigSupplierMap.equals(other$payConfigSupplierMap)) {
            return false;
        }
        final Object this$payFactory = this.getPayFactory();
        final Object other$payFactory = other.getPayFactory();
        if (this$payFactory == null ? other$payFactory != null : !this$payFactory.equals(other$payFactory)) {
            return false;
        }
        final Object this$payBroadcaster = this.getPayBroadcaster();
        final Object other$payBroadcaster = other.getPayBroadcaster();
        if (this$payBroadcaster == null ? other$payBroadcaster != null : !this$payBroadcaster.equals(other$payBroadcaster)) {
            return false;
        }
        final Object this$unconfirmedOrderFetcher = this.getUnconfirmedOrderFetcher();
        final Object other$unconfirmedOrderFetcher = other.getUnconfirmedOrderFetcher();
        if (this$unconfirmedOrderFetcher == null ? other$unconfirmedOrderFetcher != null : !this$unconfirmedOrderFetcher.equals(other$unconfirmedOrderFetcher)) {
            return false;
        }
        final Object this$queryNoticeManager = this.getQueryNoticeManager();
        final Object other$queryNoticeManager = other.getQueryNoticeManager();
        if (this$queryNoticeManager == null ? other$queryNoticeManager != null : !this$queryNoticeManager.equals(other$queryNoticeManager)) {
            return false;
        }
        if (this.getQueryNoticeDelay() != other.getQueryNoticeDelay()) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof GlobalConfig;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $payConfigSupplierMap = this.getPayConfigSupplierMap();
        result = result * PRIME + ($payConfigSupplierMap == null ? 43 : $payConfigSupplierMap.hashCode());
        final Object $payFactory = this.getPayFactory();
        result = result * PRIME + ($payFactory == null ? 43 : $payFactory.hashCode());
        final Object $payBroadcaster = this.getPayBroadcaster();
        result = result * PRIME + ($payBroadcaster == null ? 43 : $payBroadcaster.hashCode());
        final Object $unconfirmedOrderFetcher = this.getUnconfirmedOrderFetcher();
        result = result * PRIME + ($unconfirmedOrderFetcher == null ? 43 : $unconfirmedOrderFetcher.hashCode());
        final Object $queryNoticeManager = this.getQueryNoticeManager();
        result = result * PRIME + ($queryNoticeManager == null ? 43 : $queryNoticeManager.hashCode());
        final long $queryNoticeDelay = this.getQueryNoticeDelay();
        result = result * PRIME + (int) ($queryNoticeDelay >>> 32 ^ $queryNoticeDelay);
        return result;
    }

    @Override
    public String toString() {
        return "GlobalConfig(payConfigSupplierMap=" + this.getPayConfigSupplierMap() + ", payFactory=" + this.getPayFactory() + ", payBroadcaster=" + this.getPayBroadcaster() + ", unconfirmedOrderFetcher=" + this.getUnconfirmedOrderFetcher() + ", queryNoticeManager=" + this.getQueryNoticeManager() + ", queryNoticeDelay=" + this.getQueryNoticeDelay() + ")";
    }
}
