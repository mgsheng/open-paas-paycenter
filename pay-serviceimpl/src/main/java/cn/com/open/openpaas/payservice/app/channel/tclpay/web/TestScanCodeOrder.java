package cn.com.open.openpaas.payservice.app.channel.tclpay.web;

import cn.com.open.openpaas.payservice.app.channel.tclpay.data.ScanCodeOrderData;
import cn.com.open.openpaas.payservice.app.channel.tclpay.service.ScanCodeOrderService;

/**
 * 名称：扫码订单支付测试类
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class TestScanCodeOrder {

	public static void main(String[] args) {
		ScanCodeOrderService scanCode = new ScanCodeOrderService();
		scanCode.order(ScanCodeOrderData.buildOrderDataMap(Thread
				.currentThread().getName()));
	}
}

