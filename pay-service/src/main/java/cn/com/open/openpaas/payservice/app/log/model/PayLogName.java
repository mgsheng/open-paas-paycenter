package cn.com.open.openpaas.payservice.app.log.model;

public interface PayLogName {
	//支付接口调用开始
	public static String PAY_START = "pay_start";
	//支付接口调用结束
	public static String PAY_END = "pay_end";
	//通知开始
	public static String NOTIFY_START = "notify_start";
	//通知结束
	public static String NOTIFY_END = "notify_end";
	//第三方回调开始
	public static String CALLBACK_START = "callback_start";
	//第三方回调结束
	public static String CALLBACK_END = "callback_end";
	
	//第三方回调开始
	public static String CALLBACK_NOTIFY_START = "callback_notify_start";
	//第三方回调结束
	public static String CALLBACK_NOTIFY_END = "callback_notify_end";
	//支付宝页面回调开始
	public static String ALIPAY_RETURN_START = "alipay_return_start";
	//支付宝页面回调结束
	public static String ALIPAY_RETURN_END = "alipay_return_end";
	
	//拉卡拉页面回调开始
	public static String PAYMAX_RETURN_START = "paymax_return_start";
	//拉卡拉页面回调结束
	public static String PAYMAX_RETURN_END = "paymax_return_end";
	//易宝页面回调开始
	public static String YEEPAY_RETURN_START = "paymax_return_start";
	//易宝页面回调结束
	public static String YEEPAY_RETURN_END = "paymax_return_end";
	//拉卡拉异步回调开始
	public static String PAYMAX_NOTIFY_START = "paymax_notify_start";
	//拉卡拉异步回调结束
	public static String PAYMAX_NOTIFY_END = "paymax_notify_end";
	//扣费开始
	public static String COSTS_START = "costs_start";
	//扣费结束
	public static String COSTS_END = "costs_end";
	//订单补发开始
	public static String ORDER_MANUAL_START = "order_manual_start";
	//订单补发结束
	public static String ORDER_MANUAL_END = "order_manual_end";
	//订单补发开始
	public static String ORDER_AUTO_START = "order_auto_start";
	//订单补发结束
	public static String ORDER_AUTO_END = "order_auto_end";
}
