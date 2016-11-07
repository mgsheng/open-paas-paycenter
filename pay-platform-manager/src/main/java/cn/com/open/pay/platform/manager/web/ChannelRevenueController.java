package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.tools.SysUtil;
import cn.com.open.pay.platform.manager.department.model.DictTradePayment;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.department.service.DictTradePaymentService;
import cn.com.open.pay.platform.manager.department.service.MerchantInfoService;
import cn.com.open.pay.platform.manager.log.service.PrivilegeLogService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderOffline;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderInfoService;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderOfflineService;
import cn.com.open.pay.platform.manager.paychannel.model.ChannelRate;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelSwitch;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelDictionaryService;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelSwitchService;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.OrderDeriveExport;
import cn.com.open.pay.platform.manager.tools.WebUtils;
/**
 * 渠道营收管理
 * @author lvjq
 *
 */
@Controller
@RequestMapping("/paychannel/")
public class ChannelRevenueController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private MerchantOrderOfflineService merchantOrderOfflineService;
	@Autowired
	private DictTradePaymentService dictTradePaymentService;
	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private PayChannelDictionaryService payChannelDictionaryService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeLogService privilegeLogService;
	@Autowired
	private PayChannelSwitchService payChannelSwitchService;
	@Autowired
	private MerchantOrderInfoService merchantOrderInfoService;
	
	/**
	 * 跳转到渠道营收管理页面
	 * @return
	 */
	@RequestMapping(value="channelRevenue")
	public String channelRate(){
		log.info("---------------channelRevenuePages----------------");
		return "paychannel/channelRevenue";
	}
	
	/**
	 * 将从数据库查询的数据封装为json，返回到前端页面
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getChannelRevenue")
	public void getChannelRevenue(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("---------------getChannelRevenue----------------");
		String channelId = request.getParameter("channelId");
		String dimension = request.getParameter("dimension");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		System.out.println("channelId:"+channelId+"  dimension:"+dimension+"  startDate:"+startDate+"  endDate:"+endDate);
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
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		if(("day").equals(dimension)){//天
			if(startDate==null || ("").equals(startDate)){
				cal.add(Calendar.DATE,-1);
				startDate = sdf.format(cal.getTime());
			}
		}else if(("week").equals(dimension)){//自然周
			if(startDate==null || ("").equals(startDate)){
				cal.add(Calendar.DATE,0);
				startDate = sdf.format(cal.getTime());
			}
		    try {
				cal.setTime(sdf.parse(startDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}  
		    // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
		    int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
		    if (1 == dayWeek) {  
		       cal.add(Calendar.DAY_OF_MONTH, -1);  
		    }  
		    cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一   
		    int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天 
		    cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
	     	startDate=sdf.format(cal.getTime());
		    cal.add(Calendar.DATE, 6);  
		    endDate=sdf.format(cal.getTime());
		    System.out.println("该日期所在自然周开始结束日期------StartDate:"+startDate+"  endDate:"+endDate);
		}else if(("month").equals(dimension)){//月
			if(startDate==null || ("").equals(startDate)){
				cal.add(Calendar.DATE,0);
				startDate = sdf.format(cal.getTime());
			}
		    try {
				cal.setTime(sdf.parse(startDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}  
			//获取当前月第一天：    
	        cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	        startDate = sdf.format(cal.getTime());
	        //获取当前月最后一天
	        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
	        endDate = sdf.format(cal.getTime());
	        System.out.println("该日期所在月开始结束日期-------StartDate:"+startDate+"  endDate:"+endDate);
		}else if(("year").equals(dimension)){//年
			if(startDate==null || ("").equals(startDate)){
				cal.add(Calendar.DATE,0);
				startDate = sdf.format(cal.getTime());
			}
			String year=startDate.split("-")[0];
	        startDate=year+"-01-01";
	        endDate=year+"-12-31";
	        System.out.println("该日期所在年份开始结束日期--------StartDate:"+startDate+"  endDate:"+endDate);
		}else{
			System.out.println("自定义查询区间日期--------StartDate:"+startDate+"  endDate:"+endDate);
		}
		
		MerchantOrderInfo orderInfo=new MerchantOrderInfo();
	    orderInfo.setPageSize(pageSize);
	    orderInfo.setStartRow(startRow);
	    
		if(channelId!=null && channelId.length()!=0){
			//orderInfo.setChannelId(Integer.parseInt(channelId));
			orderInfo.setSourceType(Integer.parseInt(channelId));
		}
	    orderInfo.setDimension(dimension);
	    orderInfo.setStartDate(startDate);
	    orderInfo.setEndDate(endDate);
	    List<MerchantOrderInfo> channelRevenues = merchantOrderInfoService.findChannelRevenue(orderInfo);
	    JSONObject json =  new JSONObject();
    	int total = merchantOrderInfoService.findChannelRevenueCount(orderInfo);
	    json.put("total", total);
	    List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
	    PayChannelSwitch channel=null;
	    if(channelRevenues != null){
	    	Map<String,Object> map = null;
	    	for(MerchantOrderInfo r : channelRevenues){
	    		map = new LinkedHashMap<String,Object>(); 
	    		if(r.getSourceType()!=null){
		    		channel=payChannelSwitchService.findNameById(r.getSourceType()+"");
					if(channel!=null){
		    			map.put("channelName", channel.getChannelName());
		    		}
	    		}
	    		map.put("countOrderAmount", r.getCountOrderAmount()); 
	    		map.put("countPayAmount", r.getCountPayAmount());
	    		map.put("payCharge", r.getPayCharge());
	    		if(("day").equals(dimension)){
	    			map.put("dismension", "天");
		    		map.put("foundDate", startDate);
	    		}else if(("week").equals(dimension)){
	    			map.put("dismension", "自然周");
		    		map.put("foundDate", startDate+"~"+endDate);
	    		}else if(("month").equals(dimension)){
	    			map.put("dismension", "月");
		    		map.put("foundDate", startDate+"~"+endDate);
	    		}else if(("year").equals(dimension)){
	    			map.put("dismension", "年");
		    		map.put("foundDate", startDate+"~"+endDate);
	    		}else{
	    			map.put("dismension", "自定义");
	    			if(startDate==null || ("").equals(startDate)){
	    				map.put("foundDate", endDate+"之前");
	    			}else if(endDate==null || ("").equals(endDate)){
	    				map.put("foundDate", startDate+"~至今");
	    			}else{
	    				map.put("foundDate", startDate+"~"+endDate);
	    			}
	    		}
	    		maps.add(map);
	    	}
	    	JSONArray jsonArr = JSONArray.fromObject(maps);
	    	json.put("rows", jsonArr);
	    	WebUtils.writeJson(response, json);
	    }
		return;
	}
	
	/**
	 * 下载为excel
	 * @param request
	 * @param response
	 * @return 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("revenueDownloadSubmit")
	public void revenueDownloadSubmit(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("---------------getChannelRevenueDownloadSubmit----------------");
		String channelId = request.getParameter("channelId");
		String dimension = request.getParameter("dimension");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		System.out.println("channelId:"+channelId+"  dimension:"+dimension+"  startDate:"+startDate+"  endDate:"+endDate);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		if(("day").equals(dimension)){//天
			if(startDate==null || ("").equals(startDate)){
				cal.add(Calendar.DATE,-1);
				startDate = sdf.format(cal.getTime());
			}
		}else if(("week").equals(dimension)){//自然周
			if(startDate==null || ("").equals(startDate)){
				cal.add(Calendar.DATE,0);
				startDate = sdf.format(cal.getTime());
			}
		    try {
				cal.setTime(sdf.parse(startDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}  
		    // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
		    int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
		    if (1 == dayWeek) {  
		       cal.add(Calendar.DAY_OF_MONTH, -1);  
		    }  
		    cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一   
		    int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天 
		    cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
	     	startDate=sdf.format(cal.getTime());
		    cal.add(Calendar.DATE, 6);  
		    endDate=sdf.format(cal.getTime());
		    System.out.println("该日期所在自然周开始结束日期------StartDate:"+startDate+"  endDate:"+endDate);
		}else if(("month").equals(dimension)){//月
			if(startDate==null || ("").equals(startDate)){
				cal.add(Calendar.DATE,0);
				startDate = sdf.format(cal.getTime());
			}
		    try {
				cal.setTime(sdf.parse(startDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}  
			//获取当前月第一天：    
	        cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	        startDate = sdf.format(cal.getTime());
	        //获取当前月最后一天
	        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
	        endDate = sdf.format(cal.getTime());
	        System.out.println("该日期所在月开始结束日期-------StartDate:"+startDate+"  endDate:"+endDate);
		}else if(("year").equals(dimension)){//年
			if(startDate==null || ("").equals(startDate)){
				cal.add(Calendar.DATE,0);
				startDate = sdf.format(cal.getTime());
			}
			String year=startDate.split("-")[0];
	        startDate=year+"-01-01";
	        endDate=year+"-12-31";
	        System.out.println("该日期所在年份开始结束日期--------StartDate:"+startDate+"  endDate:"+endDate);
		}else{
			System.out.println("自定义查询区间日期--------StartDate:"+startDate+"  endDate:"+endDate);
		}
		
		MerchantOrderInfo orderInfo=new MerchantOrderInfo();
	    
		if(channelId!=null && channelId.length()!=0){
			//orderInfo.setChannelId(Integer.parseInt(channelId));
			orderInfo.setSourceType(Integer.parseInt(channelId));
		}
	    orderInfo.setDimension(dimension);
	    orderInfo.setStartDate(startDate);
	    orderInfo.setEndDate(endDate);
	    List<MerchantOrderInfo> channelRevenues = merchantOrderInfoService.findChannelRevenueNoPage(orderInfo);
	    
	    PayChannelSwitch channel=null;
    	for(MerchantOrderInfo r : channelRevenues){
    		if(r.getSourceType()!=null){
	    		channel=payChannelSwitchService.findNameById(r.getSourceType()+"");
				if(channel!=null){
	    			r.setChannelName(channel.getChannelName());
	    		}
    		}
    		if(("day").equals(dimension)){
    			r.setDimension("天");
    			r.setFoundDate(startDate);
    		}else if(("week").equals(dimension)){
    			r.setDimension("自然周");
	    		r.setFoundDate(startDate+"~"+endDate);
    		}else if(("month").equals(dimension)){
    			r.setDimension("月");
	    		r.setFoundDate(startDate+"~"+endDate);
    		}else if(("year").equals(dimension)){
    			r.setDimension("年");
	    		r.setFoundDate(startDate+"~"+endDate);
    		}else{
    			r.setDimension("自定义");
    			if(startDate==null || ("").equals(startDate)){
    				r.setFoundDate(endDate+"之前");
    			}else if(endDate==null || ("").equals(endDate)){
    				r.setFoundDate(startDate+"~至今");
    			}else{
    				r.setFoundDate(startDate+"~"+endDate);
    			}
    		}
    	}
    	OrderDeriveExport.exportChannelRevenus(response, channelRevenues);
	}   
}
