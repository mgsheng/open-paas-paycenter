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
import cn.com.open.openpaas.payservice.web.api.order.QuerySerialRecordController;
import cn.com.open.payservice.signature.Signature;

public class QuerySerialRecordControllerTest extends BaseTest {
	
    @Autowired
    QuerySerialRecordController querySerialRecordController;
    
    @Test
    public void QuerySerialRecord() throws Exception{//4.9.	查询流水记录列表
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureApp_id(sParaTemp);
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("start_time","2017-05-21 11:23:29");
	        sParaTemp.put("end_time","2017-08-21 11:23:29");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("pay_type","1");
	        
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
			querySerialRecordController.query(request, response, model);
			return result;
    }
    
}
