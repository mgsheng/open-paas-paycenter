package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.paychannel.model.ChannelRate;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelRateService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
/**
 * 渠道费率管理
 * @author lvjq
 *
 */
@Controller
@RequestMapping("/paychannel/")
public class PayChannelRateController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private PayChannelRateService payChannelRateService;
	
	/**
	 * 跳转到渠道费率维护页面
	 * @return
	 */
	@RequestMapping(value="rate")
	public String channelRate(){
		log.info("---------------rate----------------");
		return "paychannel/channelRate";
	}
	
	/**
	 * 查询所有渠道代码
	 * @return 返回到前端json数据
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="submitAddChannelRate")
	public void submitAddChannelRate(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException{
		log.info("-------------------------submitAddChannelRate         start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String addPayName = request.getParameter("addPayName");
		String addMerchantID = request.getParameter("addMerchantID");
		String addPayChannelCode = request.getParameter("addPayChannelCode");
		String payRate = request.getParameter("payRate");
		
		System.out.println("-----------addPayName : "+addPayName+"     addMerchantID : "+addMerchantID+"    " +
				"addPayChannelCode : "+addPayChannelCode+"      payRate:"+payRate+"-----------");
		
		JSONObject json =  new JSONObject();
		//封装参数
		ChannelRate channelRate = new ChannelRate();
		channelRate.setPayName(addPayName);
		channelRate.setMerchantID(addMerchantID);
		channelRate.setPayChannelCode(addPayChannelCode);
		channelRate.setPayRate(payRate);
		//result = 1 添加成功  result = 2 该记录已存在  result = 0 添加失败 
		int result = -1;
		//先查询该记录是否已经存在
		List<ChannelRate> rates = payChannelRateService.findChannelRate(channelRate);
		if(rates.size()>=1){
			System.out.println(rates.toString());
			result = 2;
		}else{
			//添加费率
			boolean isSuccess = payChannelRateService.addPayChannelRate(channelRate);
			if(isSuccess){
				result = 1;
			}else{
				result = 0;
			}
		}
		json.put("result", result);
		WebUtils.writeJson(response, json);
		return ;
	}
	
	/**
	 * 查询渠道代码
	 * @return 返回到前端json数据
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="findPayChannelCode")
	public void findPayChannelCode(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException{
		log.info("-------------------------findPayChannelCode         start------------------------------------");
		String payChannelName = request.getParameter("payChannelName");
		payChannelName = ((payChannelName == null || payChannelName == "") ? "" : new String(payChannelName.getBytes("iso8859-1"),"utf-8" ));
		System.out.println("---------------payChannelName  : "+payChannelName);
		//封装参数支付名称
		PayChannelDictionary payChannelDictionary = new PayChannelDictionary();
		payChannelDictionary.setChannelName(payChannelName);
		
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		Map<String,Object> map= null;
		String str=null;
		List<PayChannelDictionary> list = payChannelRateService.findPayChannelCode(payChannelDictionary);
		if(list.size()>1){
			list = payChannelRateService.findPayChannelCodeAll();
		}
		for(PayChannelDictionary p : list){
			map = new HashMap<String,Object>();
			map.put("id", p.getId());
			map.put("text", p.getChannelCode());
			maps.add(map);
		} 
		JSONArray jsonArr = JSONArray.fromObject(maps);
		str = jsonArr.toString();
		WebUtils.writeJson(response, str);
//		System.out.println(str);
		return ;
	}
	
	/**
	 * 查询所有商户名称，商户号
	 * @return 返回到前端json数据
	 */
	@RequestMapping(value="findMerchantNames")
	public void findMerchantNames(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------findMerchantNames         start------------------------------------");
		List<MerchantInfo> list = payChannelRateService.findMerchantNamesAll();
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		Map<String,Object> map= null;
		String str=null;
		if(list != null){
			for(MerchantInfo m : list){
				map = new HashMap<String,Object>();
				map.put("id", m.getId());
				map.put("text", m.getMerchantName());
				maps.add(map);
			} 
			JSONArray jsonArr = JSONArray.fromObject(maps);
			str = jsonArr.toString();
			WebUtils.writeJson(response, str);
			System.out.println(str);
		}
		return ;
	}
	
	/**
	 * 查询所有支付渠道名称
	 * @return 返回到前端json数据
	 */
	@RequestMapping(value="findPayNames")
	public void findPayNames(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------findPayNames         start------------------------------------");
		List<PayChannelDictionary> list = payChannelRateService.findPayChannelCodeAll();
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		Map<String,Object> map= null;
		String str=null;
		if(list != null){
			for(PayChannelDictionary d : list){
				map = new HashMap<String,Object>();
				//map.put("id", d.getId());
				map.put("id", d.getChannelID());
				map.put("text", d.getChannelName());
				maps.add(map);
			} 
			JSONArray jsonArr = JSONArray.fromObject(maps);
			str = jsonArr.toString();
			WebUtils.writeJson(response, str);
			System.out.println(str);
		}
		return ;
	}
	
	/**
	 * 根据id删除目标渠道费率记录
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("removeChannelRate")
	public void removeChannelRate(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("---------------removeChannelRate----------------");
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		System.out.println(id);
		ChannelRate rate = new ChannelRate();
		rate.setId(Integer.valueOf(id));
		boolean result = payChannelRateService.removeChannelRate(rate);
		JSONObject json =  new JSONObject();
		json.put("result", result);
		WebUtils.writeJson(response, json);
		return;
	}
	
	/**
	 * 将从数据库查询的数据封装为json，返回到前端页面
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getChannelRate")
	public void getChannelRate(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("---------------getChannelRate----------------");
		String merchantID = request.getParameter("merchantID");
		merchantID = ((merchantID == null || merchantID == "") ? null : (new String(merchantID.getBytes("iso8859-1"),"utf-8")));
//		System.out.println("merchantID  :   "+merchantID);
		//当前第几页
		String page=request.getParameter("page");
//				System.out.println("page  :" +page);
		//每页显示的记录数
	    String rows=request.getParameter("rows"); 
//			    System.out.println("rows : "+ rows);
		//当前页  
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		//每页显示条数  
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		//每页的开始记录  第一页为1  第二页为number +1   
	    int startRow = (currentPage-1)*pageSize;
	    ChannelRate rate = new ChannelRate();
	    rate.setMerchantID(merchantID);
	    rate.setPageSize(pageSize);
	    rate.setStartRow(startRow);
	    
	    List<ChannelRate> rates = payChannelRateService.findRateAll(rate);
	    int total = payChannelRateService.findRateAllCount(rate);
	    JSONObject json =  new JSONObject();
	    json.put("total", total);
	    List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
	    if(rates != null){
	    	Map<String,Object> map = null;
	    	for(ChannelRate r : rates){
	    		map = new LinkedHashMap<String,Object>();
	    		map.put("id", r.getId());
	    		map.put("merchantID", r.getMerchantID());
	    		map.put("payChannelCode", r.getPayChannelCode());
	    		map.put("payName", r.getPayName());
	    		map.put("payRate", r.getPayRate());
	    		maps.add(map);
	    	}
	    	JSONArray jsonArr = JSONArray.fromObject(maps);
	    	json.put("rows", jsonArr);
	    	WebUtils.writeJson(response, json);
	    }
//	    System.out.println(json);
		return;
	}
	
	/**
	 * 根据封装的id修改渠道费率
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("submitRate")
	public void updateRate(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("---------------submitRate----------------");
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		String payRate = request.getParameter("payRate");
//		System.out.println("id:"+id+"   payRate:"+payRate);
		ChannelRate rate = new ChannelRate();
		rate.setId(Integer.valueOf(id));
		rate.setPayRate(payRate);
		
		boolean result = payChannelRateService.updateRate(rate);
		JSONObject json =  new JSONObject();
		json.put("result", result);
		WebUtils.writeJson(response, json);
		return;
	}
	
}
