package com.developcollect.commonpay.pay.alipay.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.codec.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author Zhu KaiXiao
 * @version 1.0
 * @date 2021/4/23 15:03
 */
public class AlipaySignature extends com.alipay.api.internal.util.AlipaySignature {


    public static boolean rsaCertContentCheckV1(Map<String, String> params, String certContent,
                                         String charset, String signType) throws AlipayApiException {
        String publicKey = getAlipayPublicKeyFromCertContent(certContent);
        return rsaCheckV1(params, publicKey, charset, signType);
    }

    public static String getAlipayPublicKeyFromCertContent(String certContent) throws AlipayApiException {
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(certContent.getBytes());
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
            PublicKey publicKey = cert.getPublicKey();
            return Base64.encodeBase64String(publicKey.getEncoded());
        } catch (NoSuchProviderException | CertificateException e) {
            throw new AlipayApiException(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new AlipayApiException(e);
            }
        }
    }
}
