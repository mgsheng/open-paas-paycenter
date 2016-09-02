package cn.com.open.user.platform.manager.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.com.open.user.platform.manager.dev.UserManagerDev;
import cn.com.open.user.platform.manager.kafka.KafkaConsumer;
import cn.com.open.user.platform.manager.user.service.UserAccountBalanceService;

/**
 * 项目启动执行任务
 * @author dongminghao
 *
 */
public class InitJob implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger log = Logger.getLogger(InitJob.class);
	@Autowired
	private UserAccountBalanceService userAccountBalanceService;
	@Autowired
	private UserManagerDev userManagerDev;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")){
			System.out.println("~~~~~~~~~~~~~~~Kafka message service start~~~~~~~~~~~~~~~~");
		Thread thread = new Thread( new KafkaConsumer(userAccountBalanceService,userManagerDev));
		thread.run();
		}
	}
}
