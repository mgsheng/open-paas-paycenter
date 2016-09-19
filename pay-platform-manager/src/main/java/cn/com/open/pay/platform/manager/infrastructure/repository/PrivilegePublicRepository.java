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
	public boolean insertPublic(PrivilegePublic privilegePublic);
	public boolean deletePublic();
}