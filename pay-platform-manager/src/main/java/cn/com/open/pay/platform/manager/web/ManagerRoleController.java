package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
		return "privilege/role/roleMessage";
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
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		for(int i=0;i<privilegeRoleList.size();i++){
			PrivilegeRole privilegeRole1 = privilegeRoleList.get(i);
			int status = privilegeRole1.getStatus();
			if(status==0){
				privilegeRoleList.get(i).setStatusName("启用");
			}else{
				privilegeRoleList.get(i).setStatusName("停用");
			}
			Date createTime = privilegeRole1.getCreateTime();
			privilegeRole1.setCreate_Time(df.format(createTime));//交易时间
		}
		JSONArray jsonArr = JSONArray.fromObject(privilegeRoleList);
		 JSONObject jsonObjArr = new JSONObject();  
		 jsonObjArr.put("total", countNum);
		 jsonObjArr.put("rows", jsonArr);
		 System.out.println(jsonArr);
	     WebUtils.writeJson(response,jsonObjArr);
	     return "privilege/role/roleMessage";
	}
	
	
	 /**
     * 添加角色
     * @param request
     * @param model
     * @param bool
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "addRole")
	public void addRole(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
    	String  status= request.getParameter("status");
    	Map<String,Object> map = new HashMap<String, Object>();
    	String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
    	List <PrivilegeRole> list= roleService.findByName(name);
    	if(list!=null&& list.size()>0){
    		map.put("returnMsg", "0");
    	}else{
    		PrivilegeRole privilegeRole=new PrivilegeRole();
    		privilegeRole.setName(name);
    		privilegeRole.setCreateTime(new Date());
    		if(nullEmptyBlankJudge(status)){
    			status="0";
    		}
    		privilegeRole.setStatus(Integer.parseInt(status));
    		roleService.savePrivilegeRole(privilegeRole);
    		map.put("returnMsg", "1");
    	}
    	WebUtils.writeErrorJson(response, map);
    }
    
    /**
     * 删除资源
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "delete")
	public void delete(HttpServletRequest request,HttpServletResponse response) {
    	String id = request.getParameter("id");
    	Map<String,Object> map = new HashMap<String, Object>();
    	try {
    		if(nullEmptyBlankJudge(id)){
       		 map.put("returnMsg", "0");//不存在	
       		}else{
       			roleService.deletePrivilegeRole(Integer.parseInt(id));
           		map.put("returnMsg", "1");//修改成功	
       		}
			} catch (Exception e) {
				 map.put("returnMsg", "0");//不存在	
			}
    	
    		
    	WebUtils.writeErrorJson(response, map);
    }
	
//	/**
//	 * 跳转到添加用户的页面
//	 * @return 返回的是 jsp文件名路径及文件名
//	 */
//	@RequestMapping(value="showAdd")
//	public String showAdd(){
//		log.info("-------------------------showAdd         start------------------------------------");
//		return "show/add";
//	}
//	
	
}
