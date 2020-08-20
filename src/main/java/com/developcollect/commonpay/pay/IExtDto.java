package com.developcollect.commonpay.pay;

import java.io.Serializable;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/8/20 11:59
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface IExtDto extends Serializable {

    /**
     * 获取扩展参数
     * 比如自定义余额支付可能需要支付密码
     * 或者自定义网盾支付又需要什么秘钥等等
     *
     * @param key 扩展参数key
     * @return T 扩展参数值
     */
    default <T> T getExt(String key) {
        return null;
    }

    /**
     * 放入扩展参数
     * 默认就是什么都不干, 如果有需要, 那就重写这个方法
     *
     * @param key          扩展参数key
     * @param extParameter 扩展参数值
     */
    default void putExt(String key, Object extParameter) {
        // do nothing
        // 留给子类去重写
    }
}
