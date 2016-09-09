package cn.com.open.pay.platform.manager.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;

/**
 * 模块管理基础类
 * @author admin
 *
 */
@Controller
@RequestMapping("/module/")
public class PrivilegeModuleController extends BaseControllerUtil{
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	 /**
     * 跳转模块管理页面
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "index")
	public String stats(HttpServletRequest request,HttpServletResponse response,Model model) {
    	List <PrivilegeResource> resourceList= privilegeResourceService.findAllResource();
    	List<Map<String,Object>> listMap = new LinkedList<Map<String,Object>>();
    	if(resourceList!=null && resourceList.size()>0){
    		Map<String,Object> map = null;
    		for(PrivilegeResource pr : resourceList){
    			map = new HashMap<String,Object>();
    			map.put("id", pr.getId()+"");
    			map.put("name", pr.getName());
    			listMap.add(map);
    		}
    	}
    	JSONArray json = new JSONArray();  
        json.addAll(listMap); 
        model.addAttribute("resourceList", json);
    	return "privilege/model/index";
    }
    @RequestMapping(value = "tree")
	public void getModelTree(HttpServletRequest request,HttpServletResponse response) {
		List<TreeNode> nodes = privilegeModuleService.getDepartmentTree();//见方法02
		 //buildTree(nodes)见方法01，return返回值自动转为json,不懂的话看下面紫色部分，懂的略过
		JSONArray jsonArr = JSONArray.fromObject(buildTree(nodes));
    	WebUtils.writeJson(response,jsonArr);
	}
	
	/**
	* 构建树
	* @param treeNodes
	* @return
	*/
	protected List<TreeNode> buildTree(List<TreeNode> treeNodes) {
		List<TreeNode> results = new ArrayList<TreeNode>();

		Map<String, TreeNode> aidMap = new LinkedHashMap<String, TreeNode>();
		for (TreeNode node : treeNodes) {
			aidMap.put(node.getId(), node);
		}
		treeNodes = null;

		Set<Entry<String, TreeNode>> entrySet = aidMap.entrySet();
		for (Entry<String, TreeNode> entry : entrySet) {
			String pid = entry.getValue().getPid();
			TreeNode node = aidMap.get(pid);
			if (node == null) {
				results.add(entry.getValue());
			} else {
				List<TreeNode> children = node.getChildren();
				if (children == null) {
					children = new ArrayList<TreeNode>();
					node.setChildren(children);
					node.setState("closed");
				}
				children.add(entry.getValue());
			}
		}
		aidMap = null;

		return results;
	}
    /**
     * 添加模块
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "add")
	public void add(HttpServletRequest request,HttpServletResponse response,PrivilegeModule privilegeModule) {
    	//返回
    		Map<String, Object> map = new LinkedHashMap<String, Object>();
    		String  id= request.getParameter("id");
	    		if(nullEmptyBlankJudge(id)){
	    			try {
	    				privilegeModule.setCreateTime(new Date());
	    				privilegeModuleService.savePrivilegeModule(privilegeModule);
	    				map.put("returnMsg", "1");
	    				
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    				map.put("returnMsg", "0");
	    			}	
	    		}else{
	    			privilegeModule.setCreateTime(new Date());
	    			privilegeModuleService.updatePrivilegeModule(privilegeModule);
	        		map.put("returnMsg", "2");
	    			
	    		}
    			
    			WebUtils.writeErrorJson(response, map);
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
     * 编辑模块
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "detail")
	public void detail(HttpServletRequest request,HttpServletResponse response,String id) {
    	//返回
  		Map<String, Object> map = new LinkedHashMap<String, Object>();
  		if(!nullEmptyBlankJudge(id)){
  			try {
  	  			PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(Integer.parseInt(id));
  	  			map.put("id", privilegeModule.getId());
  	  			map.put("parentId", privilegeModule.getParentId());
  	  			map.put("name", privilegeModule.getName());
  	  			map.put("url", privilegeModule.getUrl());
  	  			map.put("code", privilegeModule.getCode());
  	  			map.put("displayOrder", privilegeModule.getDisplayOrder());
  	  			map.put("status", privilegeModule.getStatus());
  	  			map.put("resources", privilegeModule.getResources());
  	  			PrivilegeModule parentModule = null;
  	  			if(privilegeModule.getParentId()!=0){
  	  				parentModule = privilegeModuleService.getModuleById(privilegeModule.getParentId());
  	  				map.put("parentName", parentModule.getName());
  	  			}
  	  		map.put("returnMsg", "1");
  	  		} catch (Exception e) {
  	  			e.printStackTrace();
  	  			map.put("id", 0);
  	  		map.put("returnMsg", "0");
  	  		}	
  		}else{
  			map.put("returnMsg", "0");
  		}
  		WebUtils.writeErrorJson(response, map);
    	
    }
    
  
    /**
     * 删除模块
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,String id) {
    	Map<String,Object> map = new HashMap<String, Object>();
    	try {
    		if(nullEmptyBlankJudge(id)){
       		 map.put("returnMsg", "0");//不存在	
       		}else{
       			privilegeModuleService.deletePrivilegeModule(Integer.parseInt(id));
           		map.put("returnMsg", "1");//修改成功	
       		}
			} catch (Exception e) {
				 map.put("returnMsg", "0");//不存在	
			}
    	WebUtils.writeErrorJson(response, map);
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
