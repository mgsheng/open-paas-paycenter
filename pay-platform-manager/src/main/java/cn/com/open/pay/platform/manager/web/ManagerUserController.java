package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;

@Controller
@RequestMapping("/managerUser/")
public class ManagerUserController  extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private UserService userService;
	
	/**
	 * 跳转到用户信息列表的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value="toRole")
	public String toRole(HttpServletRequest request,HttpServletResponse response,Model model)throws UnsupportedEncodingException{
		log.info("-------------------------toRole       start------------------------------------");
		String id = request.getParameter("id");
		String userName =request.getParameter("userName");
		id =( id==null?null:new String(id.getBytes("iso-8859-1"),"utf-8"));
		userName =(userName==null?null:new String(userName.getBytes("iso-8859-1"),"utf-8"));
		model.addAttribute("id", id);
		model.addAttribute("userName", userName);
//		System.out.println("id:"+id+"userName:"+userName);
		return "show/authorizeRole";
	}
	
	/**
	 * 授权用户角色
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("authorizeRole")
	public void authorizeRole(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------authorizeRole        start------------------------------------");
		String id = request.getParameter("id");
		String role = request.getParameter("role");
		id =( id==null?null:new String(id.getBytes("iso-8859-1"),"utf-8"));
		if(role == null || role == ""){
			role = null;
		}else{
			role = new String(role.getBytes("iso-8859-1"),"utf-8");
		}
		System.out.println("****************id:"+id+"****************role:"+role);
		
		//将请求参数封装到User对象中
		User user = new User();
		user.setId(Integer.valueOf(id));
		user.setRole(role);
		boolean result = userService.authorizeRole(user);
		// result = true表示该用户授权角色成功
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("result",result);
	    WebUtils.writeJson(response,jsonobj);
		return;
	}
	
	
	
	/**
	 * 查询指定用户的角色情况
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("role")
	public void role(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------role        start------------------------------------");
		String id = request.getParameter("id");
		id =( id==null?null:new String(id.getBytes("iso-8859-1"),"utf-8"));
//		System.out.println("****************id:"+id);
		//将请求参数封装到User对象中
		User user = new User();
		user.setId(Integer.valueOf(id));
		String roles = userService.findUserRoles(user);
		
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
	    PrivilegeRole privilegeRole = new PrivilegeRole();
	    privilegeRole.setStartRow(startRow);
	    privilegeRole.setPageSize(pageSize);
	    
		List<PrivilegeRole> prs = userService.findRoleAll(privilegeRole);
		int  total = prs.size();
		JSONObject jsonObjArr = new JSONObject();  
//		System.out.println("-------------total-------:"+total);
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		for(PrivilegeRole pr : prs){
			Integer roleId = pr.getId();
			String roleName = pr.getName();
			Integer status = pr.getStatus();
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			map.put("id",roleId);
			map.put("name",roleName);
			map.put("status", status);
			if(roles == null || roles == ""){
				map.put("checked",false);
			}else{
				String[] rls = roles.split(",");
				for(int i = 0; i < rls.length; i++){
					if(String.valueOf(roleId).equals(rls[i])){
						map.put("checked",true);
					}
				}
			}
			list.add(map);
		}
		JSONArray jsonArr = JSONArray.fromObject(list);
		jsonObjArr.put("total", total);
		jsonObjArr.put("rows", jsonArr);
		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return ;
	}
	
	
	/**
	 * 	修改用户信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("updateUser")
	public void updateUserByID(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------updateUserByID        start------------------------------------");
		String realname = new String(request.getParameter("realname").getBytes("iso-8859-1"),"utf-8");
		String nickname = new String(request.getParameter("nickname").getBytes("iso-8859-1"),"utf-8");
		String deptName = new String(request.getParameter("updateDeptName").getBytes("iso-8859-1"),"utf-8");
		String deptID= new String(request.getParameter("updateDeptID").getBytes("iso-8859-1"),"utf-8");
		Integer id = Integer.valueOf(new String(request.getParameter("id").getBytes("iso-8859-1"),"utf-8"));
		
		//将请求参数封装到User对象中
		User user = new User();
		user.setId(id);
		user.setRealName(realname);
		user.setNickName(nickname);
		user.setDeptName(deptName);
		user.setDeptID(Integer.valueOf(deptID));
		
		boolean result = userService.updateUser(user);
		// result = true表示该用户修改成功
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("result",result);
	    WebUtils.writeJson(response,jsonobj);
		return;
	}
	
	/**
	 * 根据用户id删除用户
	 * @param request
	 * @param response
	 */
	@RequestMapping("removeUserByID")
	public void removeUser(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		Integer	id = Integer.valueOf(new String(request.getParameter("id").getBytes("iso-8859-1"),"utf-8"));
		boolean result = userService.removeUserByID(id);
		// result = true表示该用户删除成功
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("result",result);
	    WebUtils.writeJson(response,jsonobj);
		return;
	}
	
	/**
	 * 跳转到用户信息列表的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value="userList")
	public String userList(){
		log.info("-------------------------userlist       start------------------------------------");
		return "show/userlist";
	}
	
	/**
	 * 根据条件（username：用户名；realname：真实姓名；nickname：昵称）实现查询用户操作
	 * @param request
	 * @param response
	 * @return  查询完毕后返回查询结果 User对象或 User集合
	 */
	@RequestMapping("findUsers")
	public void findUsers(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------findUsers        start------------------------------------");
		String username = request.getParameter("username");
		String realname = request.getParameter("realname");
		String nickname = request.getParameter("nickname");
		String deptName = request.getParameter("deptName");
		username =( username==null?null:new String(username.getBytes("iso-8859-1"),"utf-8"));
		realname =( realname==null?null:new String(realname.getBytes("iso-8859-1"),"utf-8"));
		nickname =( nickname==null?null:new String(nickname.getBytes("iso-8859-1"),"utf-8"));
		deptName =( deptName==null?null:new String(deptName.getBytes("iso-8859-1"),"utf-8"));
		//当前第几页
		String page=request.getParameter("page");
		System.out.println(page);
		//每页显示的记录数
	    String rows=request.getParameter("rows"); 
	    System.out.println(rows);
		//当前页  
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		//每页显示条数  
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		//每页的开始记录  第一页为1  第二页为number +1   
	    int startRow = (currentPage-1)*pageSize;
		//将请求参数封装到User对象中
		User user = new User();
		user.setUsername(username);
		user.setRealName(realname);
		user.setNickName(nickname);
		user.setDeptName(deptName);
		user.setPageSize(pageSize);
		user.setStartRow(startRow);
//		System.out.println("-----------username："+username+"realname："+realname+"nickname："+nickname+"-----------");
		List<User> users = userService.findUsers(user);
		int count = userService.findUsersCount(user);
//		for(User u : users){
//			System.out.println(u.getCreate_Time());
//		}
		
		JSONArray jsonArr = JSONArray.fromObject(users);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
//		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
	
	/**
	 * 查询所有部门
	 * @return 返回到前端json数据
	 */
	@RequestMapping(value="findAllDepts")
	public void findAllDepts(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------findAllDepts         start------------------------------------");
		List<Department> list = userService.findAllDepts();
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		Map<String,Object> map= null;
		String str=null;
		if(list != null){
			for(Department d : list){
				map = new HashMap<String,Object>();
				map.put("id", d.getId());
				map.put("text", d.getDeptName());
				maps.add(map);
			} 
			JSONArray jsonArr = JSONArray.fromObject(maps);
			str = jsonArr.toString();
			WebUtils.writeJson(response, str);
//			System.out.println(str);
		}
		return ;
	}
	
	/**
	 * 实现添加用户的操作
	 * @param user_name  用户名
	 * @param nickname	 昵称
	 * @param realname 	 真实姓名
	 * @param sha_password	   MD5加密密码
	 */
	@ResponseBody
	@RequestMapping("addUser")
	public void addUser(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------addUser         start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String user_name = request.getParameter("user_name");
		String real_name = request.getParameter("real_name");
		String nickname = request.getParameter("nickname");
		String sha_password = request.getParameter("sha_password");
		String deptName = request.getParameter("addDeptName");
		String deptID = request.getParameter("deptID");
//		log.info("user_name："+user_name+"；real_name:"+real_name+"；nickname:"+nickname+"；sha_password:"+sha_password+";deptName:"+deptName);
		JSONObject jsonObjArr = new JSONObject();  
		//判断数据库是否已经存在该用户   
		boolean result = false;
		User user_db = userService.findByUsername(user_name);
		if(user_db != null){
			// result = false表示该用户已被注册
			jsonObjArr.put("result", result);
		    WebUtils.writeJson(response,jsonObjArr);
			return;
		}
		
		User user = new User();
		user.setUsername(user_name);
		user.setRealName(real_name);
		user.setNickName(nickname);
		user.setDeptName(deptName);
		user.setDeptID(Integer.valueOf(deptID));
		user.setCreateTime(new Date().getTime());
		//MD5加密
		user.setPlanPassword(sha_password);
		result = userService.addUser(user);
		
		// result = true 表示添加用户成功
		jsonObjArr.put("result", result);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
}
