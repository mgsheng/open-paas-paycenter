package cn.com.open.user.platform.manager.web;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.open.user.platform.manager.dev.UserManagerDev;
import cn.com.open.user.platform.manager.kafka.KafkaConsumer;
import cn.com.open.user.platform.manager.user.service.UserAccountBalanceService; 
/**
 * 项目启动执行任务
 * @author dongminghao
 *
 */
public class InitJob  implements Job {
	
	private static final Logger log = Logger.getLogger(InitJob.class);
	@Autowired
	private UserAccountBalanceService userAccountBalanceService;
	@Autowired
	private UserManagerDev userManagerDev;
	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
	} 
	public void executeA() throws JobExecutionException {  
		  log.info("~~~~~~~~~~~~~~~Kafka message service start~~~~~~~~~~~~~~~~");
		  Thread thread = new Thread( new KafkaConsumer(userAccountBalanceService,userManagerDev));
  		   thread.run();
	 } 

}
