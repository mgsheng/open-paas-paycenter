package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.department.service.ManagerDepartmentService;
import cn.com.open.pay.platform.manager.department.service.MerchantInfoService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
/**
 * 商户管理
 * @author 
 *
 */
@Controller
@RequestMapping("/commercial/")
public class CommercialTenant extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private ManagerDepartmentService managerDepartmentService;
	
	@Autowired
	private MerchantInfoService merchantInfoService;
	
	
	/**
	 * 跳转到商户信息列表的页面
	 * @return
	 */
	@RequestMapping(value="commercial")
	public String commercial(){
		log.info("-------------------------departmentList       start------------------------------------");
		return "department/commercial";
	}
	
	
	/**
	 * 根据封装的Department对象中的属性查找所以符合条件的Department对象
	 * @return
	 */
	@RequestMapping(value="findCommercial")
	public void findCommercial(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------findDepts        start------------------------------------");
		String MERCHANT_NAME = request.getParameter("MERCHANT_NAME");
		String merchantName="";
		if(MERCHANT_NAME !=null ){
			merchantName = new String(MERCHANT_NAME.getBytes("iso-8859-1"),"utf-8");
		}
		String status = request.getParameter("STATUS");
		String operater = request.getParameter("OPERATER");
		String productName = request.getParameter("PRODUCT_NAME");
		String contact = request.getParameter("CONTACT");
		//当前第几页
		String page=request.getParameter("page");
		//每页显示的记录数
	    String rows=request.getParameter("rows"); 
		//当前页  
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		//每页显示条数  
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		//每页的开始记录  第一页为1  第二页为number +1   
	    int startRow = (currentPage-1)*pageSize;
		//将请求参数封装到Department对象中
	    MerchantInfo merchantInfo = new MerchantInfo();
	    merchantInfo.setMerchantName(merchantName);
	    if(status!=""&&status!=null){
	    	merchantInfo.setStatus(Double.parseDouble(status));
	    }
	    merchantInfo.setOperater(operater);
	    merchantInfo.setProductName(productName);
	    merchantInfo.setContact(contact);
	    merchantInfo.setPageSize(pageSize);
	    merchantInfo.setStartRow(startRow);
		List<MerchantInfo> departments = merchantInfoService.findDepts(merchantInfo);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		if(departments!=null){
			for(int i=0;i<departments.size();i++){
				MerchantInfo merchantInfo1= departments.get(i);
				Date createDate1 = merchantInfo1.getCreateDate();
				merchantInfo1.setFoundDate(df.format(createDate1));//交易时间
				Double status1 = merchantInfo1.getStatus();
				if(status1==1){
					merchantInfo1.setStatusName("正常");
				}else if(status1==2){
					merchantInfo1.setStatusName("冻结");
				}else{
					merchantInfo1.setStatusName("注销");
				}
			}
		}
		int count = merchantInfoService.findQueryCount(merchantInfo);
		System.out.println(count);
		JSONArray jsonArr = JSONArray.fromObject(departments);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
//		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}
	
	/**
	 * 添加部门
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="addMerchantInfo")
	public void addMerchantInfo(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------addDept        start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		String merchantName=new String(request.getParameter("merchantName").getBytes("iso-8859-1"),"utf-8");
		String status = request.getParameter("status");
		String notifyUrl = request.getParameter("notifyUrl");
		String returnUrl = request.getParameter("returnUrl");
		String productName = request.getParameter("productName");
		if(productName!=null){
			productName=new String(productName.getBytes("iso-8859-1"),"utf-8");
		}
		String contact = request.getParameter("contact");
		if(contact!=null){
			contact=new String(contact.getBytes("iso-8859-1"),"utf-8");
		}
		String phone = request.getParameter("phone");
		String mobile = request.getParameter("mobile");
		String email = request.getParameter("email");
		String dayNorm = request.getParameter("dayNorm");
		String monthNorm = request.getParameter("monthNorm");
		String singleNorm = request.getParameter("singleNorm");
		String memo = request.getParameter("memo");
		if(memo!=null){
			memo=new String(memo.getBytes("iso-8859-1"),"utf-8");
		}
		String operater =  request.getParameter("operater");
		if(operater!=null){
			operater=new String(operater.getBytes("iso-8859-1"),"utf-8");
		}
		String payKey = request.getParameter("payKey");
		JSONObject jsonObjArr = new JSONObject();  
		//判断数据库是否已经存在该商品
		boolean result = false;
//		Department dept_db = managerDepartmentService.findByDeptName(deptName);
//		if(dept_db != null){
//			// result = false表示该部门已被注册
//			jsonObjArr.put("result", result);
//		    WebUtils.writeJson(response,jsonObjArr);
//			return;
//		}
		//将参数封装到Department对象中
		Date createTime = new Date();
		MerchantInfo merchantInfo = new MerchantInfo();
		merchantInfo.setId(Integer.parseInt(id));
		merchantInfo.setMerchantName(merchantName);
		merchantInfo.setStatus(Double.parseDouble(status));
		merchantInfo.setCreateDate(createTime);
		merchantInfo.setOperater(operater); //操作员
		merchantInfo.setNotifyUrl(notifyUrl);
		merchantInfo.setReturnUrl(returnUrl);
		merchantInfo.setPayKey(payKey);//秘钥
		merchantInfo.setProductName(productName);
		merchantInfo.setContact(contact);
		merchantInfo.setPhone(phone);
		merchantInfo.setMobile(mobile);
		merchantInfo.setEmail(email);
		merchantInfo.setDayNorm(Double.parseDouble(dayNorm));
		merchantInfo.setMonthNorm(Double.parseDouble(monthNorm));
		merchantInfo.setSingleNorm(Double.parseDouble(singleNorm));
		merchantInfo.setMemo(memo);
		
		
		result = merchantInfoService.addMerchantInfo(merchantInfo);
		// result = true 表示添加成功
//		jsonObjArr.put("result", result);
		jsonObjArr.put("returnMsg", "1");
		WebUtils.writeJson(response,jsonObjArr);
	}
	
	/**
	 * 根据ID删除商户
	 * @return
	 */
	@RequestMapping(value="removeCommercialID")
	public void removeCommercialID(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------removeDeptByID       start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
//		System.out.println("id  :  "+id);
		// result = false表示删除失败
		boolean result = false;
		JSONObject jsonobj = new JSONObject();
		if(id != null && id !=""){
			result = merchantInfoService.removeCommercialID(Integer.valueOf(id));
		}
		jsonobj.put("result",result);
	    WebUtils.writeJson(response,jsonobj);
		return;
	}
	
	/**
	 * 根据ID修改商户信息
	 * @return
	 */
	@RequestMapping(value="updateMerchantInfo")
	public void updateMerchantInfo(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("-------------------------updateDeptByID       start------------------------------------");
//		request.setCharacterEncoding("GBK");
		String id = request.getParameter("id");
		String merchantName=new String(request.getParameter("merchantName").getBytes("iso-8859-1"),"utf-8");
		String status = request.getParameter("status");
		String notifyUrl = request.getParameter("notifyUrl");
		String returnUrl = request.getParameter("returnUrl");
		String productName = request.getParameter("productName");
		if(productName!=null){
			productName=new String(productName.getBytes("iso-8859-1"),"utf-8");
		}
		String contact = request.getParameter("contact");
		if(contact!=null){
			contact=new String(contact.getBytes("iso-8859-1"),"utf-8");
		}
		String phone = request.getParameter("phone");
		String mobile = request.getParameter("mobile");
		String email = request.getParameter("email");
		String dayNorm = request.getParameter("dayNorm");
		String monthNorm = request.getParameter("monthNorm");
		String singleNorm = request.getParameter("singleNorm");
		String memo = request.getParameter("memo");
		if(memo!=null){
			memo=new String(memo.getBytes("iso-8859-1"),"utf-8");
		}
		String operater =  request.getParameter("operater");
		if(operater!=null){
			operater=new String(operater.getBytes("iso-8859-1"),"utf-8");
		}
		
		String payKey = request.getParameter("payKey");
		JSONObject jsonobj = new JSONObject();
//		//判断数据库是否已经存在该部门
		boolean result = false;
//		Department dept_db = managerDepartmentService.findByDeptName(deptName);
//		if(dept_db != null){
//			// result = false表示该部门已被注册
//			jsonobj.put("result", result);
//		    WebUtils.writeJson(response,jsonobj);
//			return;
//		}
		//将参数封装到Department对象中
		Date createTime = new Date();
		MerchantInfo merchantInfo = new MerchantInfo();
		merchantInfo.setId(Integer.parseInt(id));
		merchantInfo.setMerchantName(merchantName);
		merchantInfo.setStatus(Double.parseDouble(status));
		merchantInfo.setCreateDate(createTime);
		merchantInfo.setOperater(operater); //操作员
		merchantInfo.setNotifyUrl(notifyUrl);
		merchantInfo.setReturnUrl(returnUrl);
		merchantInfo.setPayKey(payKey);//秘钥
		merchantInfo.setProductName(productName);
		merchantInfo.setContact(contact);
		merchantInfo.setPhone(phone);
		merchantInfo.setMobile(mobile);
		merchantInfo.setEmail(email);
		merchantInfo.setDayNorm(Double.parseDouble(dayNorm));
		merchantInfo.setMonthNorm(Double.parseDouble(monthNorm));
		merchantInfo.setSingleNorm(Double.parseDouble(singleNorm));
		merchantInfo.setMemo(memo);
		// result = false表示修改失败
		result = merchantInfoService.updateMerchantInfo(merchantInfo);
//		jsonobj.put("result",result);
		jsonobj.put("returnMsg", "2");
	    WebUtils.writeJson(response,jsonobj);
		return;
	}
	
	
	
	
	

	/**
	 * 跳转到所选部门的员工信息页面
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("toDeptUsers")
	public String  deptUsers(HttpServletRequest request,HttpServletResponse response,Model model)throws UnsupportedEncodingException{
		log.info("-------------------------toDeptUsers       start------------------------------------");
		String deptID = request.getParameter("id");
		String deptName = request.getParameter("deptName");
		System.out.println("deptID :"+deptID+"  deptName:"+deptName);
		deptID =( deptID==null?null:new String(deptID.getBytes("iso-8859-1"),"utf-8"));
		deptName =(deptName==null?null:new String(deptName.getBytes("iso-8859-1"),"utf-8"));

		model.addAttribute("deptID",deptID);
		model.addAttribute("deptName", deptName);
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
		String deptName =  request.getParameter("deptName");
		if(deptID !=null){
			deptID = new String(deptID.getBytes("iso-8859-1"),"utf-8");
		}
		if(deptName !=null){
			deptName = new String(deptName.getBytes("iso-8859-1"),"utf-8");
		}
		
//		System.out.println("******deptID :   "+deptID+"     deptName:"+deptName);
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
	    
		//将请求参数封装到Department对象中
		User user = new User();
		user.setDeptID(Integer.valueOf(deptID));
		user.setDeptName(deptName);
		user.setPageSize(pageSize);
		user.setStartRow(startRow);
		List<User> users = managerDepartmentService.findDeptUsers(user);
		if(users ==null) return;
		int count = managerDepartmentService.findDeptUsersCount(user);
		JSONArray jsonArr = JSONArray.fromObject(users);
		JSONObject jsonObjArr = new JSONObject();  
		jsonObjArr.put("total", count);
		jsonObjArr.put("rows", jsonArr);
//		System.out.println(jsonArr);
	    WebUtils.writeJson(response,jsonObjArr);
		return;
	}

	
}
