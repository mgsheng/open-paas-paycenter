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
}