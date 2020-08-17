# common-pay

### 简介

common-pay是一个封装了常见平台支付方法调用的工具（其实目前只支持支付宝支付和微信支付），其中包括跳转网页支付、扫描二维码支付、订单退款和转账方法。为各平台提供了统一的调用方法，以及一致的方法返回值，它节省了开发人员对支付接口的对接时间，使开发专注于业务。

|        | 支付二维码码值 | 支付二维码BASE64 | 支付二维码访问链接 | PC支付页面HTML代码 | PC支付页面访问链接 | WAP支付页面HTML代码 | WAP支付页面访问链接 | 微信JS支付 | APP支付 | 退款 | 转账 |
| :----: | :------------: | :--------------: | :----------------: | :----------------: | :----------------: | :-----------------: | :-----------------: | :--------: | :-----: | :--: | :--: |
| 支付宝 |       ✓        |        ✓         |         ✓          |         ✓          |         ✓          |          ✓          |          ✓          |     ✗      |    ○    |  ✓   |  ✓   |
|  微信  |       ✓        |        ✓         |         ✓          |         ✗          |         ✗          |          ✗          |          ✓          |     ✓      |    ○    |  ✓   |  ✓   |

> ​      ✓：支持               ✗： 不支持               ○：暂未加入





### 插播一句

**spring boot项目使用[common-pay-spring-boot-starter](https://github.com/developcollect/common-pay-spring-boot-starter)更香哦~**

**具体使用案例可参考[common-pay-sample](https://github.com/developcollect/common-pay-sample)**





### 安装

##### Maven

```xml
<dependency>
  <groupId>com.developcollect</groupId>
  <artifactId>common-pay</artifactId>
  <version>1.8.8</version>
</dependency>
```

##### Gradle

```groovy
implementation 'com.developcollect:common-pay:1.8.8'
```

##### 非Maven项目

点击以下任一链接，下载`common-pay-X.X.X.jar`即可：

* [Maven中央仓库1](https://repo1.maven.org/maven2/com/developcollect/common-pay/1.8.8/)
* [Maven中央仓库2](https://repo2.maven.org/maven2/com/developcollect/common-pay/1.8.8/)

> 注意：common-pay仅支持JDK8+





### 目录结构描述

```txt
commonpay
│  PayPlatform.java                                         // 支付平台常量
│  PayUtil.java                                             // 支付工具类(重点)
│  
├─config                                                    // 支付相关配置包
│      AbstractPayConfig.java                               // 通用支付配置
│      AliPayConfig.java                                    // 支付宝支付配置
│      DefaultPayFactory.java                               // 默认的Pay工厂
│      GlobalConfig.java                                    // 全局配置(重点)
│      IPayFactory.java                                     // Pay工厂接口
│      WxPayConfig.java                                     // 微信支付配置
│      
├─exception                                                 // 异常包
│      AbstractSimpleParameterRuntimeException.java         
│      ConfigException.java                                 // 配置错误异常
│      PayException.java                                    // 支付流程异常
│      
├─notice                                                    // 通知包
│      IPayBroadcaster.java                                 // 支付结果广播器接口
│      IRefundBroadcaster.java                              // 退款结果广播器接口
│      ITransferBroadcaster.java                            // 转账结果广播器接口
│      IUnconfirmedOrderFetcher.java                        // 未确认订单提取器接口
│      Page.java                                            // 分页
│      QueryNoticeManager.java                              // 主动查询通知管理器
│      
├─pay                                                       // 支付相关包
│  │  AbstractPay.java                                      // 所有Pay基类
│  │  IOrder.java                                           // 订单抽象接口
│  │  IRefund.java                                          // 退款抽象接口
│  │  ITransfer.java                                        // 转账抽象接口
│  │  Pay.java                                              // 支付接口(重点)
│  │  PayResponse.java                                      // 支付结果
│  │  RefundResponse.java                                   // 退款结果
│  │  TransferResponse.java                                 // 转账结果
│  │  
│  ├─alipay                                                 // 支付宝支付
│  │  │  Alipay.java                                        // 支付宝支付实现
│  │  │  package-info.java                                  
│  │  │  
│  │  └─bean                                                // 支付宝调用参数实体
│  │          AliPayDTO.java
│  │          GoodsDetail.java
│  │          PayData.java
│  │          Payee.java
│  │          PayQueryData.java
│  │          RefundData.java
│  │          RoyaltyDetailData.java
│  │          TransferData.java
│  │          
│  ├─nopay
│  │      NoPay.java                                        // 无支付(取代null的“空对象”)
│  │      
│  └─wxpay                                                  // 微信支付
│      │  DefaultWXPayConfig.java                           // 微信SDK配置默认实现
│      │  package-info.java
│      │  WxPay.java                                        // 微信支付实现
│      │  
│      ├─bean                                               // 微信调用参数实体
│      │      WxPayDTO.java
│      │      WxRefundDTO.java
│      │      
│      └─sdk                                                // 微信SDK
│              IWXPayDomain.java
│              WXPay.java
│              WXPayConfig.java
│              WXPayConstants.java
│              WXPayReport.java
│              WXPayRequest.java
│              WXPayUtil.java
│              WXPayXmlUtil.java
│              
└─utils                                                     // 工具包
    LambdaUtil.java                                         // lambda工具
    SerializeUtil.java                                      // 序列化工具
    UnitUtil.java                                           // 单位换算工具
```





### 使用

1. 创建GlobalConfig对象并配置支付所需的各项参数*(支付平台支付参数、返回地址生成器、跳转地址生成器、支付结果广播器等等......)*，具体参数因支付平台不同而不同，最终将各参数赋值到GlobalConfig中的相应字段
2. 通过反射调用**GlobalConfig#init()**方法， 触发配置的初始化（之所以将init()设置为私有方法，是为了防止其他地方误调用）
3. 创建订单适配器，实现本地订单对象与IOrder接口的适配
4. 在适当的位置监听支付结果
5. 调用**PayUtil**中的相应方法
6. 这一步是来凑个六六大顺的，这时你已经可以拿起手机来支付了





### 提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本、common-pay版本和相关依赖库版本。

* [Github issue](https://github.com/developcollect/common-pay-sample/issues)





### 添砖加瓦

emmm... 分支啥的都还没弄好，等弄好了我再来补~ 咕咕咕~~





### 写在最后

common-pay是我的第一个开源项目，花了我一个周末和好几个夜晚，为此连女朋友都冷落了~ 各位觉得好用麻烦点个star，并推荐给小伙伴；要是觉得不好用欢迎来提issue，并顺手推给你讨厌的朋友吧😂