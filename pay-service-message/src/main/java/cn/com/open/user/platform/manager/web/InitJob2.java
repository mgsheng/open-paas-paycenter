package cn.com.open.user.platform.manager.web;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.open.user.platform.manager.dev.UserManagerDev;
import cn.com.open.user.platform.manager.kafka.KafkaConsumer;
import cn.com.open.user.platform.manager.user.service.UserAccountBalanceService;
/**
 * 项目启动执行任务
 * @author dongminghao
 *
 */
public class InitJob2 extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(InitJob2.class);
	@Autowired
	private UserAccountBalanceService userAccountBalanceService;
	@Autowired
	private UserManagerDev userManagerDev;
	
	   // Servlet的init方法会在Tomcat启动的时候执行  
    @Override  
    public void init() throws ServletException {  
      FutureTask<String> task = new FutureTask<String>(new Callable<String>(){  
           @Override  
           public String call() throws Exception { 
        	   Thread thread = new Thread( new KafkaConsumer(userAccountBalanceService,userManagerDev));
       		   thread.run();
              return "Collection Completed";  
           }  
      });  
      new Thread(task).start();;   
    } 

}
