package cn.com.open.pay.platform.manager.privilege.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色
 * @author dongminghao
 *
 */
public class PrivilegeRole implements Serializable {
	
	private static final long serialVersionUID = 1L;
	int id;
	String name;								//角色名
	int status;									//启用禁用
	Date createTime;							//创建时间
	
	String statusName;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}
