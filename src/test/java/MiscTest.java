import com.alibaba.fastjson.JSON;
import com.developcollect.commonpay.pay.wxpay.DefaultWXPayConfig;
import com.developcollect.commonpay.pay.wxpay.sdk.WXPay;
import org.junit.Test;

import java.util.Map;

/**
 * @author Zhu KaiXiao
 * @version 1.0
 * @date 2021/3/11 10:36
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MiscTest {

    @Test
    public void test() {
        DefaultWXPayConfig wxSdkConfig = new DefaultWXPayConfig();
        // 三项都要填
        wxSdkConfig.setMchId("1264316901");
        wxSdkConfig.setAppId("wxc596191fde33119f");
        wxSdkConfig.setKey("ccon08SVO4Ht7zLgiXjzhEsjQKawweo8");

        WXPay wxPay = new WXPay(wxSdkConfig, true, true);
        try {
            Map<String, String> sandboxSignKey = wxPay.getSandboxSignKey();
            System.out.println(JSON.toJSONString(sandboxSignKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
