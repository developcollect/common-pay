package com.developcollect.commonpay.pay.alipay;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.developcollect.commonpay.ExtKeys;
import com.developcollect.commonpay.PayPlatform;
import com.developcollect.commonpay.config.AliPayConfig;
import com.developcollect.commonpay.exception.PayException;
import com.developcollect.commonpay.pay.*;
import com.developcollect.commonpay.pay.alipay.bean.PayData;
import com.developcollect.commonpay.pay.alipay.bean.PayQueryData;
import com.developcollect.commonpay.pay.alipay.bean.RefundData;
import com.developcollect.commonpay.pay.alipay.bean.TransferData;
import com.developcollect.dcinfra.utils.DateUtil;
import com.developcollect.dcinfra.utils.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
     * https://opendocs.alipay.com/open/194/106039
     */
    @Override
    public PayResponse payScan(IPayDTO payDTO) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            AlipayTradePayRequest alipayTradePayRequest = new AlipayTradePayRequest();
            alipayTradePayRequest.setNotifyUrl(aliPayConfig.getPayNotifyUrlGenerator().apply(payDTO));


            PayData payData = PayData.of(payDTO);
            payData.setAuthCode(payDTO.getExt(ExtKeys.PAY_SCAN_AUTH_CODE).toString());
            payData.setProductCode("FACE_TO_FACE_PAYMENT");

            String param = JSONObject.toJSONString(payData);
            log.debug("支付宝支付参数: {}", param);
            alipayTradePayRequest.setBizContent(param);
            AlipayTradePayResponse response = alipayClient.execute(alipayTradePayRequest);

            // 当前预下单请求生成的二维码码串，可以用二维码生成工具根据该码串值生成对应的二维码
            PayResponse payResponse = PayResponse.of(response);
            return payResponse;
        } catch (Throwable throwable) {
            log.error("支付宝支付失败", throwable);
            throw (throwable instanceof PayException)
                    ? (PayException) throwable
                    : new PayException("支付宝支付失败", throwable);
        }
    }

    /**
     * 支付宝预下单扫码支付
     * https://docs.open.alipay.com/api_1/alipay.trade.precreate
     *
     * @param payDTO
     * @return java.lang.String
     */
    @Override
    public String payQrCode(IPayDTO payDTO) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            AlipayTradePrecreateRequest preCreateRequest = new AlipayTradePrecreateRequest();
            preCreateRequest.setNotifyUrl(aliPayConfig.getPayNotifyUrlGenerator().apply(payDTO));

            PayData payData = PayData.of(payDTO);
            payData.setProductCode("FACE_TO_FACE_PAYMENT");

            String param = JSONObject.toJSONString(payData);
            log.debug("支付宝支付参数: {}", param);
            preCreateRequest.setBizContent(param);
            AlipayTradePrecreateResponse response = alipayClient.execute(preCreateRequest);

            //网关返回码,code 非10000接口调用失败，错误信息以subMsg属性返回
            //如code为10000，需要再次判断subCode是否为空，
            //为空情况下，则表示支付宝业务处理成功，否则表示业务处理失败，详细原因以subMsg属性返回
            if (!SUCCESS_CODE.equals(response.getCode()) || StrUtil.isNotBlank(response.getSubCode())) {
                throw new PayException(response.getSubCode(), response);
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
     * @param payDTO
     * @return java.lang.String
     */
    @Override
    public String payWapForm(IPayDTO payDTO) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            // 创建API对应的request
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            String notifyUrl = aliPayConfig.getPayNotifyUrlGenerator().apply(payDTO);
            alipayRequest.setNotifyUrl(notifyUrl);

            // 在公共参数中设置回跳和通知地址
            if (aliPayConfig.getWapReturnUrlGenerator() != null) {
                alipayRequest.setReturnUrl(aliPayConfig.getWapReturnUrlGenerator().apply(payDTO));
            }

            PayData payData = PayData.of(payDTO);
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
     * @param payDTO
     * @return java.lang.String
     */
    @Override
    public String payPcForm(IPayDTO payDTO) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            // 创建API对应的request
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            String notifyUrl = aliPayConfig.getPayNotifyUrlGenerator().apply(payDTO);
            alipayRequest.setNotifyUrl(notifyUrl);

            // 在公共参数中设置回跳和通知地址
            if (aliPayConfig.getPcReturnUrlGenerator() != null) {
                alipayRequest.setReturnUrl(aliPayConfig.getPcReturnUrlGenerator().apply(payDTO));
            }

            PayData payData = PayData.of(payDTO);
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
     * @param payDTO 支付参数
     * @return 订单支付结果
     */
    @Override
    public PayResponse payQuery(IPayDTO payDTO) {
        try {
            AliPayConfig payConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(payConfig);

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

            PayQueryData payQueryData = PayQueryData.of(payDTO);

            request.setBizContent(SerializeUtil.beanToJson(payQueryData));

            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                throw new PayException(response.getSubCode(), response);
            }

            PayResponse payResponse = PayResponse.of(response);
            return payResponse;
        } catch (Exception e) {
            log.error("支付宝订单[{}]查询失败", payDTO.getOutTradeNo(), e);
            return null;
        }
    }

    @Override
    public RefundResponse refundSync(IPayDTO payDTO, IRefundDTO refundDTO) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            // 创建API对应的request
            AlipayTradeRefundRequest refundRequest = new AlipayTradeRefundRequest();

            RefundData refundData = RefundData.of(payDTO, refundDTO);
            String param = JSONObject.toJSONString(refundData);

            log.debug("支付宝退款参数: {}", param);
            refundRequest.setBizContent(param);

            AlipayTradeRefundResponse alipayTradeRefundResponse = alipayClient.execute(refundRequest);
            if (!alipayTradeRefundResponse.isSuccess()) {
                throw new PayException(alipayTradeRefundResponse.getSubCode(), alipayTradeRefundResponse);
            }

            RefundResponse refundResponse = new RefundResponse();
            refundResponse.setRefundNo(alipayTradeRefundResponse.getTradeNo() + ":" + refundDTO.getOutRefundNo());
            // 支付宝退款文档中没看到返回结果中有退款是否成功的字段，所以固定就是处理中
            refundResponse.setStatus(RefundResponse.PROCESSING);
            refundResponse.setRawObj(alipayTradeRefundResponse);
            refundResponse.setPayPlatform(getPlatform());
            refundResponse.setOutRefundNo(refundDTO.getOutRefundNo());
            refundResponse.setRefundTime(DateUtil.localDateTime(alipayTradeRefundResponse.getGmtRefundPay()));
            return refundResponse;
        } catch (Throwable throwable) {
            log.error("支付宝退款失败");
            throw (throwable instanceof PayException)
                    ? (PayException) throwable
                    : new PayException("支付宝退款失败", throwable);
        }
    }

    /**
     * 支付宝退款查询
     * https://opendocs.alipay.com/apis/api_1/alipay.trade.fastpay.refund.query
     *
     * @param refundDTO
     * @return com.developcollect.commonpay.pay.RefundResponse
     * @author Zhu Kaixiao
     * @date 2020/9/28 12:50
     */
    @Override
    public RefundResponse refundQuery(IRefundDTO refundDTO) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();

            // 退款查询参数
            Map<String, String> paramMap = new HashMap<>(2);
            // 商户退款单号

            // 支付宝订单号
            if (StringUtils.isNotBlank(refundDTO.getTradeNo())) {
                paramMap.put("trade_no", refundDTO.getTradeNo());
            }
            if (StringUtils.isNotBlank(refundDTO.getOutTradeNo())) {
                paramMap.put("out_trade_no", refundDTO.getOutTradeNo());
            }
            if (!paramMap.containsKey("trade_no") && !paramMap.containsKey("out_trade_no")) {
                throw new PayException("支付宝查询退款结果时，支付宝订单号和商户订单号不能同时为空！");
            }
            paramMap.put("out_request_no", refundDTO.getOutRefundNo());

            request.setBizContent(JSONObject.toJSONString(paramMap));
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                log.debug("支付宝退款查询调用失败");
                throw new PayException(response.getSubCode(), response);
            }

            RefundResponse refundResponse = new RefundResponse();
            refundResponse.setRawObj(response);
            refundResponse.setRefundNo(response.getTradeNo() + ":" + refundDTO.getOutRefundNo());
            if (StringUtils.isNotBlank(response.getRefundAmount()) || "REFUND_SUCCESS".equals(response.getRefundStatus())) {
                refundResponse.setStatus(RefundResponse.SUCCESS);
            } else {
                refundResponse.setStatus(RefundResponse.FAIL);
            }
            refundResponse.setPayPlatform(getPlatform());
            refundResponse.setOutRefundNo(refundDTO.getOutRefundNo());
            //退款时间； 默认不返回该信息，需与支付宝约定后配置返回；
            if (response.getGmtRefundPay() != null) {
                refundResponse.setRefundTime(DateUtil.localDateTime(response.getGmtRefundPay()));
            }
            return refundResponse;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TransferResponse transferSync(ITransferDTO transferDTO) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            // 创建API对应的request
            AlipayFundTransUniTransferRequest transferRequest = new AlipayFundTransUniTransferRequest();
            TransferData transferData = TransferData.of(transferDTO);

            String param = JSONObject.toJSONString(transferData);
            log.debug("支付宝转账参数: {}", param);
            transferRequest.setBizContent(param);
            AlipayFundTransUniTransferResponse response = alipayClient.execute(transferRequest);
            if (!response.isSuccess()) {
                throw new PayException(response.getSubCode(), response);
            }
            TransferResponse transferResponse = new TransferResponse();
            transferResponse.setTransferNo(response.getOrderId());
            transferResponse.setOutTransferNo(response.getOutBizNo());
            transferResponse.setPayPlatform(getPlatform());
            transferResponse.setRawObj(response);

            // 文档上说TransDate是必定会返回的, 但是用沙箱时会为null,  当为null时取当前时间
            LocalDateTime transDate = Optional.of(response)
                    .map(AlipayFundTransUniTransferResponse::getTransDate)
                    .map(DateUtil::parseLocalDateTime)
                    .orElse(LocalDateTime.now());

            transferResponse.setPaymentTime(transDate);
            if ("SUCCESS".equals(response.getStatus())) {
                transferResponse.setStatus(TransferResponse.SUCCESS);
            } else if ("FAIL".equals(response.getStatus())) {
                transferResponse.setStatus(TransferResponse.FAIL);
                transferResponse.setErrorCode(response.getSubCode());
                transferResponse.setErrorDesc(response.getSubMsg());
            } else {
                // DEALING, INIT, UNKNOWN, 状态都认为是在处理中
                transferResponse.setStatus(TransferResponse.PROCESSING);
            }
            return transferResponse;
        } catch (Throwable throwable) {
            log.error("支付宝转账失败");
            throw (throwable instanceof PayException)
                    ? (PayException) throwable
                    : new PayException("支付宝转账失败", throwable);
        }
    }


    @Override
    public TransferResponse transferQuery(ITransferDTO transferDTO) {
        try {
            AliPayConfig aliPayConfig = getPayConfig();
            AlipayClient alipayClient = getAlipayClient(aliPayConfig);

            AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();

            // 转账查询参数， 下面两个单号不能同时为空，如果都有，那么支付宝以‘order_id’为准
            Map<String, String> paramMap = new HashMap<>(2);
            // 商户退款单号
            paramMap.put("out_biz_no", transferDTO.getOutTransferNo());
            // 支付宝退款单号
            paramMap.put("order_id", transferDTO.getTransferNo());

            request.setBizContent(JSONObject.toJSONString(paramMap));
            AlipayFundTransOrderQueryResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                log.debug("支付宝转账查询调用失败");
                throw new PayException(response.getSubCode(), response);
            }

            TransferResponse transferResponse = new TransferResponse();
            transferResponse.setTransferNo(response.getOrderId());
            transferResponse.setOutTransferNo(response.getOutBizNo());
            transferResponse.setPayPlatform(getPlatform());
            transferResponse.setRawObj(response);

            LocalDateTime transDate = Optional.of(response)
                    .map(AlipayFundTransOrderQueryResponse::getPayDate)
                    .map(DateUtil::parseLocalDateTime)
                    .orElse(LocalDateTime.now());

            transferResponse.setPaymentTime(transDate);
            if ("SUCCESS".equals(response.getStatus())) {
                transferResponse.setStatus(TransferResponse.SUCCESS);
            } else if ("FAIL".equals(response.getStatus())) {
                transferResponse.setStatus(TransferResponse.FAIL);
                transferResponse.setErrorCode(response.getSubCode());
                transferResponse.setErrorDesc(response.getSubMsg());
            } else {
                // DEALING, INIT, UNKNOWN, 状态都认为是在处理中
                transferResponse.setStatus(TransferResponse.PROCESSING);
            }

            return transferResponse;
        } catch (Throwable throwable) {
            log.error("支付宝查询转账失败");
            throw (throwable instanceof PayException)
                    ? (PayException) throwable
                    : new PayException("支付宝查询转账失败", throwable);
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
