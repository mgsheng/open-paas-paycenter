package cn.com.open.payservice;

import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.web.api.order.ApiCommonController;
import cn.com.open.openpaas.payservice.web.api.order.TestOrderCallbackController;
import cn.com.open.openpaas.payservice.web.api.order.TestOrderManualSendController;
import cn.com.open.openpaas.payservice.web.api.order.ZxptController;
import cn.com.open.payservice.signature.Signature;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TestOrderManualSendControllerTest extends BaseTest {

    @Autowired
    TestOrderManualSendController testOrderManualSendController;

    @Test
    public void testOrderManualSend() throws Exception {
        System.out.println("==========");
        try {
            SortedMap<Object, Object> sParaTemp = new TreeMap<Object, Object>();
            MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
            String signature = "";
            String key = "945fa18c666a4e0097809f6727bc6997";
            sParaTemp.put("orderId", "2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");

            String params = createSign(sParaTemp);
            signature = HMacSha1.HmacSHA1Encrypt(params, key);
            request = Signature.sParaTemp(sParaTemp);
            request.addParameter("signature", signature);
            request.addParameter("orderId", "2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
            unifyPayQuery(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOrderManualSend2() throws Exception {
        System.out.println("==========");
        try {
            SortedMap<Object, Object> sParaTemp = new TreeMap<Object, Object>();
            MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
            String signature = "";
            String key = "945fa18c666a4e0097809f6727bc6997";
            sParaTemp.put("orderId", "test20160504165956");

            String params = createSign(sParaTemp);
            signature = HMacSha1.HmacSHA1Encrypt(params, key);
            request = Signature.sParaTemp(sParaTemp);
            request.addParameter("signature", signature);
            request.addParameter("orderId", "test20160504165956");
            unifyPayQuery(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testOrderManualSend3() throws Exception {//必传参数中有空值
        System.out.println("==========");
        try {
            SortedMap<Object, Object> sParaTemp = new TreeMap<Object, Object>();
            MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
            String signature = "";
            String key = "945fa18c666a4e0097809f6727bc6997";
            sParaTemp.put("orderId", "test20160504165956");

            String params = createSign(sParaTemp);
            signature = HMacSha1.HmacSHA1Encrypt(params, key);
            request = Signature.sParaTemp(sParaTemp);
        //    request.addParameter("signature", signature);
            request.addParameter("orderId", "test20160504165956");
            unifyPayQuery(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testOrderManualSend4() throws Exception {//获取订单
        System.out.println("==========");
        try {
            SortedMap<Object, Object> sParaTemp = new TreeMap<Object, Object>();
            sParaTemp.put("appId","aa98545f11cb49418f18a2ea9ed9873c");
            MockHttpServletRequest request = Signature.getSignatureByAppId(sParaTemp);
            String signature = "";
            String key = "945fa18c666a4e0097809f6727bc6997";
            sParaTemp.put("orderId", "test20160504165956");

            String params = createSign(sParaTemp);
            signature = HMacSha1.HmacSHA1Encrypt(params, key);
            request = Signature.sParaTemp(sParaTemp);
            //    request.addParameter("signature", signature);
            unifyPayQuery(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String unifyPayQuery(MockHttpServletRequest request) throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        String result = "";
        Model model = new Model() {
            @Override
            public Model mergeAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public boolean containsAttribute(String attributeName) {
                return false;
            }

            @Override
            public Map<String, Object> asMap() {
                return null;
            }

            @Override
            public Model addAttribute(String attributeName, Object attributeValue) {
                return null;
            }

            @Override
            public Model addAttribute(Object attributeValue) {
                return null;
            }

            @Override
            public Model addAllAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public Model addAllAttributes(Collection<?> attributeValues) {
                return null;
            }
        };

        testOrderManualSendController.orderManualSend(request, response);
        return result;
    }

}
