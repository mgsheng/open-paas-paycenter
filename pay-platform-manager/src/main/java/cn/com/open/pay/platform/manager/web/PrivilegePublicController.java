package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import cn.com.open.pay.platform.manager.privilege.model.PrivilegePublic;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
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
	 * 将privilege_module,privilege_resource表中的数据根据关系按照TreeNode结构构建
	 * @param request
	 * @param response
	 */
	 @RequestMapping(value = "tree2")
		public void getModelTree2(HttpServletRequest request,HttpServletResponse response) {
		 
		 
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
		/**
	     * 修改权限，先将原数据全部删除再重新将前端获取的数据插入到 数据库表：privilege_public 中
	     * @param request
	     * @param model
	     * @param bool
	     * @return
	     * @throws UnsupportedEncodingException 
	     */
	    @RequestMapping(value = "updatePublic")
		public void updatePublic(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
	    	request.setCharacterEncoding("utf-8");
	    	String  roleTreeNodeIds= request.getParameter("temp");

	    	Map<String,Object> map = new HashMap<String, Object>();
	    	boolean result = false;
	    	result = privilegePublicService.deletePublic();
	    	
			if(roleTreeNodeIds!=null && !roleTreeNodeIds.equals("")){
				PrivilegePublic roleDetails=null;//公共权限实体
				String[] moduleRes=roleTreeNodeIds.split(",,,");//模块与模块拆分
				for(int i=0;i<moduleRes.length;i++){
					if(moduleRes[i]!=null && !moduleRes[i].equals("")){
						String[] mres=moduleRes[i].split(",,");//模块与资源拆分
						if(mres[0]!=null && !mres[0].equals("")){
							roleDetails=new PrivilegePublic();
							roleDetails.setModuleId(Integer.parseInt(mres[0]));
							if(mres.length>1 && mres[1]!=null && !mres[1].equals("")){
								roleDetails.setResources(mres[1].replace(",", ","));
							}
							result = privilegePublicService.insertPublic(roleDetails);
						}
					}
				}
			}
			map.put("result", result);
	    	WebUtils.writeErrorJson(response, map);
	    }
}
