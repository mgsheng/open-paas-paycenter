package cn.com.open.pay.order.service.log;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.pay.order.service.dev.PayserviceDev;
import cn.com.open.pay.order.service.thread.SendLogToServerThread;

import com.alibaba.fastjson.JSONObject;


/**
 * 输出日志类，用于统计各接口执行效率和结果，配置在log4j中的, dailyRollingFileUnifyPayControllerLog
 * @author dongminghao
 *
 */
public class UnifyPayControllerLog {
	

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
		 	long endTime = System.currentTimeMillis(); //获取结束时间
			//根据堆栈[1]获取到调用的方法名 0是自身
			if (ex.getStackTrace() != null && ex.getStackTrace()[1] != null) {
				log.setServiceName(ex.getStackTrace()[1].getMethodName());
				log.setExecutionTime(endTime-startTime);
			}
			Map <String,String>logMap=new HashMap<String,String>();
			logMap.put("tag", "payservice");
			logMap.put("logData", JSONObject.toJSONString(log));
			try {
				Thread thread = new Thread(new SendLogToServerThread(logMap, payserviceDev));
				thread.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ex = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}