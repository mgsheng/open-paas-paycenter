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
import cn.com.open.openpaas.payservice.web.api.order.OrderAutoSendController;
import cn.com.open.payservice.signature.Signature;

public class OrderAutoSendControllerTest extends BaseTest {
	
	 
    @Autowired
    OrderAutoSendController orderAutoSendController;
    
    @Test
	public void orderAutoSend() throws Exception{
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";
			String params=createSign(sParaTemp);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request.addParameter("signature",signature);
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
			orderAutoSendController.orderAutoSend();
    }
}
