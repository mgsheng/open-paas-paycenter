package cn.com.open.payservice;

import java.util.*;

import cn.com.open.openpaas.payservice.web.api.order.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.payservice.signature.Signature;

public class PayServiceControllerTests extends BaseTest {
	
	 
    @Autowired
    OrderAutoSendController orderAutoSendController;
    @Autowired
    UserAccountBalanceController userAccountBalanceController;
    @Autowired
    ZxptBqsController zxptBqsController;
    @Autowired
    ZxptThirdController zxptThirdController;
	@Autowired
	ApiCommonController apiCommonController;


	public void orderAutoSend() throws Exception{
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//	//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			unifyPayQuery(request,"1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void userAccountBalance() throws Exception{
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();

			String appId="1";
			String key="945fa18c666a4e0097809f6727bc6997";//9ebada02676c4ccbbbdaeae27362896b
			String timestamp="2017-07-11T12:00:00Z";
			String signatureNonce="";
			if(key!=null){
				timestamp=DateTools.getSolrDate(new Date());
				signatureNonce=StringTools.getRandom(100,1);
				sParaTemp.put("appId",appId);//1
				sParaTemp.put("timestamp", timestamp);
				sParaTemp.put("signatureNonce", signatureNonce);
			}
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.addParameter("appId", appId);
			request.addParameter("timestamp", timestamp);
			request.addParameter("signatureNonce", signatureNonce);
			String signature="";
			sParaTemp.put("userId", "2334");
			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request.addParameter("signature",signature);
			request.addParameter("userId","2334");

			unifyPayQuery(request,"2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void bqsRequest() throws Exception{
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
			//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			
			request.addParameter("certNo","203185");
			request.addParameter("merchantId","10001");
			request.addParameter("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
			request.addParameter("channelId","10001");
			request.addParameter("mobile","1356851348");
			request.addParameter("userName","1356851348");

	   		unifyPayQuery(request,"3");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    

    public void thirdScoreRequest() throws Exception{ 
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
	        
	//        sParaTemp.put("appId","1356851348");
	        sParaTemp.put("reasonNo","10001");
	        sParaTemp.put("cardNo","1356851348");
	        sParaTemp.put("userName","10001");
	        
	        
	        
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
	//		//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			
			request.addParameter("certNo","203185");
			request.addParameter("merchantId","10001");
			request.addParameter("outTradeNo","2016050913385373_aa98545f11cb49418f18a2ea9ed9873c");
			request.addParameter("channelId","10001");
			request.addParameter("mobile","1356851348");
			
		//	request.addParameter("appId","1356851348");
			request.addParameter("reasonNo","10001");
			request.addParameter("cardNo","1356851348");
			request.addParameter("userName","10001");
			
	   		unifyPayQuery(request,"4");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
  	public void apiCommon() throws Exception{
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";
			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			//	//signature=HMacSha1.getNewResult(signature);
			request.addParameter("signature",signature);
			unifyPayQuery(request,"5");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
      
    public void unifyPayQuery(MockHttpServletRequest request,String type) throws Exception{
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
			
			if("1".equals(type)){
				orderAutoSendController.orderAutoSend();
			}else if("2".equals(type)){
				userAccountBalanceController.record(request, response, model);
			}else if("3".equals(type)){
				zxptBqsController.bqsRequest(request, response, model);
			}else if("4".equals(type)){
				zxptThirdController.thirdScoreRequest(request, response, model);
			}else if("5".equals(type)){
				apiCommonController.status(request, response);
			}
			
    }
    
    @Test
    public void testPay(){
    	try {
    		orderAutoSend();
    		userAccountBalance();
    		bqsRequest();
    		thirdScoreRequest();
			apiCommon();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
     
}
