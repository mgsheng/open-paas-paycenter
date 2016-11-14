package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import cn.com.open.pay.platform.manager.department.model.DictTradePayment;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.department.service.DictTradePaymentService;
import cn.com.open.pay.platform.manager.department.service.MerchantInfoService;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderInfoService;
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
@RequestMapping("/payment/")
public class BankPaymentController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private DictTradePaymentService dictTradePaymentService;
	@Autowired
	private MerchantOrderInfoService merchantOrderInfoService;
	@Autowired
	private MerchantInfoService merchantInfoService;
	
	/**
	 * 跳转到银行缴费管理页面
	 * @return
	 */
	@RequestMapping(value="bankPayment")
	public String channelRate(){
		log.info("---------------bankPaymentPages----------------");
		return "payment/bankPayment";
	}
	
	/**
	 * 查询所有支付银行
	 * @return 返回到前端json数据
	 */
	@RequestMapping(value="findPayment")
	public void findPayment(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------findPayment         start------------------------------------");
		List<DictTradePayment> list = dictTradePaymentService.findPaymentNamesAll();
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		Map<String,Object> map= null;
		map = new HashMap<String,Object>();
		map.put("id", "");
		map.put("text", "全部");
		maps.add(map);
		String str=null;
		if(list != null){
			for(DictTradePayment d : list){
				map = new HashMap<String,Object>();
				map.put("id", d.getId());
				map.put("text", d.getRemark());
				maps.add(map);
			} 
			JSONArray jsonArr = JSONArray.fromObject(maps);
			str = jsonArr.toString();
			WebUtils.writeJson(response, str);
		}
		return ;
	}
	
	/**
	 * 将从数据库查询的数据封装为json，返回到前端页面
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @throws ParseException 
	 */
	@RequestMapping("getPayment")
	public void getPayment(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException, ParseException{
		log.info("---------------getPayment----------------");
		String paymentId = request.getParameter("paymentId");
		String merchantId = request.getParameter("merchantId");
		String dimension = request.getParameter("dimension");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		System.out.println("paymentId:"+paymentId+"  merchantId:"+merchantId+"  dimension:"+dimension+"  startDate:"+startDate+"  endDate:"+endDate);
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
	    List<MerchantOrderInfo> bankPayments=new ArrayList<MerchantOrderInfo>();
	    int total=0;
	    JSONObject json =  new JSONObject();
	    List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
	    DictTradePayment payment=null;
	    MerchantInfo merchant=null;
	    MerchantOrderInfo orderInfo=new MerchantOrderInfo();
	    orderInfo.setPageSize(pageSize);
	    orderInfo.setStartRow(startRow);
	    
		if(paymentId!=null && paymentId.length()!=0){
			orderInfo.setPaymentId(Integer.parseInt(paymentId));
		}
		if(merchantId!=null && merchantId.length()!=0){
			orderInfo.setMerchantId(Integer.parseInt(merchantId));
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
		String foundDate="";
		if(("day").equals(dimension) || ("custom").equals(dimension)){//天  自定义
			if(("day").equals(dimension)){
				dimensionName = "天";
			}else if(("custom").equals(dimension)){
				dimensionName = "自定义";
				if((startDate==null || ("").equals(startDate)) && (endDate!=null && !("").equals(endDate))){
					foundDate = "之前~"+endDate;
				}else if((startDate!=null && !("").equals(startDate)) && (endDate==null || ("").equals(endDate))){
					foundDate = startDate+"~至今";
				}else{
					foundDate = startDate+"~"+endDate;
				}
			}
			orderInfo.setStartDate(startDate);
			if(endDate!=null && endDate!=""){
				cal.setTime(sdf.parse(endDate));
				cal.add(Calendar.DATE,1);
			    endDate=sdf.format(cal.getTime());
			}
		    orderInfo.setEndDate(endDate);
		    bankPayments = merchantOrderInfoService.findBankPayment(orderInfo);
		    total = merchantOrderInfoService.findBankPaymentCount(orderInfo);
		    json.put("total", total);
		    if(bankPayments != null){
		    	Map<String,Object> map = null;
		    	for(MerchantOrderInfo r : bankPayments){
		    		map = new LinkedHashMap<String,Object>(); 
		    		if(r.getPaymentId()!=null){
			    		payment=dictTradePaymentService.findNameById(""+r.getPaymentId());
						if(payment!=null){
			    			map.put("paymentName", payment.getRemark());
			    		}
		    		}
		    		if(r.getMerchantId()!=null){
			    		merchant=merchantInfoService.findNameById(r.getMerchantId());
						if(merchant!=null){
			    			map.put("merchantName", merchant.getMerchantName());
			    		}
		    		}
		    		map.put("countOrderAmount", r.getCountOrderAmount()); 
		    		map.put("countPayAmount", r.getCountPayAmount());
		    		map.put("countPayCharge", r.getCountPayCharge());
		    		map.put("dismension", dimensionName);
		    		if(("day").equals(dimension)){
		    			foundDate = sdf.format(r.getCreateDate());
					}
		    		map.put("foundDate", foundDate);
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
				cal.setTime(sdf.parse(endDate));
				cal.add(Calendar.DATE,1);
			    orderInfo.setEndDate(sdf.format(cal.getTime()));
			    bankPayments = merchantOrderInfoService.findBankPayment(orderInfo);
			    if(bankPayments != null){
			    	Map<String,Object> map = null;
			    	for(MerchantOrderInfo r : bankPayments){
			    		map = new LinkedHashMap<String,Object>(); 
			    		if(r.getPaymentId()!=null){
				    		payment=dictTradePaymentService.findNameById(""+r.getPaymentId());
							if(payment!=null){
				    			map.put("paymentName", payment.getRemark());
				    		}
			    		}
			    		if(r.getMerchantId()!=null){
				    		merchant=merchantInfoService.findNameById(r.getMerchantId());
							if(merchant!=null){
				    			map.put("merchantName", merchant.getMerchantName());
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
	@RequestMapping("paymentDownloadSubmit")
	public void paymentDownloadSubmit(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException, ParseException{
		log.info("---------------getBankPaymentDownloadSubmit----------------");
		String paymentId = request.getParameter("paymentId");
		String merchantId = request.getParameter("merchantId");
		String dimension = request.getParameter("dimension");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		System.out.println("paymentId:"+paymentId+"merchantId:"+merchantId+"  dimension:"+dimension+"  startDate:"+startDate+"  endDate:"+endDate);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		List<MerchantOrderInfo> bankPayments=new ArrayList<MerchantOrderInfo>();
		List<MerchantOrderInfo> lists=new ArrayList<MerchantOrderInfo>();
	    DictTradePayment payment=null;
	    MerchantInfo merchant = null;
	    MerchantOrderInfo orderInfo=new MerchantOrderInfo();
	    
		if(paymentId!=null && paymentId.length()!=0){
			orderInfo.setPaymentId(Integer.parseInt(paymentId));
		}
		if(merchantId!=null && merchantId.length()!=0){
			orderInfo.setMerchantId(Integer.parseInt(merchantId));
		}
	    orderInfo.setDimension(dimension);
		List<String> timeDuring = new ArrayList<String>();
		
		if((startDate==null || ("").equals(startDate)) && (endDate==null || ("").equals(endDate))){
			cal.add(Calendar.DATE,-1);
			startDate = sdf.format(cal.getTime());
			endDate = sdf.format(cal.getTime());
		}
		String dimensionName="";
		String foundDate="";
		if(("day").equals(dimension) || ("custom").equals(dimension)){//天  自定义
			if(("day").equals(dimension)){
				dimensionName = "天";
			}else if(("custom").equals(dimension)){
				dimensionName = "自定义";
				if((startDate==null || ("").equals(startDate)) && (endDate!=null && !("").equals(endDate))){
					foundDate = "之前~"+endDate;
				}else if((startDate!=null && !("").equals(startDate)) && (endDate==null || ("").equals(endDate))){
					foundDate = startDate+"~至今";
				}else{
					foundDate = startDate+"~"+endDate;
				}
			}
			orderInfo.setStartDate(startDate);
			if(endDate!=null && endDate!=""){
				cal.setTime(sdf.parse(endDate));
				cal.add(Calendar.DATE,1);
			    endDate=sdf.format(cal.getTime());
			}
		    orderInfo.setEndDate(endDate);
		    bankPayments = merchantOrderInfoService.findBankPaymentNoPage(orderInfo);
		    if(bankPayments != null){
		    	for(MerchantOrderInfo r : bankPayments){
		    		if(r.getPaymentId()!=null){
			    		payment=dictTradePaymentService.findNameById(""+r.getPaymentId());
						if(payment!=null){
			    			r.setPaymentName(payment.getRemark());
			    		}
		    		}
		    		if(r.getMerchantId()!=null){
			    		merchant=merchantInfoService.findNameById(r.getMerchantId());
						if(merchant!=null){
			    			r.setMerchantName(merchant.getMerchantName());
			    		}
		    		}
		    		r.setDimension(dimensionName);
		    		if(("day").equals(dimension)){
		    			foundDate = sdf.format(r.getCreateDate());
					}
		    		r.setFoundDate(foundDate);
		    		lists.add(r);
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
				cal.setTime(sdf.parse(endDate));
				cal.add(Calendar.DATE,1);
			    orderInfo.setEndDate(sdf.format(cal.getTime()));
			    bankPayments = merchantOrderInfoService.findBankPaymentNoPage(orderInfo);
			    if(bankPayments != null){
			    	for(MerchantOrderInfo r : bankPayments){
			    		if(r.getPaymentId()!=null){
				    		payment=dictTradePaymentService.findNameById(""+r.getPaymentId());
							if(payment!=null){
				    			r.setPaymentName(payment.getRemark());
				    		}
			    		}	
			    		if(r.getMerchantId()!=null){
				    		merchant=merchantInfoService.findNameById(r.getMerchantId());
							if(merchant!=null){
				    			r.setMerchantName(merchant.getMerchantName());
				    		}
			    		}
		    			r.setDimension(dimensionName);
			    		r.setFoundDate(during);
			    		lists.add(r);
			    	}
			    }
			}
		}
    	OrderDeriveExport.exportBankPayment(response, lists);
	}   
}
