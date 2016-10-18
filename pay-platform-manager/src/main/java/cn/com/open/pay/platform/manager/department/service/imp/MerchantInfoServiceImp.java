package cn.com.open.pay.platform.manager.department.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.department.service.ManagerDepartmentService;
import cn.com.open.pay.platform.manager.department.service.MerchantInfoService;
import cn.com.open.pay.platform.manager.infrastructure.repository.ManagerDepartmentRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.MerchantInfoRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.UserRepository;
import cn.com.open.pay.platform.manager.login.model.User;
/**
 * 部门管理
 * @author 
 *
 */
@Service("MerchantInfoService")
public class MerchantInfoServiceImp implements  MerchantInfoService{
//	@Autowired
//	private ManagerDepartmentRepository managerDepartmentRepository;
	
	@Autowired
	private MerchantInfoRepository merchantInfoRepository;
	@Autowired
	private UserRepository UserRepository;
	
	
	/**
	 * 查询list
	 * @return
	 */
	@Override
	public List<MerchantInfo> findDepts(MerchantInfo merchantInfo) {
		return merchantInfoRepository.findDepts(merchantInfo);
	}
	
	/**
	 * 统计
	 * @param deptName
	 * @return
	 */
	@Override
	public int findQueryCount(MerchantInfo merchantInfo) {
		return merchantInfoRepository.findQueryCount(merchantInfo);
	}
	
	/**
	 * 添加
	 * @return
	 */
	@Override
	public boolean addMerchantInfo(MerchantInfo merchantInfo){
		try{
			merchantInfoRepository.insert(merchantInfo);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 根据商户id 删除
	 * @return
	 */
	@Override
	public boolean removeCommercialID(Integer id){
		try{
			merchantInfoRepository.removeCommercialID(id);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	@Override
	public boolean updateMerchantInfo(MerchantInfo merchantInfo){
		try{
			merchantInfoRepository.updateMerchantInfo(merchantInfo);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public MerchantInfo findNameById(Integer merchantId) {
		return merchantInfoRepository.findNameById(merchantId);
	}

	/**
	 * 查询所有的商户
	 * @return
	 */
	@Override
	public List<MerchantInfo> findMerchantNamesAll() {
		// TODO Auto-generated method stub
		return merchantInfoRepository.findMerchantNamesAll();
	}
	
	
	
	
}
