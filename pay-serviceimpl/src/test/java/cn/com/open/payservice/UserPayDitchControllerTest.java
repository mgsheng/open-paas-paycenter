package cn.com.open.payservice;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.web.api.order.ApiCommonController;
import cn.com.open.openpaas.payservice.web.api.order.OrderManualSendController;
import cn.com.open.openpaas.payservice.web.api.order.PayOrderManagerController;
import cn.com.open.openpaas.payservice.web.api.order.QueryOrderInfoController;
import cn.com.open.openpaas.payservice.web.api.order.QuerySerialRecordController;
import cn.com.open.openpaas.payservice.web.api.order.TclPayController;
import cn.com.open.openpaas.payservice.web.api.order.TestOrderCallbackController;
import cn.com.open.openpaas.payservice.web.api.order.TestOrderManualSendController;
import cn.com.open.openpaas.payservice.web.api.order.TztBindCardController;
import cn.com.open.openpaas.payservice.web.api.order.TztBindPayController;
import cn.com.open.openpaas.payservice.web.api.order.TztChangeCardController;
import cn.com.open.openpaas.payservice.web.api.order.TztUnbindCardController;
import cn.com.open.openpaas.payservice.web.api.order.UnifyCostsController;
import cn.com.open.openpaas.payservice.web.api.order.UnifyPayController;
import cn.com.open.openpaas.payservice.web.api.order.UserOrderQueryController;
import cn.com.open.openpaas.payservice.web.api.order.UserPayDitchController;
import cn.com.open.openpaas.payservice.web.api.order.ZxptController;
import cn.com.open.payservice.signature.Signature;

public class UserPayDitchControllerTest extends BaseTest {
	 
    @Autowired
    UserPayDitchController userPayDitchController;
    
     
    @Test
    public void UserPayDitch() throws Exception{//4.6.支付渠道列表
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request.addParameter("signature",signature);
	   		unifyPayQuery(request);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
      
    public String unifyPayQuery(MockHttpServletRequest request) throws Exception{
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	String result="";
    	 Model model=new Model() {
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
			userPayDitchController.channelList(request, response, model);
			return result;
    }
}
