package cn.com.open.pay.platform.manager.department.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.department.model.DictTradePayment;
import cn.com.open.pay.platform.manager.department.service.DictTradeChannelService;
import cn.com.open.pay.platform.manager.department.service.DictTradePaymentService;
import cn.com.open.pay.platform.manager.department.service.ManagerDepartmentService;
import cn.com.open.pay.platform.manager.infrastructure.repository.DictTradeChannelRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.DictTradePaymentRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.ManagerDepartmentRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.UserRepository;
import cn.com.open.pay.platform.manager.login.model.User;
/**
 * 部门管理
 * @author 
 *
 */
@Service("DictTradePaymentService")
public class DictTradePaymentServiceImp implements  DictTradePaymentService{

	@Autowired
	private DictTradePaymentRepository dictTradePaymentRepository;
	
	/**
	 * 根据封装的Department对象中的属性查询符合条件的Department集合
	 * @return
	 */
	@Override
	public List<DictTradePayment> findPaymentNamesAll() {
		return dictTradePaymentRepository.findPaymentNamesAll();
	}
	
}
