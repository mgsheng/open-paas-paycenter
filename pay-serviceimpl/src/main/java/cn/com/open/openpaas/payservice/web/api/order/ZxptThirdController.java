package cn.com.open.openpaas.payservice.web.api.order;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaymentType;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.channel.zxpt.http.HttpClientUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.rsa.RSACoderUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.sign.MD5;
import cn.com.open.openpaas.payservice.app.channel.zxpt.xml.XOUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.DateUtil;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.Request;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.RequestHead;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.Response;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ResponseHead;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ThirdScoreRequest;
import cn.com.open.openpaas.payservice.app.channel.zxpt.zx.ThirdScoreResponse;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.app.zxpt.model.PayZxptInfo;
import cn.com.open.openpaas.payservice.app.zxpt.service.PayZxptInfoService;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 *征信平台第三方
 */
@Controller
@RequestMapping("/zxpt/thirdScore")
public class ZxptThirdController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(ZxptThirdController.class);
	
	 @Autowired
	 private PayZxptInfoService payZxptInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 


	/**
	 * 1006个人第三方评分请求
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("request")
    public String thirdScoreRequest(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception  {
        long startTime = System.currentTimeMillis();
    	//String fullUri=payserviceDev.getServer_host()+"zxpt/thirdScore/errorPayChannel";
        String fullUri=payserviceDev.getServer_host()+"pay/redirect/thirdScore/errorPayChannel";
        String appId = request.getParameter("appId");
    	String outTradeNo=request.getParameter("outTradeNo");
    	String certNo=request.getParameter("certNo");
    	String cardNo=request.getParameter("cardNo");
        String merchantId=request.getParameter("merchantId");
        String mobile=request.getParameter("mobile");
        String reasonNo=request.getParameter("reasonNo");
	    String channelId=request.getParameter("channelId");
        String signature=request.getParameter("signature");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
	    String userName="";
    	if (!nullEmptyBlankJudge(request.getParameter("userName"))){
    		userName=new String(request.getParameter("userName").getBytes("iso-8859-1"),"utf-8");
    	}
    	String parameter="";
    	if (!nullEmptyBlankJudge(request.getParameter("parameter"))){
    		parameter=new String(request.getParameter("parameter").getBytes("iso-8859-1"),"utf-8");
    	}
		String newId="";
		newId=SysUtil.careatePayOrderId();
	    PayServiceLog payServiceLog=new PayServiceLog();
	    payServiceLog.setOrderId(newId);
	    payServiceLog.setAppId(appId);
	    payServiceLog.setChannelId("THIRD_SCORE");//
	    payServiceLog.setPaymentId("THIRD_SCORE");
	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
	    payServiceLog.setLogType(payserviceDev.getLog_type());
	    payServiceLog.setMerchantId(merchantId);
	    payServiceLog.setMerchantOrderId(outTradeNo);
    	payServiceLog.setStatus("ok");
    	payServiceLog.setLogName(PayLogName.THIRD_REQUEST_START);
    	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        if(!paraMandatoryCheck(Arrays.asList(outTradeNo,merchantId,appId,mobile,reasonNo,cardNo,certNo,userName,channelId))){
        	//paraMandaChkAndReturn(1, response,"必传参数中有空值");
        	payServiceLog.setErrorCode("1");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.THIRD_REQUEST_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"1";
        }
        //获取商户信息
    	MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	//paraMandaChkAndReturn(2, response,"商户ID不存在");
    		payServiceLog.setErrorCode("2");
    		payServiceLog.setStatus("error");
    		payServiceLog.setLogName(PayLogName.THIRD_REQUEST_END);
    		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"2";
        }
    	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("appId",appId);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		sParaTemp.put("outTradeNo",outTradeNo );
   		sParaTemp.put("merchantId", merchantId);
   		sParaTemp.put("userName", userName);
   		sParaTemp.put("mobile", mobile);
   		sParaTemp.put("channelId", channelId);
   		sParaTemp.put("cardNo", cardNo);
   		sParaTemp.put("certNo", certNo);
   		sParaTemp.put("reasonNo", reasonNo);
   		sParaTemp.put("parameter", parameter);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
		if(!hmacSHA1Verification){
			//paraMandaChkAndReturn(3, response,"认证失败");
			payServiceLog.setErrorCode("3");
			payServiceLog.setStatus("error");
			payServiceLog.setLogName(PayLogName.THIRD_REQUEST_END);
			UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"3";
		}
		//查询第三方征信报告订单是否存在
		PayZxptInfo payZxptInfo=payZxptInfoService.findByMerchantOrderId(outTradeNo,appId);
		Boolean f=false;
		if(payZxptInfo!=null){
				payServiceLog.setErrorCode("5");
				payServiceLog.setStatus("error");
				payServiceLog.setLogName(PayLogName.THIRD_REQUEST_END);
				UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	        	return "redirect:" + fullUri+"?outTradeNo="+outTradeNo+"&errorCode="+"5";
		}else{ 
			//创建订单
			payZxptInfo=new PayZxptInfo();
			payZxptInfo.setId(newId);
			payZxptInfo.setAppId(appId);
			payZxptInfo.setAuthDate(new Date());
			payZxptInfo.setCardNo(cardNo);
			payZxptInfo.setCertNo(certNo);
			payZxptInfo.setCertType(payserviceDev.getZxpt_cert_type());
			payZxptInfo.setEntityAuthCode(payserviceDev.getZxpt_entityAuthCode());
			payZxptInfo.setMerchantOrderId(outTradeNo);
			payZxptInfo.setPhone(mobile);
			payZxptInfo.setReasonNo(reasonNo);
			payZxptInfo.setScoreChannel(channelId);
			payZxptInfo.setScoreMethod(payserviceDev.getZxpt_score_method());
			payZxptInfo.setUserName(userName);
			f=payZxptInfoService.savePayZxptInfo(payZxptInfo);
			
		}
		if(f){
			//第三方评分交易码1006
			String paket = "";
			String sign = "";
			Request<ThirdScoreRequest> zxptrequest = new Request<ThirdScoreRequest>();
			RequestHead head = initHead("1006",DateUtil.getDateTime(new Date()),payserviceDev);
			zxptrequest.setHead(head);
			List<ThirdScoreRequest> list = new ArrayList<ThirdScoreRequest>();
			ThirdScoreRequest rquestdetail = initThirdScoreRequest(payZxptInfo);
			list.add(rquestdetail);
			zxptrequest.setBody(list);
			String requestXml = XOUtil.objectToXml(zxptrequest, Request.class, RequestHead.class, ThirdScoreRequest.class);
//			XStream xstream=XMLUtil.fromXML(requestXml);
			System.out.println(requestXml);
			paket = RSACoderUtil.encrypt(requestXml.getBytes(payserviceDev.getZxpt_charset()), payserviceDev.getZxpt_charset(), payserviceDev.getZxpt_public_key());
			sign = MD5.sign(paket, "123456", "utf-8");
			
			//http请求参数
			Map<String, String> zxptParams = new HashMap<String, String>();
			//参数加密串
			zxptParams.put("packet", paket);
			//MD5签名
			zxptParams.put("checkValue", sign);
			//交易码
			zxptParams.put("tranCode", "1006");
			//商户号 奥鹏
			zxptParams.put("sender", payserviceDev.getZxpt_sender());
			//SIT环境
			String url = payserviceDev.getZxpt_third_url();
			//http请求
			String responsexml = HttpClientUtil.httpPost(url, zxptParams);
			//解密
			String decode = new String(RSACoderUtil.decrypt(responsexml, payserviceDev.getZxpt_key(), payserviceDev.getZxpt_charset()), payserviceDev.getZxpt_charset());
			String decodexml = URLDecoder.decode(decode, payserviceDev.getZxpt_charset());
			Response<ThirdScoreResponse>packet=new Response<ThirdScoreResponse>();
	    	 Map<String, Object> map=new HashMap<String,Object>();
	    	try {
	    		//将xml转成对象
				XOUtil.xmlToObject(decodexml,packet,payserviceDev.getZxpt_charset(),Response.class, ResponseHead.class, ThirdScoreResponse.class);
			    if(packet.getBody()==null||packet.getBody().size()==0){
		    	 map.put("status", "error");
		    	 map.put("errMsg", "获取body失败");
		    	 map.put("errorCode","T10001");
			    }else{
			    	//获取返回信息的body内容
			    	List<ThirdScoreResponse>list1=packet.getBody();
			    	ThirdScoreResponse thirdScoreResponse=null;
			    	if(list!=null && list1.size()>0){
			    		thirdScoreResponse=list1.get(0);
			    	}
			    //业务方请求流水号
			    String orderId=thirdScoreResponse.getOrderId();
			    //第三方征信平台流水号
			    String orderNo=thirdScoreResponse.getOrderNo();
			    String credooScore="";
			    Map<String ,Object> reponsemap=thirdScoreResponse.getMap();
			    //获取明细数据
			    List<Map<String ,String>>recordsList=(List<Map<String, String>>) reponsemap.get("records");
			    if(recordsList!=null&&recordsList.size()>0){
			    	Map<String ,String> recordsMap=recordsList.get(0);
			    	credooScore=recordsMap.get("credooScore");
			    	map.clear();
			    	map.put("status", "ok");
			    	map.put("errMsg", "");
			    	map.put("credooScore",credooScore);
			    	map.put("errorCode", "");
			    	//更新订单征信平台流水号以及订单状态
			    	PayZxptInfo payZxptInfo1= payZxptInfoService.findById(orderId);
			    	if(payZxptInfo1!=null){
			    		payZxptInfo1.setZxptOrderNo(orderNo);
			    		payZxptInfo1.setZxptRequestStatus("1");
			    		payZxptInfoService.updateZxptInfo(payZxptInfo1);
			    	}else{
			    		 map.clear();
				    	 map.put("status", "error");
				    	 map.put("errMsg", "订单信息查询失败");
				    	 map.put("errorCode", "N10002");	
			    	}
			    }else{
			    	 map.clear();
			    	 map.put("status", "error");
			    	 map.put("errMsg", "获取明细数据失败");
			    	 map.put("errorCode", "N10001");
			      }
			    }
	    	} catch (Exception e) {
	    		 map.clear();
		    	 map.put("status", "error");
		    	 map.put("errMsg", "获取明细数据失败");
		    	 map.put("errorCode", "N10001");
		    	 e.printStackTrace();
			}
	    	 writeSuccessJson(response,map);
    	    payServiceLog.setLogName(PayLogName.THIRD_REQUEST_END);
		    UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	 	   
		  }
		  return "redirect:" + fullUri;
    }
  
}