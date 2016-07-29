package cn.com.open.openpaas.payservice.app.log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.app.model.App;
import cn.com.open.openpaas.payservice.app.log.model.LogMonitor;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.HttpTools;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

import com.alibaba.fastjson.JSONObject;


/**
 * 输出日志类，用于统计各接口执行效率和结果，配置在log4j中的, dailyRollingFileUnifyPayControllerLog
 * @author dongminghao
 *
 */
public class UnifyPayControllerLog {
	
	private static final Logger logger = LoggerFactory.getLogger(UnifyPayControllerLog.class);

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
	public static void log(long startTime,PayServiceLog log, PayserviceDev payserviceDev){
		try {
			Throwable ex = new Throwable();
			StringBuffer msg = new StringBuffer();
		 	Map<String ,Object> map=new HashMap<String,Object>();
		 	long endTime = System.currentTimeMillis(); //获取结束时间
			//根据堆栈[1]获取到调用的方法名 0是自身
			if (ex.getStackTrace() != null && ex.getStackTrace()[1] != null) {
				//获取时间
				msg.append(DateTools.dateToString(new Date(), "yyyyMMddHHmmss")).append("#");
				//获取方法名
				msg.append(ex.getStackTrace()[1].getMethodName()).append("#");
				log.setServiceName(ex.getStackTrace()[1].getMethodName());
				log.setExecutionTime(endTime-startTime);
				//获取业务方唯一订单号
				msg.append(log.getMerchantOrderId()).append("#");
				//获取用户Id
				msg.append(log.getSourceUid()).append("#");
				//获取商户Id
				msg.append(log.getMerchantId()).append("#");
				//商品名称
				msg.append(log.getProductName()).append("#");
				//订单金额
				msg.append(log.getAmount()).append("#");
				//实收金额
				msg.append(log.getRealAmount()).append("#");
				//用户名
				msg.append(log.getUsername()).append("#");
				//支付渠道订单号
				msg.append(log.getPayOrderId()).append("#");
				//支付服务日志流水号或订单号
				msg.append(log.getOrderId()).append("#");
				//渠道号
				msg.append(log.getChannelId()).append("#");
				//支付方式
				msg.append(log.getPaymentId()).append("#");
				//商品描述
				msg.append(log.getProductDesc()).append("#");
				//日志类型
				msg.append(log.getLogType()).append("#");
				//应用id
				msg.append(log.getAppId()).append("#");
				//获取执行状态
				
				if(map!=null && map.get("status") !=null){
					log.setStatus((String) map.get("status"));
					msg.append(map.get("status")).append("#");
					//获取错误号status=0为错误
					if(map.get("status").toString().equals("error") && map.get("errorCode")!=null){
						msg.append(map.get("errorCode")).append("#");
						log.setErrorCode((String) map.get("errorCode"));
					}
					else{
						msg.append("#");
					}
				}
			}
			//logger.info(msg.toString()+"|"+JSONObject.toJSONString(log));
			Map <String,String>logMap=new HashMap<String,String>();
			logMap.put("tag", "payservice");
			logMap.put("logData", JSONObject.toJSONString(log));
			HttpTools.URLPost(payserviceDev.getKong_log_url(), logMap,"UTF-8");
			
			ex = null;
			msg = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}