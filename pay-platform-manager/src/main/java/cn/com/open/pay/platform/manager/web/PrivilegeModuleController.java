package cn.com.open.pay.platform.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 模块管理基础类
 * @author admin
 *
 */
@Controller
@RequestMapping("/module/")
public class PrivilegeModuleController {
	 /**
     * 跳转模块管理页面
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "index")
	public String stats(HttpServletRequest request,HttpServletResponse response) {
    	return "model/index";
    }

}
