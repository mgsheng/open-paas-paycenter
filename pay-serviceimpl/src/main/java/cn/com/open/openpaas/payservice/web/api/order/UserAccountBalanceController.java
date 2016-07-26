package cn.com.open.openpaas.payservice.web.api.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.zookeeper.DistributedLock;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

@Controller
@RequestMapping("/user/balance/")
public class UserAccountBalanceController extends BaseControllerUtil{
	 @Autowired
	 private UserSerialRecordService userSerialRecordService;
	 @Autowired
	 private UserAccountBalanceService userAccountBalanceService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @RequestMapping("record")
	 public void record(HttpServletRequest request,HttpServletResponse response,Model model){
		String userId=request.getParameter("userId");
		String total_fee=request.getParameter("total_fee");//单位是分
		String appId=request.getParameter("appId");
		String userName=request.getParameter("userName");
		String out_trade_no=request.getParameter("out_trade_no");
		Map<String ,Object> map=new HashMap<String,Object>();
	    //流水记录
		UserSerialRecord userSerialRecord=new UserSerialRecord();
		if(!nullEmptyBlankJudge(total_fee)){
			userSerialRecord.setAmount(Double.parseDouble(total_fee)/100);	
		}
 	    if(!nullEmptyBlankJudge(appId)){
 	    	userSerialRecord.setAppId(Integer.parseInt(appId));
 	    }
 	    userSerialRecord.setSerialNo(out_trade_no);
 	    userSerialRecord.setSourceId(userId);
 	    userSerialRecord.setPayType(1);
 	    userSerialRecord.setCreateTime(new Date());
 	    userSerialRecord.setUserName(userName);
 	    userSerialRecordService.saveUserSerialRecord(userSerialRecord);
 	    //充值
	    UserAccountBalance  userAccountBalance=userAccountBalanceService.findByUserId(userId);
			if(userAccountBalance!=null){
				if(!nullEmptyBlankJudge(total_fee)){
					userAccountBalance.setBalance(Double.parseDouble(total_fee)/100);	
				}
				 DistributedLock lock = null;
              try {
        		  lock = new DistributedLock(payserviceDev.getZookeeper_config(),userAccountBalance.getSourceId()+userAccountBalance.getAppId());
        		  lock.lock();
        		  userAccountBalanceService.updateBalanceInfo(userAccountBalance);
        		    map.clear();
            		map.put("status","1");
            		writeSuccessJson(response,map);
    		     } catch (Exception e) {
    			// TODO Auto-generated catch block
	       			e.printStackTrace();
	       			map=paraMandaChkAndReturnMap(1, response,"必传参数中有空值");
	            	writeErrorJson(response,map);
	            	return ;
	       		  }finally{
	       			  lock.unlock(); 
	       		  }
			  }else{
				userAccountBalance=new UserAccountBalance();
				userAccountBalance.setUserId(userId);
				userAccountBalance.setStatus(1);
				userAccountBalance.setType(1);
				userAccountBalance.setCreateTime(new Date());
				userAccountBalanceService.saveUserAccountBalance(userAccountBalance);
				map.clear();
         		map.put("status","1");
         		writeSuccessJson(response,map);
			}
		  
	 }

}
