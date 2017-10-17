package cn.com.open.payservice.signature;

import org.springframework.mock.web.MockHttpServletRequest;

import cn.com.open.openpaas.payservice.app.tools.DateUtils;
import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.app.tools.StringTool;
import cn.com.open.payservice.DateTools;
import cn.com.open.payservice.StringTools;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Signature {

    public static MockHttpServletRequest getSignatureRequest(SortedMap<Object,Object> sParaTemp) {
    	 String appId="aa98545f11cb49418f18a2ea9ed9873c";
    	 String key="945fa18c666a4e0097809f6727bc6997";//9ebada02676c4ccbbbdaeae27362896b
  	  	 String timestamp="2017-07-11T12:00:00Z";
  	  	 String signatureNonce="";
	   	 if(key!=null){
	   	//	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	   		timestamp=DateTools.getSolrDate(new Date());
	   		signatureNonce=StringTools.getRandom(100,1);
	   		sParaTemp.put("appId",appId);//1
	   		sParaTemp.put("timestamp", timestamp);
	   		sParaTemp.put("signatureNonce", signatureNonce);
	   	 }
	   	 MockHttpServletRequest request = new MockHttpServletRequest();
	   	 request.addParameter("appId", appId);
	 //	 request.addParameter("signature", signature);
	   	 request.addParameter("timestamp", timestamp);
	   	 request.addParameter("signatureNonce", signatureNonce);
        return request;
    }
    
    public static MockHttpServletRequest getSignatureApp_id(SortedMap<Object,Object> sParaTemp) {
   	 String appId="aa98545f11cb49418f18a2ea9ed9873c";
   	 String key="945fa18c666a4e0097809f6727bc6997";//9ebada02676c4ccbbbdaeae27362896b
 	  	 String timestamp="2017-07-11T12:00:00Z";
 	  	 String signatureNonce="";
	   	 if(key!=null){
	   	//	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	   		timestamp=DateTools.getSolrDate(new Date());
	   		signatureNonce=StringTools.getRandom(100,1);
	   		sParaTemp.put("app_id",appId);//1
	   		sParaTemp.put("timestamp", timestamp);
	   		sParaTemp.put("signatureNonce", signatureNonce);
	   	 }
	   	 MockHttpServletRequest request = new MockHttpServletRequest();
	   	 request.addParameter("app_id", appId);
	 //	 request.addParameter("signature", signature);
	   	 request.addParameter("timestamp", timestamp);
	   	 request.addParameter("signatureNonce", signatureNonce);
       return request;
   }

    public static SortedMap<Object,Object>  createSigns(SortedMap<Object,Object> parameters){
   		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
   		String timestamp=DateTools.getSolrDate(new Date());
   		String signatureNonce=StringTools.getRandom(100,1);
   		sParaTemp.put("appId","aa98545f11cb49418f18a2ea9ed9873c");//1
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
		return sParaTemp;
    }

    	
    

    public static String createSign(SortedMap<Object,Object> parameters){
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v)&& !"null".equals(v) 
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		 String temp_params = sb.toString();  
		return sb.toString().substring(0, temp_params.length()-1);
	}
    
}
