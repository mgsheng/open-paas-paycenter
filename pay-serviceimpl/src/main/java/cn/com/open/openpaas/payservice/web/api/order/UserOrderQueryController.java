package cn.com.open.openpaas.payservice.web.api.order;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayController;
import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxPayCommonUtil;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxpayController;
import cn.com.open.openpaas.payservice.app.channel.wxpay.WxpayInfo;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.AmountUtil;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.StringTool;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 订单查询接口
 */
@Controller
@RequestMapping("/user/order")
public class UserOrderQueryController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserOrderQueryController.class);
	
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 /**
     * 查询用户订单
     * @return Json
     */
    @RequestMapping("query")
    public void unifyPay(HttpServletRequest request,HttpServletResponse response,Model model) throws MalformedURLException, DocumentException, IOException, Exception {
    	String outTradeNo=request.getParameter("outTradeNo");
        String appId = request.getParameter("appId");
      //获取当前订单
      		MerchantOrderInfo orderInfo = merchantOrderInfoService.findByMerchantOrderId(outTradeNo,appId);
      		if(!paraMandatoryCheck(Arrays.asList(outTradeNo,appId))){
    			writeMsgToClient("error",1,response,"必传参数中有空值");
                return;
              }
      		if(orderInfo==null){
    			writeMsgToClient("error",2,response,"业务方订单号不存在");
                return;
      		}
      		
      	MerchantInfo merchantInfo=merchantInfoService.findById(orderInfo.getMerchantId());
        //认证
        Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request,merchantInfo);
		if(!hmacSHA1Verification){
			writeMsgToClient("error",3,response,"认证失败");
            return;
		} 
		if(orderInfo.getPayStatus()==null || orderInfo.getPayStatus()==0){
			writeMsgToClient("success",4,response,"第三方未处理");
            return;
		}
		if(orderInfo.getPayStatus()==2){
			writeMsgToClient("success",5,response,"第三方未处理失败");
            return;
		}
		if(orderInfo.getPayStatus()==1){
			writeMsgToClient("success",6,response,"第三方处理成功");
            return;
		}
		
		
    }	
}