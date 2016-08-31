package cn.com.open.pay.order.service.thread;

import java.io.IOException;
import java.util.Map;

import cn.com.open.pay.order.service.dev.PayserviceDev;
import cn.com.open.pay.order.service.tools.HttpTools;
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