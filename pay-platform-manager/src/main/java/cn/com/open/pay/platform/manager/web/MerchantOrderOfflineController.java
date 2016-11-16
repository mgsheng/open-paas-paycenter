package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String merchantOrderId = request.getParameter("merchantOrderId");
		String sourceUserName = request.getParameter("sourceUserName");
		String merchantName = request.getParameter("merchantName");
		String appId = request.getParameter("appId");
		String channelId = request.getParameter("channelId");
		String operator = request.getParameter("operator");
		String orderId = request.getParameter("orderId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		//merchantID = ((merchantID == null || merchantID == "") ? null : (new String(merchantID.getBytes("iso8859-1"),"utf-8")));
		System.out.println("orderId:"+orderId+"  merchantOrderId:"+merchantOrderId+"  sourceUserName:"+sourceUserName+"  merchantName:"+merchantName+"  appId:"+appId+"  channelId:"+channelId+"  operator:"+operator+" startDate:"+startDate+" endDate:"+endDate);
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
	    
	    offline.setId(orderId);
	    offline.setMerchantOrderId(merchantOrderId);
	    if(merchantName!=null && merchantName!=""){
	    	offline.setMerchantId(Integer.parseInt(merchantName));
	    }
	    offline.setSourceUserName(sourceUserName);
	    offline.setAppId(appId);
	    offline.setChannelId(channelId);
	    offline.setOperator(operator);
	    if(startDate!=null && startDate!=""){
	    	try {
				offline.setStartDate(sdf.parse(startDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    if(endDate!=null && endDate!=""){
	    	try {
				offline.setEndDate(sdf.parse(endDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    List<MerchantOrderOffline> offlines = merchantOrderOfflineService.findOfflineAll(offline);
	    int total = merchantOrderOfflineService.findOfflineAllCount(offline);
	    JSONObject json =  new JSONObject();
	    json.put("total", total);
	    List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
	    MerchantInfo merchantInfo=null;
	    PayChannelSwitch channel=null;
	    DictTradePayment payment=null;
	    if(offlines != null){
	    	Map<String,Object> map = null;
	    	for(MerchantOrderOffline r : offlines){
	    		map = new LinkedHashMap<String,Object>();
	    		map.put("id", r.getId());
	    		map.put("merchantOrderId", r.getMerchantOrderId());  
	    		if(r.getPayTime()!=null){
	    			map.put("payTime", sdf.format(r.getPayTime()));
	    		}
	    		map.put("money", r.getMoney());
	    		map.put("sourceUID", r.getSourceUid());
	    		map.put("sourceUserName", r.getSourceUserName());
	    		map.put("realName", r.getRealName());
	    		map.put("phone", r.getPhone());
	    		merchantInfo=merchantInfoService.findNameById(r.getMerchantId());
	    		if(merchantInfo!=null){
		    		map.put("merchantId", merchantInfo.getMerchantName());
	    		}
	    		map.put("appId", r.getAppId());
	    		//channel=payChannelDictionaryService.findNameById(r.getChannelId());
	    		channel=payChannelSwitchService.findNameById(r.getChannelId());
				if(channel!=null){
	    			map.put("channelId", channel.getChannelName());
	    		}
	    		map.put("remark", r.getRemark());
	    		map.put("operator", r.getOperator());
	    		payment=dictTradePaymentService.findNameById(r.getBankCode());
	    		if(payment!=null){
	    			map.put("bankCode", payment.getRemark());
	    		}
	    		map.put("createTime", sdf1.format(r.getCreateTime()));
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
		String flag=request.getParameter("flag");
		List<DictTradePayment> list = dictTradePaymentService.findPaymentNamesAll();
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		Map<String,Object> map= null;
		String str=null;
		if(("all").equals(flag)){
			map = new HashMap<String,Object>();
			map.put("id", "");
			map.put("text", "全部");
			maps.add(map);
		}
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
	
	/**
	 * 提交添加线下订单单据
	 * @return 返回到前端json数据
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="submitAddOrderOffline")
	public void submitAddOrderOffline(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException{
		log.info("-------------------------submitAddOrderOffline         start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String addMerchantOrderId = request.getParameter("addMerchantOrderId");
		Double addMoney = Double.parseDouble(request.getParameter("addMoney"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date addPayTime=null;
		try {
			addPayTime = sdf.parse(request.getParameter("addPayTime"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String addSourceUserName = request.getParameter("addSourceUserName");
		String addRealName = request.getParameter("addRealName");
		String addPhone = request.getParameter("addPhone");
		String addMerchantName = request.getParameter("addMerchantName");
		Integer merchantId = Integer.parseInt(addMerchantName);
		String addAppId = request.getParameter("addAppId");
		String addChannelId = request.getParameter("addChannelId");
		String addBankCode = request.getParameter("addBankCode");
		String addRemark = request.getParameter("addRemark");
		String addOperator = request.getParameter("addOperator");
		String addSourceUID = request.getParameter("addSourceUID");
		
		System.out.println("-----------addMerchantOrderId : "+addMerchantOrderId+"     addMoney : "+addMoney+"    " +
				"addPayTime : "+addPayTime+"   addSourceUserName : "+addSourceUserName+"      addRealName:"+addRealName+"      addPhone : "+addPhone+
				"   addMerchantName : "+addMerchantName+"   addAppId : "+addAppId+"   addChannelId : "+addChannelId+
				"   addBankCode : "+addBankCode+"   addRemark : "+addRemark+"-----------");
		
		JSONObject json =  new JSONObject();
		//封装参数
		String newId="";
		newId=SysUtil.careatePayOrderId();
		MerchantOrderOffline merchantOrderOffline = new MerchantOrderOffline();
		merchantOrderOffline.setId(newId);
		merchantOrderOffline.setMerchantOrderId(addMerchantOrderId);
		merchantOrderOffline.setMoney(addMoney);
		merchantOrderOffline.setPayTime(addPayTime);
		merchantOrderOffline.setSourceUserName(addSourceUserName);
		merchantOrderOffline.setRealName(addRealName);
		merchantOrderOffline.setPhone(addPhone);
		merchantOrderOffline.setMerchantId(merchantId);
		merchantOrderOffline.setAppId(addAppId);
		merchantOrderOffline.setChannelId(addChannelId);
		merchantOrderOffline.setBankCode(addBankCode);
		merchantOrderOffline.setRemark(addRemark);
		merchantOrderOffline.setOperator(addOperator);
		merchantOrderOffline.setSourceUid(addSourceUID);
		//result = 1 添加成功  result = 2 该记录(线下订单号)已存在  result = 0 添加失败 
		int result = -1;
		//先查询该线下订单号是否已经存在
		MerchantOrderOffline OrderOffline = merchantOrderOfflineService.findByMerchantOrderId(addMerchantOrderId);
		if(OrderOffline != null){
			result = 2;
		}else{
			//添加线下收费
			boolean isSuccess = merchantOrderOfflineService.addOrderOffline(merchantOrderOffline);
			//添加日志
			PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(82);
			PrivilegeModule privilegeModule1 = privilegeModuleService.getModuleById(privilegeModule.getParentId());
			String towLevels = privilegeModule.getName();
			String  oneLevels = privilegeModule1.getName();
			User user1 = (User)request.getSession().getAttribute("user");
			String operator = user1.getUsername(); //操作人
			String operatorId = user1.getId()+""; //操作人Id
			PrivilegeResource privilegeResource = privilegeResourceService.findByCode("add");
			privilegeLogService.addPrivilegeLog(operator,privilegeResource.getName(),"经营分析","线下收费维护",privilegeResource.getId()+"",operator+"添加了收费记录，订单号为"+newId,operatorId);
			
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
	 * 下载为excel
	 * @param request
	 * @param response
	 * @return 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("offlineDownloadSubmit")
	public void offlineDownloadSubmit(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("---------------getMerchantOrderOfflineDownloadSubmit----------------");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String merchantOrderId = request.getParameter("merchantOrderId");
		String sourceUserName = request.getParameter("sourceUserName");
		String merchantName = request.getParameter("merchantName");
		String appId = request.getParameter("appId");
		String channelId = request.getParameter("channelId");
		String operator = request.getParameter("operator");
		String orderId = request.getParameter("orderId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		System.out.println("orderId:"+orderId+"  merchantOrderId:"+merchantOrderId+"  sourceUserName:"+sourceUserName+"  merchantName:"+merchantName+"  appId:"+appId+"  channelId:"+channelId+"  operator:"+operator+" startDate:"+startDate+" endDate:"+endDate);
		
	    MerchantOrderOffline offline = new MerchantOrderOffline();
	    
	    offline.setId(orderId);
	    offline.setMerchantOrderId(merchantOrderId);
	    if(merchantName!=null && merchantName!=""){
	    	offline.setMerchantId(Integer.parseInt(merchantName));
	    }
	    offline.setSourceUserName(sourceUserName);
	    offline.setAppId(appId);
	    offline.setChannelId(channelId);
	    offline.setOperator(operator);
	    
	    if(startDate!=null && startDate!=""){
	    	try {
				offline.setStartDate(sdf.parse(startDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    if(endDate!=null && endDate!=""){
	    	try {
				offline.setEndDate(sdf.parse(endDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    List<MerchantOrderOffline> offlines = merchantOrderOfflineService.findOfflineAllNoPage(offline);
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	    MerchantInfo merchantInfo = null;
	    PayChannelDictionary channel = null;
	    DictTradePayment payment=null;
	    for(MerchantOrderOffline r : offlines){
			 Date createDate1 = r.getCreateTime();
			 r.setFoundDate(df.format(createDate1));
			 String appId1=r.getAppId();
			 String appName="";
			 if(appId1!=null && appId1!="" && appId1!="0"){
				 if(appId1.equals("1")){
					 appName = "OES学历";
				 }else if(appId1.equals("10026")){
					 appName = "mooc2u";
				 }
			 }
			 r.setAppName(appName);
			 String channelId1=r.getChannelId();
			 String channelName="";
			 if(channelId1!=null && channelId1!=""){
				channel=payChannelDictionaryService.findNameById(channelId1);
	    		if(channel!=null){
	    			channelName=channel.getChannelName();
	    		}
    			r.setChannelName(channelName);
			 }
			 Integer merchantId1=r.getMerchantId();
			 String merchantName1="";
			 if(merchantId1!=null){				 
				merchantInfo=merchantInfoService.findNameById(merchantId1);
	    		if(merchantInfo!=null){
		    		merchantName1=merchantInfo.getMerchantName();
	    		}
	    		r.setMerchantName(merchantName1);
			 }
			 String bankCode1=r.getBankCode();
			 String bankName="";
			 if(bankCode1!=null && bankCode1!=""){
				payment=dictTradePaymentService.findNameById(bankCode1);
	    		if(payment!=null){
	    			bankName=payment.getRemark();
	    		}
	    		r.setBankName(bankName);
			 }
	    }
	    OrderDeriveExport.exportOrderOffline(response, offlines);
	    //return "usercenter/merchantOrderOffline";
	}
}
