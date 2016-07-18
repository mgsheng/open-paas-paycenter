package cn.com.open.openpaas.payservice.web.api.order;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayConfig;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayController;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayCore;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayNotify;
import cn.com.open.openpaas.payservice.app.channel.alipay.MD5;
import cn.com.open.openpaas.payservice.app.log.AlipayControllerLog;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;


/**
 * 
 */
@Controller
@RequestMapping("/test/order/")
public class TestOrderCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(TestOrderCallbackController.class);
    private static final String PAY_KEY = "fc3db98a48a5434aaddbd2c75a7382ba";
    @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	
	/**
	 * 业务订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("callBack")
	public void testDirctPay(HttpServletRequest request,HttpServletResponse response) throws MalformedURLException, DocumentException, IOException {
	/*	MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId("test20160517","10026");
		String newId="";
		newId=SysUtil.careatePayOrderId();
		if(merchantOrderInfo!=null){
			//更新现有订单信息
			merchantOrderInfo.setId(newId);
			merchantOrderInfo.setCreateDate(new Date());
	     DistributedLock lock = null;
          try {
        	
    		  lock = new DistributedLock("10.100.136.36:2181,10.100.136.37:2181,10.100.136.38:2181","account1");
    		  lock.lock();
        	merchantOrderInfoService.updateOrderId(merchantOrderInfo);	
        	  System.out.println("sleep 5s:"+newId);
			Thread.sleep(10000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }finally{
			  lock.unlock(); 
		  }
		}*/
		//获取支付宝GET过来反馈信息
		long startTime = System.currentTimeMillis();
		String secret=request.getParameter("secret");
		SortedMap<Object,Object> params = new TreeMap<Object,Object>();
		params.put("orderId", request.getParameter("orderId"));
		params.put("outTradeNo", request.getParameter("outTradeNo"));
		params.put("merchantId", request.getParameter("merchantId"));
		params.put("paymentType", request.getParameter("paymentType"));
		params.put("paymentChannel", request.getParameter("paymentChannel"));
		params.put("feeType", request.getParameter("feeType"));
		params.put("guid", request.getParameter("guid"));
		params.put("appUid", request.getParameter("appUid"));
		params.put("timeEnd", request.getParameter("timeEnd"));
		params.put("totalFee", request.getParameter("totalFee"));
		params.put("goodsId", request.getParameter("goodsId"));
		params.put("goodsName", request.getParameter("goodsName"));
		params.put("goodsDesc", request.getParameter("goodsDesc"));
		params.put("parameter", request.getParameter("parameter"));
		params.put("rechargeMsg", request.getParameter("rechargeMsg"));
		 //除去数组中的空值和签名参数
        Map<String, Object> map=new HashMap<String, Object>();
        //生成签名结果
       if(getSignVeryfy(params,secret)){
    	   map.put("state", "ok");
       }else{
    	   map.put("state", "error");
    	   map.put("errorCode", "1");
    	   map.put("errorMsg", "验证错误");
       }
		WebUtils.writeErrorJson(response, map);
	  }

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	private static boolean getSignVeryfy(SortedMap<Object,Object> parameters, String sign) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v)&& !"null".equals(v) 
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + PAY_KEY);
        boolean isSign = false;
        String newsign = MD5.Md5(sb.toString()).toUpperCase();
        System.out.println("newsign="+newsign+"-----sign="+sign);
        if(sign.equals(newsign)){
        	isSign=true;
        }
        return isSign;
    }
}