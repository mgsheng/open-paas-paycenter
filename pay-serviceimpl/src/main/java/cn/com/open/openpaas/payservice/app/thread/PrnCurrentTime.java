package cn.com.open.openpaas.payservice.app.thread;

import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;

import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.service.AlipayTradeService;
public class PrnCurrentTime implements Runnable {
	
  private AlipayTradeService tradeService;
  private MerchantOrderInfo merchantOrderInfo;
  private MerchantOrderInfoService merchantOrderInfoService;
  public PrnCurrentTime( AlipayTradeService tradeService,MerchantOrderInfo merchantOrderInfo,MerchantOrderInfoService merchantOrderInfoService){
		this.tradeService = tradeService;
		this.merchantOrderInfo=merchantOrderInfo;
		this.merchantOrderInfoService=merchantOrderInfoService;
	}
	@Override
	public void run() {
		int i = 0;
		while (i < 6) {
			i++;
			  // (必填) 商户订单号，通过此商户订单号查询当面付的交易状态
	        AlipayF2FQueryResult result = tradeService.queryTradeResult(merchantOrderInfo.getMerchantOrderId());
			try {
				Thread.sleep(5000);// 设置5秒钟运行一次
			} catch (Exception e) {
				e.printStackTrace();
			}
			 if(result.getTradeStatus().toString().equals("SUCCESS")){
		        	merchantOrderInfoService.updatePayStatus(1,merchantOrderInfo.getId());
		        	break;
		     }
		}
	}

}