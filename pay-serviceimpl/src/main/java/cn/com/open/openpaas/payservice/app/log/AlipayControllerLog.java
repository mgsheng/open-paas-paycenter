package cn.com.open.openpaas.payservice.app.log;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.app.model.App;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayCore;
import cn.com.open.openpaas.payservice.app.tools.DateTools;


/**
 * 输出日志类，用于统计各接口执行效率和结果，配置在log4j中的dailyRollingFileAlipayControllerLog
 * @author ws
 *
 */
public class AlipayControllerLog {
	
	private static final Logger logger = LoggerFactory.getLogger(AlipayControllerLog.class);

	/**
	 * 输出日志
	 * 格式：
	 * 日期#方法名#执行时间#用户名#密码#应用ID#执行status#错误号#
	 * @param startTime long startTime = System.currentTimeMillis();
	 * @param username
	 * @param password
	 * @param app
	 * @param map
	 */
	public static void log(long startTime,Map<String,String>params,Map<String,Object> map){
		try {
			Throwable ex = new Throwable();
			StringBuffer msg = new StringBuffer();
			long endTime = System.currentTimeMillis(); //获取结束时间
			//根据堆栈[1]获取到调用的方法名 0是自身
			if (ex.getStackTrace() != null && ex.getStackTrace()[1] != null) {
				//获取时间
				msg.append(DateTools.dateToString(new Date(), "yyyyMMddHHmmss")).append("#");
				//获取方法名
				msg.append(ex.getStackTrace()[1].getMethodName()).append("#");
				//获取执行时间
				msg.append(endTime-startTime).append("#");
				//获取结果返回的订单号
				msg.append(params.get("out_trade_no")).append("#");
				//获取结果返回的通知校验ID
				msg.append(params.get("total_fee")).append("#");
				//获取结果返回的交易状态 
				msg.append(params.get("trade_status")).append("#");
				//获取执行状态
				if(map!=null && map.get("status") !=null){
					msg.append(map.get("status")).append("#");
					//获取错误号status=0为错误
					if(map.get("status").toString().equals("ok") && map.get("errorCode")!=null){
						msg.append(map.get("errorCode")).append("#");
					}
					else{
						msg.append("#");
					}
				}
				if(map!=null && map.get("flag")!=null){
					if(map.get("flag").equals("false")){
						msg.append("0").append("#").append(map.get("error_code"));
					}else{
						msg.append("1").append("#").append("#");
					}
				}
			}
			logger.info(msg.toString());
			ex = null;
			msg = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}