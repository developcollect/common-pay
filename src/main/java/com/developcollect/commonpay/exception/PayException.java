package com.developcollect.commonpay.exception;

import lombok.Getter;

/**
 * 支付异常
 * @author zak
 * @since 1.0.0
 */
public class PayException extends RuntimeException {

    /**
     * 附加信息
     */
    @Getter
    private Object data;

    public PayException() {
        super();
    }

    public PayException(String message) {
        super(message);
    }

    public PayException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayException(Throwable cause) {
        super(cause);
    }


}
