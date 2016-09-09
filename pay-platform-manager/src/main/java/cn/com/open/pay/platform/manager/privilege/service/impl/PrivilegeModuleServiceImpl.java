package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeModuleRepository;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
@Service("privilegeModuleService")
public class PrivilegeModuleServiceImpl implements PrivilegeModuleService {
    @Autowired
	private PrivilegeModuleRepository privilegeModuleRepository;
	@Override
	public List<PrivilegeModule> findByName(String name) {
		return privilegeModuleRepository.findByName(name);
	}
	@Override
	public void savePrivilegeModule(PrivilegeModule privilegeModule) {
		privilegeModuleRepository.savePrivilegeModule(privilegeModule);
	}
	@Override
	public void updatePrivilegeModule(PrivilegeModule privilegeModule) {
		// TODO Auto-generated method stub
		privilegeModuleRepository.updatePrivilegeModule(privilegeModule);
	}
	@Override
	public void deletePrivilegeModule(Integer id) {
		// TODO Auto-generated method stub
		privilegeModuleRepository.deletePrivilegeModule(id);
	}
	@Override
	public List<TreeNode>  getDepartmentTree() {
		List<PrivilegeModule> modules = privilegeModuleRepository.findAllModules();//用于取出所有的部门对象的list集合
		return convertTreeNodeList(modules);
	}
	private List<TreeNode> convertTreeNodeList(List<PrivilegeModule> modules) {
			List<TreeNode> nodes = null;
			
			if(modules != null){
				nodes = new ArrayList<TreeNode>();
				for(PrivilegeModule department:modules){
					TreeNode node = convertTreeNode(department);
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
			node.setChecked(false);
			node.setText(privilegeModule.getName());//部门名称
			node.setTarget("");
			node.setPid(String.valueOf(privilegeModule.getParentId()));//父级部门ID
			Map<String,Object> map = new HashMap<String,Object>();
			node.setAttributes(map);
		}
		return node;
	}
    
}