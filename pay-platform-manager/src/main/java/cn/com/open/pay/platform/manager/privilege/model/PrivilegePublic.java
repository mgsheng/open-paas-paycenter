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
	private int id;
	int module;
	String resources;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getModule() {
		return module;
	}
	public void setModule(int module) {
		this.module = module;
	}
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}
	
}
