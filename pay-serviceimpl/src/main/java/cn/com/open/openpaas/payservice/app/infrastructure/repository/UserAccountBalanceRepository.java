package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;


/**
 * 
 */
public interface UserAccountBalanceRepository extends Repository {

	
	void saveUserAccountBalance(UserAccountBalance userAccountBalance);
	UserAccountBalance findByUserId(String userId);
	void updateBalanceInfo(UserAccountBalance userAccountBalance);
	UserAccountBalance getBalanceInfo(@Param("sourceId")String sourceId,@Param("appId")Integer appId);
}