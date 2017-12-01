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

public class PayOrderManagerControllerTest extends BaseTest {
	

    @Autowired
    PayOrderManagerController payOrderManagerController;

    @Test
	public void payOrderManager() throws Exception{//4.12.	订单详情存储接口
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";
			sParaTemp.put("startTime","2017-05-21 11:23:29");
			sParaTemp.put("endTime","2017-08-21 11:23:29");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("marking","1");

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
		    payOrderManagerController.orderDetailSave(request, response, model);
			return result;
    }
    
}
