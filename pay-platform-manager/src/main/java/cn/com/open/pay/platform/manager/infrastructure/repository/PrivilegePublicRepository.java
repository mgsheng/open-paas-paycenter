package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegePublic;
/**
 * 公共权限
 * @author lvjq
 *
 */
public interface PrivilegePublicRepository extends Repository {
	public List<PrivilegePublic> findPublic();
	public void updatePublic(PrivilegePublic privilegePublic);
	public void insertPublic(PrivilegePublic privilegePublic);
	public List<PrivilegePublic> queryRoleDetails(PrivilegePublic privilegePublic);
}