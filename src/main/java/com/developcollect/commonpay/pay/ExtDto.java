package com.developcollect.commonpay.pay;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/8/20 11:08
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public abstract class ExtDto implements IExtDto {

    private Map<String, Object> extMap = new HashMap<>();


    @Override
    public void putExt(String key, Object extParameter) {
        extMap.put(key, extParameter);
    }

    @Override
    public <T> T getExt(String key) {
        return (T) extMap.get(key);
    }
}
