package cn.com.open.payservice;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.web.api.order.ZxptBqsController;
import cn.com.open.payservice.signature.Signature;

public class ZxptBqsControllerTest extends BaseTest {
	
    @Autowired
    ZxptBqsController zxptBqsController;

    @Test
	public void bqsRequest() throws Exception{
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("certNo","203185");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c00");
	        sParaTemp.put("mobile","1356851348");
	        sParaTemp.put("channelId","10001");
	        sParaTemp.put("userName","1356851348");

	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request=Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);

	   		String result=unifyPayQuery(request);
	   		boolean b = result.contains("redirect:");
	   		Assert.assertEquals(true, b);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	
	@Test
	public void bqsRequest2() throws Exception{
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	         
	        sParaTemp.put("certNo","203185");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
	        sParaTemp.put("mobile","1356851348");
	        sParaTemp.put("channelId","10001");
	        sParaTemp.put("userName","1356851348");

	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request=Signature.sParaTemp(sParaTemp);
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
			String result=zxptBqsController.bqsRequest(request, response, model);
			return result;
			
    }
    
}
