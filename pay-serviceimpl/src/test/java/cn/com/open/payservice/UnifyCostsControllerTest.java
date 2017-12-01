package cn.com.open.payservice;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.web.api.order.UnifyCostsController;
import cn.com.open.payservice.signature.Signature;

public class UnifyCostsControllerTest extends BaseTest {
	
    @Autowired
    UnifyCostsController unifyCostsController;

    @Test
    public void UunifyCosts() throws Exception{//4.7.统一扣费接口
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureApp_id(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("amount","10");
	        sParaTemp.put("source_id","10");
	        sParaTemp.put("user_name","10");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("serial_no","10");
	        
	   		String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			unifyPayQuery(request);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	@Test
	public void UunifyCosts2() throws Exception{//4.7.统一扣费接口
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureApp_id(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";
			sParaTemp.put("amount22","10");
			sParaTemp.put("source_id","10");
			sParaTemp.put("user_name","10");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("serial_no","10");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			unifyPayQuery(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void UunifyCosts3() throws Exception{//4.7.统一扣费接口
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureApp_id(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";
			sParaTemp.put("amount","10");
			sParaTemp.put("source_id","10");
			sParaTemp.put("user_name","10");
			sParaTemp.put("merchantId","100010000");
			sParaTemp.put("serial_no","10");

			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request = Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
			unifyPayQuery(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void UunifyCosts4() throws Exception{//4.7.统一扣费接口
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureApp_id(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";
			sParaTemp.put("amount","金额不能为字符测试");
			sParaTemp.put("source_id","10");
			sParaTemp.put("user_name","10");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("serial_no","10");

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
			unifyCostsController.unifyPay(request, response, model);
			return result;
    }
    
}
