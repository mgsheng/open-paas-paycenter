package cn.com.open.openpaas.payservice.app.log;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.app.model.App;
import cn.com.open.openpaas.payservice.app.tools.DateTools;


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
	 * @param outTradeNo long startTime = System.currentTimeMillis();
	 * @param username
	 * @param password
	 * @param goodsName
	 * @param totalFee
	 * @param map 
	 */
	public static void log(String outTradeNo,String userId,String merchantId,String goodsName, String totalFee, Map<String, Object> map){
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
				//获取业务方唯一订单号
				msg.append(outTradeNo).append("#");
				//获取用户Id
				msg.append(userId).append("#");
				//获取商户Id
				msg.append(merchantId).append("#");
				//商品名称
				msg.append(goodsName).append("#");
				//订单金额
				msg.append(totalFee).append("#");
				//获取执行状态
				if(map!=null && map.get("status") !=null){
					msg.append(map.get("status")).append("#");
					//获取错误号status=error为错误
					if(map.get("status").toString().equals("error") && map.get("error_code")!=null){
						msg.append(map.get("error_code")).append("#");
					}
					else{
						msg.append("#");
					}
				}
				/*if(map!=null && map.get("flag")!=null){
					if(totalFee.get("flag").equals("false")){
						msg.append("0").append("#").append(totalFee.get("error_code"));
					}else{
						msg.append("1").append("#").append("#");
					}
				}*/
			}
			logger.info(msg.toString());
			ex = null;
			msg = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}