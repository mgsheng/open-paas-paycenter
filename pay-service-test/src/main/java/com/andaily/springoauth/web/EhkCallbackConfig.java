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

public class EhkCallbackConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	//对应商户网站的订单系统中的唯一订单号，非支付宝交易号。需保证在商户网站中的唯一性。是请求时对应的参数，原样返回
	public static String requestId = "20161009115311634158";
	//易汇金系统交易流水号
	public static String serialNumber = "201610091153116111111";
	//支付金额
    public static String orderAmount = "1";


		
	
}

