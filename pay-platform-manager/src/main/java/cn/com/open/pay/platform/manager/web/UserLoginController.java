package cn.com.open.pay.platform.manager.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户登录接口(通过用户名-密码)
 */
@Controller
@RequestMapping("/user/")
public class UserLoginController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;

	/**
	 * 登录验证
	 * 
	 * @param request
	 * @param response
	 * @param username
	 * @param password
	 */
	@RequestMapping("loginVerify")
	public void verify(HttpServletRequest request, HttpServletResponse response, String username, String password) {
		log.info("-----------------------login start----------------");
		boolean flag = false;
		String errorCode = "";
		User user = null;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		user = checkUsername(username, userService);
		if (user != null) {
			if (user.checkPasswod(password)) {
				flag = true;
				errorCode = "ok";
				// Manager manager=managerService.getManagerById(user.getId());
				// int role=0;
				// List<PrivilegeRoleDetails> list=null;
				// List<PrivilegeModule> modules=null;
				// if(manager!=null){
				// role=manager.getRole();
				// list=privilegeRoleDetailsService.findRoleDetailsByRoleId(role);
				// List<Integer>ids=new ArrayList<Integer>(list.size());
				// for(int i=0;i<list.size();i++){
				// ids.add(list.get(i).getModuleId());
				// }
				// if(ids!=null&&list.size()>0){
				// modules= privilegeModuleService.findModuleByIds(ids);
				// for(int j=0;j<modules.size();j++){
				//
				// }
				// }
				// }
				HttpSession session = request.getSession();
				// session.setAttribute("serverHost",payManagerDev.getServer_host());
				// session.setAttribute("manager",manager);
				// session.setAttribute("modules",modules);
				session.setAttribute("user", user);

			} else {
				errorCode = "error";
			}

		} else {
			errorCode = "error";
		}
		map.put("flag", flag);
		map.put("errorCode", errorCode);
		WebUtils.writeJsonToMap(response, map);
	}

	/**
	 * 登录跳转
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = (User) request.getSession().getAttribute("user");
		String username = request.getParameter("userName");
		// 管理员标识，ture为管理员
		Boolean isManager = false;
		if (user != null && user.getUsername().equals(username)) {
			String roleIds = user.getRole();
			// 查找该用户角色，角色拥有的菜单
			if (roleIds != null && !("").equals(roleIds)) {
				String[] roleId = roleIds.split(",");
				List<Integer> roleList = new ArrayList<Integer>();
				for (String string : roleId) {
					if (string != null && !("").equals(string)) {
						roleList.add(Integer.parseInt(string));
					}
				}
				// 判断用户拥有的角色是否是管理员
				List<PrivilegeRole> roles = privilegeRoleService.findByRoleIds(roleList);
				for (PrivilegeRole privilegeRole : roles) {
					if (privilegeRole.getRoleType() == 1) {
						isManager = true;
						break;
					}
				}
				// 查找用户拥有的菜单
				List<PrivilegeModule> moduleList = null;
				if (isManager) {// ，若为管理员，查找所有菜单。
					moduleList = privilegeModuleService.findAllModules();
				} else {
					moduleList = privilegeModuleService.findModuleByRoleIds(roleList);
				}
				// 构建菜单json数据
				JSONArray menuArray = treeMenuList(moduleList, 0);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("menus", menuArray);
				model.addAttribute("menu", JSONObject.fromObject(map));
			}

			model.addAttribute("userName", username);
			model.addAttribute("realName", user.getRealName());
			HttpSession session = request.getSession();
			session.setAttribute("isManager", isManager);
			session.setAttribute("user", user);
			return "login/index";

		}

		return "/index";
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "update")
	public void updatePassword(HttpServletRequest request, HttpServletResponse response) {
		String password = request.getParameter("newpass");
		String username = request.getParameter("userName");
		User user = null;
		user = userService.findByUsername(username);
		// 刷新盐值，重新加密
		user.buildPasswordSalt();
		user.setPlanPassword(password);
		user.setUpdatePwdTime(new Date());
		userService.updateUser(user);
	}

	// 构建菜单tree Json
	public JSONArray treeMenuList(List<PrivilegeModule> menuList, Integer parentId) {
		JSONArray childMenu = new JSONArray();
		for (PrivilegeModule menu : menuList) {
			JSONObject jsonMenu = JSONObject.fromObject(menu);
			Integer menuId = menu.getId();
			Integer pid = menu.getParentId();
			if (parentId == pid) {
				JSONArray childrenNode = treeMenuList(menuList, menuId);
				jsonMenu.put("menus", childrenNode);
				childMenu.add(jsonMenu);
			}
		}
		return childMenu;
	}

	/**
	 * 退出且跳转
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loginOut")
	public String loginOut(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return "/index";
	}
}