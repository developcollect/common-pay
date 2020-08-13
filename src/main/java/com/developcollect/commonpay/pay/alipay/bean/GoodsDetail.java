package com.developcollect.commonpay.pay.alipay.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * https://docs.open.alipay.com/api_1/alipay.trade.refund
 *
 * @author zak
 * @since 1.0.0
 */
@Data
public class GoodsDetail implements Serializable {

    /**
     * 必填, 商品的编号
     */
    @JSONField(name = "goods_id")
    private String goodsId;

    /**
     * 必填, 商品名称
     */
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 必填, 商品数量
     */
    @JSONField(name = "quantity")
    private Integer quantity;

    /**
     * 必填, 商品单价，单位为元
     */
    @JSONField(name = "price")
    private Integer price;

    /**
     * 可选, 商品类目
     */
    @JSONField(name = "goods_category")
    private String goodsCategory;

    /**
     * 可选, 商品类目树，从商品类目根节点到叶子节点的类目id组成，类目id值使用|分割
     */
    @JSONField(name = "categories_tree")
    private String categoriesTree;

    /**
     * 可选, 商品描述信息
     */
    @JSONField(name = "body")
    private String body;

    /**
     * 可选, 商品的展示地址
     */
    @JSONField(name = "show_url")
    private String showUrl;
}
