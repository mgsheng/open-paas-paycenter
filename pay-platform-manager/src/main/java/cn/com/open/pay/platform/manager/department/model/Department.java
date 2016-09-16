package cn.com.open.pay.platform.manager.department.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;
/**
 * 该类是部门的实体类
 * @author lvjq
 *
 */
public class Department extends AbstractDomain{
	private static final long serialVersionUID = 1L;
	private Integer id;//ID 
	private String deptName;//部门名称
	private Date createTime;//部门注册时间
	private String create_Time;//将时间类型转换成字符串
	public Department(){
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getCreate_Time() {
		setCreate_Time();
		return create_Time;
	}

	public void setCreate_Time() {
		if(createTime != null){
			String createtime = new SimpleDateFormat("yyyy-MM-dd").format(createTime);
			this.create_Time = createtime;
		}else{
			this.create_Time = "";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result
				+ ((deptName == null) ? 0 : deptName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (deptName == null) {
			if (other.deptName != null)
				return false;
		} else if (!deptName.equals(other.deptName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", deptName=" + deptName
				+ ", createTime=" + createTime + "]";
	}
	
}
