package com.developcollect.commonpay.pay.alipay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.developcollect.commonpay.PayPlatform;
import com.developcollect.commonpay.config.AliPayConfig;
import com.developcollect.commonpay.exception.PayException;
import com.developcollect.commonpay.pay.*;
import com.developcollect.commonpay.pay.alipay.bean.PayData;
import com.developcollect.commonpay.pay.alipay.bean.PayQueryData;
import com.developcollect.commonpay.pay.alipay.bean.RefundData;
import com.developcollect.commonpay.pay.alipay.bean.TransferData;
import com.developcollect.commonpay.utils.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 支付宝支付
 *
 * @author zak
 * @since 1.0.0
 */
@Slf4j
public class Alipay extends AbstractPay {

    /**
     * 支付宝网关(固定)
     * 沙箱环境: https://openapi.alipaydev.com/gateway.do
     * 真实环境: https://openapi.alipay.com/gateway.do
     */
    private static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";
    private static final String SANDBOX_SERVER_URL = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 参数返回格式，只支持 json
     */
    private static final String FORMAT = "json";


    /**
     * 交易完成
     **/
    public static final String FINISHED = "TRADE_FINISHED";
    /**
     * 支付成功
     **/
    public static final String SUCCESS = "TRADE_SUCCESS";
    /**
     * 支付关闭
     **/
    public static final String CLOSED = "TRADE_CLOSED";

    /**
     * 支付宝成功返回常量
     **/
    public static final String SUCCESS_CODE = "10000";
    /**
     * 支付宝成功返回但未支付状态
     **/
    public static final String SUCCESS_NOT_PAY_CODE = "40000";

    /**
     * 销售产品码，与支付宝签约的产品码名称,必选, PC端支付使用
     **/
    public static final String PC_PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";
    /**
     * 销售产品码，与支付宝签约的产品码名称，必选, 手机端使用
     **/
    public static final String WAP_PRODUCT_CODE = "QUICK_WAP_WAY";

//    /**
//     * alipayClient是可重用的
//     */
//    private volatile AlipayClient alipayClient;

    /**
     * 支付宝预下单扫码支付
     * https://docs.open.alipay.com/api_1/alipay.trade.precreate
     *
     * @param order
     * @return java.lang.String
     */
    @Override
    public String payQrCode(IOrder order) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            AlipayTradePrecreateRequest preCreateRequest = new AlipayTradePrecreateRequest();
            preCreateRequest.setNotifyUrl(aliPayConfig.getPayNotifyUrlGenerator().apply(order));

            PayData payData = PayData.of(order);
            payData.setProductCode("FACE_TO_FACE_PAYMENT");

            String param = JSONObject.toJSONString(payData);
            log.debug("支付宝支付参数: {}", param);
            preCreateRequest.setBizContent(param);
            AlipayTradePrecreateResponse response = alipayClient.execute(preCreateRequest);

            //网关返回码,code 非10000接口调用失败，错误信息以subMsg属性返回
            //如code为10000，需要再次判断subCode是否为空，
            //为空情况下，则表示支付宝业务处理成功，否则表示业务处理失败，详细原因以subMsg属性返回
            if (!SUCCESS_CODE.equals(response.getCode()) || StrUtil.isNotBlank(response.getSubCode())) {
                throw new PayException(response.getSubMsg());
            }

            // 当前预下单请求生成的二维码码串，可以用二维码生成工具根据该码串值生成对应的二维码
            String qrCodeContent = response.getQrCode();
            return qrCodeContent;
        } catch (Throwable throwable) {
            log.error("支付宝支付失败", throwable);
            throw (throwable instanceof PayException)
                    ? (PayException) throwable
                    : new PayException("支付宝支付失败", throwable);
        }
    }


    /**
     * 支付宝跳转wap页面支付
     * <p>
     * https://opendocs.alipay.com/open/203/107090
     *
     * @param order
     * @return java.lang.String
     */
    @Override
    public String payWapForm(IOrder order) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            // 创建API对应的request
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            String notifyUrl = aliPayConfig.getPayNotifyUrlGenerator().apply(order);
            alipayRequest.setNotifyUrl(notifyUrl);

            // 在公共参数中设置回跳和通知地址
            if (aliPayConfig.getWapReturnUrlGenerator() != null) {
                alipayRequest.setReturnUrl(aliPayConfig.getWapReturnUrlGenerator().apply(order));
            }

            PayData payData = PayData.of(order);
            payData.setProductCode("QUICK_WAP_PAY");

            try {
                String param = JSONObject.toJSONString(payData);
                log.debug("支付宝支付参数: {}", param);
                alipayRequest.setBizContent(param);
                //调用SDK生成表单
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                return form;
            } catch (AlipayApiException e) {
                log.error("支付宝支付时生成WAP表单失败", e);
                throw new PayException("调起支付失败", e);
            }
        } catch (Throwable throwable) {
            log.error("支付宝WAP支付失败", throwable);
            throw (throwable instanceof PayException)
                    ? (PayException) throwable
                    : new PayException("支付宝WAP支付失败", throwable);
        }
    }

    /**
     * 支付宝统一收单下单并支付页面
     * https://opendocs.alipay.com/apis/api_1/alipay.trade.page.pay
     *
     * @param order
     * @return java.lang.String
     */
    @Override
    public String payPcForm(IOrder order) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            // 创建API对应的request
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            String notifyUrl = aliPayConfig.getPayNotifyUrlGenerator().apply(order);
            alipayRequest.setNotifyUrl(notifyUrl);

            // 在公共参数中设置回跳和通知地址
            if (aliPayConfig.getPcReturnUrlGenerator() != null) {
                alipayRequest.setReturnUrl(aliPayConfig.getPcReturnUrlGenerator().apply(order));
            }

            PayData payData = PayData.of(order);
            payData.setProductCode("FAST_INSTANT_TRADE_PAY");

            String param = JSONObject.toJSONString(payData);
            log.debug("支付宝支付参数: {}", param);
            alipayRequest.setBizContent(param);

            try {
                //调用SDK生成表单
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                return form;
            } catch (AlipayApiException e) {
                log.error("支付宝支付时生成表单失败", e);
                throw new PayException("调起支付失败", e);
            }

        } catch (Throwable throwable) {
            log.error("支付宝支付失败", throwable);
            throw (throwable instanceof PayException)
                    ? (PayException) throwable
                    : new PayException("支付宝支付失败", throwable);
        }
    }


    /**
     * 订单查询
     * https://opendocs.alipay.com/apis/api_1/alipay.trade.query
     *
     * @param order 订单
     * @return 订单支付结果
     */
    @Override
    public PayResponse payQuery(IOrder order) {
        try {
            AliPayConfig payConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(payConfig);

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

            PayQueryData payQueryData = PayQueryData.of(order);

            request.setBizContent(SerializeUtil.beanToJson(payQueryData));

            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                throw new PayException(response.getSubMsg());
            }

            PayResponse payResponse = PayResponse.of(response);
            return payResponse;
        } catch (Exception e) {
            log.error("支付宝订单[{}]查询失败", order.getOutTradeNo(), e);
            return null;
        }
    }

    @Override
    public RefundResponse refundSync(IOrder order, IRefund refund) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            // 创建API对应的request
            AlipayTradeRefundRequest refundRequest = new AlipayTradeRefundRequest();

            RefundData refundData = RefundData.of(order, refund);
            String param = JSONObject.toJSONString(refundData);

            log.debug("支付宝退款参数: {}", param);
            refundRequest.setBizContent(param);

            AlipayTradeRefundResponse alipayTradeRefundResponse = alipayClient.execute(refundRequest);
            if (!alipayTradeRefundResponse.isSuccess()) {
                throw new PayException(alipayTradeRefundResponse.getSubMsg());
            }

            RefundResponse refundResponse = new RefundResponse();
            refundResponse.setRefundNo(alipayTradeRefundResponse.getTradeNo());
            refundResponse.setSuccess(true);
            refundResponse.setRawObj(alipayTradeRefundResponse);
            refundResponse.setPayPlatform(PayPlatform.ALI_PAY);
            refundResponse.setOutRefundNo(refund.getOutRefundNo());
            return refundResponse;
        } catch (Throwable throwable) {
            log.error("支付宝退款失败");
            throw (throwable instanceof PayException)
                    ? (PayException) throwable
                    : new PayException("支付宝退款失败", throwable);
        }
    }

    @Override
    public TransferResponse transferSync(ITransfer transfer) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            // 创建API对应的request
            AlipayFundTransUniTransferRequest transferRequest = new AlipayFundTransUniTransferRequest();
            TransferData transferData = TransferData.of(transfer);

            String param = JSONObject.toJSONString(transferData);
            log.debug("支付宝转账参数: {}", param);
            transferRequest.setBizContent(param);
            AlipayFundTransUniTransferResponse response = alipayClient.execute(transferRequest);
            if (!response.isSuccess()) {
                throw new PayException(response.getSubMsg());
            }
            TransferResponse transferResponse = new TransferResponse();
            transferResponse.setTransferNo(response.getOrderId());

            // 文档上说TransDate是必定会返回的, 但是用沙箱时会为null,  当为null时取当前时间
            LocalDateTime transDate = Optional.of(response)
                    .map(AlipayFundTransUniTransferResponse::getTransDate)
                    .map(DateUtil::parseLocalDateTime)
                    .orElse(LocalDateTime.now());

            transferResponse.setPaymentTime(transDate);
            if ("SUCCESS".equals(response.getStatus())) {
                transferResponse.setStatus(TransferResponse.STATUS_SUCCESS);
            } else if ("DEALING".equals(response.getStatus())) {
                transferResponse.setStatus(TransferResponse.STATUS_PROCESSING);
            } else if ("FAIL".equals(response.getStatus())) {
                transferResponse.setStatus(TransferResponse.STATUS_FAIL);
                transferResponse.setErrorCode(response.getSubCode());
                transferResponse.setErrorDesc(response.getSubMsg());
            }
            return transferResponse;
        } catch (Throwable throwable) {
            log.error("支付宝转账失败");
            throw (throwable instanceof PayException)
                    ? (PayException) throwable
                    : new PayException("支付宝转账失败", throwable);
        }
    }


    private AlipayClient getAlipayClient(AliPayConfig aliPayConfig) {
        AlipayClient alipayClient = new DefaultAlipayClient(
                aliPayConfig.isDebug() ? SANDBOX_SERVER_URL : SERVER_URL,
                aliPayConfig.getAppId(),
                aliPayConfig.getPrivateKey(),
                FORMAT,
                aliPayConfig.getCharset(),
                aliPayConfig.getPublicKey(),
                aliPayConfig.getSignType()
        );

        return alipayClient;
    }


    @Override
    protected int getPlatform() {
        return PayPlatform.ALI_PAY;
    }
}
