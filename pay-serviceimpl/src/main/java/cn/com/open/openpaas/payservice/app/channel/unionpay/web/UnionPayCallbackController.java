package cn.com.open.openpaas.payservice.app.channel.unionpay.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.util.DateUtil;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayConfig;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayUtil;
import cn.com.open.openpaas.payservice.app.channel.unionpay.sdk.AcpService;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.SendPostMethod;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

/**
 * 
 */
@Controller
@RequestMapping("/union/order/")
public class UnionPayCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory
			.getLogger(UnionPayCallbackController.class);
	@Autowired
	private MerchantOrderInfoService merchantOrderInfoService;
	@Autowired
	private PayserviceDev payserviceDev;
	@Autowired
	private MerchantInfoService merchantInfoService;

	/**
	 * 支付宝订单回调接口
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	@RequestMapping("callBack")
	public String unionCallBack(HttpServletRequest request,
			HttpServletResponse response, Model model)
			throws MalformedURLException, DocumentException, IOException {
		long startTime = System.currentTimeMillis();
		PayServiceLog payServiceLog = new PayServiceLog();

		log.info("-----------------------------union---callBack------start");

		String encoding = request.getParameter("encoding");
		log.info("encoding=[" + encoding + "]");
		Map<String, String> respParam = getAllRequestParam(request);
		Map<String, String> valideData = null;
		if (null != respParam && !respParam.isEmpty()) {
			Iterator<Entry<String, String>> it = respParam.entrySet().iterator();
			valideData = new HashMap<String, String>(respParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				value = new String(value.getBytes(encoding), encoding);
				valideData.put(key, value);
			}
		}
		String orderId = valideData.get("orderId"); // 获取后台通知的数据，其他字段也可用类似方式获取
		String backMsg = "";
		log.info("unionPay notify orderId=======================" + orderId);
		MerchantOrderInfo merchantOrderInfo = merchantOrderInfoService
				.findById(orderId);
		if (merchantOrderInfo != null && merchantOrderInfo.getPayStatus() != 1) {
			if (!AcpService.validate(valideData, encoding)) {
				log.info("-----------------------------union---callBack------verify fail");
				payServiceLog.setLogName(PayLogName.UNION_CALLBACK_END);
				payServiceLog.setStatus("VERIFYERROR");
				UnifyPayControllerLog.log(startTime, payServiceLog,payserviceDev);
				// merchantOrderInfoService.updatePayInfo(4,String.valueOf(merchantOrderInfo.getId()),"VERIFYERROR");
				backMsg = "VERIFYERROR";
			} else {
				log.info("-----------------------------union---callBack------verify success");
				String queryId = valideData.get("queryId");// 原始预授权交易的queryId
				String txnAmt = valideData.get("txnAmt");// 交易金额
				// 添加日志
				payServiceLog.setAmount(txnAmt);
				payServiceLog.setAppId(merchantOrderInfo.getAppId());
				payServiceLog.setChannelId(String.valueOf(merchantOrderInfo
						.getChannelId()));
				payServiceLog.setCreatTime(DateTools.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
				payServiceLog.setLogType(payserviceDev.getLog_type());
				payServiceLog.setMerchantId(merchantOrderInfo.getMerchantId()+ "");
				payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
				payServiceLog.setPaymentId(merchantOrderInfo.getPaymentId()+ "");
				payServiceLog.setPayOrderId(queryId);
				payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
				payServiceLog.setUsername(merchantOrderInfo.getUserName());
				payServiceLog.setStatus("ok");
				payServiceLog.setLogName(PayLogName.UNION_CALLBACK_START);
				UnifyPayControllerLog.log(startTime, payServiceLog,
						payserviceDev);
				// 校验返回数据包

				String returnUrl = merchantOrderInfo.getNotifyUrl();
				MerchantInfo merchantInfo = null;
				merchantInfo = merchantInfoService.findById(merchantOrderInfo.getMerchantId());
				if (nullEmptyBlankJudge(returnUrl)) {
				
					returnUrl = merchantInfo.getReturnUrl();
				}
				log.info("-----------------------------union---callBack------returnUrl "+returnUrl);
				if (!nullEmptyBlankJudge(returnUrl)) {
					// Map<String, String> dataMap=new HashMap<String,
					// String>();
					String buf = "";
					SortedMap<String, String> sParaTemp = new TreeMap<String, String>();
					sParaTemp.put("orderId", merchantOrderInfo.getId());
					sParaTemp.put("outTradeNo",merchantOrderInfo.getMerchantOrderId());
					sParaTemp.put("merchantId",String.valueOf(merchantOrderInfo.getMerchantId()));
					sParaTemp.put("paymentType",String.valueOf(merchantOrderInfo.getPaymentId()));
					sParaTemp.put("paymentChannel",String.valueOf(merchantOrderInfo.getChannelId()));
					sParaTemp.put("feeType", "CNY");
					sParaTemp.put("guid", merchantOrderInfo.getGuid());
					sParaTemp.put("appUid",String.valueOf(merchantOrderInfo.getSourceUid()));
					// sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
					sParaTemp.put("timeEnd",DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
					sParaTemp.put("totalFee",String.valueOf((int) (merchantOrderInfo.getOrderAmount() * 100)));
					sParaTemp.put("goodsId",merchantOrderInfo.getMerchantProductId());
					sParaTemp.put("goodsName",
							merchantOrderInfo.getMerchantProductName());
					sParaTemp.put("goodsDesc",
							merchantOrderInfo.getMerchantProductDesc());
					sParaTemp.put("parameter",
							merchantOrderInfo.getParameter1());
					sParaTemp.put("userName",
							merchantOrderInfo.getSourceUserName());
					String mySign = PayUtil.callBackCreateSign(
							AlipayConfig.input_charset, sParaTemp,
							merchantInfo.getPayKey());
					sParaTemp.put("secret", mySign);
					buf = SendPostMethod.buildRequest(sParaTemp, "post", "ok",
							returnUrl);
					model.addAttribute("res", buf);
					return "pay/payRedirect";
				} else {
					backMsg = "success";
				}
			}
		} else {
			log.info("-----------------------------union---callBack------merchantOrderInfo is null");
			payServiceLog.setErrorCode("2");
			payServiceLog.setStatus("error");
			backMsg = "error";
			if (merchantOrderInfo != null&& merchantOrderInfo.getPayStatus() == 1) {
				log.info("-----------------------------union---callBack------merchantOrderInfo is alerdy pay");
				String returnUrl = merchantOrderInfo.getReturnUrl();
				MerchantInfo merchantInfo = null;
				merchantInfo = merchantInfoService.findById(merchantOrderInfo.getMerchantId());
				if (nullEmptyBlankJudge(returnUrl)) {
				
					returnUrl = merchantInfo.getReturnUrl();
				}
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				payServiceLog.setLogName(PayLogName.ALIPAY_RETURN_END);
				if (!nullEmptyBlankJudge(returnUrl)) {
					String buf = "";
					SortedMap<String, String> sParaTemp = new TreeMap<String, String>();
					sParaTemp.put("orderId", merchantOrderInfo.getId());
					sParaTemp.put("outTradeNo",
							merchantOrderInfo.getMerchantOrderId());
					sParaTemp.put("merchantId",
							String.valueOf(merchantOrderInfo.getMerchantId()));
					sParaTemp.put("paymentType",
							String.valueOf(merchantOrderInfo.getPaymentId()));
					sParaTemp.put("paymentChannel",
							String.valueOf(merchantOrderInfo.getChannelId()));
					sParaTemp.put("feeType", "CNY");
					sParaTemp.put("guid", merchantOrderInfo.getGuid());
					sParaTemp.put("appUid",
							String.valueOf(merchantOrderInfo.getSourceUid()));
					// sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
					sParaTemp.put("timeEnd",
							DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
					sParaTemp.put("totalFee",
							String.valueOf((int) (merchantOrderInfo
									.getOrderAmount() * 100)));
					sParaTemp.put("goodsId",
							merchantOrderInfo.getMerchantProductId());
					sParaTemp.put("goodsName",
							merchantOrderInfo.getMerchantProductName());
					sParaTemp.put("goodsDesc",
							merchantOrderInfo.getMerchantProductDesc());
					sParaTemp.put("parameter",merchantOrderInfo.getParameter1()+ "payCharge="+ String.valueOf(merchantOrderInfo.getPayCharge()));
					sParaTemp.put("userName",merchantOrderInfo.getSourceUserName());
					String mySign = PayUtil.callBackCreateSign(AlipayConfig.input_charset, sParaTemp,merchantInfo.getPayKey());
					sParaTemp.put("secret", mySign);
					buf = SendPostMethod.buildRequest(sParaTemp, "post", "ok",
							returnUrl);
					model.addAttribute("res", buf);
					return "pay/payMaxRedirect";
				}
				payServiceLog.setStatus("already processed");
				backMsg="success";
			}
			payServiceLog.setLogName(PayLogName.UNION_CALLBACK_END);
			UnifyPayControllerLog.log(startTime, payServiceLog, payserviceDev);

		}
		model.addAttribute("backMsg", backMsg);
		model.addAttribute("productName",
				merchantOrderInfo.getMerchantProductName());
		return "pay/callBack";
	}

}