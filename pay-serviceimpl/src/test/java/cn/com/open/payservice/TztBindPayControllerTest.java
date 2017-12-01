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

public class TztBindPayControllerTest extends BaseTest {

    @Autowired
    TztBindPayController tztBindPayController;
	@Test
    public void direct() throws Exception{//4.13.9.	无短信充值接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	 //       sParaTemp.put("identityId","223201198710241028");
	//        sParaTemp.put("identityType","1");
	        sParaTemp.put("amount","10001");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","13655887852");
	        sParaTemp.put("userId","1"); 
	        sParaTemp.put("productName","zhangsan");
	        sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
	        sParaTemp.put("terminalNo","13655887852");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"20");
	   		boolean b = result.contains("redirect:");
	   		Assert.assertEquals(true, b);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	@Test
	public void direct2() throws Exception{//4.13.9.	无短信充值接口
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String type="10";
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("amount","10001");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("outTradeNo2","13655887852");
			sParaTemp.put("userId","1");
			sParaTemp.put("productName","zhangsan");
			sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
			sParaTemp.put("terminalNo","13655887852");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"20");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void direct3() throws Exception{//测试金额不能为字符
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String type="10";
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("amount","hhaha");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("outTradeNo2","13655887852");
			sParaTemp.put("userId","1");
			sParaTemp.put("productName","zhangsan");
			sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
			sParaTemp.put("terminalNo","13655887852");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"20");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Test
	public void direct4() throws Exception{//测试金额不能为字符
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String type="10";
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("amount","10001");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("outTradeNo","13655887852000");
			sParaTemp.put("userId","1");
			sParaTemp.put("productName","zhangsan");
			sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
			sParaTemp.put("terminalNo","13655887852");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"20");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	@Test
	public void direct5() throws Exception{//获取订单
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			sParaTemp.put("appId","aa98545f11cb49418f18a2ea9ed9873c");
			MockHttpServletRequest request = Signature.getSignatureByAppId(sParaTemp);
			String type="10";
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("amount","10001");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("outTradeNo","test20160505094356");
			sParaTemp.put("userId","1");
			sParaTemp.put("productName","zhangsan");
			sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
			sParaTemp.put("terminalNo","13655887852");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"20");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
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

			result=tztBindPayController.direct(request, response, model);
			tztBindPayController.payError(request, response, model,"1","2","3","4");
			return result;
			
			
			
			
    }
    
}
