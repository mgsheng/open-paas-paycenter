package cn.com.open.pay.platform.manager.privilege.model;

import java.io.Serializable;


/**
 * 角色权限
 * @author dongminghao
 *
 */
public class PrivilegeRoleDetails implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	int roleId;
	int moduleId;
	String resources;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getModuleId() {
		return moduleId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}
	
}
