package cn.com.open.pay.platform.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;



/**
 *  用户登录接口(通过用户名-密码)
 */
@Controller
@RequestMapping("/user/")
public class UserLoginController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	
	 @Autowired
	 private UserService userService;
	

	 @RequestMapping("login")
	 public String  login(HttpServletRequest request,HttpServletResponse response) {
		 log.info("-----------------------login start----------------");
		 
	   return null;
	 }	
   
}