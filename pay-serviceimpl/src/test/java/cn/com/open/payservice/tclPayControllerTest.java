package cn.com.open.payservice;

import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.web.api.order.TclPayController;
import cn.com.open.payservice.signature.Signature;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class tclPayControllerTest extends BaseTest {
	
	 
    @Autowired
    TclPayController tclPayController;

    public void TclPay(String paymentChannel,String paymentType) throws Exception{//4.11.	易学贷请求支付
    	System.out.println("==========");
    	try {
		   	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	        MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
	        String type="10";
	        String signature="";
	        String key="945fa18c666a4e0097809f6727bc6997";
	        sParaTemp.put("outTradeNo",type);
	        sParaTemp.put("userId",type);
	        sParaTemp.put("goodsName","10001");
	        sParaTemp.put("totalFee","10");
	        sParaTemp.put("clientIp","10001");
	        sParaTemp.put("businessType","1");
	        sParaTemp.put("merchantId","10001");
	        sParaTemp.put("paymentChannel",paymentChannel);
	        sParaTemp.put("paymentType",paymentType);
	        
	   		String params=createSign(sParaTemp);
	   		System.out.println("TclPay==="+params);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request.addParameter("signature",signature);
			request = Signature.sParaTemp(sParaTemp);
	   		unifyPayQuery(request);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	public void TclPay2(String paymentChannel,String paymentType) throws Exception{//4.11.	易学贷请求支付
		System.out.println("==========");
		try {
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
			MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
			String type="10";
			String signature="";
			String key="945fa18c666a4e0097809f6727bc6997";
			sParaTemp.put("outTradeNo",type);
			sParaTemp.put("userId",type);
			sParaTemp.put("goodsName","100011");
			sParaTemp.put("totalFee","10");
			sParaTemp.put("clientIp","10001");
			sParaTemp.put("businessType","1");
			sParaTemp.put("merchantId","10001");
			sParaTemp.put("paymentChannel",paymentChannel);
			sParaTemp.put("paymentType",paymentType);

			String params=createSign(sParaTemp);
			System.out.println("TclPay==="+params);
			signature=HMacSha1.HmacSHA1Encrypt(params, key);
			request=Signature.sParaTemp(sParaTemp);
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
			result=tclPayController.unifyPay(request, response, model);
			result=tclPayController.errorPayChannel(request, model,"1","2","3","4");//errorPayChannel
			return result;
    }

	@Test
	public void testPay(){
		try {
			String s="10001-ALIPAY;10002-WEIXIN;10003-UPOP;10005-CMB;10005-ICBC;10005-CCB;10005-ABC;10005-BOC;10005-BCOM;10005-PSBC;"
					+ "10005-CGB;10005-SPDB;10005-CEB;10005-PAB;10006-YEEPAY_EHK;10007-PAYMAX;10008-YEEPAY_GM;10009-ALIFAF;10010-PAYMAX;"
					+"10011-WECHAT_WAP;10012-EHK_BANK;10013-PAYMAX_WECHAT_CSB;10015-PAYMAX_H5;10016-YEEPAY_WEIXIN;10017-YEEPAY_ALI;"
					+ "10018-YEEPAY_ALL;10019-EHK_ALI_PAY;10020-EHK_INSTALLMENT_LOAN;";

			String  ss[]=s.split(";");
			for (int i = 0; i < ss.length; i++) {
				String sss[]=ss[i].split("-");
				TclPay(sss[0],sss[1].trim());
				TclPay2(sss[0],sss[1].trim());
				Thread.sleep(10000);
				System.out.println(sss[0]+"==="+sss[1].trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
