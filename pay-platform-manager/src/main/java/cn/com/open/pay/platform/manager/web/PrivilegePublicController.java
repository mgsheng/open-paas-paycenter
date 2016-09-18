package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegePublic;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegePublicService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;
/**
 * 公共权限
 * @author lvjq
 *
 */
@Controller
@RequestMapping("/privilegePublic/")
public class PrivilegePublicController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private PrivilegePublicService privilegePublicService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	
	/**
	 * 跳转到公共权限的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request,HttpServletResponse response,Model model) {
		log.info("-------------------index----------------------");
		return "privilege/privilegePublic/index";
	}
	
	/**
	 * 将查询到的privilege_public 表中的数据，封装，返回到公共权限的页面
	 * @return 返回的是 jsp文件名路径及文件名
	 */
//	@RequestMapping(value = "indexjson",method=RequestMethod.POST)
	public void stats(HttpServletRequest request,HttpServletResponse response,Model model) {
		log.info("-------------------indexjson----------------------");
		List <PrivilegePublic> publicList = privilegePublicService.findPublic();
		
		if(publicList!=null && publicList.size()>0){
			List<Map<String,Object>> listMap = new LinkedList<Map<String,Object>>();
			JSONArray json = new JSONArray(); 
			Map<String,Object> map = null;
			
			for(PrivilegePublic pp : publicList){
				map = new HashMap<String,Object>();
				map.put("id", pp.getId()+"");
				map.put("moduleId", pp.getModuleId()+"");
				map.put("resources", pp.getResources()==null?"":pp.getResources());
				listMap.add(map);
			}
	        json.addAll(listMap); 
	        model.addAttribute("listMap", json);
	        WebUtils.writeJson(response,json);
		}
//    	return "privilege/privilegePublic/indexjson";
    }
	
	/**
	 * 将privilege_module,privilege_resource表中的数据根据关系按照TreeNode结构构建
	 * @param request
	 * @param response
	 */
//	@RequestMapping(value = "tree")
//	public void getModelTree(HttpServletRequest request,HttpServletResponse response) {
//		log.info("-------------------getModelTree----------------------");
//		List<TreeNode> nodes = privilegeModuleService.getDepartmentTree();//见方法02
//		 //buildTree(nodes)见方法01，return返回值自动转为json
//		List<PrivilegeResource> privilegeResourceList = privilegeResourceService.findAllResource();
//		JSONArray jsonArr = JSONArray.fromObject(buildTree(nodes,privilegeResourceList));
//    	WebUtils.writeJson(response,jsonArr);
//	}
    
	  @RequestMapping(value = "QueryRoleDetails")
		public void QueryRoleDetails(HttpServletRequest request,HttpServletResponse response,String id) {
	    	List<Map<String,String>> listMap = new LinkedList<Map<String,String>>();
	    	PrivilegePublic privilegeRoleDetails=new PrivilegePublic();
	    	privilegeRoleDetails.setModuleId(Integer.parseInt(id));
	    	List<PrivilegePublic> list = privilegePublicService.QueryRoleDetails(privilegeRoleDetails);
//	    	String id = request.getParameter("id");
	    	if(list!=null && list.size()>0){
				Map<String,String> map = null;
				for(PrivilegePublic roleDetail : list){
					map = new HashMap<String,String>();
					map.put("id", roleDetail.getId()+"");
					map.put("moduleId", roleDetail.getModuleId()+"");
					map.put("resources", roleDetail.getResources()==null?"":roleDetail.getResources());
					listMap.add(map);
				}
			}
			//返回
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("list", listMap);
//			JsonUtil.writeJson(response,JsonUtil.getContentData(map));
			JSONArray jsonArr = JSONArray.fromObject(map);
			WebUtils.writeJson(response,jsonArr);
			
		
	    }
	
	 @RequestMapping(value = "tree1")
		public void getModelTree1(HttpServletRequest request,HttpServletResponse response,String id) {
		 
		 System.out.println(id);
		 	PrivilegePublic privilegeRoleDetails=new PrivilegePublic();
	    	privilegeRoleDetails.setModuleId(Integer.valueOf(id));
	    	List<PrivilegePublic> privilegeRoleDetailslist = privilegePublicService.QueryRoleDetails(privilegeRoleDetails);
		 
			List<TreeNode> nodes = privilegePublicService.getDepartmentTree(privilegeRoleDetailslist);//见方法02
			 //buildTree(nodes)见方法01，return返回值自动转为json,不懂的话看下面紫色部分，懂的略过
			List<PrivilegeResource> privilegeResourceList = privilegeResourceService.findAllResource();
			JSONArray jsonArr = JSONArray.fromObject(buildTree1(nodes,privilegeResourceList,privilegeRoleDetailslist));
	    	WebUtils.writeJson(response,jsonArr);
		}
	/**
	 * 判断 privilege_public 表中是否有数据 
	 * @return
	 */
	public List<TreeNode> hasPublic(List<TreeNode> trees){
		log.info("-------------------hasPublic----------------------");
		List<PrivilegePublic> privilegePublics = privilegePublicService.findPublic();
		if(privilegePublics != null) {
			System.out.println("------------privilegePublics != null-----------");
			for(TreeNode tn : trees){
				System.out.println("------------for1----------");
				for(PrivilegePublic pp : privilegePublics){
					System.out.println("------------for2-----------");
					if(pp.getModuleId() == Integer.valueOf(tn.getPid())){
						System.out.println("------------pp.getModuleId() == Integer.valueOf(tn.getPid())----------");
						String rs = pp.getResources();
						if(rs !=null){
							System.out.println("------------rs !=null-----------");
							String[] rses= rs.split(",");
							for(int j = 0; j < rses.length; j++){
								tn.setChecked(true);
							}
						}
					}
				}
			}
		}	
		return trees;
	}
	
    /**
	* （json结构）构建树
	* @param treeNodes
	* @return
	*/
	
	protected List<TreeNode> buildTree1(List<TreeNode> treeNodes,List<PrivilegeResource> resourceList ,List<PrivilegePublic> privilegeRoleDetailslist ) {
		String[] sourceStrArray = null;
		String[] sourceStrArray1 = null;
		String[] sourceStrArray2 = null;
		StringBuffer module=new StringBuffer();
		StringBuffer moduleRole=new StringBuffer();
		if(privilegeRoleDetailslist!=null){
			for(int i=0;i<privilegeRoleDetailslist.size();i++){
				PrivilegePublic privilegeRoleDetails = privilegeRoleDetailslist.get(i);
				String resources = privilegeRoleDetails.getResources();
				int moduleId=0;
				String resourcesArry = null;
				if(resources!=null){
					moduleId = privilegeRoleDetails.getModuleId();
					resourcesArry = privilegeRoleDetails.getResources();
				    module.append(moduleId+",");
				    moduleRole.append(resourcesArry+",,");
				}
			}
		}
		
		
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
//				else{
					//添加三级
					String resource = entry.getValue().getResource();
					String idt = entry.getValue().getId();
					List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
					if(resource!=null){
						String[] resourceStrArray = resource.split(",");
						for(int i=0;i<resourceStrArray.length;i++){
							String vl = resourceStrArray[i];
							for(int j=0;j<resourceList.size();j++){
								int id = resourceList.get(j).getId();
								String nameValue = resourceList.get(j).getName();
								TreeNode treeNode=new TreeNode();
								if(Integer.parseInt(vl)==id){
									treeNode.setId(vl);
									treeNode.setText(nameValue);
//									treeNode.setState("closed");
									treeNode.setIsmodule("1");
									
									sourceStrArray = module.toString().split(",");
									for(int p=0;p<sourceStrArray.length;p++){
										String value = sourceStrArray[p];
										if(value.equals(idt)){
											sourceStrArray1  =	moduleRole.toString().split(",,");
											sourceStrArray2 = sourceStrArray1[p].split(",");
											for(int u=0;u<sourceStrArray2.length;u++){
												String vo = sourceStrArray2[u];
												if(vl.equals(vo)){
													treeNode.setChecked(true);
												}
											}
//											node.setChecked(true);
										}
									}
									
									treeNodeList.add(treeNode);
								}
								
							}
						}
					}
					
					entry.getValue().setChildren(treeNodeList);
//				}
				children.add(entry.getValue());
			}
		}
		aidMap = null;

		return results;
	}
	
	/**
	 * 根据情况将数据 更新或插入到 数据库表：privilege_public 中
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "updatePublic")
	public void updatePublic(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		log.info("-----------------------updatePublic---------------------------");
		request.setCharacterEncoding("utf-8");
		String[] moduleIds = request.getParameterValues("checkedId");
		
		
		PrivilegePublic privilegePublic = new PrivilegePublic();
		List<PrivilegePublic> rivelegePublics = privilegePublicService.findPublic();
		for(PrivilegePublic p : rivelegePublics){
			for(int i = 0;i < moduleIds.length;i++){
				if(p.getModuleId() == Integer.valueOf( moduleIds[i])){
					Integer id = p.getId();
//					String[] resource = //获取所有的节点   在选择所有节点的资源Id
//					privilegePublic.setId(id);
//					privilegePublic.setResources(resources);
					privilegePublic.setModuleId(p.getModuleId());
					privilegePublicService.updatePublic(privilegePublic);
				}else{
//					privilegePublic.setId(id);
//					privilegePublic.setResources(resources);
					privilegePublicService.updatePublic(privilegePublic);
				}
//				System.out.println(s.toString());
//				Integer moId = Integer.valueOf(s);
//				if(p.getModuleId() == moId){
//					privilegePublicService.updatePublic(privilegePublic);
//				}else{
//					privilegePublicService.updatePublic(privilegePublic);
//				}
			}
		}
	}
	
	
	 @RequestMapping(value = "tree2")
		public void getModelTree2(HttpServletRequest request,HttpServletResponse response,String id) {
		 
		 
		 	PrivilegeRoleDetails privilegeRoleDetails=new PrivilegeRoleDetails();
//	    	privilegeRoleDetails.setRoleId(Integer.parseInt(id));
//	    	List<PrivilegeRoleDetails> privilegeRoleDetailslist = privilegeRoleDetailsService.QueryRoleDetails(privilegeRoleDetails);
	    	List<PrivilegePublic> privilegePublicDetailslist = privilegePublicService.findPublic();
		 
			List<TreeNode> nodes = privilegeModuleService.getDepartmentTree2(privilegePublicDetailslist);//见方法02
			 //buildTree(nodes)见方法01，return返回值自动转为json,不懂的话看下面紫色部分，懂的略过
			List<PrivilegeResource> privilegeResourceList = privilegeResourceService.findAllResource();
			JSONArray jsonArr = JSONArray.fromObject(buildTree2(nodes,privilegeResourceList,privilegePublicDetailslist));
	    	WebUtils.writeJson(response,jsonArr);
		}
	    
	    
	    /**
		* 构建树
		* @param treeNodes
		* @return
		*/
		protected List<TreeNode> buildTree2(List<TreeNode> treeNodes,List<PrivilegeResource> resourceList ,List<PrivilegePublic> privilegeRoleDetailslist ) {
			String[] sourceStrArray = null;
			String[] sourceStrArray1 = null;
			String[] sourceStrArray2 = null;
			StringBuffer module=new StringBuffer();
			StringBuffer moduleRole=new StringBuffer();
			if(privilegeRoleDetailslist!=null){
				for(int i=0;i<privilegeRoleDetailslist.size();i++){
					PrivilegePublic privilegeRoleDetails = privilegeRoleDetailslist.get(i);
					String resources = privilegeRoleDetails.getResources();
					int moduleId=0;
					String resourcesArry = null;
					if(resources!=null){
						moduleId = privilegeRoleDetails.getModuleId();
						resourcesArry = privilegeRoleDetails.getResources();
					    module.append(moduleId+",");
					    moduleRole.append(resourcesArry+",,");
					}
				}
			}
			
			
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
//					else{
						//添加三级
						String resource = entry.getValue().getResource();
						String idt = entry.getValue().getId();
						List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
						if(resource!=null){
							String[] resourceStrArray = resource.split(",");
							for(int i=0;i<resourceStrArray.length;i++){
								String vl = resourceStrArray[i];
								for(int j=0;j<resourceList.size();j++){
									int id = resourceList.get(j).getId();
									String nameValue = resourceList.get(j).getName();
									TreeNode treeNode=new TreeNode();
									if(Integer.parseInt(vl)==id){
										treeNode.setId(vl);
										treeNode.setText(nameValue);
//										treeNode.setState("closed");
										treeNode.setIsmodule("1");
										
										sourceStrArray = module.toString().split(",");
										for(int p=0;p<sourceStrArray.length;p++){
											String value = sourceStrArray[p];
											if(value.equals(idt)){
												sourceStrArray1  =	moduleRole.toString().split(",,");
												sourceStrArray2 = sourceStrArray1[p].split(",");
												for(int u=0;u<sourceStrArray2.length;u++){
													String vo = sourceStrArray2[u];
													if(vl.equals(vo)){
														treeNode.setChecked(true);
													}
												}
//												node.setChecked(true);
											}
										}
										
										treeNodeList.add(treeNode);
									}
									
								}
							}
						}
						
						entry.getValue().setChildren(treeNodeList);
//					}
					children.add(entry.getValue());
				}
			}
			aidMap = null;

			return results;
		}
	
}
