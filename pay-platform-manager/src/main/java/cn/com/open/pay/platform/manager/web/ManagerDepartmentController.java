package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
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

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.service.ManagerDepartmentService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
/**
 * 部门管理
 * @author lvjq
 *
 */
@Controller
@RequestMapping("/department/")
public class ManagerDepartmentController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private ManagerDepartmentService managerDepartmentService;
	
	/**
	 * 跳转到所选部门的员工信息页面
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("deptUsers")
	public String  deptUsers(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------deptUsers       start------------------------------------");
		request.setCharacterEncoding("utf-8");
		Integer id = Integer.valueOf(request.getParameter("id"));
		System.out.println(id);
		
		return "department/deptUsers";
	}
	
	/**
	 * 根据封装的User对象中的属性查找所以所选部门的User对象集合
	 * @return
	 */
	@RequestMapping(value="findDeptUsers")
	public void findDeptUsers(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------findDeptUsers        start------------------------------------");
		
		String deptID = request.getParameter("deptID");
		if(deptID !=null){
			deptID = new String(deptID.getBytes("iso-8859-1"),"utf-8");
		}
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
	    
		//将请求参数封装到Department对象中
		User user = new User();
		user.setDeptID(Integer.valueOf(deptID));
		user.setPageSize(pageSize);
		user.setStartRow(startRow);
		List<User> users = managerDepartmentService.findDeptUsers(user);
		if(users ==null) return;
		int count = users.size();
		JSONArray jsonArr = JSONArray.fromObject(users);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
//		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
	
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	@RequestMapping(value="updateDept")
	public void updateDept(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------updateDeptByID       start------------------------------------");
		request.setCharacterEncoding("utf-8");
		Integer id = Integer.valueOf(request.getParameter("id"));
		String deptName = new String(request.getParameter("dept_name").getBytes("iso8859-1"),"utf-8");
//		System.out.println(deptName);
		JSONObject jsonobj = new JSONObject();
		//判断数据库是否已经存在该部门
		boolean result = false;
		Department dept_db = managerDepartmentService.findByDeptName(deptName);
		if(dept_db != null){
			// result = false表示该部门已被注册
			jsonobj.put("result", result);
		    WebUtils.writeJson(response,jsonobj);
			return;
		}
		//将参数封装到Department对象中
		Department department = new Department();
		department.setId(id);
		department.setDeptName(deptName);
		// result = false表示修改失败
		result = managerDepartmentService.updateDept(department);
		jsonobj.put("result",result);
	    WebUtils.writeJson(response,jsonobj);
		return;
	}
	
	
	/**
	 * 根据ID删除部门
	 * @return
	 */
	@RequestMapping(value="removeDeptByID")
	public void removeDeptByID(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------removeDeptByID       start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		System.out.println("id  :  "+id);
		// result = false表示删除失败
		boolean result = false;
		JSONObject jsonobj = new JSONObject();
		if(id != null && id !=""){
			result = managerDepartmentService.removeDeptByID(Integer.valueOf(id));
		}
		jsonobj.put("result",result);
	    WebUtils.writeJson(response,jsonobj);
		return;
	}
	
	
	/**
	 * 添加部门
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="addDept")
	public void addDept(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------addDept        start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String deptName = request.getParameter("deptname");
		JSONObject jsonObjArr = new JSONObject();  
		//判断数据库是否已经存在该部门
		boolean result = false;
		Department dept_db = managerDepartmentService.findByDeptName(deptName);
		if(dept_db != null){
			// result = false表示该部门已被注册
			jsonObjArr.put("result", result);
		    WebUtils.writeJson(response,jsonObjArr);
			return;
		}
		//将参数封装到Department对象中
		Date createTime = new Date();
		Department department = new Department();
		department.setDeptName(deptName);
		department.setCreateTime(createTime);
		
		result = managerDepartmentService.addDept(department);
		// result = true 表示添加成功
		jsonObjArr.put("result", result);
		WebUtils.writeJson(response,jsonObjArr);
	}
	/**
	 * 跳转到部门信息列表的页面
	 * @return
	 */
	@RequestMapping(value="departmentList")
	public String departmentList(){
		log.info("-------------------------departmentList       start------------------------------------");
		return "department/departmentList";
	}
	
	/**
	 * 根据封装的Department对象中的属性查找所以符合条件的Department对象
	 * @return
	 */
	@RequestMapping(value="findDepts")
	public void findDepts(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------findDepts        start------------------------------------");
		String deptName = request.getParameter("dept_name");
		
		if(deptName !=null ){
			deptName = new String(deptName.getBytes("iso-8859-1"),"utf-8");
		}
		System.out.println(deptName);
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
	    
		//将请求参数封装到Department对象中
		Department department = new Department();
		department.setDeptName(deptName);
		department.setPageSize(pageSize);
		department.setStartRow(startRow);
		List<Department> departments = managerDepartmentService.findDepts(department);
		int count = managerDepartmentService.findQueryCount(department);
		System.out.println(count);
		JSONArray jsonArr = JSONArray.fromObject(departments);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
//		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
	
}
