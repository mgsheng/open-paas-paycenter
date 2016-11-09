package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderInfoService;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelSwitch;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelSwitchService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.OrderDeriveExport;
import cn.com.open.pay.platform.manager.tools.ReportDateTools;
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
	 * @throws ParseException 
	 */
	@RequestMapping("getChannelRevenue")
	public void getChannelRevenue(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException, ParseException{
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
	    List<MerchantOrderInfo> channelRevenues=new ArrayList<MerchantOrderInfo>();
	    int total=0;
	    JSONObject json =  new JSONObject();
	    List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
	    PayChannelSwitch channel=null;
	    MerchantOrderInfo orderInfo=new MerchantOrderInfo();
	    orderInfo.setPageSize(pageSize);
	    orderInfo.setStartRow(startRow);
	    
		if(channelId!=null && channelId.length()!=0){
			orderInfo.setSourceType(Integer.parseInt(channelId));
		}
	    orderInfo.setDimension(dimension);
	    
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		List<String> timeDuring = new ArrayList<String>();
		
		if((startDate==null || ("").equals(startDate)) && (endDate==null || ("").equals(endDate))){
			cal.add(Calendar.DATE,-1);
			startDate = sdf.format(cal.getTime());
			endDate = sdf.format(cal.getTime());
		}
		String dimensionName="";
		if(("day").equals(dimension) || ("custom").equals(dimension)){//天  自定义
			if(("day").equals(dimension)){
				dimensionName = "天";
			}else if(("custom").equals(dimension)){
				dimensionName = "自定义";
			}
			orderInfo.setStartDate(startDate);
		    orderInfo.setEndDate(endDate);
		    channelRevenues = merchantOrderInfoService.findChannelRevenue(orderInfo);
		    total = merchantOrderInfoService.findChannelRevenueCount(orderInfo);
		    json.put("total", total);
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
		    		map.put("countPayCharge", r.getCountPayCharge());
		    		map.put("dismension", dimensionName);
		    		if(("day").equals(dimension)){
		    			map.put("foundDate", sdf.format(r.getCreateDate()));
					}else if(("custom").equals(dimension)){
						if((startDate==null || ("").equals(startDate)) && (endDate!=null && !("").equals(endDate))){
							map.put("foundDate", "之前~"+endDate);
						}else if((startDate!=null && !("").equals(startDate)) && (endDate==null || ("").equals(endDate))){
							map.put("foundDate", startDate+"~至今");
						}
					}
		    		maps.add(map);
		    	}
		    }
		}else if(("week").equals(dimension) || ("month").equals(dimension) || ("year").equals(dimension)){//自然周   月   年
			if((startDate==null || ("").equals(startDate)) && (endDate!=null || !("").equals(endDate))){
				startDate = endDate;
			}else if((startDate!=null || !("").equals(startDate)) && (endDate==null || ("").equals(endDate))){
				endDate = startDate;
			}
			if(("week").equals(dimension)){
				timeDuring = ReportDateTools.getWeek(startDate, endDate);
				dimensionName = "自然周";
			}else if(("month").equals(dimension)){
				timeDuring = ReportDateTools.getMonth(startDate,endDate);
				dimensionName = "月";
			}else{
				timeDuring = ReportDateTools.getYear(startDate,endDate);
				dimensionName = "年";
			}
			for(String during : timeDuring){
				startDate=during.split("~")[0];
				endDate=during.split("~")[1];
				orderInfo.setStartDate(startDate);
			    orderInfo.setEndDate(endDate);
			    channelRevenues = merchantOrderInfoService.findChannelRevenue(orderInfo);
			    total = total + merchantOrderInfoService.findChannelRevenueCount(orderInfo);
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
			    		map.put("countPayCharge", r.getCountPayCharge());			    		
		    			map.put("dismension", dimensionName);
			    		map.put("foundDate", during);
			    		maps.add(map);
			    	}
			    }
			}
		    json.put("total", total);
		}
		
		JSONArray jsonArr = JSONArray.fromObject(maps);
    	json.put("rows", jsonArr);
    	WebUtils.writeJson(response, json);
		return;
	}
	
	/**
	 * 下载为excel
	 * @param request
	 * @param response
	 * @return 
	 * @throws UnsupportedEncodingException
	 * @throws ParseException 
	 */
	@RequestMapping("revenueDownloadSubmit")
	public void revenueDownloadSubmit(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException, ParseException{
		log.info("---------------getChannelRevenueDownloadSubmit----------------");
		String channelId = request.getParameter("channelId");
		String dimension = request.getParameter("dimension");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		System.out.println("channelId:"+channelId+"  dimension:"+dimension+"  startDate:"+startDate+"  endDate:"+endDate);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		List<MerchantOrderInfo> channelRevenues=new ArrayList<MerchantOrderInfo>();
	    PayChannelSwitch channel=null;
	    MerchantOrderInfo orderInfo=new MerchantOrderInfo();
	    
		if(channelId!=null && channelId.length()!=0){
			orderInfo.setSourceType(Integer.parseInt(channelId));
		}
	    orderInfo.setDimension(dimension);
		List<String> timeDuring = new ArrayList<String>();
		
		if((startDate==null || ("").equals(startDate)) && (endDate==null || ("").equals(endDate))){
			cal.add(Calendar.DATE,-1);
			startDate = sdf.format(cal.getTime());
			endDate = sdf.format(cal.getTime());
		}
		String dimensionName="";
		if(("day").equals(dimension) || ("custom").equals(dimension)){//天  自定义
			if(("day").equals(dimension)){
				dimensionName = "天";
			}else if(("custom").equals(dimension)){
				dimensionName = "自定义";
			}
			orderInfo.setStartDate(startDate);
		    orderInfo.setEndDate(endDate);
		    channelRevenues = merchantOrderInfoService.findChannelRevenueNoPage(orderInfo);
		    if(channelRevenues != null){
		    	for(MerchantOrderInfo r : channelRevenues){
		    		if(r.getSourceType()!=null){
			    		channel=payChannelSwitchService.findNameById(r.getSourceType()+"");
						if(channel!=null){
			    			r.setChannelName(channel.getChannelName());
			    		}
		    		}
		    		r.setDimension(dimensionName);
		    		if(("day").equals(dimension)){
		    			r.setFoundDate(sdf.format(r.getCreateDate()));
					}else if(("custom").equals(dimension)){
						if((startDate==null || ("").equals(startDate)) && (endDate!=null && !("").equals(endDate))){
							r.setFoundDate("之前~"+endDate);
						}else if((startDate!=null && !("").equals(startDate)) && (endDate==null || ("").equals(endDate))){
							r.setFoundDate(startDate+"~至今");
						}
					}
		    	}
		    }
		}else if(("week").equals(dimension) || ("month").equals(dimension) || ("year").equals(dimension)){//自然周   月   年
			if((startDate==null || ("").equals(startDate)) && (endDate!=null || !("").equals(endDate))){
				startDate = endDate;
			}else if((startDate!=null || !("").equals(startDate)) && (endDate==null || ("").equals(endDate))){
				endDate = startDate;
			}
			if(("week").equals(dimension)){
				timeDuring = ReportDateTools.getWeek(startDate,endDate);
				dimensionName = "自然周";
			}else if(("month").equals(dimension)){
				timeDuring = ReportDateTools.getMonth(startDate,endDate);
				dimensionName = "月";
			}else{
				timeDuring = ReportDateTools.getYear(startDate,endDate);
				dimensionName = "年";
			}
			for(String during : timeDuring){
				startDate=during.split("~")[0];
				endDate=during.split("~")[1];
				orderInfo.setStartDate(startDate);
			    orderInfo.setEndDate(endDate);
			    channelRevenues = merchantOrderInfoService.findChannelRevenueNoPage(orderInfo);
			    if(channelRevenues != null){
			    	for(MerchantOrderInfo r : channelRevenues){
			    		if(r.getSourceType()!=null){
				    		channel=payChannelSwitchService.findNameById(r.getSourceType()+"");
							if(channel!=null){
				    			r.setChannelName(channel.getChannelName());
				    		}
			    		}		
		    			r.setDimension(dimensionName);
			    		r.setFoundDate(during);
			    	}
			    }
			}
		}
    	OrderDeriveExport.exportChannelRevenus(response, channelRevenues);
	}   
}
