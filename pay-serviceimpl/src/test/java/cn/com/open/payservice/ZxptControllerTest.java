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

public class ZxptControllerTest extends BaseTest {

    @Autowired
    ZxptController zxptController;

	@Test
    public void thirdScore() throws Exception{//4.1.1.	第三方征信评分接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	        sParaTemp.put("outTradeNo","10001");
	        sParaTemp.put("certNo","10001");
	        sParaTemp.put("userName","zhangsan");
	        sParaTemp.put("mobile","13655887852");
	        sParaTemp.put("reasonNo","10001");
	        sParaTemp.put("cardNo","10001"); 
	        sParaTemp.put("merchantId","10001");
	         
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request.addParameter("signature",signature);
			request = Signature.sParaTemp(sParaTemp);
			String result=unifyPayQuery(request);
	   		boolean b = result.contains("redirect:");
	   		Assert.assertEquals(true, b);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	@Test
	public void thirdScore2() throws Exception{//4.1.1.	第三方征信评分接口
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("outTradeNo","1000100");
			sParaTemp.put("certNo","1000100");
			sParaTemp.put("userName","zhangsan");
			sParaTemp.put("mobile","13655887852");
			sParaTemp.put("reasonNo","10001");
			sParaTemp.put("cardNo","10001");
			sParaTemp.put("merchantId","10001");


			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request);
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
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
			result=zxptController.thirdScoreRequest(request, response, model);
			zxptController.payError(request, response, model,"1","2","3","4");
			return result;
    }

}
