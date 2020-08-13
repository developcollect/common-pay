package com.developcollect.commonpay.utils;


/**
 * lambda工具类
 *
 * @author zak
 * @since 1.0.0
 */
public class LambdaUtil {


    /**
     * 编写一个泛型方法对异常进行包装
     * 从而绕过受检异常在编译时编译不通过的错误
     *
     * @param e   异常
     * @param <R> 返回值类型
     * @param <E> 异常类型
     * @throws E 异常
     * @author zak
     * @since 1.0.0
     */
    public static <R, E extends Throwable> R doThrow(Throwable e) throws E {
        throw (E) e;
    }

    /**
     * 通过函数式接口包装原有含受检异常的代码， 将受检异常通过泛型绕过编译错误
     *
     * @param doThrowWrapper 异常包装
     * @param <T>            返回值类型
     * @return 返回值
     * @author zak
     * @since 1.0.0
     */
    public static <T> T doThrow(DoThrowWrapper<T> doThrowWrapper) {
        try {
            return doThrowWrapper.get();
        } catch (Throwable throwable) {
            return doThrow(throwable);
        }
    }


    /**
     * 异常代码包装
     *
     * @author zak
     * @since 1.0.0
     */
    @FunctionalInterface
    public interface DoThrowWrapper<T> {
        T get() throws Throwable;
    }
}



