package cn.com.open.openpaas.payservice.app.thread;

import java.io.IOException;
import java.util.Map;

import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.HttpTools;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.service.AlipayTradeService;
public class SendLogToServerThread implements Runnable {
	
  private 	Map <String,String> logMap ;
  private PayserviceDev payserviceDev;
  public SendLogToServerThread( Map <String,String> logMap, PayserviceDev payserviceDev){
	 this.logMap=logMap;
	 this.payserviceDev=payserviceDev;
	}
	@Override
	public void run() {
		try {
			HttpTools.URLPost(payserviceDev.getKong_log_url(), logMap,"UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}