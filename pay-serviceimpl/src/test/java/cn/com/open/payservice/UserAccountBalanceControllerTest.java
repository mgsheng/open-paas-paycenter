package cn.com.open.payservice;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.web.api.order.UserAccountBalanceController;
import cn.com.open.payservice.utils.StringTools;

public class UserAccountBalanceControllerTest extends BaseTest {
	
    @Autowired
    UserAccountBalanceController userAccountBalanceController;
 

    @Test
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

			unifyPayQuery(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
      
    public void unifyPayQuery(MockHttpServletRequest request) throws Exception{
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
			userAccountBalanceController.record(request, response, model);
			 
			
    }
     
}
