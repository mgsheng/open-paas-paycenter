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
	//TCL回调开始
	public static String TCL_CALLBACK_START = "tcl_callback_start";
	//TCL回调结束
	public static String TCL_CALLBACK_END = "tcl_callback_end";
	//TCL回调开始
	public static String TCL_NOTIFY_START = "tcl_notify_start";
	//TCL回调结束
	public static String TCL_NOTIFY_END = "tcl_notify_end";
	
	//银联同步回调开始
	public static String UNION_CALLBACK_START = "union_callback_start";
	//银联同步回调结束
	public static String UNION_CALLBACK_END = "union_callback_end";
	//银联异步回调开始
	public static String UNION_NOTIFY_START = "union_notify_start";
	//银联异步回调结束
	public static String UNION_NOTIFY_END = "union_notify_end";
	
	//支付宝当面付回调开始
	public static String ALIFAF_NOTIFY_START = "alifaf_notify_start";
	//支付宝当面付回调结束
	public static String ALIFAF_NOTIFY_END = "alifaf_notify_end";
	//微信回调开始
	public static String WEIXIN_NOTIFY_START = "weixin_notify_start";
	//微信回调结束
	public static String WEIXIN_NOTIFY_END = "weixin_notify_end";
	
	//第三方回调开始
	public static String CALLBACK_NOTIFY_START = "callback_notify_start";
	//第三方回调结束
	public static String CALLBACK_NOTIFY_END = "callback_notify_end";
	//支付宝页面回调开始
	public static String ALIPAY_RETURN_START = "alipay_return_start";
	//支付宝页面回调结束
	public static String ALIPAY_RETURN_END = "alipay_return_end";
	//支付宝回调开始
	public static String ALIPAY_NOTIFY_START = "alipay_notify_start";
	//支付宝回调结束
	public static String ALIPAY_NOTIFY_END = "alipay_notify_end";
	//易宝回调开始
	public static String YEEPAY_NOTIFY_START = "yeepay_notify_start";
	//易宝回调结束
	public static String YEEPAY_NOTIFY_END = "yeepay_notify_end";
	//易宝回调结束
	public static String YEEPAY_CALLBACK_START = "yeepay_callback_start";
	//易宝回调结束
	public static String YEEPAY_CALLBACK_END = "yeepay_callback_end";
	
	//拉卡拉页面回调开始
	public static String PAYMAX_RETURN_START = "paymax_return_start";
	//拉卡拉页面回调结束
	public static String PAYMAX_WAP_RETURN_END = "paymax_wap_return_end";
	//拉卡拉页面回调开始
	public static String PAYMAX_WAP_RETURN_START = "paymax_wap_return_start";
	//拉卡拉页面回调结束
	public static String PAYMAX_RETURN_END = "paymax_return_end";
	//易宝页面回调开始
	public static String YEEPAY_RETURN_START = "yeepay_return_start";
	//易宝页面回调结束
	public static String YEEPAY_RETURN_END = "yeepay_return_end";
	//拉卡拉异步回调开始
	public static String PAYMAX_NOTIFY_START = "paymax_notify_start";
	//拉卡拉异步回调结束
	public static String PAYMAX_NOTIFY_END = "paymax_notify_end";
	
	
	//易宝扫码页面回调开始
	public static String EHK_RETURN_START = "ehk_return_start";
	//易宝扫码页面回调结束
	public static String EHK_RETURN_END = "ehk_return_end";
	//易宝扫码回调开始
	public static String EHK_NOTIFY_START = "ehk_notify_start";
	//易宝扫码回调结束
	public static String EHK_NOTIFY_END = "ehk_notify_end";
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
	//绑卡开始
	public static String BIND_CARD_START = "bind_card_start";
	//绑卡结束
	public static String BIND_CARD_END = "bind_card_end";
	
	//有短验绑卡请求短验确认
	public static String BIND_CARD_CONFIRM_START = "bind_card_confirm_start";
	//有短验绑卡请求短验确认
	public static String BIND_CARD_CONFIRM_END = "bind_card_confirm_end";
	//投资通绑卡超时通知开始
    public static String BIND_CARD_NOTIFY_START = "bind_card_notify_start";
	//投资通绑卡超时通知结束
	public static String BIND_CARD_NOTIFY_END = "bind_card_notify_end";
	//投资通绑卡通知业务方开始
    public static String BIND_CARD_CALLBACK_START = "bind_card_callback_start";
	//投资通通知业务方结束
	public static String BIND_CARD_CALLBACK_END = "bind_card_callback_end";
	//投资通无短充值超时通知开始
    public static String BIND_PAY_NOTIFY_START = "bind_pay_notify_start";
	//投资通无短充值超时通知结束
	public static String BIND_PAY_NOTIFY_END = "bind_pay_notify_end";
	//投资通无短充值通知业务方开始
	public static String BIND_PAY_CALLBACK_START = "bind_pay_callback_start";
    //投资通无短充值业务方结束
	public static String BIND_PAY_CALLBACK_END = "bind_pay_callback_end";	
	
	//换卡开始
	public static String CHANGE_CARD_START = "change_card_start";
	//换卡结束
	public static String CHANGE_CARD_END = "change_card_end";
	//解绑开始
	public static String UNBIND_CARD_START = "unbind_card_start";
	//投资通换卡超时通知开始
    public static String CHANGE_CARD_NOTIFY_START = "change_card_notify_start";
	//投资通换卡超时通知结束
	public static String CHANGE_CARD_NOTIFY_END = "change_card_notify_end";
	//解绑结束
	public static String UNBIND_CARD_END = "unbind_card_end";
	
	//第三方评分请求接口开始
	public static String THIRD_REQUEST_START = "third_request_start";
	//第三方评分请求接口结束
	public static String THIRD_REQUEST_END = "third_request_end";
	//第三方评分请求接口开始
	public static String BQS_REQUEST_START = "bqs_request_start";
    //第三方评分请求接口结束
	public static String BQS_REQUEST_END = "bqs_request_end";
	//自动还款开始
	public static String BIND_PAY_START = "bind_pay_start";
	//自动还款结束
	public static String BIND_PAY_END = "bind_pay_end";
	
	
	
	
}
