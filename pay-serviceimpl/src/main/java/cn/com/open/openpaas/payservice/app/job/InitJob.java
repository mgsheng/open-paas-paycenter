package cn.com.open.openpaas.payservice.app.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.com.open.openpaas.payservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.payservice.app.redis.service.RedisConstant;



/**
 * 项目启动执行任务
 * @author dongminghao
 *
 */
public class InitJob implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger log = Logger.getLogger(InitJob.class);
	
	//@Autowired
	//private UserCacheService userCacheService;
	@Autowired
	private RedisClientTemplate redisClient;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//判断是否为主容器，否则会执行2-3次
		if(event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")){
			log.info("项目启动完成");
			log.info("更新UserCache缓存 start...");
			/*List<UserCache> userCacheList = userCacheService.findAll();
			if(userCacheList!=null && userCacheList.size()>0){
				for(UserCache userCache : userCacheList){
					redisClient.setObject(RedisConstant.USER_CACHE_INFO+userCache.username(), "");
				}
			}*/
			log.info("更新UserCache缓存 end...");
		}
	}
}
