package com.developcollect.commonpay.pay.wxpay.sdk;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class WXPayConfig {


    /**
     * 获取 App ID
     *
     * @return App ID
     */
    public abstract String getAppID();


    /**
     * 获取 Mch ID
     *
     * @return Mch ID
     */
    public abstract String getMchID();


    /**
     * 获取 API 密钥
     *
     * @return API密钥
     */
    public abstract String getKey();


    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    public InputStream getCertStream() {
        return null;
    }

    /**
     * HTTP(S) 连接超时时间，单位毫秒
     *
     * @return
     */
    public int getHttpConnectTimeoutMs() {
        return 6 * 1000;
    }

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     *
     * @return
     */
    public int getHttpReadTimeoutMs() {
        return 8 * 1000;
    }

    /**
     * 获取WXPayDomain, 用于多域名容灾自动切换
     *
     * @return
     */
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {

            private DomainInfo primaryDomainInfo = new DomainInfo("api.mch.weixin.qq.com", true);
            private DomainInfo secondDomainInfo = new DomainInfo("api2.mch.weixin.qq.com", true);

            private volatile boolean primaryNormal = true;
            private ScheduledExecutorService scheduledExecutorService;
            private Lock lock = new ReentrantLock();

            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
                if (ex instanceof UnknownHostException || ex instanceof ConnectTimeoutException) {
                    if (primaryDomainInfo.domain.equals(domain)) {
                        primaryNormal = false;
                        // 定时探测主域名可用性, 可用就换回主域名
                        if (scheduledExecutorService == null) {
                            if (lock.tryLock()) {

                                scheduledExecutorService = Executors.newScheduledThreadPool(1,
                                        ThreadFactoryBuilder.create().setNamePrefix("weixinconfig-scheduled-pool-").setDaemon(true).build());
                                scheduledExecutorService.scheduleWithFixedDelay(() -> {
                                    String ipByHost = NetUtil.getIpByHost("api.mch.weixin.qq.com");
                                    if (!"api.mch.weixin.qq.com".equals(ipByHost)) {
                                        primaryNormal = true;
                                        scheduledExecutorService.shutdown();
                                        scheduledExecutorService = null;
                                    }
                                }, 0, 30, TimeUnit.MINUTES);
                            }
                        }
                    }
                }
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return primaryNormal ? primaryDomainInfo : secondDomainInfo;
            }
        };
    }

    /**
     * 是否自动上报。
     * 若要关闭自动上报，子类中实现该函数返回 false 即可。
     *
     * @return
     */
    public boolean shouldAutoReport() {
        return true;
    }

    /**
     * 进行健康上报的线程的数量
     *
     * @return
     */
    public int getReportWorkerNum() {
        return 6;
    }


    /**
     * 健康上报缓存消息的最大数量。会有线程去独立上报
     * 粗略计算：加入一条消息200B，10000消息占用空间 2000 KB，约为2MB，可以接受
     *
     * @return
     */
    public int getReportQueueMaxSize() {
        return 10000;
    }

    /**
     * 批量上报，一次最多上报多个数据
     *
     * @return
     */
    public int getReportBatchSize() {
        return 10;
    }

    static int tmp = 3;

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1,
                ThreadFactoryBuilder.create().setNamePrefix("weixinconfig-scheduled-pool-").setDaemon(true).build());

        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println(System.currentTimeMillis());
            --tmp;
            if (tmp < 0) {
                System.out.println("shutdown");
                scheduledExecutorService.shutdown();
            }
        }, 0, 3, TimeUnit.SECONDS);


        Thread.sleep(15000);

        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println("第二个任务" + System.currentTimeMillis());

        }, 0, 3, TimeUnit.SECONDS);
    }
}
