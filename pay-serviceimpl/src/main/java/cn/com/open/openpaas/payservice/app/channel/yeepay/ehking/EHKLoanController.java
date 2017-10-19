package cn.com.open.openpaas.payservice.app.channel.yeepay.ehking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson15.JSONArray;
import com.alibaba.fastjson15.JSONObject;
import com.ehking.sdk.consumerFinance.v_2.entity.PersonalInfo;
import com.ehking.sdk.consumerFinance.v_2.executer.OrderExecuter;
import com.ehking.sdk.entity.BankCard;
import com.ehking.sdk.entity.Payer;
import com.ehking.sdk.exception.HmacVerifyException;
import com.ehking.sdk.exception.RequestException;
import com.ehking.sdk.exception.ResponseException;
import com.ehking.sdk.exception.UnknownException;
import com.ehking.sdk.executer.ResultListenerAdpater;
import com.ehking.sdk.onlinepay.executer.OnlinePayOrderExecuter;

import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaymentType;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.payservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import com.ehking.sdk.consumerFinance.v_2.builder.OrderBuilder;
import com.ehking.sdk.consumerFinance.v_2.entity.ProductDetail;

/**
 * 
 */
@Controller
@RequestMapping("/ehk/loan/")
public class EHKLoanController extends BaseControllerUtil{
	static final Logger LOGGER = LoggerFactory.getLogger(EHKLoanController.class);
	 @Autowired
	private DictTradeChannelService dictTradeChannelService;
	 @Autowired
	 private RedisClientTemplate redisClient;
	 
	/**
	 * 易汇金-分期贷款
	 */
	@RequestMapping("order")
	public void loan(HttpServletRequest req, final HttpServletResponse resp,Model model) throws ServletException, IOException {
		 String id=req.getParameter("id");
		 String merid=req.getParameter("merid");
		 String paymentType=req.getParameter("paymentType");
		 
		 DictTradeChannel dictTradeChannels=null;
         if(!nullEmptyBlankJudge(paymentType)&&paymentType.equals(PaymentType.EHK_INSTALLMENT_LOAN.getValue())){
        	  dictTradeChannels=dictTradeChannelService.findByMAI(merid,Channel.EHK_INSTALLMENT_LOAN.getValue());
		 }
		 Map<String, String> others =null;
   	     if(dictTradeChannels!=null){
   		   String other= dictTradeChannels.getOther();
		  	others = new HashMap<String, String>();
		  	others=getPartner(other);
		   // EHKOrderUtil.doPost(merchantOrderInfo, others, productDetails);
   	     }
//		String merchantId = req.getParameter("merchantId");
//		String requestId = id;
//		String orderAmount = req.getParameter("orderAmount");
//		String orderCurrency = req.getParameter("orderCurrency");
//		String notifyUrl = req.getParameter("notifyUrl");
//		String callbackUrl = req.getParameter("callbackUrl");
//		String paymentModeCode = req.getParameter("paymentModeCode");
//		String clientIp=req.getParameter("clientIp");
   	     //merchantId:120140078#orderCurrency:CNY#paymentModeCode:SCANCODE-WEIXIN_PAY-P2P#clientIp:127.0.0.1#timeout:10
		String merchantId = others.get("merchantId");//120140078
		final String requestId = id;
		String orderAmount =req.getParameter("payAmount");
		String notifyUrl =dictTradeChannels.getNotifyUrl();
		
//		String merchantId = req.getParameter("merchantId").trim();
//		String requestId = req.getParameter("requestId").trim();
//		String orderAmount = req.getParameter("orderAmount").trim();
//		String notifyUrl = req.getParameter("notifyUrl").trim();
//		String remark = req.getParameter("remark").trim();
		String name = URLDecoder.decode(req.getParameter("name").trim(),"UTF-8");
		String idCard = req.getParameter("idCard").trim();
		String mobliePhone = req.getParameter("mobliePhone").trim();
//		String email = req.getParameter("email").trim();
//		String residenceAddress = req.getParameter("residenceAddress").trim();
//		String permanentAddress = req.getParameter("permanentAddress").trim();
//		String companyName = req.getParameter("companyName").trim();
//		String companyAddress = req.getParameter("companyAddress").trim();


		OrderBuilder builder = new OrderBuilder(merchantId);
		builder.setRequestId(requestId).setAmount(orderAmount)
				.setNotifyUrl(notifyUrl);
			//	.setRemark(remark);

		String jsonPDStr = req.getParameter("productDetails");
		ProductDetail productDetail = new ProductDetail();
		//String jsonPDStr = req.getParameter("hiddenPD").trim();

		LOGGER.info("productDetail++++++++++++++"+jsonPDStr);

		JSONArray productDetailArray = JSONObject.parseArray(jsonPDStr);
		for(Object o:productDetailArray) {
			productDetail = JSONObject.parseObject(o.toString(), ProductDetail.class);
			builder.addProductDetail(productDetail);
		}
		PersonalInfo personalInfo = new PersonalInfo();
		personalInfo.setName(name);
		personalInfo.setMobliePhone(mobliePhone);
		personalInfo.setIdCard(idCard);
//		personalInfo.setEmail(email);
//		personalInfo.setCompanyAddress(companyAddress);
//		personalInfo.setCompanyName(companyName);
//		personalInfo.setPermanentAddress(permanentAddress);
//		personalInfo.setResidenceAddress(residenceAddress);
		builder.setPersonalInfo(personalInfo);
		final PrintWriter out = resp.getWriter();
		try{
			OrderExecuter executer = new OrderExecuter();
			executer.order(builder, new ResultListenerAdpater() {
				@Override
				public void redirect(JSONObject jsonObject, String redirectUrl) throws IOException {
					resp.sendRedirect(redirectUrl);
					LOGGER.debug("redirectUrl:[" + redirectUrl + "]");
				}
			});
		}
		catch(ResponseException e){
			out.println("响应异常</br>");
			out.println(e.toString());
		}
		catch(HmacVerifyException e){
			out.println("签名验证异常</br>");
			out.println(e.toString());
		}
		catch(RequestException e){
			out.println("请求异常</br>");
			out.println(e.toString());
		}
		catch(UnknownException e){
			out.println("未知异常</br>");
			out.println(e.toString());
		}
	
	}
	
	  /**
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    public void getCode(HttpServletRequest request,HttpServletResponse response, Model model){
    	String requestId=request.getParameter("requestId");
    	//OrderInfoDto dto=(OrderInfoDto) redisClient.getObject(RedisConstant.ORDER_ID+requestId);
    	String scanCode="";
    	Map<String, String> map=new HashMap<String, String>();
    	  if(redisClient.getObject(RedisConstant.ORDER_ID+requestId)!=null){
    		  scanCode=redisClient.getObject(RedisConstant.ORDER_ID+requestId).toString(); 
    	      Base64Image.convertByteToImage(scanCode, response);
    	  }else{
    		  map.put("status","error");
    		  map.put("errorMsg","获取二维码超时,请重新下单");
    		  WebUtils.writeErrorJson(response, map);
    	  }
      return ;    	   
    }
}
