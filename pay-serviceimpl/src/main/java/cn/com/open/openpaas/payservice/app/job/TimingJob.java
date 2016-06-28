package cn.com.open.openpaas.payservice.app.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.open.openpaas.payservice.app.log.AlipayControllerLog;
import cn.com.open.openpaas.payservice.app.log.service.LogMonitorService;
import cn.com.open.openpaas.payservice.app.thread.LogMonitorThread;
import cn.com.open.openpaas.payservice.app.tools.DateTools;


public class TimingJob {
	
	private static final Logger log = Logger.getLogger(TimingJob.class);
	
	@Autowired
	private  LogMonitorService logMonitorService;

	public void analyticalLog() {
		try {
			//获取日志文件
			Logger logger = LogManager.getLogger(AlipayControllerLog.class);
			DailyRollingFileAppender appender = (DailyRollingFileAppender)logger.getAppender("dailyRollingFileOauthControllerLog");
			//检查是否存在昨天的日志文件
			File logFile = new File(appender.getFile()+DateTools.getYesterDay(appender.getDatePattern()));
			if(!logFile.exists() || !logFile.isFile()){
				//如果不存在昨天日志，则读取当前日志
				//解释：如果没有新日志输出，则tomact不生成“昨天”的日志，如果有新日志输出，则会立刻生成“昨天”的日志
				//所以在没有新日志输出的情况下，“今天”的日志有可能是昨天，甚至前天以前的日志
				//需要判断他的最后修改日期是否是“昨天”范围内的（避免是前天以前的日志重复记录）
				logFile = new File(appender.getFile());
				if(logFile.exists() && logFile.isFile()){
					if(
						logFile.lastModified() >= DateTools.getYesterDayStartTime().getTime()
						&& logFile.lastModified() <= DateTools.getYesterDayEndTime().getTime()){
						//修改日期在昨天，符合条件
					}
					else{
						logFile = null;
					}
				}
			}
			if(logFile!=null && logFile.exists() && logFile.isFile()){
				//逐行读取并记录
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
					//临时变量
					String[] logArray = null;
					LogMonitorThread logMonitorThread;
					int index=0;
					int poolCount = 5;//线程池数量
					int queueCount = 10;//队列数量
					//线程池
					ExecutorService pool = Executors.newFixedThreadPool(poolCount);
					for (String line = br.readLine(); line != null; line = br.readLine()) {
						index++;
						logArray = line.split("#",-1);
//						日期#方法名#执行时间#用户名#密码#应用ID#执行status#错误号#
						if(logArray.length!=9){
							System.out.println("行"+index+",解析失败");
							continue;
						}
						logMonitorThread = new LogMonitorThread(logArray, index, logMonitorService);
						pool.execute(logMonitorThread);
			        	if(index%queueCount==0){
							//停止插入线程队列
							pool.shutdown();
							while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
								//等待线程池执行完毕
							}
							log.info("--接口日志已经解析"+index+"条");
							//生成新线程池
							pool = Executors.newFixedThreadPool(poolCount);
						}
					}
					log.info("--接口日志已经解析"+index+"条,完成");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
