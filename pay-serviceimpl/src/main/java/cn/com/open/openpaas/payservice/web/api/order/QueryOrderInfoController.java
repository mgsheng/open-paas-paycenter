package cn.com.open.openpaas.payservice.web.api.order;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.sign.RSASign;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytPacketUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytUtils;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.web.api.oauth.AccountDownload;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 
 */
@Controller
@RequestMapping("/unify/order")
public class QueryOrderInfoController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(QueryOrderInfoController.class);
	
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	
	 /**
     * 查询流水记录
     * @throws Exception 
     */
    @RequestMapping("query")
    public void query(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
    	String start_time=request.getParameter("start_time");
    	String appId=request.getParameter("app_id");
    	String end_time=request.getParameter("end_time");
    	String signature=request.getParameter("signature");
  	    String timestamp=request.getParameter("timestamp");
  	    String merchantId=request.getParameter("merchantId");
  	    String signatureNonce=request.getParameter("signatureNonce");
  	    log.info("=============query order info start=========");
    	Map<String ,Object> map=new HashMap<String,Object>();
    	
        if(!paraMandatoryCheck(Arrays.asList(start_time,appId,end_time))){
        	map=paraMandaChkAndReturnMap(1, response,"必传参数中有空值");
        	writeErrorJson(response,map);
        	return ;
        }
        MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	map=paraMandaChkAndReturnMap(2, response,"商户ID不存在");
        	writeErrorJson(response,map);
        	return ;
        }
    	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("app_id",appId);
   		sParaTemp.put("start_time",start_time);
   		sParaTemp.put("end_time", end_time);
   		sParaTemp.put("merchantId",merchantId);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
		if(!hmacSHA1Verification){
			map=paraMandaChkAndReturnMap(3, response,"认证失败");
			writeErrorJson(response,map);
        	return ;
		} 
		//Date startTime=DateTools.stringtoDate(start_time, "yyyy-MM-dd HH:mm:ss");
		//Date endTime=DateTools.stringtoDate(start_time, "yyyy-MM-dd HH:mm:ss");
		List<MerchantOrderInfo> merchantOrderInfoList=merchantOrderInfoService.findOrderByTime(start_time, end_time, appId);
		HashMap<String, Object> totalAmountMap=merchantOrderInfoService.getTotalAmountByTime(start_time, end_time, appId);
		BigDecimal  totalAmount = null;
		String total_amount="0";
		if(totalAmountMap!=null){
			totalAmount=(BigDecimal) totalAmountMap.get("totalAmount");
			total_amount=totalAmount.stripTrailingZeros().toString();
		}
		
		map.clear();
		map.put("order_count", merchantOrderInfoList.size());
		map.put("total_amount",total_amount );
		map.put("status", "ok");
		map.put("merchantOrderInfoList", merchantOrderInfoList);
		writeErrorJson(response, map);
        }
      
    
    /**downlond
	     * 对账单下载申请
	     * @throws Exception 
	     */
	    @RequestMapping(value = "download")
	    public void download(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
	    	String appId = request.getParameter("appId");
	        String timestamp=request.getParameter("timestamp");
		    String signatureNonce=request.getParameter("signatureNonce");
		    String signature=request.getParameter("signature");  
		    String startTime=request.getParameter("startTime");
		    String endTime=request.getParameter("endTime");
		    String merchantId=request.getParameter("merchantId");
		    String marking=request.getParameter("marking");
		 if(!paraMandatoryCheck(Arrays.asList(startTime,endTime,appId,merchantId))){
			  	paraMandaChkAndReturn(2, response,"必传参数中有空值");
			  	return ;
	     }
	  		MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
	  		if(merchantInfo==null){
	         	paraMandaChkAndReturn(2, response,"认证失败");
	         	return ;
	         } 
	  		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	  		
	  		sParaTemp.put("merchantId",merchantId );
	    	sParaTemp.put("appId",appId);
	   		sParaTemp.put("timestamp", timestamp);
	   		sParaTemp.put("signatureNonce", signatureNonce);
	   		sParaTemp.put("startTime",startTime );
	   		sParaTemp.put("endTime",endTime );
	   		sParaTemp.put("marking",marking );
	   		
	   		String params=createSign(sParaTemp);
	   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
	        //认证
			if(!hmacSHA1Verification){
				paraMandaChkAndReturn(2, response,"认证失败");
				return;
			} 
				try {
					List<MerchantOrderInfo> merchantOrderInfoList=merchantOrderInfoService.findOrderMessage(startTime, endTime, appId);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for(int i=0;i<merchantOrderInfoList.size();i++){
						MerchantOrderInfo merchantOrderInfo = merchantOrderInfoList.get(i);
						Date createDate = merchantOrderInfo.getCreateDate();
						merchantOrderInfo.setCreateDate1(df.format(createDate));
						if(merchantOrderInfo.getDealDate()!=null){
							Date dealDate = merchantOrderInfo.getDealDate();
							merchantOrderInfo.setDealDate1(df.format(dealDate));
						}
						
						
					}
					AccountDownload.AccountDownload(response, merchantOrderInfoList,marking);
				} catch (Exception e) {
					// TODO: handle exception
					paraMandaChkAndReturn(2, response,"error");
				}
					
	    }
	    
	    
	    public static Map<String, String> getDownlond(String other){
			if(other==null&&"".equals(other)){
				return null;
			}else{
			String others []=other.split("&");
			Map<String, String> sParaTemp = new HashMap<String, String>();
			String values="";
			String charset="";
			String version="";
			String download_url="";
			for (int i=0;i<others.length;i++){
			   values=others[i];
			   int j=values.indexOf("=");
			   if(values.substring(0, j).equals("charset")){
				  charset=values.substring(j+1,values.length());  
			   }
			   if(values.substring(0, j).equals("version")){
				   version=values.substring(j+1,values.length());  
			   }
			   if(values.substring(0, j).equals("download_url")){
				   download_url=values.substring(j+1,values.length());  
			   }
			}
			sParaTemp.put("charset", charset);
			sParaTemp.put("version", version);
			sParaTemp.put("download_url", download_url);
			return sParaTemp;
			}
		}

}