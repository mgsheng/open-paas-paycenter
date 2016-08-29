package cn.com.open.pay.platform.manager.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.model.UserSerialRecord;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderInfoService;
import cn.com.open.pay.platform.manager.order.service.UserSerialRecordService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
import cn.com.open.pay.platform.manager.tools.OrderDeriveExport;



/**
 *  账户流水记录查询
 */
@Controller
@RequestMapping("/running/")
public class AccountRunningManage extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(AccountRunningManage.class);
	
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 
	 @Autowired
	 private UserSerialRecordService userSerialRecordService;
	
	 
	 /**
	  * 账户流水记录查询页面跳转
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("accountRunningPages")
	 public String  accountRunningPages(HttpServletRequest request,HttpServletResponse response) {
	     return "usercenter/accountRunningMessage";
	 }

	 /**
	  * 流水账户查询
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("accountQueryMerchant")
	 public String  queryMerchant(HttpServletRequest request,HttpServletResponse response) {
		 
		 
		 String serialNo=request.getParameter("serialNo");//流水号
		 String userName=request.getParameter("userName");//用户名
		 int payType=0;
		 int appId=0;
		 String payTypeInt=request.getParameter("payType");//交易类型
		 String appIdInt=request.getParameter("appId");//业务类型 
		 if(payTypeInt!=null&&!payTypeInt.equals("")){
			 payType=Integer.parseInt(payTypeInt);
		 }
		 if(appIdInt!=null&&!appIdInt.equals("")){
			 appId=Integer.parseInt(appIdInt);
		 }
		 String startDate=request.getParameter("startDate");//开始时间
		 String endDate=request.getParameter("endDate");//结束时间
		
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
		
		 String startDate1 = null;
		 String endDate1 = null;
		 if(!startDate.equals("")&&!endDate.equals("")){
			 startDate1 = startDate+" 00:00:00";
			 endDate1 = endDate+" 23:59:59";
		 }
		
		 UserSerialRecord userSerialRecord =new UserSerialRecord();
		 userSerialRecord.setAppId(appId);
		 userSerialRecord.setPayType(payType);
		 userSerialRecord.setSerialNo(serialNo);
		 userSerialRecord.setUserName(userName);
		 userSerialRecord.setStartDate(startDate1);
		 userSerialRecord.setEndDate(endDate1);
		 userSerialRecord.setPageSize(pageSize); //结束条数
		 userSerialRecord.setStartRow(startRow); //开始条数
		 
		 List<UserSerialRecord> userSerialRecordList = userSerialRecordService.findQueryRunning(userSerialRecord);
		 int queryCount = userSerialRecordService.findRunningCount(userSerialRecord);
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		 for(int i=0;i<userSerialRecordList.size();i++){
			 UserSerialRecord userSerialRecord1 = userSerialRecordList.get(i);
			 Date createTime1 = userSerialRecord1.getCreateTime();
			 if(createTime1!=null){
				 userSerialRecord1.setCreateDate(df.format(createTime1));//交易时间
			 }
			 
			 int dealDate1 = userSerialRecord1.getPayType();
			 if(dealDate1==1){
				 userSerialRecord1.setPayTypeName("充值");
			 }else{
				 userSerialRecord1.setPayTypeName("消费");
			 }
			 int appIdValue = userSerialRecord1.getAppId();
			 if(appIdValue==1){
				 userSerialRecord1.setAppIdName("OES学历");
			 }else if(appIdValue==10026){
				 userSerialRecord1.setAppIdName("mooc2u");
			 }else{
				 userSerialRecord1.setAppIdName("未定");
			 }
		 }
		 JSONArray jsonArr = JSONArray.fromObject(userSerialRecordList);
		 JSONObject jsonObjArr = new JSONObject();  
		 jsonObjArr.put("total", queryCount);
		 jsonObjArr.put("rows", jsonArr);
		 System.out.println(jsonArr);
	     WebUtils.writeJson(response,jsonObjArr);
	     
	     return "usercenter/userQueryMessage";
	 }	
	
	
}