package cn.com.open.pay.platform.manager.department.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.service.ManagerDepartmentService;
import cn.com.open.pay.platform.manager.infrastructure.repository.ManagerDepartmentRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.UserRepository;
import cn.com.open.pay.platform.manager.login.model.User;
/**
 * 部门管理
 * @author lvjq
 *
 */
@Service("managerDepartmentService")
public class ManagerDepartmentServiceImp implements  ManagerDepartmentService{
	@Autowired
	private ManagerDepartmentRepository managerDepartmentRepository;
	@Autowired
	private UserRepository UserRepository;
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	@Override
	public boolean updateDept(Department department){
		try{
			managerDepartmentRepository.updateDept(department);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 根据封装的Department对象中的属性查询符合条件的Department集合
	 * @return
	 */
	@Override
	public List<Department> findDepts(Department department) {
		return managerDepartmentRepository.findDepts(department);
	}
	
	/**
	 * 根据封装的Department对象中的属性添加Department对象
	 * @return
	 */
	@Override
	public boolean addDept(Department department){
		try{
			managerDepartmentRepository.addDept(department);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 根据部门名查询Department对象
	 * @return
	 */
	@Override
	public Department findByDeptName(String deptName){
		return managerDepartmentRepository.findByDeptName(deptName);
	}
	
	/**
	 * 根据部门id 删除Department对象
	 * @return
	 */
	@Override
	public boolean removeDeptByID(Integer id){
		try{
			managerDepartmentRepository.removeDeptByID(id);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 根据User中的部门ID，部门名属性查询所有该部门的User对象
	 * @param user
	 * @return User集合
	 */
	@Override
	public List<User> findDeptUsers(User user) {
		return UserRepository.findDeptUsers(user);
	}
	
	/**
	 * 查询指定部门名的个数，没有指定则查询所有
	 * @param deptName
	 * @return
	 */
	@Override
	public int findQueryCount(Department deptName) {
		return managerDepartmentRepository.findQueryCount(deptName);
	}
}
