package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
import cn.com.open.pay.platform.manager.log.model.PrivilegeLog;
import cn.com.open.pay.platform.manager.log.service.PrivilegeLogService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleDetailsService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
@Controller
@RequestMapping("/privilegeLog/")
public class PrivilegeLogController  extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private PrivilegeLogService privilegeLogService;

	/**
	 * 跳转到查询日志的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value="privilegeLog")
	public String showSearch(){
		log.info("-------------------------showSearch         start------------------------------------");
		return "common/log/index";
	}
	
	
	/**
	 * 根据封装的Department对象中的属性查找所以符合条件的Department对象
	 * @return
	 */
	@RequestMapping(value="findPrivilegeLog")
	public void findPrivilegeLog(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------findDepts        start------------------------------------");
		String operator1 = request.getParameter("operator");
		String operator="";
		if(operator1 !=null ){
			operator = new String(operator1.getBytes("iso-8859-1"),"utf-8");
		}
		String operationContent1 = request.getParameter("operationContent");
		String operationContent="";
		if(operationContent1 !=null ){
			operationContent = new String(operationContent1.getBytes("iso-8859-1"),"utf-8");
		}
		
		
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
	    
		//将请求参数封装到Department对象中
	    PrivilegeLog privilegeLog = new PrivilegeLog();
	    privilegeLog.setOperator(operator);
	    privilegeLog.setOperationContent(operationContent);
	    privilegeLog.setPageSize(pageSize);
	    privilegeLog.setStartRow(startRow);
		List<PrivilegeLog> departments = privilegeLogService.findDepts(privilegeLog);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		for(int i=0;i<departments.size();i++){
			PrivilegeLog privilegeLog1 = departments.get(i);
			Date createDate1 = privilegeLog1.getOperationTime();
			privilegeLog1.setFoundDate(df.format(createDate1));//交易时间
		}
		int count = privilegeLogService.findQueryCount(privilegeLog);
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
	 * 添加
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="addPrivilegeLog")
	public void addPrivilegeLog(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------addDept        start------------------------------------");
		request.setCharacterEncoding("utf-8");
		
		String oneLevels = request.getParameter("oneLevels");
		String towLevels = request.getParameter("towLevels");
		String operationAuthority = request.getParameter("operationAuthority");
		
		User user = (User)request.getSession().getAttribute("user");
		String operator = user.getUsername(); //操作人
		String operatorId = user.getId()+""; //操作人Id
		

		privilegeLogService.addPrivilegeLog(operator,"添加",oneLevels,towLevels,operationAuthority,"日志添加",operatorId);
//		// result = true 表示添加成功
//		if(result=true){
//			jsonObjArr.put("returnMsg", "1");
//		}else{
//			jsonObjArr.put("returnMsg", "3");
//		}
		
//		jsonObjArr.put("result", result);
//		WebUtils.writeJson(response,jsonObjArr);
	}
	
}
