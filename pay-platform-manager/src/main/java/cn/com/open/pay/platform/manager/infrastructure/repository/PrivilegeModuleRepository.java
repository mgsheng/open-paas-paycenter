package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;

/**
 * 
 */
public interface PrivilegeModuleRepository extends Repository {
	List<PrivilegeModule> findByName(@Param("name")String name);
	void savePrivilegeModule(PrivilegeModule privilegeModule);
    void updatePrivilegeModule(PrivilegeModule privilegeModule);
    void deletePrivilegeModule(Integer id);
    List<PrivilegeModule> findAllModules();
}