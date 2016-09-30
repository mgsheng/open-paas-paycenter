package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.department.service.DictTradeChannelService;
import cn.com.open.pay.platform.manager.department.service.ManagerDepartmentService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
/**
 *渠道管理
 * @author
 *
 */
@Controller
@RequestMapping("/irrigation/")
public class IrrigationDitch extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private ManagerDepartmentService managerDepartmentService;
	
	@Autowired
	private DictTradeChannelService dictTradeChannelService;
	
	/**
	 * 跳转到渠道信息列表的页面
	 * @return
	 */
	@RequestMapping(value="irrigation")
	public String irrigation(){
		log.info("-------------------------departmentList       start------------------------------------");
		return "department/irrigationDitch";
	}
	
	/**
	 * 根据封装的Department对象中的属性查找所以符合条件的Department对象
	 * @return
	 */
	@RequestMapping(value="findIrrigation")
	public void findIrrigation(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------findDepts        start------------------------------------");
		String CHANNEL_NAME = request.getParameter("CHANNEL_NAME");
		String channelName="";
		if(CHANNEL_NAME !=null ){
			channelName = new String(CHANNEL_NAME.getBytes("iso-8859-1"),"utf-8");
		}
		String channelStatus = request.getParameter("CHANNEL_STATUS");
		String merId = request.getParameter("MERID");
		
//		System.out.println(deptName);
		//当前第几页
		String page=request.getParameter("page");
//		System.out.println(page);
		
		//每页显示的记录数
	    String rows=request.getParameter("rows"); 
//	    System.out.println(rows);
		//当前页  
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		//每页显示条数  
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		//每页的开始记录  第一页为1  第二页为number +1   
	    int startRow = (currentPage-1)*pageSize;
	    
		//将请求参数封装到Department对象中
	    DictTradeChannel dictTradeChannel = new DictTradeChannel();
	    dictTradeChannel.setChannelName(channelName);
	    if(channelStatus!=""&&channelStatus!=null){
	    	dictTradeChannel.setChannelStatus(Double.parseDouble(channelStatus));
	    }
	    dictTradeChannel.setMerId(merId);
	    dictTradeChannel.setPageSize(pageSize);
	    dictTradeChannel.setStartRow(startRow);
		List<DictTradeChannel> departments = dictTradeChannelService.findDepts(dictTradeChannel);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		if(departments!=null){
			for(int i=0;i<departments.size();i++){
				DictTradeChannel dictTradeChannel1= departments.get(i);
				Date createDate1 = dictTradeChannel1.getCreateDate();
				dictTradeChannel1.setFoundDate(df.format(createDate1));//交易时间
				Double ChannelStatus = dictTradeChannel1.getChannelStatus();
				if(ChannelStatus==1){
					dictTradeChannel1.setChannelStatusName("正常");
				}else if(ChannelStatus==2){
					dictTradeChannel1.setChannelStatusName("锁定");
				}else{
					dictTradeChannel1.setChannelStatusName("关闭");
				}
				Double priority = dictTradeChannel1.getPriority();
				if(priority==0){
					dictTradeChannel1.setPriorityName("高");
				}else if(priority==1){
					dictTradeChannel1.setPriorityName("中");
				}else{
					dictTradeChannel1.setPriorityName("低");
				}
			}
		}
		int count = dictTradeChannelService.findQueryCount(dictTradeChannel);
		System.out.println(count);
		JSONArray jsonArr = JSONArray.fromObject(departments);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
//		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
	
	
	/**
	 * 根据ID删除
	 * @return
	 */
	@RequestMapping(value="removeDictTradeID")
	public void removeDictTradeID(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------removeDeptByID       start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
//		System.out.println("id  :  "+id);
		// result = false表示删除失败
		boolean result = false;
		JSONObject jsonobj = new JSONObject();
		if(id != null && id !=""){
			result = dictTradeChannelService.removeDictTradeID(Integer.valueOf(id));
		}
		jsonobj.put("result",result);
	    WebUtils.writeJson(response,jsonobj);
		return;
	}
	
	/**
	 * 添加
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="addDictTrade")
	public void addDictTrade(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------addDept        start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String channelName=new String(request.getParameter("channelName").getBytes("iso-8859-1"),"utf-8");
		String channelStatus = request.getParameter("channelStatus");
		String priority = request.getParameter("priority");
		String paymentChannel = request.getParameter("paymentChannel");
		String merId = request.getParameter("merId");
		String keyValue = request.getParameter("keyValue");
		String notifyUrl = request.getParameter("notifyUrl");
		String other = request.getParameter("other");
		String paymentType = request.getParameter("paymentType");
		
		String backUrl = request.getParameter("backUrl");
		String type = request.getParameter("type");
		String sighType = request.getParameter("sighType");
		String inputCharset = request.getParameter("inputCharset");
		String memo = request.getParameter("memo");
		if(memo!=null){
			memo=new String(memo.getBytes("iso-8859-1"),"utf-8");
		}
		JSONObject jsonObjArr = new JSONObject();  
//		//判断数据库是否已经存在该渠道
		boolean result = false;
//		Department dept_db = managerDepartmentService.findByDeptName(deptName);
//		if(dept_db != null){
//			// result = false表示该部门已被注册
//			jsonObjArr.put("result", result);
//		    WebUtils.writeJson(response,jsonObjArr);
//			return;
//		}
		//将参数封装到Department对象中
		Date createTime = new Date();
		DictTradeChannel dictTradeChannel = new DictTradeChannel();
		dictTradeChannel.setChannelName(channelName);
		dictTradeChannel.setChannelStatus(Double.parseDouble(channelStatus));
		dictTradeChannel.setPriority(Double.parseDouble(priority));
		dictTradeChannel.setCreateDate(createTime);
		dictTradeChannel.setPaymentChannel(paymentChannel);
		dictTradeChannel.setMerId(merId);
		dictTradeChannel.setKeyValue(keyValue);
		dictTradeChannel.setNotifyUrl(notifyUrl);
		dictTradeChannel.setOther(other);
		dictTradeChannel.setPaymentType(Integer.parseInt(paymentType));
		dictTradeChannel.setBackUrl(backUrl);
		dictTradeChannel.setType(Integer.parseInt(type));
		dictTradeChannel.setSighType(sighType);
		dictTradeChannel.setInputCharset(inputCharset);
		dictTradeChannel.setMemo(memo);
		
		result = dictTradeChannelService.addDictTrade(dictTradeChannel);
		// result = true 表示添加成功
		jsonObjArr.put("result", result);
		WebUtils.writeJson(response,jsonObjArr);
	}
	
	
	
	/**
	 * 根据ID修改渠道信息
	 * @return
	 */
	@RequestMapping(value="updateDictTrade")
	public void updateDictTrade(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------updateDeptByID       start------------------------------------");
		String id = request.getParameter("id");
		String channelName=new String(request.getParameter("channelName").getBytes("iso-8859-1"),"utf-8");
		String channelStatus = request.getParameter("channelStatus");
		String priority = request.getParameter("priority");
		String paymentChannel = request.getParameter("paymentChannel");
		String notifyUrl = request.getParameter("notifyUrl");
		String paymentType = request.getParameter("paymentType");
		String backUrl = request.getParameter("backUrl");
		String type = request.getParameter("type");
		String sighType = request.getParameter("sighType");
		String inputCharset = request.getParameter("inputCharset");
		String memo = request.getParameter("memo");
		if(memo!=null){
			memo=new String(memo.getBytes("iso-8859-1"),"utf-8");
		}
		
		JSONObject jsonobj = new JSONObject();
		boolean result = false;
		DictTradeChannel dictTradeChannel = new DictTradeChannel();
		dictTradeChannel.setId(Integer.parseInt(id));
		dictTradeChannel.setChannelName(channelName);
		dictTradeChannel.setChannelStatus(Double.parseDouble(channelStatus));
		dictTradeChannel.setPriority(Double.parseDouble(priority));
		dictTradeChannel.setPaymentChannel(paymentChannel);
		dictTradeChannel.setNotifyUrl(notifyUrl);
		dictTradeChannel.setPaymentType(Integer.parseInt(paymentType));
		dictTradeChannel.setBackUrl(backUrl);
		dictTradeChannel.setType(Integer.parseInt(type));
		dictTradeChannel.setSighType(sighType);
		dictTradeChannel.setInputCharset(inputCharset);
		dictTradeChannel.setMemo(memo);
		// result = false表示修改失败
		result = dictTradeChannelService.updateDictTrade(dictTradeChannel);
		jsonobj.put("result",result);
	    WebUtils.writeJson(response,jsonobj);
		return;
	}
	
	

	/**
	 * 跳转到所选部门的员工信息页面
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("toDeptUsers")
	public String  deptUsers(HttpServletRequest request,HttpServletResponse response,Model model)throws UnsupportedEncodingException{
		log.info("-------------------------toDeptUsers       start------------------------------------");
		String deptID = request.getParameter("id");
		String deptName = request.getParameter("deptName");
		System.out.println("deptID :"+deptID+"  deptName:"+deptName);
		deptID =( deptID==null?null:new String(deptID.getBytes("iso-8859-1"),"utf-8"));
		deptName =(deptName==null?null:new String(deptName.getBytes("iso-8859-1"),"utf-8"));

		model.addAttribute("deptID",deptID);
		model.addAttribute("deptName", deptName);
		return "department/deptUsers";
	}
	
	/**
	 * 根据封装的User对象中的属性查找所以所选部门的User对象集合
	 * @return
	 */
	@RequestMapping(value="findDeptUsers")
	public void findDeptUsers(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------findDeptUsers        start------------------------------------");
		
		String deptID = request.getParameter("deptID");
		String deptName =  request.getParameter("deptName");
		if(deptID !=null){
			deptID = new String(deptID.getBytes("iso-8859-1"),"utf-8");
		}
		if(deptName !=null){
			deptName = new String(deptName.getBytes("iso-8859-1"),"utf-8");
		}
		
//		System.out.println("******deptID :   "+deptID+"     deptName:"+deptName);
		//当前第几页
		String page=request.getParameter("page");
//		System.out.println(page);
		
		//每页显示的记录数
	    String rows=request.getParameter("rows"); 
//	    System.out.println(rows);
		//当前页  
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		//每页显示条数  
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		//每页的开始记录  第一页为1  第二页为number +1   
	    int startRow = (currentPage-1)*pageSize;
	    
		//将请求参数封装到Department对象中
		User user = new User();
		user.setDeptID(Integer.valueOf(deptID));
		user.setDeptName(deptName);
		user.setPageSize(pageSize);
		user.setStartRow(startRow);
		List<User> users = managerDepartmentService.findDeptUsers(user);
		if(users ==null) return;
		int count = managerDepartmentService.findDeptUsersCount(user);
		JSONArray jsonArr = JSONArray.fromObject(users);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
//		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
	
	
}
