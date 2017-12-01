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

import java.util.*;

public class TztBindCardControllerTest extends BaseTest {

    @Autowired
    TztBindCardController tztBindCardController;

	@Test
    public void TztBindCard() throws Exception{//4.13.1.	有短验绑卡请求接口
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
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
	   		String result=unifyPayQuery(request,"1");
	   		boolean b = result.contains("redirect:");
	   		Assert.assertEquals(true, b);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	@Test
	public void TztBindCard2() throws Exception{//参数有空值
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("identityId","223201198710241028");
			sParaTemp.put("identityType","1");
			sParaTemp.put("cardNo222","10001");
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

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"1");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void TztBindCard3() throws Exception{//merchantId查询结果空值
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
			sParaTemp.put("merchantId","10001000");
			sParaTemp.put("outTradeNo","13655887852");
			sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
			sParaTemp.put("userId","1");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"1");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void TztBindCard4() throws Exception{//认证失败
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
			sParaTemp.put("merchantId","10001000");
			sParaTemp.put("outTradeNo","13655887852");
			sParaTemp.put("avaliabletime","2017-08-21 11:23:29");
			sParaTemp.put("userId","1");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
		//	request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"1");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
    public void confirm() throws Exception{//无订单信息
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	        sParaTemp.put("validateCode","203185");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
	        sParaTemp.put("requestNo","1");
	        
	   		String params=createSign(sParaTemp);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"2");
	   		boolean b = result.contains("redirect:");
	   		Assert.assertEquals(true, b);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	@Test
	public void confirm2() throws Exception{//获取订单信息
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("validateCode","203185");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("outTradeNo","test20160504165956");
			sParaTemp.put("requestNo","1");

			String params=createSign(sParaTemp);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"2");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void confirm3() throws Exception{//认证失败
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("validateCode","203185");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("outTradeNo","test20160504165956");
			sParaTemp.put("requestNo","1");

			String params=createSign(sParaTemp);
			request = Signature.sParaTemp(sParaTemp);
		//	request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"2");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
    public void resendsms() throws Exception{//4.13.3.	有短绑卡短验重发请求接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
	        sParaTemp.put("requestNo","1");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"3");
	   		boolean b = result.contains("redirect:");
	   		Assert.assertEquals(true, b);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	@Test
	public void resendsms2() throws Exception{//认证失败
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("merchantId","10001");
			sParaTemp.put("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
			sParaTemp.put("requestNo","1");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
		//	request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"3");
			boolean b = result.contains("redirect:");
			Assert.assertEquals(true, b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void resendsms3() throws Exception{//获取订单
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			sParaTemp.put("merchantId","10001");
			sParaTemp.put("outTradeNo","test20160504165956");
			sParaTemp.put("requestNo","1");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			String result=unifyPayQuery(request,"3");
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
    	 if("1".equals(type)){
			 result=tztBindCardController.request(request, response, model);
		}else if("2".equals(type)){
			 result=tztBindCardController.confirm(request, response, model);
		}else if("3".equals(type)){
			 result=tztBindCardController.resendsms(request, response, model);
			 tztBindCardController.bindError(request, response, model,"1","2","3","4");
			 tztBindCardController.bindBack(request, response, model);
		}
			return result;
    }
    
}
