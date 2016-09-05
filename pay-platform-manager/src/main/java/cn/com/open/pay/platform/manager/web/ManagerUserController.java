package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
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
	 * 跳转到查询用户的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value="showSearch")
	public String showSearch(){
		log.info("-------------------------showSearch         start------------------------------------");
		return "show/search";
	}
	
	/**
	 * 根据条件实现查询用户操作
	 * @param request
	 * @param response
	 * @param model
	 * @return  查询完毕后返回查询结果
	 */
	@RequestMapping("searchUser")
	public User searchUser(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("-------------------------searchUser         start------------------------------------");
		String username = request.getParameter("userName");
		String realname = request.getParameter("realname");
		String nickname = request.getParameter("nickname");
		User user=null;
		user=userService.findByUsername(username);
//		model.addAttribute("userName",username);
//		model.addAttribute("realName",user.getRealName());
		return user;
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
	 * 实现添加用户的操作
	 * @param user_name  用户名
	 * @param nickname	 昵称
	 * @param realname 	 真实姓名
	 * @param sha_password	   MD5加密密码
	 */
	@ResponseBody
	@RequestMapping("addUser")
	public void addUser(HttpServletRequest request,HttpServletResponse response){
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
		User user_db = userService.findByUsername(user_name);
		if(user_db != null){
			//
			result = "1";
			jsonObjArr.put("result", result);
		    WebUtils.writeJson(response,jsonObjArr);
			return;
		}
		
		User user = new User();
		user.setUsername(user_name);
		user.setRealName(real_name);
		user.setNickName(nickname);
		
		//MD5加密
		user.setPlanPassword(sha_password);
		userService.addUser(user);
		
		result = "2";
		jsonObjArr.put("result", result);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
}
