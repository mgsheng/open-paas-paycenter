package cn.com.open.pay.platform.manager.department.service;

import java.util.List;

import cn.com.open.pay.platform.manager.department.model.Department;
/**
 * 部门管理
 * @author lvjq
 *
 */
public interface ManagerDepartmentService {
	
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	public boolean updateDept(Department department);
	
	/**
	 * 根据封装的Department对象中的属性查询符合条件的Department集合
	 * @return
	 */
	public List<Department> findDepts(Department department);
	
	/**
	 * 根据封装的Department对象中的属性添加Department对象
	 * @return
	 */
	public boolean addDept(Department department);
	
	/**
	 * 根据部门名查询Department对象
	 * @return
	 */
	public Department findByDeptName(String deptName);
	
	/**
	 * 根据部门id 删除Department对象
	 * @return
	 */
	public boolean removeDeptByID(Integer id);
}
