package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.open.pay.platform.manager.login.service.RoleService;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
@Controller
@RequestMapping("/managerRole/")
public class ManagerRoleController  extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private RoleService roleService;
	/**
	 * 跳转到查询角色的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value="roleMessage")
	public String showSearch(){
		log.info("-------------------------showSearch         start------------------------------------");
		return "role/roleMessage";
	}
	
	/**
	 * 根据条件实现查询角色操作
	 * @param request
	 * @param response
	 * @param model
	 * @return  查询完毕后返回查询结果
	 */
	@RequestMapping("QueryRoleMessage")
	public String QueryRoleMessage(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------searchUser         start------------------------------------");
		String roleName = request.getParameter("roleName");
		PrivilegeRole privilegeRole=new PrivilegeRole();
		privilegeRole.setName(roleName);
		List<PrivilegeRole> privilegeRoleList=roleService.findByRoleName(privilegeRole);
		int countNum= roleService.findQueryCount(privilegeRole);
		for(int i=0;i<privilegeRoleList.size();i++){
//			PrivilegeRole privilegeRoleV= privilegeRoleList.get(0);
			int status = privilegeRoleList.get(i).getStatus();
			if(status==0){
				privilegeRoleList.get(i).setStatusName("启用");
			}else{
				privilegeRoleList.get(i).setStatusName("停用");
			}
		}
		JSONArray jsonArr = JSONArray.fromObject(privilegeRoleList);
		 JSONObject jsonObjArr = new JSONObject();  
		 jsonObjArr.put("total", countNum);
		 jsonObjArr.put("rows", jsonArr);
		 System.out.println(jsonArr);
	     WebUtils.writeJson(response,jsonObjArr);
	     return "role/roleMessage";
	}
	
	
	/**
	 * 跳转到添加用户的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value="showAdd")
	public String showAdd(){
		log.info("-------------------------showAdd         start------------------------------------");
		return "show/add";
	}
	
	
	
	/**
	 * 实现添加角色的操作
	 * @param name  角色名
	 * @param status	 状态
	 * @param date 	 真实姓名
	 */
	@ResponseBody
	@RequestMapping("addRole")
	public void addRole(HttpServletRequest request,HttpServletResponse response){
		log.info("-------------------------addUser         start------------------------------------");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String user_name = request.getParameter("user_name");
		String real_name = request.getParameter("real_name");
		String nickname = request.getParameter("nickname");
		String sha_password = request.getParameter("sha_password");
		log.info("--------------user_name："+user_name+"；real_name:"+real_name+"；nickname:"+nickname+"；sha_password:"+sha_password+"-------------------------");
		JSONObject jsonObjArr = new JSONObject();  
		//判断数据库是否已经存在该用户   result 为状态  0表示添加失败  1表示该用户已被注册   2 表示添加用户成功
		String result = "0";
//		PrivilegeRole user_db = roleService.findByRoleName(user_name);
//		if(user_db != null){
//			//
//			result = "1";
//			jsonObjArr.put("result", result);
//		    WebUtils.writeJson(response,jsonObjArr);
//			return;
//		}
//		
		PrivilegeRole privilegeRole = new PrivilegeRole();
		privilegeRole.setName(user_name);
		roleService.addRole(privilegeRole);
		
		result = "2";
		jsonObjArr.put("result", result);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
}
