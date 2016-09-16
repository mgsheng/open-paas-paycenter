package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;

@Controller
@RequestMapping("/managerUser/")
public class ManagerUserController  extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private UserService userService;
	
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
		Integer id = Integer.valueOf(new String(request.getParameter("id").getBytes("iso-8859-1"),"utf-8"));
		
		//将请求参数封装到User对象中
		User user = new User();
		user.setId(id);
		user.setRealName(realname);
		user.setNickName(nickname);
		
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
		String username = new String(request.getParameter("username").getBytes("iso-8859-1"),"utf-8");
		String realname = new String(request.getParameter("realname").getBytes("iso-8859-1"),"utf-8");
		String nickname = new String(request.getParameter("nickname").getBytes("iso-8859-1"),"utf-8");
		//将请求参数封装到User对象中
		User user = new User();
		user.setUsername(username);
		user.setRealName(realname);
		user.setNickName(nickname);
//		System.out.println("-----------username："+username+"realname："+realname+"nickname："+nickname+"-----------");
		List<User> users = userService.findUsers(user);
		if(users ==null) return;
		int count = users.size();
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
	 * 跳转到添加用户的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value="showAddUser")
	public String showAdd(){
		log.info("-------------------------showAdd         start------------------------------------");
		return "show/addUser";
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
//		log.info("--------------user_name："+user_name+"；real_name:"+real_name+"；nickname:"+nickname+"；sha_password:"+sha_password+"-------------------------");
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
