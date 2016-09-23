package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.department.model.Department;
/**
 * 部门管理
 * @author lvjq
 *
 */
public interface ManagerDepartmentRepository extends Repository {
	/**
	 * 查询所有部门
	 * @return
	 */
	public List<Department> findAllDepts();
	
	/**
	 * 查询指定部门名的个数，没有指定则查询所有
	 * @param deptName
	 * @return
	 */
	public int findQueryCount(Department deptName);
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	public void updateDept(Department department);
	
	/**
	 * 根据封装的Department对象中的属性查询符合条件的Department集合
	 * @return
	 */
	public List<Department> findDepts(Department department);
	
	/**
	 * 根据封装的Department对象中的属性添加Department对象
	 * @return
	 */
	public void addDept(Department department);
	
	/**
	 * 根据部门名查询Department对象
	 * @return
	 */
	public Department findByDeptName(String deptName);
	
	/**
	 * 根据部门id 删除Department对象
	 * @return
	 */
	public void removeDeptByID(Integer id);
}
