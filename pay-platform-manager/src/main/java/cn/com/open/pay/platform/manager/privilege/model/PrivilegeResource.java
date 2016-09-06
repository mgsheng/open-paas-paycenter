package cn.com.open.pay.platform.manager.privilege.model;


import java.io.Serializable;

/**
 * 资源
 * @author dongminghao
 *
 */
public class PrivilegeResource implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	String name;								//资源名
	String code;								//资源编号（用于标识功能按钮）
	int status = Common.STATUS_ON;									//启用禁用
	long createTime;
	
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}
