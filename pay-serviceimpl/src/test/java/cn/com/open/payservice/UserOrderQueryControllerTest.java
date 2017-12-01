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

public class UserOrderQueryControllerTest extends BaseTest {
	
    @Autowired
    UserOrderQueryController userOrderQueryController;
    
    @Test
    public void testorderQuery() throws Exception{//订单查询接口
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			sParaTemp.put("appId","aa98545f11cb49418f18a2ea9ed9873c");
	        MockHttpServletRequest request = Signature.getSignatureByAppId(sParaTemp);
	        String signature="";
	        sParaTemp.put("outTradeNo","10");
			String params=createSign(sParaTemp);
			request = Signature.sParaTemp(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, "945fa18c666a4e0097809f6727bc6997");
	   		request.addParameter("outTradeNo","10");
	   		request.addParameter("signature",signature);
	   		unifyPayQuery(request,"4");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	@Test
	public void getOrderQuery() throws Exception{//订单号存在
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			sParaTemp.put("appId","aa98545f11cb49418f18a2ea9ed9873c");
			MockHttpServletRequest request = Signature.getSignatureByAppId(sParaTemp);
			String signature="";
			sParaTemp.put("outTradeNo","test20160504165956");
			String params=createSign(sParaTemp);
			request = Signature.sParaTemp(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, "945fa18c666a4e0097809f6727bc6997");
			request.addParameter("signature",signature);
			unifyPayQuery(request,"2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Test
	public void getOrderQuer2() throws Exception{//获取订单信息
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			sParaTemp.put("appId","aa98545f11cb49418f18a2ea9ed9873c");
			MockHttpServletRequest request = Signature.getSignatureByAppId(sParaTemp);
			String signature="";
			sParaTemp.put("outTradeNo","test20160504165956");
			sParaTemp.put("acDate","acDate");
			String params=createSign(sParaTemp);
			request = Signature.sParaTemp(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, "945fa18c666a4e0097809f6727bc6997");
			request.addParameter("signature",signature);
			unifyPayQuery(request,"3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Test
	public void getOrderQuer3() throws Exception{//认证失败
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			sParaTemp.put("appId","aa98545f11cb49418f18a2ea9ed9873c");
			MockHttpServletRequest request = Signature.getSignatureByAppId(sParaTemp);
			String signature="";
			sParaTemp.put("outTradeNo","test20160504165956");
			sParaTemp.put("acDate","acDate");
			String params=createSign(sParaTemp);
			request = Signature.sParaTemp(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, "945fa18c666a4e0097809f6727bc6997");
		//	request.addParameter("signature",signature);
			unifyPayQuery(request,"3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public String unifyPayQuery(MockHttpServletRequest request,String type) throws Exception{
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

		if("1".equals(type)){
			userOrderQueryController.unifyPay(request, response, model);
		}else if("2".equals(type)) {
			userOrderQueryController.getOrderQuery(request, response, model);
		}else if("3".equals(type)) {
			userOrderQueryController.fileDownlond(request, response, model);
		}else{
			userOrderQueryController.unifyPay(request, response, model);
			userOrderQueryController.getOrderQuery(request, response, model);
			userOrderQueryController.fileDownlond(request, response, model);
		}

			return result;
    }
    
}
