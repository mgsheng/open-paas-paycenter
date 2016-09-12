package cn.com.open.pay.platform.manager.privilege.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能：系统管理员
 * 
 */

public class Manager implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int TYPE_SUPERSYS = 1;	//超级管理员
	public static int TYPE_ADMIN = 2;		//管理员
	
    int type = TYPE_ADMIN;
    Date createTime;
    int role;
    int id;

    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
     
    
}