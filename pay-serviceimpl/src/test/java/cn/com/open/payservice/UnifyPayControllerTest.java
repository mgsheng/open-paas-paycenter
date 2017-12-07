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
/*
 	@Test
	public void unifyPayALIPAY() throws Exception{
		MockHttpServletRequest request=unifyPay("10001","ALIPAY");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}*/
/*
	@Test
	public void unifyPayWEIXIN() throws Exception{
		MockHttpServletRequest request=unifyPay("10002","WEIXIN");
		payserviceDev.setPay_switch("1#1#5#3#3#6");
		 unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}*/

	@Test
	public void unifyPayWEIXIN2() throws Exception{
		MockHttpServletRequest request=unifyPay("10002","WEIXIN");
		payserviceDev.setPay_switch("1#2#5#3#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayWEIXIN3() throws Exception{
		MockHttpServletRequest request=unifyPay("10002","WEIXIN");
		payserviceDev.setPay_switch("1#3#5#3#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	@Test
	public void unifyPayWEIXIN4() throws Exception{
		MockHttpServletRequest request=unifyPay("10002","WEIXIN");
		payserviceDev.setPay_switch("1#4#5#3#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayWEIXIN6() throws Exception{
		MockHttpServletRequest request=unifyPay("10002","WEIXIN");
		payserviceDev.setPay_switch("1#6#5#3#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayCMB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","CMB");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayCCB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","CCB");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayBOC() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","BOC");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayABC() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","ABC");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayBCOM() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","BCOM");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPSBC() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","PSBC");
		payserviceDev.setPay_switch("1#6#5#0#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPSBC1() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","PSBC");
		payserviceDev.setPay_switch("1#6#5#1#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPSBC2() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","PSBC");
		payserviceDev.setPay_switch("1#6#5#2#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPSBC3() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","PSBC");
		payserviceDev.setPay_switch("1#6#5#3#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPSBC6() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","PSBC");
		payserviceDev.setPay_switch("1#6#5#6#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	@Test
	public void unifyPayCGB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","CGB");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPaySPDB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","SPDB");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayCEB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","CEB");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPAB() throws Exception{
		MockHttpServletRequest request=unifyPay("10005","PAB");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayYEEPAY_EHK() throws Exception{
		MockHttpServletRequest request=unifyPay("10006","YEEPAY_EHK");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPAYMAX() throws Exception{
		MockHttpServletRequest request=unifyPay("10007","PAYMAX");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayYEEPAY_GM() throws Exception{
		MockHttpServletRequest request=unifyPay("10008","YEEPAY_GM");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	/*public void unifyPayALIFAF() throws Exception{
		MockHttpServletRequest request=unifyPay("10009","ALIFAF");
		String result=unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}*/

	@Test
	public void unifyPayPAYMAX10() throws Exception{
		MockHttpServletRequest request=unifyPay("10010","PAYMAX");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayWECHAT_WAP() throws Exception{
		MockHttpServletRequest request=unifyPay("10011","WECHAT_WAP");
		payserviceDev.setPay_switch("1#6#5#3#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayEHK_BANK() throws Exception{
		MockHttpServletRequest request=unifyPay("10012","EHK_BANK");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPAYMAX_WECHAT_CSB() throws Exception{
		MockHttpServletRequest request=unifyPay("10013","PAYMAX_WECHAT_CSB");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayPAYMAX_H5() throws Exception{
		MockHttpServletRequest request=unifyPay("10015","PAYMAX_H5");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	@Test
	public void unifyPayYEEPAY_WEIXIN() throws Exception{
		MockHttpServletRequest request=unifyPay("10016","YEEPAY_WEIXIN");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	@Test
	public void unifyPayYEEPAY_ALI() throws Exception{
		MockHttpServletRequest request=unifyPay("10017","YEEPAY_ALI");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayYEEPAY_ALL() throws Exception{
		MockHttpServletRequest request=unifyPay("10018","YEEPAY_ALL");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	@Test
	public void unifyPayEHK_ALI_PAY() throws Exception{
		MockHttpServletRequest request=unifyPay("10019","EHK_ALI_PAY");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	/*@Test
	public void unifyPayUPOP() throws Exception{
		MockHttpServletRequest request=unifyPay("10003","UPOP");
		payserviceDev.setPay_switch("1#6#1#3#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPayUPOP5() throws Exception{
		MockHttpServletRequest request=unifyPay("10003","UPOP");
		payserviceDev.setPay_switch("1#6#5#3#3#6");
		unifyPayQuery(request,"1");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}
*/
	@Test
	public void unifyPayEHK_INSTALLMENT_LOAN() throws Exception{
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpServletRequest request=unifyPay("10020","EHK_INSTALLMENT_LOAN");
		unifyPayQuery(request,"1");
		unifyPayController.unifyPay(request, response,null);
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}

	@Test
	public void unifyPay() throws Exception{
		MockHttpServletRequest request=unifyPay("10001","payChannel");
		payserviceDev.setPay_switch("1#6#5#3#3#6");
		unifyPayQuery(request,"1000");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
	}


	@Test
	public void unifyPay2() throws Exception{
		MockHttpServletRequest request=unifyPay("10001","payChannel");
		payserviceDev.setPay_switch("0#6#5#3#3#6");
		unifyPayQuery(request,"1000");
		// 		boolean b = result.contains("redirect:");
		// 		Assert.assertEquals(true, b);
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
			if("payChannel".equals(paymentType)){
				sParaTemp.put("areaCode","2");
				sParaTemp.put("merchantOrderId","test20160504165956");
			}
			sParaTemp.put("outTradeNo",outTradeNo);
			sParaTemp.put("userId","36133476-3827-4188-AE4A-0B9DBFC6AC64");
			sParaTemp.put("goodsName",goodsName);
			sParaTemp.put("totalFee",totalFee);
			sParaTemp.put("businessType",businessType);
			sParaTemp.put("phone",phone);
			sParaTemp.put("paymentChannel",paymentChannel);
			sParaTemp.put("paymentType",paymentType);
			sParaTemp.put("merchantId",merchantId);
			sParaTemp.put("parameter","R=1;S=01;U=99999;L=00001;O=;open_id=1;GateId=WECHAT_WAP;identitytype=5;identityid=142724198902240834;terminaltype=1;terminalid=98-90-96-E4-32-5C");

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
			if("1000".equals(type)){
				unifyPayController.getOrderQuery(request, response,model);
				unifyPayController.payAccomplish(request, response);
				unifyPayController.payChannel(request,model);
			//	unifyPayController.selectPayChannel(request, response,model);
				unifyPayController.errorPayChannel(request, model,"1","2","3","4");
			//	result=unifyPayController.errorPayChannel(request, model,"1","2","3","4");
			}else{
				unifyPayController.unifyPay(request, response,model);
			}
			return result;
    }

	public static String getRandomNum(int CODE_LENGTH) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < CODE_LENGTH; i++)
			sb.append(CHARS_NUM[random.nextInt(CHARS_NUM.length)]);
		return sb.toString();
	}


}
