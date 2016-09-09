package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;

/**
 * 
 */
public interface PrivilegeResourceRepository extends Repository {
	
	List<PrivilegeResource> findByName(@Param("name")String name);
	PrivilegeResource  findById(@Param("id")Integer id);
	List<PrivilegeResource> getPageListByName(PrivilegeResource privilegeResource);
	void savePrivilegeResource(PrivilegeResource privilegeResource);
    void updatePrivilegeResource(PrivilegeResource privilegeResource);
    void deletePrivilegeResource(Integer id);
    int findQueryCount(@Param("name")String name);
    List<PrivilegeResource> findAllResource();
}