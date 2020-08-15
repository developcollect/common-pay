package com.developcollect.commonpay.pay.wxpay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import com.developcollect.commonpay.PayPlatform;
import com.developcollect.commonpay.config.WxPayConfig;
import com.developcollect.commonpay.exception.PayException;
import com.developcollect.commonpay.pay.*;
import com.developcollect.commonpay.pay.wxpay.sdk.WXPay;
import com.developcollect.commonpay.pay.wxpay.sdk.WXPayConstants;
import com.developcollect.commonpay.pay.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付
 * https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
 *
 * @author zak
 * @since 1.0.0
 */
@Slf4j
public class WxPay extends AbstractPay {


    private WXPay getWxSdkPay(WxPayConfig wxPayConfig) {
        DefaultWXPayConfig wxSdkConfig = new DefaultWXPayConfig();
        wxSdkConfig.setAppId(wxPayConfig.getAppId());
        wxSdkConfig.setMchId(wxPayConfig.getMchId());
        wxSdkConfig.setKey(wxPayConfig.getKey());

        WXPay wxPay = new WXPay(wxSdkConfig, true, wxPayConfig.isDebug());
        return wxPay;
    }

    private Map<String, String> convertToPayReqMap(IOrder order) {
        Map<String, String> reqData = new HashMap<>(16);
        reqData.put("body", "商品_" + order.getOutTradeNo());
        reqData.put("out_trade_no", order.getOutTradeNo());
        // 实付款 = 订单总额 + 运费 - 折扣
        reqData.put("total_fee", String.valueOf(order.getTotalFee()));
        // 这个ip好像可以随便填
        reqData.put("spbill_create_ip", "117.43.68.32");
        if (order.getTimeStart() != null) {
            reqData.put("time_start", DateUtil.format(order.getTimeStart(), "yyyyMMddHHmmss"));
        }
        // time_expire只能第一次下单传值，不允许二次修改，二次修改微信接口将报错。
        // 目前根据订单中是否有微信支付订单号判断是否已下单
        if (order.getTimeExpire() != null && order.getTradeNo() == null) {
            reqData.put("time_expire", DateUtil.format(order.getTimeExpire(), "yyyyMMddHHmmss"));
        }

        return reqData;
    }

    private Map<String, String> convertToRefundReqMap(IOrder order, IRefund refund) {
        Map<String, String> reqData = new HashMap<>(16);
        // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
        //transaction_id、out_trade_no二选一，如果同时存在优先级：transaction_id> out_trade_no
        reqData.put("out_trade_no", order.getOutTradeNo());
        // 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔
        reqData.put("out_refund_no", refund.getOutRefundNo());
        // 订单总金额，单位为分，只能为整数，详见支付金额
        reqData.put("total_fee", String.valueOf(order.getTotalFee()));
        // 退款总金额，单位为分，只能为整数，详见支付金额
        reqData.put("refund_fee", String.valueOf(refund.getRefundFee()));
        // 异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数

        // 若商户传入，会在下发给用户的退款消息中体现退款原因
        // 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
//        reqData.put("refund_desc", "");
        return reqData;
    }

    private Map<String, String> convertToTransferReqMap(ITransfer transfer) {
        Map<String, String> reqData = new HashMap<>(16);
        // 商户订单号，需保持唯一性(只能是字母或者数字，不能包含有其它字符)
        reqData.put("partner_trade_no", transfer.getOutTransferNo());
        // 商户appid下，某用户的openid
        reqData.put("openid", transfer.getAccount());
        // NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
        reqData.put("check_name", transfer.needCheckName() ? "FORCE_CHECK" : "NO_CHECK");
        // 收款用户真实姓名。如果check_name设置为FORCE_CHECK，则必填用户真实姓名
        if (StrUtil.isNotBlank(transfer.getReUserName())) {
            reqData.put("re_user_name", transfer.getReUserName());
        }
        // 企业付款备注，必填。注意：备注中的敏感词会被转成字符*
        reqData.put("desc", transfer.getDescription());
        // 企业付款金额，单位为分
        reqData.put("amount", String.valueOf(transfer.getAmount()));
        // 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。
        reqData.put("spbill_create_ip", "10.2.3.10");
        return reqData;
    }

    /**
     * 微信统一下单
     *
     * @param order       订单
     * @param wxPayConfig 微信支付配置
     * @param tradeType   交易类型
     *                    JSAPI -JSAPI支付
     *                    NATIVE -Native支付
     *                    APP -APP支付
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author Zhu Kaixiao
     * @date 2020/8/15 14:18
     */
    private Map<String, String> unifiedOrder(IOrder order, WxPayConfig wxPayConfig, String tradeType, String openId) throws Exception {
        WXPay wxSdkPay = getWxSdkPay(wxPayConfig);
        Map<String, String> reqData = convertToPayReqMap(order);
        reqData.put("trade_type", tradeType);
        if (StrUtil.isNotBlank(openId)) {
            // trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。
            reqData.put("openid", openId);
        }
        reqData.put("notify_url", wxPayConfig.getPayNotifyUrlGenerator().apply(order));

        if (log.isDebugEnabled()) {
            log.debug("微信支付参数:{}", JSONObject.toJSONString(reqData));
        }

        Map<String, String> map = wxSdkPay.unifiedOrder(reqData);

        if ("FAIL".equals(map.get("return_code"))) {
            throw new PayException(map.get("return_msg"));
        }
        if ("FAIL".equals(map.get("result_code"))) {
            throw new PayException(map.get("err_code_des"));
        }
        return map;
    }

    /**
     * 微信扫码支付
     * https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
     *
     * @param order
     * @return java.lang.String
     */
    @Override
    public String payQrCode(IOrder order) {
        try {
            WxPayConfig wxPayConfig = getPayConfig();
            Map<String, String> map = unifiedOrder(order, wxPayConfig, "NATIVE", null);

            String codeUrl = map.get("code_url");
            log.debug("微信支付,code_url: {}", codeUrl);
            return codeUrl;
        } catch (Throwable throwable) {
            log.error("微信支付失败");
            throw throwable instanceof PayException
                    ? (PayException) throwable
                    : new PayException("微信支付失败", throwable);
        }
    }

    /**
     * 微信js支付
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7&index=6
     *
     * @param order
     * @return com.developcollect.commonpay.pay.WxJsPayResult
     */
    @Override
    public PayWxJsResult payWxJs(IOrder order, String openId) {
        try {
            WxPayConfig wxPayConfig = getPayConfig();
            Map<String, String> map = unifiedOrder(order, wxPayConfig, "JSAPI", openId);
            String prepayId = map.get("prepay_id");

            Map<String, String> wxJsPayMap = new HashMap<>(8);
            wxJsPayMap.put("package", "prepay_id=" + prepayId);
            wxJsPayMap.put("appid", wxPayConfig.getAppId());
            wxJsPayMap.put("nonce_str", WXPayUtil.generateNonceStr());
            wxJsPayMap.put("timeStamp", String.valueOf((System.currentTimeMillis() / 1000)));
            wxJsPayMap.put("sign_type", wxPayConfig.isDebug() ? WXPayConstants.MD5 : WXPayConstants.HMACSHA256);
            wxJsPayMap.put("sign", WXPayUtil.generateSignature(wxJsPayMap, wxPayConfig.getKey(),
                    wxPayConfig.isDebug() ? WXPayConstants.SignType.MD5 : WXPayConstants.SignType.HMACSHA256));
            wxJsPayMap.put("prepay_id", prepayId);

            PayWxJsResult payWxJsResult = PayWxJsResult.of(wxJsPayMap);
            return payWxJsResult;
        } catch (Throwable throwable) {
            log.error("微信支付失败");
            throw throwable instanceof PayException
                    ? (PayException) throwable
                    : new PayException("微信支付失败", throwable);
        }
    }

    /**
     * 微信客户端外的移动端网页支付
     * https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_4
     *
     * @param order
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2020/8/15 14:23
     */
    @Override
    public String payWapForm(IOrder order) {
        try {
            WxPayConfig wxPayConfig = getPayConfig();
            Map<String, String> map = unifiedOrder(order, wxPayConfig, "MWEB", null);

            String mwebUrl = map.get("mweb_url");

            if (wxPayConfig.getWapReturnUrlGenerator() != null) {
                mwebUrl = mwebUrl + "&redirect_url=" + URLUtil.encode(wxPayConfig.getWapReturnUrlGenerator().apply(order));
            }

            log.debug("微信支付,mweb_url: {}", mwebUrl);
            return mwebUrl;
        } catch (Throwable throwable) {
            log.error("微信WAP支付失败");
            throw throwable instanceof PayException
                    ? (PayException) throwable
                    : new PayException("微信WAP支付失败", throwable);
        }
    }

    /**
     * 微信订单查询
     * https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_2
     *
     * @param order
     * @return
     */
    @Override
    public PayResponse payQuery(IOrder order) {
        try {
            WxPayConfig payConfig = getPayConfig();
            WXPay wxSdkPay = getWxSdkPay(payConfig);
            Map<String, String> reqData = convertToPayQueryMap(order);
            Map<String, String> map = wxSdkPay.orderQuery(reqData);

            if ("FAIL".equals(map.get("return_code"))) {
                throw new PayException(map.get("return_msg"));
            }
            if ("FAIL".equals(map.get("result_code"))) {
                throw new PayException(map.get("err_code_des"));
            }
            // --
            PayResponse payResponse = new PayResponse();
            payResponse.setSuccess("SUCCESS".equals(map.get("trade_state")));
            payResponse.setTradeNo(map.get("transaction_id"));
            payResponse.setOutTradeNo(map.get("out_trade_no"));
            payResponse.setPayPlatform(PayPlatform.WX_PAY);
            payResponse.setPayTime(DateUtil.parseLocalDateTime(map.get("time_end"), "yyyyMMddHHmmss"));
            payResponse.setRawObj((Serializable) map);
            return payResponse;
        } catch (Exception e) {
            log.error("微信订单[{}]查询失败", order.getOutTradeNo(), e);
            return null;
        }
    }

    private Map<String, String> convertToPayQueryMap(IOrder order) {
        Map<String, String> reqData = new HashMap<>(16);
        reqData.put("out_trade_no", order.getOutTradeNo());
        return reqData;
    }

    @Override
    public RefundResponse refundSync(IOrder order, IRefund refund) {
        try {
            WxPayConfig wxPayConfig = getPayConfig();
            WXPay wxSdkPay = getWxSdkPay(wxPayConfig);

            Map<String, String> reqData = convertToRefundReqMap(order, refund);

            // 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。
            if (wxPayConfig.getRefundNotifyUrlGenerator() != null) {
                reqData.put("notify_url", wxPayConfig.getRefundNotifyUrlGenerator().apply(order, refund));
            }

            if (log.isDebugEnabled()) {
                log.debug("微信退款参数:{}", JSONObject.toJSONString(reqData));
            }

            Map<String, String> map = wxSdkPay.refund(reqData);
            if ("FAIL".equals(map.get("return_code"))) {
                throw new PayException(map.get("return_msg"));
            }
            if ("FAIL".equals(map.get("result_code"))) {
                throw new PayException(map.get("err_code_des"));
            }
            RefundResponse refundResponse = new RefundResponse();
            refundResponse.setRefundNo(map.get("refund_id"));
            refundResponse.setOutRefundNo(map.get("out_refund_no"));
            refundResponse.setPayPlatform(PayPlatform.WX_PAY);
            refundResponse.setSuccess(true);
            refundResponse.setRawObj((Serializable) map);
            return refundResponse;
        } catch (Throwable throwable) {
            log.error("微信退款失败");
            throw throwable instanceof PayException
                    ? (PayException) throwable
                    : new PayException("微信退款失败", throwable);
        }
    }


    /**
     * 微信转账
     * https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2
     * 用于企业向微信用户个人付款
     * 目前支持向指定微信用户的openid付款。
     * （获取openid参见微信公众平台开发者文档： https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html）
     */
    @Override
    public TransferResponse transferSync(ITransfer transfer) {
        try {
            WxPayConfig wxPayConfig = getPayConfig();
            WXPay wxSdkPay = getWxSdkPay(wxPayConfig);


            Map<String, String> reqData = convertToTransferReqMap(transfer);

            if (log.isDebugEnabled()) {
                log.debug("微信转账参数:{}", JSONObject.toJSONString(reqData));
            }

            Map<String, String> map = wxSdkPay.transfer(reqData);
            if ("FAIL".equals(map.get("return_code"))) {
                throw new PayException(map.get("return_msg"));
            }
            TransferResponse transferResponse = new TransferResponse();
            if ("FAIL".equals(map.get("result_code"))) {
                transferResponse.setErrorCode(map.get("err_code"));
                transferResponse.setErrorDesc(map.get("err_code_des"));
                transferResponse.setStatus(TransferResponse.STATUS_FAIL);
            } else {
                transferResponse.setTransferNo(map.get("payment_no"));
                transferResponse.setPaymentTime(DateUtil.parseLocalDateTime(map.get("payment_time")));
                transferResponse.setStatus(TransferResponse.STATUS_SUCCESS);
            }
            return transferResponse;
        } catch (Throwable throwable) {
            log.error("微信转账失败", throwable);
            throw throwable instanceof PayException
                    ? (PayException) throwable
                    : new PayException("微信转账失败", throwable);
        }

    }

    @Override
    protected int getPlatform() {
        return PayPlatform.WX_PAY;
    }
}
