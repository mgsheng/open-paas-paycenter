package cn.com.open.pay.platform.manager.department.service;

import java.util.List;


import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
/**
 * 商户管理
 * @author 
 *
 */
public interface MerchantInfoService {
	
	
	
	/**
	 * 查询所有商户
	 * @return
	 */
	public List<MerchantInfo> findMerchantNamesAll();
	
	/**
	 * 查询list
	 * @return
	 */
	public List<MerchantInfo> findDepts(MerchantInfo merchantInfo);
	
	/**
	 * 统计
	 * @param deptName
	 * @return
	 */
	public int findQueryCount(MerchantInfo merchantInfo);
	
	/**
	 * 添加
	 * @return
	 */
	public boolean addMerchantInfo(MerchantInfo merchantInfo);
	
	/**
	 * 根据商户id 删除
	 * @return
	 */
	public boolean removeCommercialID(Integer id);
	
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	public boolean updateMerchantInfo(MerchantInfo merchantInfo);

	public MerchantInfo findNameById(Integer merchantId);
	
	MerchantInfo findById(Integer merchantId);
	
}
