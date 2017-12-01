package cn.com.open.payservice;

import cn.com.open.openpaas.payservice.app.tools.HMacSha1;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.order.UnifyPayController;
import cn.com.open.payservice.signature.Signature;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import java.util.*;

public class UnifyPayControllerTest extends BaseTest {
	
	 @Autowired
	 UnifyPayController unifyPayController;
	 @Autowired
	PayserviceDev payserviceDev;

    
 // 随机字符串范围
 	private final static String RAND_RANGE_NUM = "1234567890";
	private final static char[] CHARS_NUM = RAND_RANGE_NUM.toCharArray();

    public static String getRandomNum(int CODE_LENGTH) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < CODE_LENGTH; i++)
			sb.append(CHARS_NUM[random.nextInt(CHARS_NUM.length)]);
		return sb.toString();
	}
    
    
    public MockHttpServletRequest unifyPay(String paymentChannel,String paymentType ) throws Exception{//订单支付接口
    	System.out.println("==========");
		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
		MockHttpServletRequest request = Signature.getSignatureRequest(sParaTemp);
    	try {
	        String merchantId="10003";
	        String goodsName="goodsName";
	        String totalFee="1000";
	        String businessType="1";
	        String phone="15727398579";
	        String outTradeNo=getRandomNum(8);
	        String signature="";
	        sParaTemp.put("outTradeNo",outTradeNo);
	        sParaTemp.put("userId","36133476-3827-4188-AE4A-0B9DBFC6AC64");
	        sParaTemp.put("goodsName",goodsName);
	        sParaTemp.put("totalFee",totalFee);
	        sParaTemp.put("businessType",businessType);
	        sParaTemp.put("phone",phone);
	        sParaTemp.put("paymentChannel",paymentChannel);
	        sParaTemp.put("paymentType",paymentType);
	        sParaTemp.put("merchantId",merchantId);

	   		String params=createSign(sParaTemp);
			System.err.println(params);
	   		signature=HMacSha1.HmacSHA1Encrypt(params, "67d73cec5d6b4c8a8a9883748f4066fe");
	   		request=Signature.sParaTemp(sParaTemp);
			request.addParameter("signature",signature);
		//	String result=unifyPayQuery(request,"1");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return request;
    }
 	@Test
	public void unifyPayALIPAY() throws Exception{
		MockHttpServletRequest request=unifyPay("10001","ALIPAY");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayWEIXIN() throws Exception{
		MockHttpServletRequest request=unifyPay("10002","WEIXIN");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayCMB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","CMB");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayCCB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","CCB");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayBOC() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","BOC");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayABC() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","ABC");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayBCOM() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","BCOM");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPSBC() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","PSBC");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayCGB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","CGB");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPaySPDB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","SPDB");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayCEB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","CEB");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPAB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","PAB");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayYEEPAY_EHK() throws Exception{
		MockHttpServletRequest request=unifyPay("10006","YEEPAY_EHK");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPAYMAX() throws Exception{
		MockHttpServletRequest request=unifyPay("10007","PAYMAX");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayYEEPAY_GM() throws Exception{
		MockHttpServletRequest request=unifyPay("10008","YEEPAY_GM");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayALIFAF() throws Exception{
		MockHttpServletRequest request=unifyPay("10009","ALIFAF");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPAYMAX10() throws Exception{
		MockHttpServletRequest request=unifyPay("10010","PAYMAX");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayWECHAT_WAP() throws Exception{
		MockHttpServletRequest request=unifyPay("10011","WECHAT_WAP");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayEHK_BANK() throws Exception{
		MockHttpServletRequest request=unifyPay("10012","EHK_BANK");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPAYMAX_WECHAT_CSB() throws Exception{
		MockHttpServletRequest request=unifyPay("10013","PAYMAX_WECHAT_CSB");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPAYMAX_H5() throws Exception{
		MockHttpServletRequest request=unifyPay("10015","PAYMAX_H5");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	@Test
	public void unifyPayYEEPAY_WEIXIN() throws Exception{
		MockHttpServletRequest request=unifyPay("10016","YEEPAY_WEIXIN");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	@Test
	public void unifyPayYEEPAY_ALI() throws Exception{
		MockHttpServletRequest request=unifyPay("10017","YEEPAY_ALI");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayYEEPAY_ALL() throws Exception{
		MockHttpServletRequest request=unifyPay("10018","YEEPAY_ALL");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	@Test
	public void unifyPayEHK_ALI_PAY() throws Exception{
		MockHttpServletRequest request=unifyPay("10019","EHK_ALI_PAY");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayEHK_INSTALLMENT_LOAN() throws Exception{
		MockHttpServletRequest request=unifyPay("10020","EHK_INSTALLMENT_LOAN");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPay() throws Exception{
		MockHttpServletRequest request=unifyPay("10001","ALIPAY");
		String result=unifyPayQuery(request,"1000");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
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

		//	payserviceDev.setPay_switch("0#6#5#3#3#6");
			result=unifyPayController.unifyPay(request, response,model);
			/*
			payserviceDev.setPay_switch("0#2#1#1#3#2");
			result=unifyPayController.unifyPay(request, response,model);
			payserviceDev.setPay_switch("0#3#1#2#3#6");
			result=unifyPayController.unifyPay(request, response,model);
			payserviceDev.setPay_switch("1#4#5#3#3#1");
			result=unifyPayController.unifyPay(request, response,model);
			payserviceDev.setPay_switch("1#6#5#6#3#2");
			result=unifyPayController.unifyPay(request, response,model);*/
		//	payserviceDev.setPay_switch("1#6#5#3#3#6");
		//	result=unifyPayController.unifyPay(request, response,model);
			if("1000".equals(type)){
				unifyPayController.getOrderQuery(request, response,model);
				unifyPayController.payAccomplish(request, response);
				unifyPayController.payChannel(request, response,model);
				unifyPayController.payChannel(request,model);
				unifyPayController.selectPayChannel(request, response,model);
				result=unifyPayController.errorPayChannel(request, model,"1","2","3","4");
			}
			return result;
    }
    
    
    /*
    public void testPay(){
    	try {
    		*//*String s="10001-ALIPAY;10002-WEIXIN;10003-UPOP;10005-PAYMAX;10006-EHK_WECHAT_WAP;10007-PAYMAX;"
			+ "10008-YEEPAY_GW;10009-ALIFAF;10011-WECHAT_WAP;10012-EHK_WEIXIN_PAY;10014-UPOP;10013-PAYMAX_WECHAT_CSB;"
			+ "10015-PAYMAX_H5;10016-WEIXIN;10017-YEEPAY_WEIXIN;10018-YEEPAY_ALI;10019-YEEPAY_ALL;";
    		*//*
    		//10003-UPOP;
    		String s="10001-ALIPAY;10002-WEIXIN;10005-CMB;10005-ICBC;10005-CCB;10005-ABC;10005-BOC;10005-BCOM;10005-PSBC;"
    				+ "10005-CGB;10005-SPDB;10005-CEB;10005-PAB;10006-YEEPAY_EHK;10007-PAYMAX;10008-YEEPAY_GM;10009-ALIFAF;10010-PAYMAX;"
    	    +"10011-WECHAT_WAP;10012-EHK_BANK;10013-PAYMAX_WECHAT_CSB;10015-PAYMAX_H5;10016-YEEPAY_WEIXIN;10017-YEEPAY_ALI;"
    	    + "10018-YEEPAY_ALL;10019-EHK_ALI_PAY;10020-EHK_INSTALLMENT_LOAN;";
    		
    		String  ss[]=s.split(";");
    		for (int i = 0; i < ss.length; i++) {
				String sss[]=ss[i].split("-");
				unifyPay(sss[0],sss[1].trim());
				Thread.sleep(15000);
				System.out.println(sss[0]+"==="+sss[1].trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }*/
    
     
}
