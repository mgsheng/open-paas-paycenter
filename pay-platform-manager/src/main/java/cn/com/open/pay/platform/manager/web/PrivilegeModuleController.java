package cn.com.open.pay.platform.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;

/**
 * 模块管理基础类
 * @author admin
 *
 */
@Controller
@RequestMapping("/module/")
public class PrivilegeModuleController {
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
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
    /**
     * 添加模块
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "add")
	public void add(HttpServletRequest request,HttpServletResponse response) {
    }
    /**
     * 编辑模块
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "edit")
	public void edit(HttpServletRequest request,HttpServletResponse response) {
    }
    /**
     * 删除模块
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "delete")
	public void delete(HttpServletRequest request,HttpServletResponse response) {
    }
    /**
     * 查询模块
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "findModuel")
	public void findModuel(HttpServletRequest request,HttpServletResponse response) {
    }

}
