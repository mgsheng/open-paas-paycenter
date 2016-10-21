package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderInfoService;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.DateTools;
import cn.com.open.pay.platform.manager.tools.OrderCountExport;
import cn.com.open.pay.platform.manager.tools.OrderDeriveExport;
import cn.com.open.pay.platform.manager.tools.WebUtils;



/**
 *  用户交易数据统计
 */
@Controller
@RequestMapping("/user/")
public class UserDataStatsController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserDataStatsController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 /**
	     * 跳转统计页面
	     * @param request
	     * @param model
	     * @param bool
	     * @return
	     */
	    @RequestMapping(value = "stats")
		public String stats(HttpServletRequest request,HttpServletResponse response) {
	    	return "stats/index";
	    }
	    @RequestMapping(value = "datagrid_data1.json")
	    public void  getDate(){
	    	
	    }
		/**
		  * 
		  * @param request
		  * @param response
		  * @throws UnsupportedEncodingException
		  */
		@RequestMapping(value = "statistics/chart",method = RequestMethod.POST)
		public void chart(HttpServletRequest request,HttpServletResponse response,
				String paymentId,
				String channelId,
				String appId,
				String payClient,
				String startTime,String endTime,String timeType) throws UnsupportedEncodingException {
			//时间范围默认值
			if(!nullEmptyBlankJudge(timeType)){
				if(timeType.equals("1")){
					//三十天内
					startTime=DateTools.getStatetimeByMonth("yyyy-MM-dd");
					endTime=DateTools.getCurrDate("yyyy-MM-dd");
				}else{
					//七天内
					startTime=DateTools.getStatetime("yyyy-MM-dd");
					endTime=DateTools.getTmotime("yyyy-MM-dd");
				}
			}else if(nullEmptyBlankJudge(timeType)&&nullEmptyBlankJudge(startTime)&&nullEmptyBlankJudge(endTime)){
				startTime=DateTools.getStatetime("yyyy-MM-dd");
				endTime=DateTools.getCurrDate("yyyy-MM-dd");
				
			}
			//解析时间值
			StringBuffer dateString=new StringBuffer("");//曲线图x轴线日期
			Map<String, Integer> dateMap = new LinkedHashMap<String, Integer>();//为返回的数据数组赋值
			//按天
			int dcount=DateTools.daysOfTwo(DateTools.stringtoDate(startTime, "yyyy-MM-dd"), DateTools.stringtoDate(endTime, "yyyy-MM-dd"), Calendar.DAY_OF_YEAR);
			for(int i=0;i<=dcount;i++){
				if(dateString.toString().equals("")){
						dateString.append(DateTools.dateSub(DateTools.SUB_DAY, DateTools.stringtoDate(startTime, "yyyy-MM-dd"), i, "yyyy-MM-dd"));
					}else{
						dateString.append(",").append(DateTools.dateSub(DateTools.SUB_DAY, DateTools.stringtoDate(startTime, "yyyy-MM-dd"), i, "yyyy-MM-dd"));
					}
					dateMap.put(DateTools.dateSub(DateTools.SUB_DAY, DateTools.stringtoDate(startTime, "yyyy-MM-dd"), i, "yyyy-MM-dd"), i);
				}
			//查询成交金额
			endTime=DateTools.getTmotime("yyyy-MM-dd");
			List<Map<String, Object>> payAmountList=merchantOrderInfoService.getPayAmount(startTime, endTime, appId, paymentId, channelId);
			List<Map<String, Object>>getPayCountList=merchantOrderInfoService.getPayCount(startTime, endTime, appId, paymentId, channelId);
			List<Map<String, Object>>userCountList=merchantOrderInfoService.getUserCount(startTime, endTime, appId, paymentId, channelId);
			List<Map<String, Object>>payChargeList=merchantOrderInfoService.payCharge(startTime, endTime, appId, paymentId, channelId);
			//查询总的金额
			HashMap<String, Object> totalPayAmountMap=merchantOrderInfoService.getTotalPayAmount(startTime, endTime, appId, paymentId, channelId);
			HashMap<String, Object> totalPayCountMap=merchantOrderInfoService.getTotalPayCount(startTime, endTime, appId, paymentId, channelId);
			HashMap<String, Object> totalUserCountMap=merchantOrderInfoService.getTotalUserCount(startTime, endTime, appId, paymentId, channelId);
			HashMap<String, Object> totalPayChargeMap=merchantOrderInfoService.payTotalCharge(startTime, endTime, appId, paymentId, channelId);
			
			//生成返回数据
			List<Map<String,Object>> payAmountListMap = new ArrayList<Map<String,Object>>();
			Map<String,Object> payAmountMap = null;//成交金额Map
			List<Map<String,Object>> payCountListMap = new ArrayList<Map<String,Object>>();
			Map<String,Object> payCountMap = null;//成交笔数Map
			List<Map<String,Object>> userCountListMap = new ArrayList<Map<String,Object>>();
			Map<String,Object> userCountMap = null;//缴费人数Map
			List<Map<String,Object>> payChargeListMap = new ArrayList<Map<String,Object>>();
			Map<String,Object> payChargeMap = null;//手续费Map
			//获取每个app查询的条数，根据日期决定
			int appCount=dateString.toString().split(",").length;
			Double[] payAmountTotal = new Double[appCount];//成交金额数组
			int[] payCountTotal=new int[appCount];//成交笔数
			int[] userCountTotal=new int[appCount];//缴费人数
			Double[] payChargeTotal=new Double[appCount];//手续费
			//数组赋值
			for(int i=0;i<appCount;i++){
				payAmountTotal[i]=0.0;
				payCountTotal[i]=0;
				userCountTotal[i]=0;
				payChargeTotal[i]=0.0;
			}
			int usCount=0;//集合数量
			if(payAmountList!=null && appCount>0&&payAmountList.size()>0){
				usCount=payAmountList.size();//集合数量
				//循环所有的数据
				for(int i =0;i<payAmountList.size();i++){
						payAmountMap = new LinkedHashMap<String, Object>();
						payChargeMap = new LinkedHashMap<String, Object>();
						String createDate=payAmountList.get(i).get("createDate").toString();
						int index=dateMap.get(createDate);
						try {
							payAmountTotal[index]=Double.parseDouble(payAmountList.get(i).get("orderAmount").toString());
						} catch (Exception e) {
							e.printStackTrace();
						}	
						
					}
				payAmountMap.put("data",payAmountTotal);
				payAmountListMap.add(payAmountMap);
			}
			if(getPayCountList!=null && appCount>0&&getPayCountList.size()>0){
				usCount=payAmountList.size();//集合数量
				//循环所有的数据
				for(int i =0;i<payAmountList.size();i++){
					   payCountMap = new LinkedHashMap<String, Object>();
					   String createDate=getPayCountList.get(i).get("createDate").toString();
						int index=dateMap.get(createDate);
						try {
							payCountTotal[index]=Integer.parseInt(getPayCountList.get(i).get("payCount").toString());
						} catch (Exception e) {
							e.printStackTrace();
						}	
						
					}
				payCountMap.put("data",payCountTotal);
				payCountListMap.add(payCountMap);
			}if(userCountList!=null && appCount>0&&userCountList.size()>0){
				usCount=userCountList.size();//集合数量
				//循环所有的数据
				for(int i =0;i<userCountList.size();i++){
					   userCountMap = new LinkedHashMap<String, Object>();
						String createDate=userCountList.get(i).get("createDate").toString();
						int index=dateMap.get(createDate);
						try {
							userCountTotal[index]=Integer.parseInt(userCountList.get(i).get("userCount").toString());
						} catch (Exception e) {
							e.printStackTrace();
						}	
						
					}
				userCountMap.put("data",userCountTotal);
				userCountListMap.add(userCountMap);
			}
			if(payChargeList!=null && appCount>0&&payChargeList.size()>0){
				usCount=payChargeList.size();//集合数量
				//循环所有的数据
				for(int i =0;i<payChargeList.size();i++){
					payChargeMap = new LinkedHashMap<String, Object>();
					String createDate=payChargeList.get(i).get("createDate").toString();
					int index=dateMap.get(createDate);
						try {
							payChargeTotal[index]=Double.parseDouble(payChargeList.get(i).get("payCharge").toString());
						
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}
				payChargeMap.put("data",payChargeTotal);
				payChargeListMap.add(payChargeMap);
			}
			//返回
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("count", usCount);//查询数据数量
			map.put("payAmountListMap", payAmountListMap);
			map.put("payCountListMap", payCountListMap);
			map.put("userCountListMap", userCountListMap);
			map.put("payChargeListMap", payChargeListMap);
			map.put("timeData", dateString.toString().split(","));
			//总的值
			String totalPayAmount="0.0";
			String totalPayCount="0";
			String totalUserCount="0";
			String totalPayCharge="0.0";
			if(totalPayAmountMap!=null&&!nullEmptyBlankJudge(totalPayAmountMap.get("orderAmount").toString())){
				totalPayAmount=totalPayAmountMap.get("orderAmount").toString();	
			}
			if(totalPayCountMap!=null&&!nullEmptyBlankJudge(totalPayCountMap.get("payCount").toString())){
				totalPayCount=totalPayCountMap.get("payCount").toString();	
			}
			if(totalUserCountMap!=null&&!nullEmptyBlankJudge(totalUserCountMap.get("userCount").toString())){
				totalUserCount=totalUserCountMap.get("userCount").toString();	
			}
			if(totalPayChargeMap!=null&&!nullEmptyBlankJudge(totalPayChargeMap.get("payCharge").toString())){
				totalPayCharge=totalPayChargeMap.get("payCharge").toString();	
			}
			map.put("totalPayAmount",totalPayAmount);
			map.put("totalPayCount",totalPayCount);
			map.put("totalUserCount",totalUserCount);
			map.put("totalPayCharge",totalPayCharge);
			WebUtils.writeSuccessJson(response, map);
			//JsonUtil.writeJson(response,JsonUtil.getContentData(map));
		}
		
		 /**
		  * 查询统计信息
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
			 String CI=request.getParameter("channelId");//支付方式是
			 String payClient=request.getParameter("payClient");//缴费来源
			 String pt=request.getParameter("paymentId");//缴费银行
			 String appId=request.getParameter("appId");//业务来源
			 String startDate=request.getParameter("startDate"); //支付方式
			 String endDate=request.getParameter("endDate"); //支付方式
			 if(CI!=null&&!CI.equals("")){
				 channelId=Integer.parseInt(CI);
			 }
			 int paymentId=0;
			 if(pt!=null&&!pt.equals("")){
				 paymentId=Integer.parseInt(pt);
			 }
			 String startDate1 = null;
			 String endDate1 = null;
			 if(!startDate.equals("")&&!endDate.equals("")){
				 startDate1 = startDate+" 00:00:00";
				 endDate1 = endDate+" 23:59:59";
			 }
			
			 MerchantOrderInfo merchantOrderInfo =new MerchantOrderInfo();
			 merchantOrderInfo.setStartDate(startDate1);
			 merchantOrderInfo.setEndDate(endDate1);
			 merchantOrderInfo.setChannelId(channelId); 	//支付方式
			 merchantOrderInfo.setPaymentId(paymentId);		//发卡行
//			 merchantOrderInfo.setPayCharge(payClient);
			 if(appId!=""){
				 merchantOrderInfo.setMerchantId(Integer.parseInt(appId));	//业务类型
			 }
			 merchantOrderInfo.setPageSize(pageSize); //结束条数
			 merchantOrderInfo.setStartRow(startRow); //开始条数
			 
			 List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoService.getCensusAll(merchantOrderInfo);
			 int queryCount = merchantOrderInfoService.getAllCount(merchantOrderInfo);
			 DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
			 for(int i=0;i<merchantOrderInfoList.size();i++){
				 MerchantOrderInfo merchantOrderInfo1 = merchantOrderInfoList.get(i);
				Date createDate = merchantOrderInfo1.getCreateDate();
				merchantOrderInfo1.setFoundDate(df.format(createDate));//交易时间
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
		  * 导出
		  * @param request
		  * @param response
		  * @return
		  */
		 @RequestMapping("exportSubmit")
		 public String  exportSubmit(HttpServletRequest request,HttpServletResponse response) {
			
			  
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
			 String CI=request.getParameter("channelId");//支付方式是
			 String payClient=request.getParameter("payClient");//缴费来源
			 String pt=request.getParameter("paymentId");//缴费银行
			 String appId=request.getParameter("appId");//业务来源
			 String startDate=request.getParameter("startDate"); //支付方式
			 String endDate=request.getParameter("endDate"); //支付方式
			 if(CI!=null&&!CI.equals("")){
				 channelId=Integer.parseInt(CI);
			 }
			 int paymentId=0;
			 if(pt!=null&&!pt.equals("")){
				 paymentId=Integer.parseInt(pt);
			 }
			 String startDate1 = null;
			 String endDate1 = null;
			 if(!startDate.equals("")&&!endDate.equals("")){
				 startDate1 = startDate+" 00:00:00";
				 endDate1 = endDate+" 23:59:59";
			 }
			
			 MerchantOrderInfo merchantOrderInfo =new MerchantOrderInfo();
			 merchantOrderInfo.setStartDate(startDate1);
			 merchantOrderInfo.setEndDate(endDate1);
			 merchantOrderInfo.setChannelId(channelId); 	//支付方式
			 merchantOrderInfo.setPaymentId(paymentId);		//发卡行
			 if(appId!=""){
				 merchantOrderInfo.setMerchantId(Integer.parseInt(appId));	//业务类型
			 }
			 merchantOrderInfo.setPageSize(pageSize); //结束条数
			 merchantOrderInfo.setStartRow(startRow); //开始条数
			 
			 List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoService.getCensusAllExport(merchantOrderInfo);
			 DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
			 for(int i=0;i<merchantOrderInfoList.size();i++){
				 MerchantOrderInfo merchantOrderInfo1 = merchantOrderInfoList.get(i);
				Date createDate = merchantOrderInfo1.getCreateDate();
				merchantOrderInfo1.setFoundDate(df.format(createDate));//交易时间
			 }
			  
			 
			 OrderCountExport.exportChuBei(response, merchantOrderInfoList);
		     return "stats/index";
		 }
}