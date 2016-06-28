package com.andaily.springoauth.web;

import java.util.Date;

import com.andaily.springoauth.tools.DateTools;

import net.sf.json.util.NewBeanInstanceStrategy;

/* *
 *类名：AlipayCallbackConfig
 *功能：基础配置类
 *详细：设置支付宝返回参数
 *修改日期：2016-05-04
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供测试使用。
 */

public class AlipayCallbackConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	//对应商户网站的订单系统中的唯一订单号，非支付宝交易号。需保证在商户网站中的唯一性。是请求时对应的参数，原样返回
	public static String out_trade_no = "test20160504153156";
	//商品的标题/交易标题/订单标题/订单关键字等。它在支付宝的交易明细中排在第一列，对于财务对账尤为重要。是请求时对应的参数，原样通知回来。
	public static String subject = "test1";
	// 取值范围请参见收款类型。
    public static String payment_type = "1";
	// 该交易在支付宝系统中的交易流水号。最长64位。
	public static String trade_no = "2016050421001004000243409664";
	// 取值范围请参见交易状态。
	public static String trade_status = "TRADE_SUCCESS";
	// 该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss。 
	public static Date gmt_create = DateTools.stringtoDate("2008-10-22 20:49:31", "yyyy-MM-dd HH:mm:ss");
	// 该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss。
	public static Date gmt_payment  = DateTools.stringtoDate("2008-10-22 20:49:50", "yyyy-MM-dd HH:mm:ss");
	// 交易关闭时间。格式为yyyy-MM-dd HH:mm:ss。
	public static Date gmt_close  = DateTools.stringtoDate("2008-10-22 20:49:46", "yyyy-MM-dd HH:mm:ss");
	// 取值范围请参见退款状态。
	public static String refund_status  = "REFUND_SUCCESS";
	//卖家退款的时间，退款通知时会发送。格式为yyyy-MM-dd HH:mm:ss。
	public static Date gmt_refund = DateTools.stringtoDate("2008-10-29 19:38:25", "yyyy-MM-dd HH:mm:ss");
	//卖家支付宝账号，可以是email和手机号码。
	public static String seller_email = "chao.chenc1@alipay.com";
	// 	买家支付宝账号，可以是Email或手机号码。
	public static String buyer_emai = "13758698870";
	//卖家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
	public static String seller_id = "2088801478647757";
	//买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。 
	public static String buyer_id = "2088412535212007";
	//如果请求时使用的是total_fee，那么price等于total_fee；如果请求时使用的是price，那么对应请求时的price参数，原样通知回来。
	public static String price = "10.00";
	// 	该笔订单的总金额。请求时对应的参数，原样通知回来。
	public static String total_fee  = "0.01";
	// 	如果请求时使用的是total_fee，那么quantity等于1；如果请求时使用的是quantity，那么对应请求时的quantity参数，原样通知回来。 
	public static String quantity = "1";
	//该笔订单的备注、描述、明细等。对应请求时的body参数，原样通知回来。 
	public static String body  = "sddsd";
	//支付宝系统会把discount的值加到交易金额上，如果需要折扣，本参数为负数。
	public static String discount = "-5";
	//该交易是否调整过价格。
	public static String is_total_fee_adjust = "N";
	//是否在交易过程中使用了红包。
	public static String use_coupo = "N";
	//用于商户回传参数，该值不能包含“=”、“&”等特殊字符。如果用户请求时传递了该参数，则返回给商户时会回传该参数。
	public static String extra_common_param  = "你好，这是测试商户的广告";
	//回传给商户此标识为qrpay时，表示对应交易为扫码支付。目前只有qrpay一种回传值。非扫码支付方式下，目前不会返回该参数。
	public static String business_scene = "qrpay";

		
	
}

