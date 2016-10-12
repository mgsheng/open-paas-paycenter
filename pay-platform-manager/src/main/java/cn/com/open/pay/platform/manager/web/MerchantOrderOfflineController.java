package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.department.model.DictTradePayment;
import cn.com.open.pay.platform.manager.department.service.DictTradePaymentService;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderOffline;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderOfflineService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
/**
 * 线下收费管理
 * @author lvjq
 *
 */
@Controller
@RequestMapping("/manage/")
public class MerchantOrderOfflineController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private MerchantOrderOfflineService merchantOrderOfflineService;
	@Autowired
	private DictTradePaymentService dictTradePaymentService;
	
	/**
	 * 跳转到线下收费维护页面
	 * @return
	 */
	@RequestMapping(value="offlineOrderPages")
	public String channelRate(){
		log.info("---------------offlineOrderPages----------------");
		return "usercenter/merchantOrderOffline";
	}
	
	/**
	 * 将从数据库查询的数据封装为json，返回到前端页面
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getMerchantOrderOffline")
	public void getMerchantOrderOffline(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("---------------getMerchantOrderOffline----------------");
		//String merchantID = request.getParameter("merchantID");
		//merchantID = ((merchantID == null || merchantID == "") ? null : (new String(merchantID.getBytes("iso8859-1"),"utf-8")));
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
	    MerchantOrderOffline offline = new MerchantOrderOffline();
	    offline.setPageSize(pageSize);
	    offline.setStartRow(startRow);
	    
	    List<MerchantOrderOffline> offlines = merchantOrderOfflineService.findOfflineAll();
	    int total = merchantOrderOfflineService.findOfflineAllCount(offline);
	    JSONObject json =  new JSONObject();
	    json.put("total", total);
	    List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
	    if(offlines != null){
	    	Map<String,Object> map = null;
	    	for(MerchantOrderOffline r : offlines){
	    		map = new LinkedHashMap<String,Object>();
	    		/*map.put("id", r.getId());
	    		map.put("merchantID", r.getMerchantID());
	    		map.put("payChannelCode", r.getPayChannelCode());
	    		map.put("payName", r.getPayName());
	    		map.put("payRate", r.getPayRate());*/
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
	 * 查询所有付款银行
	 * @return 返回到前端json数据
	 */
	@RequestMapping(value="findBankCode")
	public void findBankCode(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------findBankCode     start------------------------------------");
		List<DictTradePayment> list = dictTradePaymentService.findPaymentNamesAll();
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		Map<String,Object> map= null;
		String str=null;
		if(list != null){
			for(DictTradePayment m : list){
				map = new HashMap<String,Object>();
				map.put("id", m.getId());
				map.put("text", m.getRemark());
				maps.add(map);
			} 
			JSONArray jsonArr = JSONArray.fromObject(maps);
			str = jsonArr.toString();
			WebUtils.writeJson(response, str);
			System.out.println(str);
		}
		return ;
	}
}
