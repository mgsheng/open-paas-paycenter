package cn.com.open.openpaas.payservice.web.api.order;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.ditch.model.DictTradePaymentDitch;
import cn.com.open.openpaas.payservice.app.ditch.service.DictTradePaymentDitchService;
import cn.com.open.openpaas.payservice.app.payment.model.DictTradePayment;
import cn.com.open.openpaas.payservice.app.payment.service.DictTradePaymentService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

/**
 * 支付渠道查询  
 */
@Controller
@RequestMapping("/pay")
public class UserPayDitchController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserPayDitchController.class);
	
	
	 @Autowired
	 DictTradePaymentDitchService dictTradePaymentDitchService;

	 @Autowired
	 private PayserviceDev payserviceDev;
	 
		
	 /**getOrderQuery
	     * 商户查询汇银通
	     * @throws Exception 
	     */
	    @RequestMapping(value = "channelList")
	    public void channelList(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
	    	try {
		    	List<DictTradePaymentDitch> dictTradePaymentList = dictTradePaymentDitchService.findByAllDitch("1");
		    	if(dictTradePaymentList!=null){
			    	int dict = dictTradePaymentList.size();
			    	for(int i=0;i<dict;i++){
			    		DictTradePaymentDitch dictTradePayment = dictTradePaymentList.get(i);
			    		String icon = dictTradePayment.getIcon();
			    		dictTradePayment.setIcon(payserviceDev.getServer_host()+icon);
			    	}
		    	}
		  		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
		  		sParaTemp.put("channelList", dictTradePaymentList);
		  		sParaTemp.put("state", "ok");
		  		writeSuccessJson(response,sParaTemp);
	    	} catch (Exception e) {
				e.printStackTrace();
				SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
				sParaTemp.put("失败", "error");
				writeSuccessJson(response,sParaTemp);
			}
	    }
    
   
}