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

import javax.servlet.ServletException;
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

import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.UnifyPayUtil;
import cn.com.open.openpaas.payservice.app.channel.alipay.AliOrderProThread;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayConfig;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayUtil;
import cn.com.open.openpaas.payservice.app.channel.service.ChannelRateService;
import cn.com.open.openpaas.payservice.app.channel.unionpay.sdk.AcpService;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.SendPostMethod;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

/**
 * 
 */
@Controller
@RequestMapping("/union/notify/")
public class UnionNotifyCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory
			.getLogger(UnionNotifyCallbackController.class);
	@Autowired
	private MerchantOrderInfoService merchantOrderInfoService;
	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private PayserviceDev payserviceDev;
	@Autowired
	private ChannelRateService channelRateService;
	@Autowired
	private UserAccountBalanceService userAccountBalanceService;
	@Autowired
	private UserSerialRecordService userSerialRecordService;

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
	public void unionNotifyCallBack(HttpServletRequest request,
			HttpServletResponse response, Model model)
			throws MalformedURLException, DocumentException, IOException {
		log.info("-----------------------callBack unionPay/notify-----------------------------------------");
		long startTime = System.currentTimeMillis();
		String encoding = request.getParameter("encoding");
		PayServiceLog payServiceLog = new PayServiceLog();

		String backMsg = "error";
		// 获取银联通知服务器发送的后台通知参数
		Map<String, String> reqParam = getAllRequestParam(request);
		Map<String, String> valideData = null;
		if (null != reqParam && !reqParam.isEmpty()) {
			Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
			valideData = new HashMap<String, String>(reqParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				value = new String(value.getBytes(encoding), encoding);
				valideData.put(key, value);
			}
		}
		String orderId = valideData.get("orderId"); // 获取后台通知的数据，其他字段也可用类似方式获取
		log.info("unionPay notify orderId=======================" + orderId);
		MerchantOrderInfo merchantOrderInfo = merchantOrderInfoService
				.findById(orderId);
		payServiceLog.setOrderId(orderId);
		if (merchantOrderInfo != null && merchantOrderInfo.getPayStatus() != 1) {
			// 重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
			if (!AcpService.validate(valideData, encoding)) {
				log.info("=============union pay verify fail==========================");
				// 验签失败，需解决验签问题
				payServiceLog.setLogName(PayLogName.UNION_NOTIFY_END);
				payServiceLog.setStatus("VERIFYERROR");
				UnifyPayControllerLog.log(startTime, payServiceLog,
						payserviceDev);
				merchantOrderInfoService.updatePayInfo(4,
						String.valueOf(merchantOrderInfo.getId()),
						"VERIFYERROR");
			} else {
				log.info("=============union pay verify success==========================");
				// 【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
				String respCode = valideData.get("respCode"); // 获取应答码，收到后台通知了respCode的值一般是00，可以不需要根据这个应答码判断。
				String queryId = valideData.get("queryId");// 原始预授权交易的queryId
				String txnAmt = valideData.get("txnAmt");// 交易金额
				Double total_fee = Double.valueOf(txnAmt);
				log.info("unionPay notify respCode======================="
						+ respCode);
				// 添加日志
				payServiceLog.setAppId(merchantOrderInfo.getAppId());
				payServiceLog.setPayOrderId(queryId);
				payServiceLog.setAmount(txnAmt);
				payServiceLog.setRealAmount(txnAmt);
				payServiceLog.setChannelId(String.valueOf(merchantOrderInfo
						.getChannelId()));
				payServiceLog.setCreatTime(DateTools.dateToString(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				payServiceLog.setLogType(payserviceDev.getLog_type());
				payServiceLog.setMerchantId(merchantOrderInfo.getMerchantId()
						+ "");
				payServiceLog.setMerchantOrderId(merchantOrderInfo
						.getMerchantOrderId());
				payServiceLog.setPaymentId(merchantOrderInfo.getPaymentId()
						+ "");
				payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
				payServiceLog.setUsername(merchantOrderInfo.getUserName());
				payServiceLog.setStatus("ok");
				payServiceLog.setLogName(PayLogName.YEEPAY_CALLBACK_START);
				UnifyPayControllerLog.log(startTime, payServiceLog,
						payserviceDev);
				String returnUrl = merchantOrderInfo.getNotifyUrl();
				MerchantInfo merchantInfo = null;
				if (nullEmptyBlankJudge(returnUrl)) {
					merchantInfo = merchantInfoService
							.findById(merchantOrderInfo.getMerchantId());
					returnUrl = merchantInfo.getReturnUrl();
				}
				log.info("unionPay notify returnUrl=======================" + returnUrl);
				if (!nullEmptyBlankJudge(returnUrl)) {
					backMsg = "success";
					// 账户充值操作
					String rechargeMsg = "";
					int notifyStatus = merchantOrderInfo.getNotifyStatus();
					int payStatus = merchantOrderInfo.getPayStatus();
					Double payCharge = 0.0;
					payCharge = UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
					log.info("-----------------------callBack:union:notify:payCharge:"+ payCharge);
					if (payStatus != 1) {
						merchantOrderInfo.setPayStatus(1);
						merchantOrderInfo.setPayAmount(total_fee - payCharge);
						merchantOrderInfo.setAmount(total_fee);
						merchantOrderInfo.setPayCharge(payCharge);
						merchantOrderInfo.setDealDate(new Date());
						merchantOrderInfo.setPayOrderId(queryId);
						merchantOrderInfoService.updateOrder(merchantOrderInfo);
						if (merchantOrderInfo != null&& !nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&& "2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))) {
							rechargeMsg = UnifyPayUtil.recordAndBalance(total_fee * 100, merchantOrderInfo,userSerialRecordService,userAccountBalanceService, payserviceDev);
						}
					}
					log.info("-----------------------callBack:union:notify:thread start====");
					if (notifyStatus != 1) {
						Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService, payserviceDev));
						thread.run();
					}
					log.info("-----------------------callBack:union:notify:thread end====");
				} else {
					backMsg = "ok";
				}
				payServiceLog.setLogName(PayLogName.UNION_NOTIFY_END);
				UnifyPayControllerLog.log(startTime, payServiceLog,
						payserviceDev);
			}
		} else {
			log.info("-----------------------callBack:union:notify:merchantOrderInfo is null====");
			payServiceLog.setErrorCode("2");
			payServiceLog.setStatus("error");
			backMsg = "error";
			if (merchantOrderInfo != null
					&& merchantOrderInfo.getPayStatus() == 1) {
				payServiceLog.setStatus("already processed");
				backMsg = "ok";
			}
			payServiceLog.setLogName(PayLogName.UNION_NOTIFY_END);
			UnifyPayControllerLog.log(startTime, payServiceLog, payserviceDev);
		}

		log.info("BackRcvResponse接收后台通知结束");
		WebUtils.writeJson(response, backMsg);

	}

	String formatString(String text) {
		if (text == null) {
			return "";
		}
		return text;
	}

	/**
	 * 跳转第三方渠道界面
	 */
	@RequestMapping(value = "payRedirect")
	public String selectPayChannel(HttpServletRequest request, Model model) {
		String res = (String) request.getAttribute("res");
		model.addAttribute("res", res);
		return "pay/payRedirect";
	}

}