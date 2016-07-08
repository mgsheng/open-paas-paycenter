package cn.com.open.openpaas.payservice.app.channel.tclpay.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.AliOrderProThread;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.data.ScanCodeOrderData;
import cn.com.open.openpaas.payservice.app.channel.tclpay.service.ScanCodeOrderService;
import cn.com.open.openpaas.payservice.app.channel.tclpay.sign.RSASign;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytPacketUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytUtils;
import cn.com.open.openpaas.payservice.app.log.AlipayControllerLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;


/**
 * 
 */
@Controller
@RequestMapping("/tcl/order/")
public class TCLOrderCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(TCLOrderCallbackController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	/**
	 * 支付宝订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("callBack")
	public void dirctPay(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) throws MalformedURLException, DocumentException, IOException {
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no =request.getParameter("out_trade_no");
		//支付宝交易号
		String trade_no =  request.getParameter("trade_no") ;
        String total_fee= request.getParameter("total_amount") ;
		//交易状态
		String trade_status =  request.getParameter("status") ;
		String charset= request.getParameter("charset") ;
		String version= request.getParameter("version") ;
		String server_cert= request.getParameter("server_cert") ;
		String server_sign= new String(request.getParameter("server_sign").getBytes("ISO-8859-1"), "GBK");
		String sign_type=new String(request.getParameter("sign_type").getBytes("ISO-8859-1"), "GBK");
		String service= request.getParameter("service") ;
		String return_code= request.getParameter("return_code") ;
		String return_message= request.getParameter("return_message");
		String buyer_id= request.getParameter("buyer_id") ;
		String merchant_code= request.getParameter("merchant_code") ;
		String order_time= request.getParameter("order_time") ;
		String channel_code= request.getParameter("channel_code") ;
		String pay_time= request.getParameter("pay_time") ;
		String ac_date= request.getParameter("ac_date") ;
		String fee= request.getParameter("fee") ;
		String attach= request.getParameter("attach") ;
		
		Map<String, String> orderDataMap = new HashMap<String, String>();
		orderDataMap.put("out_trade_no", out_trade_no);
		orderDataMap.put("trade_no", trade_no);
		orderDataMap.put("total_fee", total_fee);
		orderDataMap.put("trade_status", trade_status);
		orderDataMap.put("charset", charset);
		orderDataMap.put("version", version);
		orderDataMap.put("server_cert", server_cert);
		orderDataMap.put("server_sign", server_sign);
		orderDataMap.put("sign_type", sign_type);
		orderDataMap.put("service", service);
		orderDataMap.put("return_code", return_code);
		orderDataMap.put("return_message", return_message);
		orderDataMap.put("buyer_id", buyer_id);
		orderDataMap.put("merchant_code", merchant_code);
		orderDataMap.put("order_time", order_time);
		orderDataMap.put("channel_code", channel_code);
		orderDataMap.put("pay_time", pay_time);
		orderDataMap.put("ac_date", ac_date);
		orderDataMap.put("fee", fee);
		orderDataMap.put("attach", attach);
	
		 // String Wsign=HytUtils.getVertifyFromStr(orderDataMap); //获得待签名报文
			String Wsign=HytPacketUtils.map2Str(orderDataMap);
		  //String Wsign="ac_date=20160708&attach=1010&channel_code=ALIPAY&charset=00&fee=0&merchant_code=800075500030008&order_time=20160708111830&out_trade_no=test201607081120&pay_time=20160708111842&return_code=000000&return_message=SUCCESS&service=PageResult&sign_type=RSA&total_fee=1&trade_no=201607080024090676&trade_status=SUCCESS&version=1.0";
          System.out.println("==================待签名报文===============>>>>"+ Wsign);
         // server_cert="308203333082021B02081BF6D4071A9FF72A300D06092A864886F70D0101050500305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F7443413020170D3135303331303032323731345A180F32333830303331303032323731345A305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F74434130820122300D06092A864886F70D01010105000382010F003082010A028201010087AAF7D89AB7656653FB847DFAB21274A31E1B18E896015A829CF58A935C085C8E8781A86F23CD1AF347C885380E2372AC052FBAEDDD806F2D18246C471115241A311802AB6FAD98DE1A15FF2BBF52DDA2FD9809F53E9B1998E40BF849E74C8692D261E9A8E8A2282D25CA1DB4D328FD8E9DAE062AEC70982DF897ED08EFB25E2B70BBC3ABF5B41121DD89D8CB90935DF6C5B16134A0CC4B03E2319D5BACF5C5F588934DCD3B1EC22E876B1D99C31D95CCECAB124AD1CFC371A80AA6111928752EBCB354AF2DF4D0FD32D183069702D46C4399A31ADF23DF5990FD1241DE0EFD391CDD959F8F95EF96AB425FD8F0E0593E784CBD45854DC84F73393CBECF8DA70203010001300D06092A864886F70D010105050003820101004485925685C81FAFA9EC49A8D426E6E8062C97C5A8BED63A1641AECA9B209C47F83795741E83FE79C26BE2AA2EDE66B41D9E19043475D25B6187CBEF044F912873340EEFFA44BAF26F9A84004E2E3CCDAE11A04FE68536545C18B6CDE2B7351E67528A9FD2016DC3CB20B5B9C7BFA85792C0EE26B6CF7C3326DF23E47CEBD51FCA1F2454B12F755B3273E01A751CF170DF5502254D1402D94E875EBDF94E22471E6CF6E11B4BD8B7DA393DA1117E8CCBE5562CA16A68894D69DB9FD9333107126BD1FC6AD2AA20589E6F5274B88DF765B30012105B0F169E5C9F8502022846DF2B5F07249416C50FCCD01C874710C4353D84BD8DBF1A7F89260BAB40D8619AFF";
          //server_sign="0C4B5C4871B711E4DA55AC8DDF9E288B9D3B0DE70B4C0990C7EBF7438E308CC20568D6C08F978232B84A8F109A686BF1D778147607380D26E1A24ACD4E0AA06727B5BD09EB8920FA386BBE27DF5C1D73B13F4AE647A58F0B8D1873747B8580DF5FF021FD0D94B8F3A282D2E621CC309EF695F2DC085D90BFD0F40778AB1A464B3E124B76DBAAAD9CB74FCD639CFC02D0A59955D92F34A071392D18DB935A76C7C5DCF6AF83800EFB593CB2DB9BEFF960642999A081B3909692B7BDA0105063E1545DCA0801A645BB49666E715EAE8BBEAE5B174298FA887D1D794DA9B0BC79C2F82D3BCA265B4D5EC71082BDB7090FA37E9FEA1BBB9E3B9CF811AF99227D7E41";
          System.out.println("==================server_cert===============>>>>"+ server_cert);
          System.out.println("==================server_sign===============>>>>"+ server_sign);
          String backMsg="";
          // -- 验证签名
				boolean flag = false;	
				
				RSASign rsautil =HytUtils.getRSASignVertifyObject(); 
			    flag = rsautil.verify(Wsign,server_sign,server_cert, HytConstants.CHARSET_GBK);//验证签名
			    if (!flag) {
					  String info="错误信息：验签错误";
					  String CODE="MCG111111";
					  System.out.println("{\"return_code\":\""+CODE+"\",\"return_message\":\""+URLDecoder.decode(info,"UTF-8")+"\"}");
					  MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(out_trade_no);
					  int payStatus=merchantOrderInfo.getPayStatus();
					  Double payCharge=0.0;
					  if(payStatus!=1){
							merchantOrderInfo.setPayStatus(2);
							merchantOrderInfo.setPayAmount(Double.valueOf(total_fee)-payCharge);
							merchantOrderInfo.setAmount(Double.valueOf(total_fee));
							merchantOrderInfo.setPayCharge(0.0);
							merchantOrderInfo.setDealDate(new Date());
							merchantOrderInfo.setPayOrderId(trade_no);
							merchantOrderInfoService.updateOrder(merchantOrderInfo);
						}
					  backMsg="error";
				}else{
					 if (!return_code.equals("000000")) { //请求异常
							  backMsg="error";
							
						}else{
							//判断该笔订单是否在商户网站中已经做过处理
							//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
							MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(out_trade_no);
							int notifyStatus=merchantOrderInfo.getNotifyStatus();
							int payStatus=merchantOrderInfo.getPayStatus();
							Double payCharge=0.0;
							if(payStatus!=1){
								merchantOrderInfo.setPayStatus(1);
								merchantOrderInfo.setPayAmount(Double.valueOf(total_fee)-payCharge);
								merchantOrderInfo.setAmount(Double.valueOf(total_fee));
								merchantOrderInfo.setPayCharge(0.0);
								merchantOrderInfo.setDealDate(new Date());
								merchantOrderInfo.setPayOrderId(trade_no);
								merchantOrderInfoService.updateOrder(merchantOrderInfo);
							}
							if(notifyStatus!=1){
								 Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService));
								   thread.run();	
							}
							  backMsg="success";
						}
						
					
				}
			    WebUtils.writeJson(response, backMsg);
			   
					//如果有做过处理，不执行商户的业务程序
		
	  } 
}