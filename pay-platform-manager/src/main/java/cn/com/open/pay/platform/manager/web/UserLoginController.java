package cn.com.open.pay.platform.manager.web;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;



/**
 *  用户登录接口(通过用户名-密码)
 */
@Controller
@RequestMapping("/user/")
public class UserLoginController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	
	 @Autowired
	 private UserService userService;
	
     /**
      * 登录验证
      * @param request
      * @param response
      * @param username
      * @param password
      */
	 @RequestMapping("verify")
	 public void  verify(HttpServletRequest request,HttpServletResponse response,String username,String password) {
		 log.info("-----------------------login start----------------");
		 boolean flag = false;
	     String errorCode = "";
	     User user = null;
	     Map<String, Object> map = new LinkedHashMap<String, Object>();
	     user=checkUsername(username,userService);
	     if(user!=null){
	    	  if(user.checkPasswod(password)){
	    		  flag = true;
	    		  errorCode="ok"; 
				}else{
					errorCode="error"; 
				}
	    	
	     }else{
	    	 errorCode="error"; 
	     }
	     map.put("flag",flag);
	     map.put("errorCode",errorCode);
		 WebUtils.writeJsonToMap(response, map);
	 }
	 /**
	     * 登录跳转页面
	     * @param request
	     * @param model
	     * @param bool
	     * @return
	     */
	    @RequestMapping(value = "login")
		public String login(HttpServletRequest request,HttpServletResponse response) {
	    	return "login/index";
	    }
}