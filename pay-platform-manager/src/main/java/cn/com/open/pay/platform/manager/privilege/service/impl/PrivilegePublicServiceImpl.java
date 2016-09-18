package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeModuleRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegePublicRepository;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegePublic;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegePublicService;
/**
 * 公共权限
 * @author lvjq
 *
 */
@Service("privilegePublicService")
public class PrivilegePublicServiceImpl implements  PrivilegePublicService{
	@Autowired
	private PrivilegePublicRepository privilegePublicRepository;
	@Autowired
	private PrivilegeModuleRepository privilegeModuleRepository;
	
	@Override
	public List<PrivilegePublic> findPublic() {
		List<PrivilegePublic> publics = privilegePublicRepository.findPublic();
		return publics;
	}
	@Override
	public boolean updatePublic(PrivilegePublic privilegePublic) {
		try{
			privilegePublicRepository.updatePublic(privilegePublic);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean insertPublic(PrivilegePublic privilegePublic) {
		try{
			privilegePublicRepository.insertPublic(privilegePublic);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public List<TreeNode>  getDepartmentTree() {
		List<PrivilegeModule> modules = privilegeModuleRepository.findAllModules();//用于取出所有的部门对象的list集合
		return convertTreeNodeList(modules);
	}
	private List<TreeNode> convertTreeNodeList(List<PrivilegeModule> modules) {
			List<TreeNode> nodes = null;
			
			if(modules != null){
				nodes = new ArrayList<TreeNode>();
				for(PrivilegeModule module : modules){
					TreeNode node = convertTreeNode(module);
					if(node != null){
						nodes.add(node);
					}
				}
			}
			
			return nodes;
		}
	/**
	* @param departments
	* @return
	*/
	private TreeNode convertTreeNode(PrivilegeModule privilegeModule){
		TreeNode node = null;
		if(privilegeModule != null){
			node = new TreeNode();
			node.setId(String.valueOf(privilegeModule.getId()));//部门ID
//			node.setChecked(false);
			node.setText(privilegeModule.getName());//部门名称
//			node.setTarget("");
			node.setPid(String.valueOf(privilegeModule.getParentId()));//父级部门ID
			node.setResource(privilegeModule.getResources());
			Map<String,Object> map = new HashMap<String,Object>();
			node.setAttributes(map);
		}
		return node;
	}
	@Override
	public List<PrivilegePublic> QueryRoleDetails(
			PrivilegePublic privilegePublic) {
		
		return privilegePublicRepository.queryRoleDetails(privilegePublic);
	}
	@Override
	public List<TreeNode> getDepartmentTree(
		List<PrivilegePublic> privilegeRoleDetailslist) {
			List<PrivilegeModule> modules = privilegeModuleRepository.findAllModules();//用于取出所有的部门对象的list集合
			return convertTreeNodeList1(modules,privilegeRoleDetailslist);
	}
		
		private List<TreeNode> convertTreeNodeList1(List<PrivilegeModule> modules,List<PrivilegePublic> privilegeRoleDetailslist) {
			StringBuffer module=new StringBuffer();
			if(privilegeRoleDetailslist.size()!=0){
				
				for(int i=0;i<privilegeRoleDetailslist.size();i++){
					PrivilegePublic privilegeRoleDetails = privilegeRoleDetailslist.get(i);
					String resources = privilegeRoleDetails.getResources();
					int moduleId=0;
					if(resources==null){
						moduleId = privilegeRoleDetails.getModuleId();
					    PrivilegeModule privilegeModule = privilegeModuleRepository.findModuleById(moduleId);
					    int parentId = privilegeModule.getParentId();
					    if(parentId!=0){
					    	module.append(moduleId+",");
					    }
					}
						
						
				}
			}
			List<TreeNode> nodes = null;
			
			if(modules != null){
				nodes = new ArrayList<TreeNode>();
				for(PrivilegeModule department:modules){
					TreeNode node = convertTreeNode1(department,module.toString());
					if(node != null){
						nodes.add(node);
					}
				}
			}
			
			return nodes;
		}
		
		private TreeNode convertTreeNode1(PrivilegeModule privilegeModule,String module){
			String[] sourceStrArray = null;
			if(module!=""){
				sourceStrArray = module.split(",");
			}
			
			TreeNode node = null;
			if(privilegeModule != null){
				
				node = new TreeNode();
				node.setId(String.valueOf(privilegeModule.getId()));//部门ID
				node.setChecked(false);
				node.setText(privilegeModule.getName());//部门名称
				node.setTarget("");
				node.setPid(String.valueOf(privilegeModule.getParentId()));//父级部门ID
				node.setResource(privilegeModule.getResources());
				node.setIsmodule("0");
				for(int i=0;i<sourceStrArray.length;i++){
					String value = sourceStrArray[i];
					String id = privilegeModule.getId()+"";
					if(value.equals(id)){
						node.setChecked(true);
					}
				}
				Map<String,Object> map = new HashMap<String,Object>();
				node.setAttributes(map);
			}
			return node;
		}
}