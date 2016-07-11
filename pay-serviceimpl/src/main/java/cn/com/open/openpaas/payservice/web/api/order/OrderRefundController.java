package cn.com.open.openpaas.payservice.web.api.order;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.refund.model.OrderRefund;
import cn.com.open.openpaas.payservice.app.refund.service.OrderRefundService;
import cn.com.open.openpaas.payservice.app.tools.AmountUtil;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;
/**
 * 订单退款
 * @author admin
 *
 */
@Controller
@RequestMapping("/user/order/")
public class OrderRefundController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(OrderRefundController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private OrderRefundService orderRefundService;
	 
	 /** 
     * 订单退款接口
     * @return Json
	 * @throws Exception 
	 * @throws NumberFormatException 
     */
	 @RequestMapping("refund")
	public void orderRefund(HttpServletRequest request,HttpServletResponse response) throws NumberFormatException, Exception{
		 log.info("~~~~~~~~~~~~~~~~~~~~~~订单退款开始执行~~~~~~~~~~~~~~~~~~~~~~~~");
			String outTradeNo=request.getParameter("outTradeNo");//业务方订单唯一ID
			String appId=request.getParameter("appId");
			String refundMoney=request.getParameter("refundMoney");
			String merchantId=request.getParameter("merchantId");
			String remark=request.getParameter("remark");
			String userid=request.getParameter("userid");
			String username=request.getParameter("username");
			String realname=request.getParameter("realName");
			String phone=request.getParameter("phone");
			String goodsId=request.getParameter("goodsId");
			String signature=request.getParameter("signature");
			String timestamp=request.getParameter("timestamp");
			String signatureNonce=request.getParameter("signatureNonce");
			
			log.info("~~~~~~~传入的业务订单号："+outTradeNo+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	    	//获取当前订单
			MerchantOrderInfo orderInfo = merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
			if(!paraMandatoryCheck(Arrays.asList(outTradeNo,appId,refundMoney))){
	        	paraMandaChkAndReturn(4, response,"必传参数中有空值");
	            return;
	        }
			if(orderInfo==null){
				paraMandaChkAndReturn(3, response,"业务方订单号不存在");
	            return;
			}
			MerchantInfo merchantInfo=merchantInfoService.findById(orderInfo.getMerchantId());
			//认证
			SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
	    	sParaTemp.put("appId",appId);
	    	sParaTemp.put("outTradeNo",outTradeNo);
	    	sParaTemp.put("refundMoney",refundMoney);
	   		sParaTemp.put("timestamp", timestamp);
	   		sParaTemp.put("signatureNonce", signatureNonce);
	   		String param=createSign(sParaTemp);
	   		
	   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,param,merchantInfo.getPayKey());
	        //Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request,merchantInfo);
			if(!hmacSHA1Verification){
				paraMandaChkAndReturn(1, response,"认证失败");
				return;
			} 
			if(orderInfo.getPayStatus()==null || orderInfo.getPayStatus()==0){
	        	paraMandaChkAndReturn(2, response,"第三方未处理");
	            return;
			}
			if(orderInfo.getPayStatus()==2){
	        	paraMandaChkAndReturn(2, response,"第三方处理失败");
	            return;
			}
			
			if(orderInfo.getPayStatus()==1){
				 Map<String, Object> map=new HashMap<String,Object>();
				OrderRefund	orderRefund=orderRefundService.findByMerchantOrderId(outTradeNo, appId);
				if(orderRefund!=null){
					  if(!nullEmptyBlankJudge(merchantId)){
							 orderRefund.setMerchantId(Integer.parseInt(merchantId));
					   }
					  orderRefund.setMerchantOrderId(outTradeNo);
					  orderRefund.setAppId(appId);
					  orderRefund.setGoodsId(goodsId);
					  orderRefund.setPhone(phone);
					  orderRefund.setRealName(realname);
					  orderRefund.setCreateTime(new Date());
					  orderRefund.setRefundMoney(Double.parseDouble(AmountUtil.changeF2Y(refundMoney)));
					  orderRefund.setSourceUid(userid);
					  orderRefund.setSourceUsername(username);
					  orderRefund.setRemark(remark);
					  orderRefundService.updateOrderRefund(orderRefund);
				}else{
					orderRefund=new OrderRefund(outTradeNo, merchantId, Double.parseDouble(AmountUtil.changeF2Y(refundMoney)), appId, remark, new Date(), userid, username, realname, phone, goodsId);
					orderRefundService.saveOrderRefundInfo(orderRefund);
				}
				map.put("status","ok");
				writeSuccessJson(response,map);
			     
				
			}
		
	}

}
