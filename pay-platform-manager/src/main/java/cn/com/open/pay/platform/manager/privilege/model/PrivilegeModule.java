package cn.com.open.pay.platform.manager.privilege.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 模块
 * @author dongminghao
 *
 */
public class PrivilegeModule implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	String name;					//模块名
	String url;						//模块urla
	int level;						//级别
	int parentId;					//父模块id
	String parentIds;				//父模块ids
	int displayOrder;				//排序
	String code;					//Code
	int status;						//启用禁用
	String icon;					//图标（暂时无用）
	String resources;				//资源集合
	long createTime;				//创建时间
	
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
