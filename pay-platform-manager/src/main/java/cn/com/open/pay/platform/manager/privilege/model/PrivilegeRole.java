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
	
	private Integer startRow;
	private Integer pageSize;
	String statusName;
	String create_Time;
	private int roleType;

	
	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

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

	public String getCreate_Time() {
		return create_Time;
	}

	public void setCreate_Time(String create_Time) {
		this.create_Time = create_Time;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
}
