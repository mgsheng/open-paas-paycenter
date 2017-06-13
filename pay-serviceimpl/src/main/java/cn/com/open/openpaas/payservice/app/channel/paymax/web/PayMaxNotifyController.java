package cn.com.open.openpaas.payservice.app.channel.paymax.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayUtil;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.paymax.sign.RSA;
import cn.com.open.openpaas.payservice.app.channel.service.ChannelRateService;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
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
@RequestMapping("/paymax/notify")
public class PayMaxNotifyController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(PayMaxNotifyController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private ChannelRateService channelRateService;
	 @Autowired
	 private UserSerialRecordService userSerialRecordService;
	 @Autowired
	 private UserAccountBalanceService userAccountBalanceService;
	/**
	 * 支付宝订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping(value = "callBack")
	public void payCallBack(HttpServletRequest request,HttpServletResponse response,Model model) throws MalformedURLException, DocumentException, IOException {
		//log.info("-----------------------callBack paxmax/order-----------------------------------------");
		String requestBody = getRequestBody(request);
	//	requestBody="{\"amount\":3725,\"amount_refunded\":0,\"amount_settle\":0,\"body\":\"XLJF\",\"client_ip\":\"127.0.0.1\",\"credential\":{\"lakala_web\":\"<form method=\"post\" action=\"http://paymax.lakala.com/hipos/cashier\">\n<input type=\"hidden\" name=\"charset\" value=\"02\">\n<input type=\"hidden\" name=\"version\" value=\"1.0\">\n<input type=\"hidden\" name=\"merchantCert\" value=\"3082036F30820257A00302010202081BFA980089EAD5E6300D06092A864886F70D0101050500305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F744341301E170D3136303930393033323833305A170D3230303930393033323833305A3064310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C0249543118301606035504030C0F38383830313030373337323030303230819F300D06092A864886F70D010101050003818D0030818902818100D8B391F6A6C7DECE7A0E8C0F8E62B2F3905BB298883B96149A5596897F7339620F390B076C5785FFB828B068429B097781C4BF8E1536D84772E13EC83ADFA7969296852F703EABDD1A8102471459CE2EBB95BDA3FCA2E9FC22E60CEC6FF3B9CA9F262A6C204E25241560C6FCA1B1CAEC90C81AA7860E1EA2C63565D21AF3E4870203010001A381B13081AE301D0603551D0E04160414B081087CF98B6BDA2FD4F7C6392F97E6FA4E560F30818C0603551D23048184308181801407C69254961EB26785D105D969445BF72CF3CCA7A15FA45D305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F74434182081BF6C2A9D76A7CF2300D06092A864886F70D0101050500038201010090317C2BAEC7343EBF0D3314FD354FBE54A8878D5DFF30F5E1DE73E06DF08EDDE31A12DF6698E9771A896BA4ECD0440B239ED94551099E20EE12357F2839A270E5BD26ADD1B44438F9BE9189DAA92C63B2BDB4CF42DBA37798F1B3C72A872C8046B5B64C0E8DF84D2F730FDB9EE86FB1977972CA114B5830D62E0162A8B9BB50011E3D5E6EDCE80AA52968F34B04E7DA18CEB250349C0010F682C71FE8CE5EF1582941BED59FB534663B12C8F1BC80676D1307DF287AFD0169E82587667C3D35233C49AB76606BBF89B8704AB183B62FFB2D56C535605297D70ED4B6ADC9294EE8C7C31433FA29DC3778C4307B86543A815A0401F9D907567BA7D009B2A9F77B\">\n<input type=\"hidden\" name=\"merchantSign\" value=\"95B584C9358DA57EAD8DE5111940538BB226EA295E3D4D54A31B32B38388683BF4D29F9DDC59425A30EFA44CAB61D4E8F107407543BA8597D8A6D9D763D88E2E11CAFF8A244B795EFC40A9C760DE43AC9DCDC2F8B2F7CA1165FA178213820C5FA1DE02CC6CEC4E9F922E692391339745BCE3DB5AA4E2A12473553CB9DE7E901F\">\n<input type=\"hidden\" name=\"signType\" value=\"RSA\">\n<input type=\"hidden\" name=\"service\" value=\"NpDirectPayment\">\n<input type=\"hidden\" name=\"pageReturnUrl\" value=\"https://www.paymax.cc/webservice/redirect/lakala_web\">\n<input type=\"hidden\" name=\"offlineNotifyUrl\" value=\"https://www.paymax.cc/webservice/callback/lakala_web\">\n<input type=\"hidden\" name=\"clientIP\" value=\"127.0.0.1\">\n<input type=\"hidden\" name=\"requestId\" value=\"dafc1a9758bb4a50b7ed9e9a0a2d95b2\">\n<input type=\"hidden\" name=\"purchaserId\" value=\"10003\">\n<input type=\"hidden\" name=\"merchantId\" value=\"888010073720002\">\n<input type=\"hidden\" name=\"merchantName\" value=\"奥鹏教育\">\n<input type=\"hidden\" name=\"orderId\" value=\"ch_34a3395763997a8e15e2df87\">\n<input type=\"hidden\" name=\"orderTime\" value=\"20161018164951\">\n<input type=\"hidden\" name=\"totalAmount\" value=\"372500\">\n<input type=\"hidden\" name=\"currency\" value=\"CNY\">\n<input type=\"hidden\" name=\"validUnit\" value=\"00\">\n<input type=\"hidden\" name=\"validNum\" value=\"60\">\n<input type=\"hidden\" name=\"showUrl\" value=\"null\">\n<input type=\"hidden\" name=\"productDesc\" value=\"XLJF\">\n<input type=\"hidden\" name=\"productName\" value=\"XLJF\">\n<input type=\"hidden\" name=\"productId\" value=\"null\">\n<input type=\"hidden\" name=\"backParam\" value=\"null\">\n<input type=\"hidden\" name=\"bankAbbr\" value=\"CMB\">\n<input type=\"hidden\" name=\"cardType\" value=\"0\">\n<\/form>\"},\"currency\":\"CNY\",\"description\":\"\",\"extra\":{\"user_id\":10003,\"bank_code\":\"CMB\",\"return_url\":\"http://pay-service-openops.myalauda.cn/pay-service/paymax/callBack\"},\"failure_msg\":\"TimeOut\",\"id\":\"ch_34a3395763997a8e15e2df87\",\"livemode\":false,\"order_no\":\"20161018164951486069\",\"refunds\":[],\"status\":\"FAILED\",\"subject\":\"XLJF\",\"time_created\":1476780591000,\"time_expire\":1476784191280,\"transaction_no\":\"201610180002486505\"}";
	//	String	requestBody="{\"data\":{\"amount\":300.00,\"amount_refunded\":0,\"amount_settle\":0,\"body\":\"XLJF\",\"client_ip\":\"127.0.0.1\",\"credential\":{\"lakala_web\":\"<form method=\"post\" action=\"http://paymax.lakala.com/hipos/cashier\">\n<input type=\"hidden\" name=\"charset\" value=\"02\">\n<input type=\"hidden\" name=\"version\" value=\"1.0\">\n<input type=\"hidden\" name=\"merchantCert\" value=\"3082036F30820257A00302010202081BFA980089EAD5E6300D06092A864886F70D0101050500305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F744341301E170D3136303930393033323833305A170D3230303930393033323833305A3064310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C0249543118301606035504030C0F38383830313030373337323030303230819F300D06092A864886F70D010101050003818D0030818902818100D8B391F6A6C7DECE7A0E8C0F8E62B2F3905BB298883B96149A5596897F7339620F390B076C5785FFB828B068429B097781C4BF8E1536D84772E13EC83ADFA7969296852F703EABDD1A8102471459CE2EBB95BDA3FCA2E9FC22E60CEC6FF3B9CA9F262A6C204E25241560C6FCA1B1CAEC90C81AA7860E1EA2C63565D21AF3E4870203010001A381B13081AE301D0603551D0E04160414B081087CF98B6BDA2FD4F7C6392F97E6FA4E560F30818C0603551D23048184308181801407C69254961EB26785D105D969445BF72CF3CCA7A15FA45D305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F74434182081BF6C2A9D76A7CF2300D06092A864886F70D0101050500038201010090317C2BAEC7343EBF0D3314FD354FBE54A8878D5DFF30F5E1DE73E06DF08EDDE31A12DF6698E9771A896BA4ECD0440B239ED94551099E20EE12357F2839A270E5BD26ADD1B44438F9BE9189DAA92C63B2BDB4CF42DBA37798F1B3C72A872C8046B5B64C0E8DF84D2F730FDB9EE86FB1977972CA114B5830D62E0162A8B9BB50011E3D5E6EDCE80AA52968F34B04E7DA18CEB250349C0010F682C71FE8CE5EF1582941BED59FB534663B12C8F1BC80676D1307DF287AFD0169E82587667C3D35233C49AB76606BBF89B8704AB183B62FFB2D56C535605297D70ED4B6ADC9294EE8C7C31433FA29DC3778C4307B86543A815A0401F9D907567BA7D009B2A9F77B\">\n<input type=\"hidden\" name=\"merchantSign\" value=\"C66CDDEEF5FC4EFE33AB87431354B65AA384022A2E44E7FCF627E787CE6F9EC696B71DFE9D33F74A393DA82B2F53B5C961004D0C717FC235FC1E228422B9772AF7E5310C34D04961648FB7623677F8CB36CA1FA849A53846C7AD6841B66CD1266BEBC8892751DA2B0CCEC9D0B40441C8F8A6FC25CC1F6DCA9C980496CFC59630\">\n<input type=\"hidden\" name=\"signType\" value=\"RSA\">\n<input type=\"hidden\" name=\"service\" value=\"NpDirectPayment\">\n<input type=\"hidden\" name=\"pageReturnUrl\" value=\"https://www.paymax.cc/webservice/redirect/lakala_web\">\n<input type=\"hidden\" name=\"offlineNotifyUrl\" value=\"https://www.paymax.cc/webservice/callback/lakala_web\">\n<input type=\"hidden\" name=\"clientIP\" value=\"127.0.0.1\">\n<input type=\"hidden\" name=\"requestId\" value=\"af8948a7a36747049142ffdb9d9a76f1\">\n<input type=\"hidden\" name=\"purchaserId\" value=\"1000100037257447\">\n<input type=\"hidden\" name=\"merchantId\" value=\"888010073720002\">\n<input type=\"hidden\" name=\"merchantName\" value=\"奥鹏教育\">\n<input type=\"hidden\" name=\"orderId\" value=\"ch_102df48965eba04821222fb6\">\n<input type=\"hidden\" name=\"orderTime\" value=\"20161018165824\">\n<input type=\"hidden\" name=\"totalAmount\" value=\"30000\">\n<input type=\"hidden\" name=\"currency\" value=\"CNY\">\n<input type=\"hidden\" name=\"validUnit\" value=\"00\">\n<input type=\"hidden\" name=\"validNum\" value=\"60\">\n<input type=\"hidden\" name=\"showUrl\" value=\"null\">\n<input type=\"hidden\" name=\"productDesc\" value=\"XLJF\">\n<input type=\"hidden\" name=\"productName\" value=\"XLJF\">\n<input type=\"hidden\" name=\"productId\" value=\"null\">\n<input type=\"hidden\" name=\"backParam\" value=\"null\">\n<input type=\"hidden\" name=\"bankAbbr\" value=\"CMB\">\n<input type=\"hidden\" name=\"cardType\" value=\"0\">\n</form>\"},\"currency\":\"CNY\",\"description\":\"\",\"extra\":{\"user_id\":\"1000100037257447\",\"bank_code\":\"CMB\",\"return_url\":\"http://pay-service-openpre.myalauda.cn/pay-service/paymax/callBack\"},\"failure_msg\":\"TimeOut\",\"id\":\"ch_102df48965eba04821222fb6\",\"livemode\":false,\"order_no\":\"20161018165824504076\",\"refunds\":[],\"status\":\"FAILED\",\"subject\":\"XLJF\",\"time_created\":1476781104000,\"time_expire\":1476784704248,\"transaction_no\":\"201610180002488545\"},\"notifyNo\":\"evt_4c823fa759954ccebe26fc08dd0d4fde\",\"timeCreated\":1476784720139,\"type\":\"CHARGE\"}";
	//	String requestBody1="{\"data\":{\"amount\":0.01,\"amount_refunded\":0,\"amount_settle\":0,\"body\":\"testGoodsDesc\",\"client_ip\":\"127.0.0.1\",\"credential\":{\"lakala_web\":\"<form method=\\\"post\\\" action=\\\"http://paymax.lakala.com/hipos/cashier\\\">\n<input type=\\\"hidden\\\" name=\\\"charset\\\" value=\\\"02\\\">\n<input type=\\\"hidden\\\" name=\\\"version\\\" value=\\\"1.0\\\">\n<input type=\\\"hidden\\\" name=\\\"merchantCert\\\" value=\\\"3082036F30820257A00302010202081BFA980089EAD5E6300D06092A864886F70D0101050500305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F744341301E170D3136303930393033323833305A170D3230303930393033323833305A3064310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C0249543118301606035504030C0F38383830313030373337323030303230819F300D06092A864886F70D010101050003818D0030818902818100D8B391F6A6C7DECE7A0E8C0F8E62B2F3905BB298883B96149A5596897F7339620F390B076C5785FFB828B068429B097781C4BF8E1536D84772E13EC83ADFA7969296852F703EABDD1A8102471459CE2EBB95BDA3FCA2E9FC22E60CEC6FF3B9CA9F262A6C204E25241560C6FCA1B1CAEC90C81AA7860E1EA2C63565D21AF3E4870203010001A381B13081AE301D0603551D0E04160414B081087CF98B6BDA2FD4F7C6392F97E6FA4E560F30818C0603551D23048184308181801407C69254961EB26785D105D969445BF72CF3CCA7A15FA45D305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F74434182081BF6C2A9D76A7CF2300D06092A864886F70D0101050500038201010090317C2BAEC7343EBF0D3314FD354FBE54A8878D5DFF30F5E1DE73E06DF08EDDE31A12DF6698E9771A896BA4ECD0440B239ED94551099E20EE12357F2839A270E5BD26ADD1B44438F9BE9189DAA92C63B2BDB4CF42DBA37798F1B3C72A872C8046B5B64C0E8DF84D2F730FDB9EE86FB1977972CA114B5830D62E0162A8B9BB50011E3D5E6EDCE80AA52968F34B04E7DA18CEB250349C0010F682C71FE8CE5EF1582941BED59FB534663B12C8F1BC80676D1307DF287AFD0169E82587667C3D35233C49AB76606BBF89B8704AB183B62FFB2D56C535605297D70ED4B6ADC9294EE8C7C31433FA29DC3778C4307B86543A815A0401F9D907567BA7D009B2A9F77B\\\">\n<input type=\\\"hidden\\\" name=\\\"merchantSign\\\" value=\\\"0DFA3771DC5C1C61A33EC1E51F86C0D6BA89B2FBEDB44F03BB8863DA4F91940B1D04BBC2805F88BEF85DF7BC05E83D584368039130C8A0F6B17BB58F68D8390E34010B1797ED7D626B1F75B235CC2648C4BE47536382D440B42876E34D5C25D8CD56E9C68612B7F1785463059E12E7CCA19B6DA52424F84E71EEC6709F3024FB\\\">\n<input type=\\\"hidden\\\" name=\\\"signType\\\" value=\\\"RSA\\\">\n<input type=\\\"hidden\\\" name=\\\"service\\\" value=\\\"NpDirectPayment\\\">\n<input type=\\\"hidden\\\" name=\\\"pageReturnUrl\\\" value=\\\"https://www.paymax.cc/webservice/redirect/lakala_web\\\">\n<input type=\\\"hidden\\\" name=\\\"offlineNotifyUrl\\\" value=\\\"https://www.paymax.cc/webservice/callback/lakala_web\\\">\n<input type=\\\"hidden\\\" name=\\\"clientIP\\\" value=\\\"127.0.0.1\\\">\n<input type=\\\"hidden\\\" name=\\\"requestId\\\" value=\\\"408b9e005bc040adb924a456b3d21ebe\\\">\n<input type=\\\"hidden\\\" name=\\\"purchaserId\\\" value=\\\"test201610181826\\\">\n<input type=\\\"hidden\\\" name=\\\"merchantId\\\" value=\\\"888010073720002\\\">\n<input type=\\\"hidden\\\" name=\\\"merchantName\\\" value=\\\"奥鹏教育\\\">\n<input type=\\\"hidden\\\" name=\\\"orderId\\\" value=\\\"ch_462fc7ac96a3c2df079f0f5e\\\">\n<input type=\\\"hidden\\\" name=\\\"orderTime\\\" value=\\\"20161018182643\\\">\n<input type=\\\"hidden\\\" name=\\\"totalAmount\\\" value=\\\"1\\\">\n<input type=\\\"hidden\\\" name=\\\"currency\\\" value=\\\"CNY\\\">\n<input type=\\\"hidden\\\" name=\\\"validUnit\\\" value=\\\"00\\\">\n<input type=\\\"hidden\\\" name=\\\"validNum\\\" value=\\\"60\\\">\n<input type=\\\"hidden\\\" name=\\\"showUrl\\\" value=\\\"null\\\">\n<input type=\\\"hidden\\\" name=\\\"productDesc\\\" value=\\\"testGoodsDesc\\\">\n<input type=\\\"hidden\\\" name=\\\"productName\\\" value=\\\"testGoodsName\\\">\n<input type=\\\"hidden\\\" name=\\\"productId\\\" value=\\\"null\\\">\n<input type=\\\"hidden\\\" name=\\\"backParam\\\" value=\\\"null\\\">\n<input type=\\\"hidden\\\" name=\\\"bankAbbr\\\" value=\\\"CMB\\\">\n<input type=\\\"hidden\\\" name=\\\"cardType\\\" value=\\\"0\\\">\n</form>\"},\"currency\":\"CNY\",\"description\":\"\",\"extra\":{\"user_id\":\"test201610181826\",\"bank_code\":\"CMB\",\"return_url\":\"http://pay-service-openpre.myalauda.cn/pay-service/paymax/callBack\"},\"id\":\"ch_462fc7ac96a3c2df079f0f5e\",\"livemode\":false,\"order_no\":\"20161018182642888654\",\"refunds\":[],\"status\":\"SUCCEED\",\"subject\":\"testGoodsName\",\"time_created\":1476786403000,\"time_expire\":1476790003064,\"time_paid\":1476786458000,\"transaction_no\":\"201610180002491550\"},\"notifyNo\":\"evt_c88950d2642546b28996b30f68abb5f6\",\"timeCreated\":1476786459576,\"type\":\"CHARGE\"}";
		//System.out.println(requestBody);
		//log.info("request boay:"+ requestBody);
		 String backMsg="";
		if(!nullEmptyBlankJudge(requestBody)){
			
		JSONObject obj = JSONObject.fromObject(requestBody);
		//JSONObject obj = null;
		String sign = request.getHeader("sign");
		//sign="hAOMbboYRDemQvIVcv4yMRMvdsDzR22KqBZKuY+QzoE0ryRFNAz23b64pNUMBEPsdonv+QKjgv3P1Kf3UGkNDHNq5GqfU6wKVicxH4bxJ0zCesOKiygsdeSS6zZwjBSnsA/R2HsP4j/qcnhb61Ym3gkEHlank6i8NbyEOg0/N4M=";
		//log.info("header sign:" + sign);
		//requestBody1.replaceAll("/n","");
		//String paymaxPublicKey1 ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDE0gdZi/icODd0WAQlxfw8FgoJ/BwCgMLn+Fh1DquRXqYkgGtv35B5j+CCSpag3wxNwtrHRHpar3dpo38+KIqaMy8bhQj1hmBJmHkgM1yZZpVC5S4uc4FYT21zQn0HBI1E1gg2nAA7JkFV1+VnvFKk9rzOA2UtrYXID+ZG4BMBwwIDAQAB";
	  	    //验证签名
	  	//boolean signCheck1 = RSA.verify(requestBody1, sign, paymaxPublicKey1);
	  	//log.info("signCheck1========================================"+signCheck1);
	    long startTime = System.currentTimeMillis();
	    //amount=0.01&amount_refunded=0&amount_settle=0&body=testGoodsDesc&client_ip=127.0.0.1&currency=CNY&description=&extra=%7B%22user_id%22%3A%2220160920155907385438%22%2C%22return_url%22%3A%22http%3A%2F%2F10.96.5.174%3A8080%2Fpay-service%2Fpaymax%2FcallBack%22%7D&id=ch_edf13224121f1f1c322b326f&livemode=false&order_no=20160920155907385438&status=PROCESSING&subject=testGoodsName&time_created=1474358348000&time_expire=1474361948402&sign=vfXIUf483Ec0n3QTX458JcmvshwkRf44UhQsYTgX%2BkkqNlhclmwrYw2I2M9aoxShF99eeY9cjnXTcUf5qVDwQGq7ZFazOAOyjUHqni7zW2OqmsaQw5hRHfcKJcXYig19vmmpfZgbVsvFGi2NHILrU1j7LKJ9nYfOj56%2FN7S0j1s%3D
		String data = obj.getString("data");
		//log.info("data======================="+ data);
		JSONObject dataobj = JSONObject.fromObject(data);
		
		//商户订单号
		
		String order_no = dataobj.getString("order_no");
		//log.info("order_no======================="+ order_no);
		//拉卡拉唯一订单id
		String id=dataobj.getString("id");
		//log.info("id======================="+ id);
		String amount=dataobj.getString("amount");
		//log.info("amount======================="+ amount);
		String status=dataobj.getString("status");
		log.info("status======================="+ status);
		//System.out.println("paymax_status=="+status);
		
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(order_no);
		//log.info("notify orderId======================="+ order_no);
		 PayServiceLog payServiceLog=new PayServiceLog();
		 payServiceLog.setOrderId(order_no);
		 payServiceLog.setPayOrderId(id);
		 payServiceLog.setAmount(String.valueOf(Double.parseDouble(amount)*100));
		 if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
			 DictTradeChannel dictTradeChannel=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.PAYMAX.getValue());
			 
			 payServiceLog.setAppId(merchantOrderInfo.getAppId());
			 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
			 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
			 payServiceLog.setLogType(payserviceDev.getLog_type());
			 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
			 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
			 
			 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
			 
			 payServiceLog.setProductDesc(merchantOrderInfo.getMerchantProductDesc());
			 payServiceLog.setProductName(merchantOrderInfo.getMerchantProductName());
			 payServiceLog.setRealAmount(String.valueOf(Double.parseDouble(amount)*100));
			 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
			 payServiceLog.setUsername(merchantOrderInfo.getUserName());
			 payServiceLog.setLogName(PayLogName.PAYMAX_RETURN_START);
	         payServiceLog.setStatus("ok");
	         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	         String returnUrl=merchantOrderInfo.getReturnUrl();
	  		MerchantInfo merchantInfo = null;
	  		merchantInfo=merchantInfoService.findById(merchantOrderInfo.getMerchantId());
	  		if(nullEmptyBlankJudge(returnUrl)){
	  		
	  			returnUrl=merchantInfo.getNotifyUrl();
	  		}
	  		 String other= dictTradeChannel.getOther();
		  	Map<String, String> others = new HashMap<String, String>();
		  	others=getPartner(other);
	  	    String paymaxPublicKey =others.get("paymax_public_key");
	  	    //验证签名
	  	   requestBody.replaceAll("/n","");
	  	   boolean signCheck = RSA.verify(requestBody, sign, paymaxPublicKey);
	  	   
		    if(signCheck){
		    	if(status.equals("SUCCEED")){
		    		//log.info("==========================SUCCEED======================================");
		    		//订单处理成功
		    		 payServiceLog.setLogName(PayLogName.PAYMAX_NOTIFY_START);
					 if(!nullEmptyBlankJudge(returnUrl)){
						 //Map<String, String> dataMap=new HashMap<String, String>();
						 String buf="";
						    int notifyStatus=merchantOrderInfo.getNotifyStatus();
							int payStatus=merchantOrderInfo.getPayStatus();
							Double payCharge=0.0;
							payCharge=UnifyPayUtil.getTclPayCharge(merchantOrderInfo,channelRateService);
							if(payStatus!=1){
								merchantOrderInfo.setPayStatus(1);
								merchantOrderInfo.setPayAmount(Double.parseDouble(amount)-payCharge);
								merchantOrderInfo.setAmount(Double.parseDouble(amount));
								merchantOrderInfo.setPayCharge(payCharge);
								merchantOrderInfo.setDealDate(new Date());
								merchantOrderInfo.setPayOrderId(id);
								merchantOrderInfoService.updateOrder(merchantOrderInfo);
								if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
									String rechargeMsg=UnifyPayUtil.recordAndBalance(Double.parseDouble(amount)*100,merchantOrderInfo,userSerialRecordService,userAccountBalanceService,payserviceDev);
								}
							}
						    SortedMap<String,String> sParaTemp = new TreeMap<String,String>();
							sParaTemp.put("orderId", merchantOrderInfo.getId());
					        sParaTemp.put("outTradeNo", merchantOrderInfo.getMerchantOrderId());
					        sParaTemp.put("merchantId", String.valueOf(merchantOrderInfo.getMerchantId()));
					        sParaTemp.put("paymentType", String.valueOf(merchantOrderInfo.getPaymentId()));
							sParaTemp.put("paymentChannel", String.valueOf(merchantOrderInfo.getChannelId()));
							sParaTemp.put("feeType", "CNY");
							sParaTemp.put("guid", merchantOrderInfo.getGuid());
							sParaTemp.put("appUid",String.valueOf(merchantOrderInfo.getSourceUid()));
							//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
							sParaTemp.put("timeEnd", DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
							sParaTemp.put("totalFee", String.valueOf((int)(merchantOrderInfo.getOrderAmount()*100)));
							sParaTemp.put("goodsId", merchantOrderInfo.getMerchantProductId());
							sParaTemp.put("goodsName",merchantOrderInfo.getMerchantProductName());
							sParaTemp.put("goodsDesc", merchantOrderInfo.getMerchantProductDesc());
							sParaTemp.put("parameter", merchantOrderInfo.getParameter1()+"payCharge="+String.valueOf((int)(merchantOrderInfo.getPayCharge()*100))+";");
							sParaTemp.put("userName", merchantOrderInfo.getSourceUserName());
						    String mySign = PayUtil.callBackCreateSign(AlipayConfig.input_charset,sParaTemp,merchantInfo.getPayKey());
						    sParaTemp.put("secret", mySign);
						    buf =SendPostMethod.buildRequest(sParaTemp, "post", "ok", returnUrl);
						    model.addAttribute("res", buf);
						    //log.info("==========================notify-start======================================");
						    //并且异步通知
						    try {
						    	if(notifyStatus!=1){
									 Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService,payserviceDev));
									   thread.run();	
								}
							} catch (Exception e) {
								backMsg="success";
							}
						   
						  // log.info("==========================notify--end======================================");
						   backMsg="success";
					 }else{
						  merchantOrderInfoService.updatePayStatus(5,String.valueOf(merchantOrderInfo.getId()));
					 }
		    		
		    	}else if(status.equals("PROCESSING")){
		    		// log.info("==========================PROCESSING======================================");
		    		//订单处理中
		    		merchantOrderInfoService.updatePayInfo(3,String.valueOf(merchantOrderInfo.getId()),"PAYPROCESSING");
		    	}else{
		    		// log.info("==========================PAYFAIL======================================");
		    		//订单处理失败
					  payServiceLog.setErrorCode("2");
			          payServiceLog.setStatus("error");
			          payServiceLog.setLogName(PayLogName.PAYMAX_NOTIFY_END);
			          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			          merchantOrderInfoService.updatePayInfo(2,String.valueOf(merchantOrderInfo.getId()),"PAYFAIL");
					 backMsg="error";
		    	}
		    	
		    }else{
		    	//log.info("==========================VERIFYERROR======================================");
		    	  payServiceLog.setErrorCode("2");
		          payServiceLog.setStatus("error");
		          payServiceLog.setLogName(PayLogName.PAYMAX_NOTIFY_END);
		          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		          merchantOrderInfoService.updatePayInfo(4,String.valueOf(merchantOrderInfo.getId()),"VERIFYERROR");
				 backMsg="error";
		    }
		    
		 }else{
			// log.info("==========================error  null======================================");
			 payServiceLog.setErrorCode("2");
	          payServiceLog.setStatus("error");
	          backMsg="error";
	          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
				 {
	        	  payServiceLog.setStatus("already processed");
	        	  backMsg="success";
				 }
	          payServiceLog.setLogName(PayLogName.PAYMAX_NOTIFY_END);
				
	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			
		 }	
		 }else{
			 backMsg="error";
		 }
		 WebUtils.writeJson(response, backMsg);
	  } 
	private String getRequestBody(HttpServletRequest request) throws IOException {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
		buffer.append(line);
		}
		return buffer.toString();
		}
	private boolean _verifySign(HttpServletRequest request, String paymaxPublicKey) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()){
		String key = names.nextElement().toString();
		map.put(key, request.getParameter(key));
		}
		Map.Entry<String,String>[] entries = map.entrySet().toArray(new Map.Entry[
		map.size()]);
		resortParams(entries);
		Set<String> ignoreProperties=new HashSet<String>();
		ignoreProperties.add("refunds");
		ignoreProperties.add("credential");
		ignoreProperties.add("sign");
		String toSignStr = generateHttpGetParamString(entries, false, ignoreProperties);
		return RSA.verify(toSignStr, map.get("sign").toString(), paymaxPublicKey);
		}
	
	private void resortParams(Map.Entry[] entries){
		Arrays.sort(entries, new Comparator<Map.Entry>() {
		@Override
		public int compare(Map.Entry o1, Map.Entry o2) {
		return String.valueOf(o1.getKey()).compareTo(String.valueOf(o2.getKey(
		)));
		}
		});
		}
	
	private String generateHttpGetParamString(Map.Entry<String,String>[] entries,boolean urlEncoding,Set<String> ignoreProperties){
		String redirectParams="";
		for(Map.Entry<String,String> entry:entries){
		if(ignoreProperties!=null && ignoreProperties.contains(entry.getKey())
		){
		continue;
		}
		if(entry.getValue()==null){
		continue;
		}
		String value = entry.getValue();
		if(urlEncoding){
		try {
		value= URLEncoder.encode(value,"utf-8");
		} catch (UnsupportedEncodingException e) {
		//LogPortal.error(e.getMessage(),e);
		}
		}
		redirectParams+="&"+entry.getKey()+"="+ value;
		}
		return redirectParams.substring(1);
		}
}