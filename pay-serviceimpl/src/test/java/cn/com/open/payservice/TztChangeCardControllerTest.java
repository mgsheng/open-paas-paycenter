package cn.com.open.payservice;

import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.web.api.order.*;
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

public class TztChangeCardControllerTest extends BaseTest {

    @Autowired
    TztChangeCardController tztChangeCardController;

	@Test
    public void changeCard() throws Exception{//4.13.5.	换绑银行卡请求接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";

	        sParaTemp.put("identityId","223201198710241028");
	        sParaTemp.put("identityType","1");
	        sParaTemp.put("cardNo","10001");
	        sParaTemp.put("userName","zhangdan");
	        sParaTemp.put("phone","131203566987");
	        sParaTemp.put("terminalId","655052");
	        sParaTemp.put("lastLoginTerminalId","10001");
	        sParaTemp.put("isSetPaypwd","1");
	        sParaTemp.put("registIp","10.100.123.17");
	        sParaTemp.put("registTime","2017-08-21 11:23:29");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","13655887852");
	        sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
	        sParaTemp.put("userId","1");
	        sParaTemp.put("oricardNo","10001");

	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
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
				result=tztChangeCardController.request(request, response, model);
				result=tztChangeCardController.resendsms(request, response, model);
				result=tztChangeCardController.confirm(request, response, model);
				tztChangeCardController.bindBack(request, response, model);
				tztChangeCardController.wxErrorPayChannel(request, response, model,"1","2","3","4");
			return result;
			
			
			
			
    }
    
}
