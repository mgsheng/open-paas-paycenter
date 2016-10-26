package cn.com.open.pay.platform.manager.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.department.service.MerchantInfoService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderInfoService;
import cn.com.open.pay.platform.manager.order.service.UserSerialRecordService;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelSwitch;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelDictionaryService;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelSwitchService;
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
	 
	 @Autowired
	 private UserSerialRecordService userSerialRecordService;
	 
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 
	 @Autowired
	 private PayChannelDictionaryService payChannelDictionaryService;
	 
	 @Autowired
	 private PayChannelSwitchService payChannelSwitchService;
	
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
	  * 用户订单查询页面跳转
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("userOrderPages")
	 public String  userOrderPages(HttpServletRequest request,HttpServletResponse response) {
	     return "usercenter/userQueryMessage";
	 }
	 
	
	 /**
	  * 查询信息
	  * @param request
	  * @param response
	  * @return
	  */
	 /**
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("queryMerchant")
	 public String  queryMerchant(HttpServletRequest request,HttpServletResponse response) {
		
		  //当前第几页
		  String page=request.getParameter("page");
		  //每页显示的记录数
		  String rows=request.getParameter("rows"); 
		  //当前页  
		        int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		        //每页显示条数  
		        int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		        //每页的开始记录  第一页为1  第二页为number +1   
		        int startRow = (currentPage-1)*pageSize;
		 log.info("-----------------------login start----------------");
		 int channelId=0;
		 String merchantOrderDate=request.getParameter("merchantOrderDate");//下单日期
		 String orderId=request.getParameter("orderId");//订单号
		 String merchantOrderId=request.getParameter("merchantOrderId");//商户订单号
		 String payOrderId=request.getParameter("payOrderId");//第三方订单号
		 String CI=request.getParameter("channelId"); //支付方式
		 if(CI!=null&&!CI.equals("")){
			 channelId=Integer.parseInt(CI);
		 }
		 String appId=request.getParameter("appId");//业务类型  字段暂时不确定
		 String source=request.getParameter("source"); //缴费来源 1、pc端2、移动端
		 int paymentId=0;
		 String pt=request.getParameter("paymentId"); //发卡行 字段暂时不确定
		 if(pt!=null&&!pt.equals("")){
			 paymentId=Integer.parseInt(pt);
		 }
		 String PS=request.getParameter("payStatus"); //交易状态
		 int payStatus=0;
		 if(PS!=null&&!PS.equals("")){
			 payStatus=Integer.parseInt(PS);
		 }
		 String sourceType=request.getParameter("sourceType"); 
		 
		 String startDate=request.getParameter("startDate"); //交易时间开始时间
		 String endDate=request.getParameter("endDate"); //交易时间结束时间
		 String startDate1 = null;
		 String endDate1 = null;
		 if(!startDate.equals("")&&!endDate.equals("")){
			 startDate1 = startDate+" 00:00:00";
			 endDate1 = endDate+" 23:59:59";
		 }
		
		 MerchantOrderInfo merchantOrderInfo =new MerchantOrderInfo();
		 merchantOrderInfo.setMerchantOrderDate(merchantOrderDate);
		 merchantOrderInfo.setStartDate(startDate1);
		 merchantOrderInfo.setEndDate(endDate1);
		 merchantOrderInfo.setMerchantOrderId(merchantOrderId);
		 merchantOrderInfo.setId(orderId);//订单号
		 merchantOrderInfo.setPayOrderId(payOrderId);   //第三方订单号
		 merchantOrderInfo.setChannelId(channelId); 	//支付方式
		 merchantOrderInfo.setPayStatus(payStatus);		//交易状态
		 merchantOrderInfo.setPaymentId(paymentId);		//发卡行
		 if(sourceType!=""){
			 merchantOrderInfo.setSourceType(Integer.parseInt(sourceType));	//支付渠道
		 }
		 if(appId!=""){
			 merchantOrderInfo.setMerchantId(Integer.parseInt(appId));	//业务类型
		 }
		 merchantOrderInfo.setPageSize(pageSize); //结束条数
		 merchantOrderInfo.setStartRow(startRow); //开始条数
		 
		 List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoService.findQueryMerchant(merchantOrderInfo);
		 int queryCount = merchantOrderInfoService.findQueryCount(merchantOrderInfo);
		 
		 List<PayChannelSwitch> listPayChannelSwitch = payChannelSwitchService.findPayChannelTypeAll();
		 Map map = new HashMap();
//		 Map<String,Object> map= null;
		 for(int i=0;i<listPayChannelSwitch.size();i++){
			 PayChannelSwitch payChannelSwitch = listPayChannelSwitch.get(i);
			 map.put(payChannelSwitch.getChannelValue(), payChannelSwitch.getChannelName());
			} 
		 Map map1 = new HashMap();
		 List<PayChannelDictionary> listPayChannelDictionary = payChannelDictionaryService.findPayChannelCodeAll();
		 for(int i=0;i<listPayChannelDictionary.size();i++){
			 PayChannelDictionary payChannelDictionary = listPayChannelDictionary.get(i);
			 map1.put(payChannelDictionary.getChannelID(), payChannelDictionary.getChannelName());
		 }
		 Map map2 = new HashMap();
		 List<MerchantInfo> listMerchantInfo = merchantInfoService.findMerchantNamesAll();
		 for(int i=0;i<listMerchantInfo.size();i++){
			 MerchantInfo merchantInfo = listMerchantInfo.get(i);
			 map2.put(merchantInfo.getId(), merchantInfo.getMerchantName());
		 }
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 for(int i=0;i<merchantOrderInfoList.size();i++){
			 MerchantOrderInfo merchantOrderInfo1 = merchantOrderInfoList.get(i);
			 String soureceType = merchantOrderInfo1.getSourceType()+"";
			 if(!soureceType.equals("null")){
				 String soureceTypeName = map.get(soureceType).toString();
				 merchantOrderInfo1.setSourceTypeName(soureceTypeName); 
			 }
			 
			 Date createDate1 = merchantOrderInfo1.getCreateDate();
			 merchantOrderInfo1.setFoundDate(df.format(createDate1));//交易时间
			 Date dealDate1 = merchantOrderInfo1.getDealDate();
			 if(dealDate1!=null){
				 merchantOrderInfo1.setBusinessDate(df.format(dealDate1));
			 }
			 String channeId = merchantOrderInfo1.getChannelId()+"";
			 if(!channeId.equals("null")){
				 String channelName = map1.get(channeId).toString();
				 merchantOrderInfo1.setChannelName(channelName);
			 }
			 Integer pMid = merchantOrderInfo1.getPaymentId();
			 String paymentName = payChange(pMid);
			 merchantOrderInfo1.setPaymentName(paymentName);
			 Integer appValue = merchantOrderInfo1.getMerchantId();
			 if(!appValue.equals("null")){
				 String appId1 = map2.get(appValue).toString();
				 merchantOrderInfo1.setAppId(appId1);
			 }
			 Integer status = merchantOrderInfo1.getPayStatus();
			 if(status!=null){
				 if(status==0){
					 merchantOrderInfo1.setPayStatusName("处理中"); 
				 }else if(status==1){
					 merchantOrderInfo1.setPayStatusName("成功"); 
				 }else if(status==2){
					 merchantOrderInfo1.setPayStatusName("失败"); 
				 }
			 }
		 }
		 JSONArray jsonArr = JSONArray.fromObject(merchantOrderInfoList);
		 JSONObject jsonObjArr = new JSONObject();  
		 jsonObjArr.put("total", queryCount);
		 jsonObjArr.put("rows", jsonArr);
		 System.out.println(jsonArr);
	     WebUtils.writeJson(response,jsonObjArr);
	     
	     return "usercenter/merchantMessage";
	 }	
	
	 
	 /**
	  * 查询信息
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("userQueryMessage")
	 public String  userQueryMessage(HttpServletRequest request,HttpServletResponse response) {
		
		  //当前第几页
		  String page=request.getParameter("page");
		  //每页显示的记录数
		  String rows=request.getParameter("rows"); 
		  //当前页  
		        int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		        //每页显示条数  
		        int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		        //每页的开始记录  第一页为1  第二页为number +1   
		        int startRow = (currentPage-1)*pageSize;
		 log.info("-----------------------login start----------------");
		 int channelId=0;
		 String merchantOrderDate=request.getParameter("merchantOrderDate");//下单日期
		 String orderId=request.getParameter("orderId");//订单号
		 String merchantOrderId=request.getParameter("merchantOrderId");//商户订单号
		 String payOrderId=request.getParameter("payOrderId");//第三方订单号
		 String userName=request.getParameter("userName");//第三方订单号
		 String CI=request.getParameter("channelId"); //支付方式
		 if(CI!=null&&!CI.equals("")){
			 channelId=Integer.parseInt(CI);
		 }
		 String appId=request.getParameter("appId");//业务类型  字段暂时不确定

		 String startDate=request.getParameter("startDate"); //交易时间开始时间
		 String endDate=request.getParameter("endDate"); //交易时间结束时间
		 String startDate1 = null;
		 String endDate1 = null;
		 if(!startDate.equals("")&&!endDate.equals("")){
			 startDate1 = startDate+" 00:00:00";
			 endDate1 = endDate+" 23:59:59";
		 }
		
		 MerchantOrderInfo merchantOrderInfo =new MerchantOrderInfo();
		 merchantOrderInfo.setMerchantOrderDate(merchantOrderDate);
		 merchantOrderInfo.setStartDate(startDate1);
		 merchantOrderInfo.setEndDate(endDate1);
		 merchantOrderInfo.setMerchantOrderId(merchantOrderId);
		 merchantOrderInfo.setId(orderId);//订单号
		 merchantOrderInfo.setPayOrderId(payOrderId);   //第三方订单号
		 merchantOrderInfo.setChannelId(channelId); 	//支付方式
//		 merchantOrderInfo.setPayStatus(payStatus);		//交易状态
		 merchantOrderInfo.setUserName(userName);		//发卡行
//		 merchantOrderInfo.setAppId(appId);				//业务类型
		 if(appId!=""){
			 merchantOrderInfo.setMerchantId(Integer.parseInt(appId));	//业务类型
		 }
		 merchantOrderInfo.setPageSize(pageSize); //结束条数
		 merchantOrderInfo.setStartRow(startRow); //开始条数
		 
		 List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoService.findQueryMerchant(merchantOrderInfo);
		 int queryCount = merchantOrderInfoService.findQueryCount(merchantOrderInfo);
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 for(int i=0;i<merchantOrderInfoList.size();i++){
			 MerchantOrderInfo merchantOrderInfo1 = merchantOrderInfoList.get(i);
			 Date createDate1 = merchantOrderInfo1.getCreateDate();
			 merchantOrderInfo1.setFoundDate(df.format(createDate1));//交易时间
			 Date dealDate1 = merchantOrderInfo1.getDealDate();
			 if(dealDate1!=null){
				 merchantOrderInfo1.setBusinessDate(df.format(dealDate1));
			 }
			 Integer channeId = merchantOrderInfo1.getChannelId();
			 if(channeId!=null){
				 PayChannelDictionary payChannelDictionary = payChannelDictionaryService.findNameById(channeId+"");
				 if(payChannelDictionary.getChannelName()!=null){
					 merchantOrderInfo1.setChannelName(payChannelDictionary.getChannelName()); 
				 }
			 }
			 Integer pMid = merchantOrderInfo1.getPaymentId();
			 String paymentName = payChange(pMid);
			 merchantOrderInfo1.setPaymentName(paymentName);
			 int appValue = merchantOrderInfo1.getMerchantId();
			 String appName="";
			 if(appValue!=0){
				 MerchantInfo merchantInfo = merchantInfoService.findNameById(appValue);
				 if(merchantInfo!=null){
					 appName = merchantInfo.getMerchantName();
				 }
			 }
			 merchantOrderInfo1.setAppId(appName); 
			 Integer status = merchantOrderInfo1.getPayStatus();
			 if(status!=null){
				 if(status==0){
					 merchantOrderInfo1.setPayStatusName("处理中"); 
				 }else if(status==1){
					 merchantOrderInfo1.setPayStatusName("成功"); 
				 }else if(status==2){
					 merchantOrderInfo1.setPayStatusName("失败"); 
				 }
			 }
		 }
		 JSONArray jsonArr = JSONArray.fromObject(merchantOrderInfoList);
		 JSONObject jsonObjArr = new JSONObject();  
		 jsonObjArr.put("total", queryCount);
		 jsonObjArr.put("rows", jsonArr);
		 System.out.println(jsonArr);
	     WebUtils.writeJson(response,jsonObjArr);
	    
	     return "usercenter/userQueryMessage";
	 }	
	 
	 /**
	  * 交易明细下载
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("downloadSubmit")
	 public void  downloadSubmit(HttpServletRequest request,HttpServletResponse response) {
		
		  
		//当前第几页
		  String page=request.getParameter("page");
		  //每页显示的记录数
		  String rows=request.getParameter("rows"); 
		  //当前页  
		        int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		        //每页显示条数  
		        int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		        //每页的开始记录  第一页为1  第二页为number +1   
		        int startRow = (currentPage-1)*pageSize;
		 log.info("-----------------------login start----------------");
		 int channelId=0;
		 String merchantOrderDate=request.getParameter("merchantOrderDate");//下单日期
		 String orderId=request.getParameter("orderId");//订单号
		 String merchantOrderId=request.getParameter("merchantOrderId");//商户订单号
		 String payOrderId=request.getParameter("payOrderId");//第三方订单号
		 String CI=request.getParameter("channelId"); //支付方式
		 if(CI!=null&&!CI.equals("")){
			 channelId=Integer.parseInt(CI);
		 }
		 String appId=request.getParameter("appId");//业务类型  字段暂时不确定
		 String source=request.getParameter("source"); //缴费来源 1、pc端2、移动端
		 int paymentId=0;
		 String pt=request.getParameter("paymentId"); //发卡行 字段暂时不确定
		 if(pt!=null&&!pt.equals("")){
			 paymentId=Integer.parseInt(pt);
		 }
		 String PS=request.getParameter("payStatus"); //交易状态
		 int payStatus=0;
		 if(PS!=null&&!PS.equals("")){
			 payStatus=Integer.parseInt(PS);
		 }
		 String startDate=request.getParameter("startDate"); //交易时间开始时间
		 String endDate=request.getParameter("endDate"); //交易时间结束时间
		 String startDate1 = null;
		 String endDate1 = null;
		 if(!startDate.equals("")&&!endDate.equals("")){
			 startDate1 = startDate+" 00:00:00";
			 endDate1 = endDate+" 23:59:59";
		 }
		
		 MerchantOrderInfo merchantOrderInfo =new MerchantOrderInfo();
		 merchantOrderInfo.setMerchantOrderDate(merchantOrderDate);
		 merchantOrderInfo.setStartDate(startDate1);
		 merchantOrderInfo.setEndDate(endDate1);
		 merchantOrderInfo.setMerchantOrderId(merchantOrderId);
		 merchantOrderInfo.setId(orderId);//订单号
		 merchantOrderInfo.setPayOrderId(payOrderId);   //第三方订单号
		 merchantOrderInfo.setChannelId(channelId); 	//支付方式
		 merchantOrderInfo.setPayStatus(payStatus);		//交易状态
		 merchantOrderInfo.setPaymentId(paymentId);		//发卡行
		 merchantOrderInfo.setAppId(appId);				//业务类型
		 
		 List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoService.findDownloadMerchant(merchantOrderInfo);
		 List<PayChannelSwitch> listPayChannelSwitch = payChannelSwitchService.findPayChannelTypeAll();
		 Map map = new HashMap();
//		 Map<String,Object> map= null;
		 for(int i=0;i<listPayChannelSwitch.size();i++){
			 PayChannelSwitch payChannelSwitch = listPayChannelSwitch.get(i);
			 map.put(payChannelSwitch.getChannelValue(), payChannelSwitch.getChannelName());
			} 
		 Map map1 = new HashMap();
		 List<PayChannelDictionary> listPayChannelDictionary = payChannelDictionaryService.findPayChannelCodeAll();
		 for(int i=0;i<listPayChannelDictionary.size();i++){
			 PayChannelDictionary payChannelDictionary = listPayChannelDictionary.get(i);
			 map1.put(payChannelDictionary.getChannelID(), payChannelDictionary.getChannelName());
		 }
		 Map map2 = new HashMap();
		 List<MerchantInfo> listMerchantInfo = merchantInfoService.findMerchantNamesAll();
		 for(int i=0;i<listMerchantInfo.size();i++){
			 MerchantInfo merchantInfo = listMerchantInfo.get(i);
			 map2.put(merchantInfo.getId(), merchantInfo.getMerchantName());
		 }
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 for(int i=0;i<merchantOrderInfoList.size();i++){
			 MerchantOrderInfo merchantOrderInfo1 = merchantOrderInfoList.get(i);
			 String soureceType = merchantOrderInfo1.getSourceType()+"";
			 if(!soureceType.equals("null")){
				 String soureceTypeName = map.get(soureceType).toString();
				 merchantOrderInfo1.setSourceTypeName(soureceTypeName); 
			 }
			 
			 Date createDate1 = merchantOrderInfo1.getCreateDate();
			 merchantOrderInfo1.setFoundDate(df.format(createDate1));//交易时间
			 Date dealDate1 = merchantOrderInfo1.getDealDate();
			 if(dealDate1!=null){
				 merchantOrderInfo1.setBusinessDate(df.format(dealDate1));
			 }
			 String channeId = merchantOrderInfo1.getChannelId()+"";
			 if(!channeId.equals("null")){
				 String channelName = map1.get(channeId).toString();
				 merchantOrderInfo1.setChannelName(channelName);
			 }
			 Integer pMid = merchantOrderInfo1.getPaymentId();
			 String paymentName = payChange(pMid);
			 merchantOrderInfo1.setPaymentName(paymentName);
			 Integer appValue = merchantOrderInfo1.getMerchantId();
			 if(!appValue.equals("null")){
				 String appId1 = map2.get(appValue).toString();
				 merchantOrderInfo1.setAppId(appId1);
			 }
			 Integer status = merchantOrderInfo1.getPayStatus();
			 if(status!=null){
				 if(status==0){
					 merchantOrderInfo1.setPayStatusName("处理中"); 
				 }else if(status==1){
					 merchantOrderInfo1.setPayStatusName("成功"); 
				 }else if(status==2){
					 merchantOrderInfo1.setPayStatusName("失败"); 
				 }
			 }
		 }
		 OrderDeriveExport.exportChuBei(response, merchantOrderInfoList);
//	     return "usercenter/merchantMessage";
	 }	
	 
	 
	 public String payChange(Integer pMid){
		 String paymentName="";
		 if(pMid!=null){
			 if(pMid==10012){
				 paymentName="支付宝-即时到账";
			 }else if(pMid==10013){
				 paymentName="微信-扫码支付";
			 }else if(pMid==10014){
				 paymentName="银联";
			 }else if(pMid==10001){
				 paymentName="招商银行";
			 }else if(pMid==10002){
				 paymentName="工商银行";
			 }else if(pMid==10003){
				 paymentName="建设银行";
			 }else if(pMid==10004){
				 paymentName="农业银行";
			 }else if(pMid==10005){
				 paymentName="中国银行";
			 }else if(pMid==10006){
				 paymentName="交通银行";
			 }else if(pMid==10007){
				 paymentName="中国邮政银行";
			 }else if(pMid==10008){
				 paymentName="广发银行";
			 }else if(pMid==10009){
				 paymentName="浦发银行";
			 }else if(pMid==10010){
				 paymentName="中国光大银行";
			 }else if(pMid==10011){
				 paymentName="中国平安银行";
			 }
		 }
		 return paymentName;
	 }
	 
	 /**
	  * 页面跳转
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("downloadSubmit1")
	 public String  downloadSubmit1(HttpServletRequest request,HttpServletResponse response) {
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
		 if(dateDay!=null&&!dateDay.equals("-2")){
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
		 OrderDeriveExport.exportChuBei(response, merchantOrderInfoList);
	     return "usercenter/merchantMessage";
	 }
	 
	 /**
		 * 查询所有商户
		 * @return 返回到前端json数据
		 */
		@RequestMapping(value="findAllDepts")
		public void findAllDepts(HttpServletRequest request,HttpServletResponse response,Model model){
			log.info("-------------------------findAllDepts         start------------------------------------");
			List<MerchantInfo> list = merchantInfoService.findMerchantNamesAll();
			List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
			Map<String,Object> map= null;
			map = new HashMap<String,Object>();
			map.put("id", "");
			map.put("text", "全部");
			maps.add(map);
			String str=null;
			if(list != null){
				for(MerchantInfo d : list){
					map = new HashMap<String,Object>();
					map.put("id", d.getId());
					map.put("text", d.getMerchantName());
					maps.add(map);
				} 
				JSONArray jsonArr = JSONArray.fromObject(maps);
				str = jsonArr.toString();
				WebUtils.writeJson(response, str);
			}
			return ;
		}
		
		 /**
		 * 查询所有商户
		 * @return 返回到前端json数据
		 */
		@RequestMapping(value="findAllPayChannel")
		public void findAllPayChannel(HttpServletRequest request,HttpServletResponse response,Model model){
			log.info("-------------------------findAllDepts         start------------------------------------");
			List<PayChannelDictionary> list = payChannelDictionaryService.findPayChannelCodeAll();
			List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
			Map<String,Object> map= null;
			map = new HashMap<String,Object>();
			map.put("id", "");
			map.put("text", "全部");
			maps.add(map);
			String str=null;
			if(list != null){
				for(PayChannelDictionary d : list){
					map = new HashMap<String,Object>();
					map.put("id", d.getChannelID());
					map.put("text", d.getChannelName());
					maps.add(map);
				} 
				JSONArray jsonArr = JSONArray.fromObject(maps);
				str = jsonArr.toString();
				WebUtils.writeJson(response, str);
//				System.out.println(str);
			}
			return ;
		}
		
		/**
		 * 查询所有支付渠道
		 * @return 返回到前端json数据
		 */
		@RequestMapping(value="findSourceType")
		public void findSourceType(HttpServletRequest request,HttpServletResponse response,Model model){
			log.info("-------------------------findSourceType         start------------------------------------");
			List<PayChannelSwitch> list = payChannelSwitchService.findPayChannelTypeAll();
			List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
			Map<String,Object> map= null;
			map = new HashMap<String,Object>();
			map.put("id", "");
			map.put("text", "全部");
			maps.add(map);
			String str=null;
			if(list != null){
				for(PayChannelSwitch d : list){
					map = new HashMap<String,Object>();
					map.put("id", d.getChannelValue());
					map.put("text", d.getChannelName());
					maps.add(map);
				} 
				JSONArray jsonArr = JSONArray.fromObject(maps);
				str = jsonArr.toString();
				WebUtils.writeJson(response, str);
//				System.out.println(str);
			}
			return ;
		}
		
	 	
}