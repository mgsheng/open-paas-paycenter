package cn.com.open.user.platform.manager.redis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.ShardedJedis;
import cn.com.open.user.platform.manager.redis.RedisDataSource;
import cn.com.open.user.platform.manager.tools.SerializeUtil;
@Repository("redisClientTemplate")
/**
 * redis 调用类
 * @author admin
 *
 */
public class RedisClientTemplate {
	  public static void main(String[] args) {
	        ApplicationContext ac =  new ClassPathXmlApplicationContext("classpath:/spring/context.xml");
	        RedisClientTemplate redisClient = (RedisClientTemplate)ac.getBean("redisClientTemplate");
	      //  redisClient.set("a", "abc");
	      //  AppUser a=new AppUser(1,2,"3");
//	        redisClient.set(a,"a");
//	        AppUser b=(AppUser) redisClient.get("a");
//	        List<AppUser> aa=new ArrayList<AppUser>();
//	        aa.add(a);
//	        redisClient.del("a");
//	        redisClient.del("key001");
//	        redisClient.setObject("a",aa);
	       // List<AppUser> bb=(List<AppUser>) redisClient.getObject("a");
//	        System.out.println(redisClient.getString("key001"));
	       // System.out.println(bb.get(0).sourceId());
	    }
	 private static final Logger log = LoggerFactory.getLogger(RedisClientTemplate.class);
	    @Autowired
	    private RedisDataSource     redisDataSource;
	    public void disconnect() {
	        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
	        shardedJedis.disconnect();
	    }
	    /**set Object*/
	      public String setObject(String key,Object object)
	     {
	    	     String result = null;
	    	     ShardedJedis shardedJedis = redisDataSource.getRedisClient();
	 	        if (shardedJedis == null) {
	 	            return result;
	 	        }
	 	       boolean broken = false;
	 	       try {
		            result =  shardedJedis.set(key.getBytes(), SerializeUtil.serialize(object));
		        } catch (Exception e) {
		            log.error(e.getMessage(), e);
		            broken = true;
		        } finally {
		            redisDataSource.returnResource(shardedJedis, broken);
		        }
	          
	            return result;
	     }
	      /**set Object*/
	      public String setObjectByTime(String key,Object object,int time)
	     {
	    	     String result = null;
	    	     ShardedJedis shardedJedis = redisDataSource.getRedisClient();
	 	        if (shardedJedis == null) {
	 	            return result;
	 	        }
	 	       boolean broken = false;
	 	       try {
	 	    	  shardedJedis.setex(key.getBytes(), time, SerializeUtil.serialize(object));
		        } catch (Exception e) {
		            log.error(e.getMessage(), e);
		            broken = true;
		        } finally {
		            redisDataSource.returnResource(shardedJedis, broken);
		        }
	          
	            return result;
	     }
	      /**get Object*/
	      public Object getObject(String key)
	     {
	    	  Object obj=null;
	          ShardedJedis shardedJedis = redisDataSource.getRedisClient();
	          if (shardedJedis == null) {
	 	            return obj;
	 	        }
	          boolean broken = false;
	          try {
	        	  byte[] value = shardedJedis.get(key.getBytes());
		            obj =  SerializeUtil.unserialize(value);

		        } catch (Exception e) {
		            log.error(e.getMessage(), e);
		            broken = true;
		        } finally {
		            redisDataSource.returnResource(shardedJedis, broken);
		        }
	            return obj;
	     }
	     
	      /**delete a key**/
	      public boolean del(String key)
	     {
	          ShardedJedis shardedJedis = redisDataSource.getRedisClient();
	            return shardedJedis.del(key)>0;
	     }


	    /**
	     * 设置单个值
	     * 
	     * @param key
	     * @param value
	     * @return
	     */
	    public String setString(String key, String value) {
	        String result = null;

	        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.set(key, value);
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            redisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }

	    /**
	     * 获取单个值
	     * 
	     * @param key
	     * @return
	     */
	    public String getString(String key) {
	        String result = null;
	        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.get(key);

	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            redisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	    }
}
