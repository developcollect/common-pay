package com.developcollect.commonpay.utils;


import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import java.io.*;


/**
 * 序列化工具
 *
 * @author zak
 * @since 1.0.0
 */
public class SerializeUtil {


    /**
     * 把json字符串转换成指定类型的对象
     *
     * @param text  json字符串
     * @param clazz 读取对象的类
     * @return T 读取的对象
     * @author zak
     * @since 1.0.0
     */
    public static <T> T jsonToBean(String text, Class<T> clazz) {
        T bean = JSONObject.parseObject(text, clazz);
        return bean;
    }


    /**
     * 将对象转成成json格式的字符串
     *
     * @param object 对象
     * @return java.lang.String json字符串
     * @author zak
     * @since 1.0.0
     */
    public static String beanToJson(Object object) {
        String jsonString = JSONObject.toJSONString(object);
        return jsonString;
    }


    /**
     * 将xml格式字符串转换成指定类型的对象
     *
     * @param <T>   读取对象的类型
     * @param xml   xml字符串
     * @param clazz 读取对象的类
     * @return T 读取的对象
     * @author zak
     * @since 1.0.0
     */
    public static <T> T xmlToBean(String xml, Class<T> clazz) {
        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new String[]{clazz.getName()});
        xStream.processAnnotations(clazz);
        T t = (T) xStream.fromXML(xml);
        return t;
    }


    /**
     * 将对象转换成xml格式文档字符串
     *
     * @param object 对象
     * @return java.lang.String xml格式的字符串
     * @author zak
     * @since 1.0.0
     */
    public static String beanToXml(Object object) {
        XStream xStream = new XStream(new DomDriver("UTF-8",
                new XmlFriendlyNameCoder("-_", "_")));
        xStream.processAnnotations(object.getClass());
        return xStream.toXML(object);
    }


    /**
     * 从流中读取xml格式数据, 并转化为指定类型的对象
     *
     * @param <T>   读取对象的类型
     * @param xmlIn 输入流
     * @param clazz 读取对象的类
     * @return T 读取出的对象
     * @author zak
     * @since 1.0.0
     */
    public static <T> T xmlToBean(InputStream xmlIn, Class<T> clazz) {
        if (xmlIn == null) {
            return null;
        }

        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new String[]{clazz.getName()});
        xStream.processAnnotations(clazz);
        T t = (T) xStream.fromXML(xmlIn);
        return t;
    }

    /**
     * 返回对象序列化后的字节数据
     *
     * @param obj 需要序列化的对象
     * @throws IORuntimeException IO异常
     * @author zak
     * @since 1.0.0
     */
    public static byte[] jdkSerialize(Serializable obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            jdkSerialize(obj, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 将对象序列化后写到流中, 写完后不关流
     *
     * @param obj 需要序列化的对象
     * @param out 写入的内容
     * @throws IORuntimeException IO异常
     * @author zak
     * @since 1.0.0
     */
    public static void jdkSerialize(Serializable obj, OutputStream out) {
        IoUtil.writeObjects(out, false, obj);
    }


    /**
     * 从字节数据中读取对象，即对象的反序列化，读取后不关闭流
     *
     * @param <T>   读取对象的类型
     * @param bytes 字节数据
     * @return 读取出的对象
     * @throws IORuntimeException IO异常
     * @throws UtilException      ClassNotFoundException包装
     * @author zak
     * @since 1.0.0
     */
    public static <T> T jdkDeserialize(byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            return jdkDeserialize(byteArrayInputStream);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 从流中读取对象，即对象的反序列化，读取后不关闭流
     *
     * @param <T> 读取对象的类型
     * @param in  输入流
     * @return 读取出的对象
     * @throws IORuntimeException IO异常
     * @throws UtilException      ClassNotFoundException包装
     * @author zak
     * @since 1.0.0
     */
    public static <T> T jdkDeserialize(InputStream in) {
        return IoUtil.readObj(in, null);
    }


}
