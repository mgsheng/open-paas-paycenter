package cn.com.open.pay.platform.manager.privilege.model;

import java.io.Serializable;

/**
 * 公共权限
 * @author Administrator
 *
 */
public class PrivilegePublic implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//主键（自增长）
	private int moduleId;//模块ID
	private String resources;//可操作资源

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getModule() {
		return moduleId;
	}
	public void setModule(int moduleId) {
		this.moduleId = moduleId;
	}
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}
	
}
