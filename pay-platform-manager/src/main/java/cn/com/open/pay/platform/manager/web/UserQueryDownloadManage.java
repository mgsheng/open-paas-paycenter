package cn.com.open.pay.platform.manager.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderInfoService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
import cn.com.open.pay.platform.manager.tools.OrderDeriveExport;



/**
 *  用户登录接口(通过用户名-密码)
 */
@Controller
@RequestMapping("/manage/")
public class UserQueryDownloadManage extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserQueryDownloadManage.class);
	
	 @Autowired
	 private UserService userService;
	 
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	
	 /**
	  * 页面跳转
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("skipPages")
	 public String  skipPages(HttpServletRequest request,HttpServletResponse response) {
	     return "usercenter/merchantMessage";
	 }	

	 /**
	  * 查询信息
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("queryMerchant")
	 public String  queryMerchant(HttpServletRequest request,HttpServletResponse response) {
		 log.info("-----------------------login start----------------");
		 int channelId=0;
		 String merchantOrderDate=request.getParameter("merchantOrderDate");//下单日期
		 String merchantOrderId=request.getParameter("merchantOrderId");//商户订单号
		 String payOrderId=request.getParameter("payOrderId");//第三方订单号
		 String CI=request.getParameter("channelId"); //支付方式
		 if(CI!=null&&!CI.equals("")){
			 channelId=Integer.parseInt(CI);
		 }
		 String businessType=request.getParameter("businessType");//业务类型  字段暂时不确定
		 String openingBank=request.getParameter("openingBank"); //发卡行 字段暂时不确定
		 String source=request.getParameter("source"); //缴费来源 1、pc端2、移动端
		 String PS=request.getParameter("payStatus"); //交易状态
		 int payStatus=0;
		 if(PS!=null&&!PS.equals("")){
			 payStatus=Integer.parseInt(PS);
		 }
		 
		 
//		 int createDate=0;
		 Integer createDate = null;
		 String dateDay=request.getParameter("createDate"); //交易时间天数
		 String startDate1 = null;
		 String endDate1 =null;
		 if(dateDay!=null){
			 createDate=Integer.parseInt(dateDay);	 
			 if(dateDay!=null&&createDate<0){
				 String startDate=request.getParameter("startDate"); //交易时间开始时间
				 String endDate=request.getParameter("endDate"); //交易时间结束时间
				 SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
				 Date Date1 = null;
				 Date Date2 = null;
				try {
					Date1 = format1.parse(startDate);
					Date2 = format1.parse(endDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 format1 = new SimpleDateFormat("yyyy-MM-dd");
				 startDate1 = format1.format(Date1)+" 00:00:00";
				 endDate1 = format1.format(Date2)+" 23:59:59";
			 }else{
	//			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				 if(createDate==0){
					    String date = df.format(new Date());
						startDate1 = date+" 00:00:00";
						endDate1 = date+" 23:59:59";
				 }else if(createDate>0){
					 endDate1 = df.format(new Date());
					 Date dat = null;
					 Calendar cd = Calendar.getInstance();
					 cd.add(Calendar.DATE, (createDate > 0) ? -createDate : createDate);
					 dat = cd.getTime();
					 startDate1 = df.format(dat);
					 startDate1 = startDate1+" 00:00:00";
					 endDate1 = endDate1+" 23:59:59";
				 } 
			 }
		 }
		 System.out.println(startDate1+"**************************"+endDate1);
		 MerchantOrderInfo merchantOrderInfo =new MerchantOrderInfo();
		 
		 merchantOrderInfo.setMerchantOrderDate(merchantOrderDate);
		 merchantOrderInfo.setStartDate(startDate1);
		 merchantOrderInfo.setEndDate(endDate1);
		 
		 merchantOrderInfo.setMerchantOrderId(merchantOrderId);
		 merchantOrderInfo.setPayOrderId(payOrderId);   //第三方订单号
		 merchantOrderInfo.setChannelId(channelId); 	//支付方式
		 merchantOrderInfo.setPayStatus(payStatus);		//交易状态
		 
		 List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoService.findQueryMerchant(merchantOrderInfo);
		 
		 
//		 OrderMessageExport.exportChuBei(response, merchantOrderInfoList);
		 
		 /* String backMsg="";
		 String username=request.getParameter("username");
	     String password=request.getParameter("password");
	     User user = null;
	     user=checkUsername(username,userService);
	     if(user!=null){
	    	  if(user.checkPasswod(password)){
	    		  backMsg="ok"; 
				}else{
				  backMsg="error"; 
				}
	    	
	     }else{
	    	 backMsg="error"; 
	     }
	     WebUtils.writeJson(response,backMsg);*/
	     return "usercenter/merchantMessage";
	 }	
	 
	 /**
	  * 页面跳转
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("downloadSubmit")
	 public String  downloadSubmit(HttpServletRequest request,HttpServletResponse response) {
		 MerchantOrderInfo merchantOrderInfo =new MerchantOrderInfo();
		 merchantOrderInfo.setMerchantOrderId("");
		 merchantOrderInfo.setPayOrderId("");   //第三方订单号
		 List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoService.findQueryMerchant(merchantOrderInfo);
		 OrderDeriveExport.exportChuBei(response, merchantOrderInfoList);
	     return "usercenter/merchantMessage";
	 }
	 
//	 @RequestMapping("downloadSubmit")
//	 public void  downloadSubmit(HttpServletRequest request,HttpServletResponse response) {
//		 log.info("-----------------------login start----------------");
////		 String e="";//下单时间 
//		 String merchantOrderDate="1";//交易时间 (今日，昨日，最近7天，最近30天，自定义日期)
//		 String merchantOrderId="";//商户订单号、
//		 String payOrderId="";//第三方订单号、
//		 int paymentId=0;//支付方式（支付宝、微信、银联、易宝、易宝POS和TCL汇银通）、
////		 String //业务类型（学历教育、慕课中国…）、发卡行、缴费来源（PC端和移动端）、
//		 int payStatus=0;//交易状态
//		 
//		 
//		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		 String startDate="";
//		 String endDate="";
//		 int day=30;
////		 String date = df.format(new Date());
////			startDate = date+" 00:00:00";
//		 if(merchantOrderDate.equals("2")){
//			    String date = df.format(new Date());
//				startDate = date+" 00:00:00";
//				endDate = date+" 23:59:59";
//		 }else if(merchantOrderDate.equals("1")){
//			 String date = df.format(new Date());
//			 Date dat = null;
//			 Calendar cd = Calendar.getInstance();
//			 cd.add(Calendar.DATE, (day > 0) ? -day : day);
//			 dat = cd.getTime();
//			 String date1 = df.format(dat);
//			 System.out.println(date);
//			 startDate = date1+" 00:00:00";
//			 endDate = date+" 23:59:59";
//		 }
//		 System.out.println(startDate+"**************************"+endDate);
//		 MerchantOrderInfo merchantOrderInfo =new MerchantOrderInfo();
//		 
//		 merchantOrderInfo.setMerchantOrderDate(merchantOrderDate);
//		 merchantOrderInfo.setStartDate(startDate);
//		 merchantOrderInfo.setEndDate(endDate);
//		 
//		 merchantOrderInfo.setMerchantOrderId(merchantOrderId);
//		 merchantOrderInfo.setPayOrderId(payOrderId);
//		 merchantOrderInfo.setPaymentId(paymentId);
//		 merchantOrderInfo.setPayStatus(payStatus);
//		 
//		 List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoService.findQueryMerchant(merchantOrderInfo);
//		 
//		 
//		 orderMessageExport.exportChuBei(response, merchantOrderInfoList);
//		 
//	 }	
	 
	 	
}