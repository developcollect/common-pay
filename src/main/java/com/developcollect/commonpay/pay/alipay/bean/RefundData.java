package com.developcollect.commonpay.pay.alipay.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.developcollect.commonpay.pay.IOrder;
import com.developcollect.commonpay.pay.IRefund;
import com.developcollect.commonpay.utils.UnitUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 支付宝统一下单退款数据包装
 *
 * @author zak
 * @since 1.0.0
 */
@Data
public class RefundData implements Serializable {

    /**
     * 必选, 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 必选, 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     */
    @JSONField(serialize = false)
    private Long refundAmount;

    /**
     * 可选, 退款的原因说明
     */
    @JSONField(name = "refund_reason")
    private String refundReason;

    /**
     * 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
     */
    @JSONField(name = "out_request_no")
    private String outRequestNo;

    /**
     * 可选, 商户的操作员编号
     */
    @JSONField(name = "operator_id")
    private String operatorId;

    /**
     * 可选, 商户的门店编号
     */
    @JSONField(name = "store_id")
    private String storeId;

    /**
     * 可选, 商户的终端编号
     */
    @JSONField(name = "terminal_id")
    private String terminalId;

    /**
     * 可选, 退款包含的商品列表信息
     */
    @JSONField(name = "goods_detail")
    private List<GoodsDetail> goodsDetail;

    /**
     * 可选, 退分账明细信息
     */
    @JSONField(name = "refund_royalty_parameters")
    private List<RoyaltyDetailData> refundRoyaltyParameters;

    /**
     * 可选, 银行间联模式下有用，其它场景请不要使用；
     * 双联通过该参数指定需要退款的交易所属收单机构的pid;
     */
    @JSONField(name = "org_pid")
    private String orgPid;


    public String getRefund_amount() {
        return UnitUtil.convertFenToYuanStr(refundAmount);
    }

    public static RefundData of(IOrder order, IRefund refund) {
        RefundData refundData = new RefundData();
        refundData.setOutTradeNo(order.getOutTradeNo());
        refundData.setRefundAmount(refund.getRefundFee());
        // 可选参数
        refundData.setRefundReason("正常退款");
        refundData.setOutRequestNo(refund.getOutRefundNo());
        return refundData;
    }
}
