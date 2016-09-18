package cn.com.open.pay.platform.manager.privilege.model;

import java.io.Serializable;

/**
 * 公共权限
 * @author Administrator
 *
 */
public class PrivilegePublic implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;//主键（自增长）
	private Integer moduleId;//模块ID
	private String resources;//可操作资源(resources id 的字符串)

	public PrivilegePublic(){
		
	};
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + moduleId;
		result = prime * result
				+ ((resources == null) ? 0 : resources.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivilegePublic other = (PrivilegePublic) obj;
		if (id != other.id)
			return false;
		if (moduleId != other.moduleId)
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PrivilegePublic [id=" + id + ", moduleId=" + moduleId
				+ ", resources=" + resources + "]";
	}
	
}
