package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegePublic;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;

/**
 * 公共权限
 * @author lvjq
 *
 */
public interface PrivilegePublicService {
	boolean updatePublic(PrivilegePublic privilegePublic);
	List<PrivilegePublic> findPublic();
	boolean insertPublic(PrivilegePublic privilegePublic);
	List<PrivilegePublic> QueryRoleDetails(PrivilegePublic privilegePublic);
	List<TreeNode> getDepartmentTree(
			List<PrivilegePublic> privilegeRoleDetailslist);
}