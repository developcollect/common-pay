package com.developcollect.commonpay.utils;

import java.math.BigDecimal;

/**
 * 单位工具类
 *     (价格 * 100)单位分
 * @author zak
 * @since 1.0.0
 */
public class UnitUtil {

    private static final BigDecimal bd100 = BigDecimal.valueOf(100.0);


    /**
     * 把分转换为元, 并保留2位小数
     *
     * @param fen
     * @return java.math.BigDecimal
     * @author zak
     * @since 1.0.0
     */
    public static BigDecimal convertFenToYuan(long fen) {
        return convertFenToYuan(fen, 2);
    }

    public static BigDecimal convertFenToYuan(long fen, int scale) {
        BigDecimal bdFen = BigDecimal.valueOf(fen);
        return convertFenToYuan(bdFen, scale);
    }

    public static String convertFenToYuanStr(long fen) {
        BigDecimal bdFen = BigDecimal.valueOf(fen);
        return convertFenToYuan(bdFen).toString();
    }

    /**
     * 把分转换为元, 并保留2位小数
     *
     * @param fen
     * @return java.math.BigDecimal
     * @author zak
     * @since 1.0.0
     */
    public static BigDecimal convertFenToYuan(double fen) {
        return convertFenToYuan(fen, 2);
    }

    public static BigDecimal convertFenToYuan(double fen, int scale) {
        BigDecimal bdFen = BigDecimal.valueOf(fen);
        return convertFenToYuan(bdFen, scale);
    }

    /**
     * 把分转换为元, 并保留2位小数
     *
     * @param fen
     * @return java.math.BigDecimal
     * @author zak
     * @since 1.0.0
     */
    public static BigDecimal convertFenToYuan(float fen) {
        return convertFenToYuan(fen, 2);
    }

    public static BigDecimal convertFenToYuan(float fen, int scale) {
        BigDecimal bdFen = BigDecimal.valueOf(fen);
        return convertFenToYuan(bdFen, scale);
    }

    /**
     * 把分转换为元, 并保留2位小数
     *
     * @param fen
     * @return java.math.BigDecimal
     * @author zak
     * @since 1.0.0
     */
    public static BigDecimal convertFenToYuan(int fen) {
        return convertFenToYuan(fen, 2);
    }

    public static BigDecimal convertFenToYuan(int fen, int scale) {
        BigDecimal bdFen = BigDecimal.valueOf(fen);
        return convertFenToYuan(bdFen, scale);
    }

    /**
     * 把分转换为元, 并保留2位小数
     *
     * @param bdFen
     * @return java.math.BigDecimal
     * @author zak
     * @since 1.0.0
     */
    public static BigDecimal convertFenToYuan(BigDecimal bdFen) {
        return convertFenToYuan(bdFen, 2);
    }

    public static BigDecimal convertFenToYuan(BigDecimal bdFen, int scale) {
        BigDecimal bdYuan = bdFen.divide(bd100, scale, BigDecimal.ROUND_DOWN);
        return bdYuan;
    }


    /**
     * 把元转成分, 只取2位小数
     *
     * @param yuan 元
     * @return long
     * @author zak
     * @since 1.0.0
     */
    public static long convertYuanToFen(String yuan) {
        return convertYuanToFen(Double.parseDouble(yuan));
    }

    public static long convertYuanToFen(double yuan) {
        BigDecimal bdYuan = BigDecimal.valueOf(yuan);
        return convertYuanToFen(bdYuan);
    }

    public static long convertYuanToFen(float yuan) {
        BigDecimal bdYuan = BigDecimal.valueOf(yuan);
        return convertYuanToFen(bdYuan);
    }

    /**
     * 把元转成分, 只取2位小数
     *
     * @param bdYuan
     * @return long
     * @author zak
     * @since 1.0.0
     */
    public static long convertYuanToFen(BigDecimal bdYuan) {
        BigDecimal bdFen = bdYuan.multiply(bd100);
        return bdFen.longValue();
    }

}
