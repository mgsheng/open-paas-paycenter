package cn.com.open.user.platform.manager.kafka;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import net.sf.json.JSONObject;
import cn.com.open.user.platform.manager.constant.ConstantMessage;
import cn.com.open.user.platform.manager.dev.UserManagerDev;
import cn.com.open.user.platform.manager.user.model.UserAccountBalance;
import cn.com.open.user.platform.manager.user.service.UserAccountBalanceService;
public class KafkaConsumer extends Thread{  
	private static final Logger log = Logger.getLogger(KafkaConsumer.class);
    private UserAccountBalanceService userAccountBalanceService;
    private UserManagerDev userManagerDev; 
	
    public KafkaConsumer(UserAccountBalanceService userAccountBalanceService,UserManagerDev userManagerDev){  
        super();  
        this.userAccountBalanceService=userAccountBalanceService;
        this.userManagerDev=userManagerDev;
    }  
      
      
    @Override  
    public void run() {  
    	int i=0;
        ConsumerConnector consumer = createConsumer();  
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();  
        topicCountMap.put(userManagerDev.getKafka_topic(), 1); // 一次从主题中获取一个数据  
        Map<String, List<KafkaStream<byte[], byte[]>>>  messageStreams = consumer.createMessageStreams(topicCountMap);  
        KafkaStream<byte[], byte[]> stream = messageStreams.get(userManagerDev.getKafka_topic()).get(0);// 获取每次接收到的这个数据  
        ConsumerIterator<byte[], byte[]> iterator =  stream.iterator(); 
         
         while(iterator.hasNext()){  
             try {
				String message = new String(iterator.next().message());
				
			     log.info("Kafka consumer receive:message:"+message);
				 //保存账户表中
				 //message: {"appId":1,"userId":80012553,"userName":"testsendpay11","type":"1","sourceId":"21292111111"}
				
				 if(!nullEmptyBlankJudge(message)){
			    	 JSONObject reqjson = JSONObject.fromObject(message);
			         String messageType = reqjson.getString("messageType");//小主题类型
			         if(messageType != null){
			        	 if((ConstantMessage.MESSAGE_CREATE_PAYACCOUNT).equals(messageType)){//创建支付账户
			        		 String userId = reqjson.getString("userId");
					         String appId = reqjson.getString("appId");
					         String sourceId = reqjson.getString("sourceId");
					         // Double balance=0.0;
					         String userName = reqjson.getString("userName");
					         String type = reqjson.getString("type");
					         //int  status=1;
					         UserAccountBalance  userAccountBalance=userAccountBalanceService.findByUserId(userId);
					         if(userAccountBalance==null){
					        	 userAccountBalance=new UserAccountBalance();
					        	  if(!nullEmptyBlankJudge(appId)){
					             	 userAccountBalance.setAppId(Integer.parseInt(appId));	 
					                }
					              userAccountBalance.setUserId(userId);
					              userAccountBalance.setSourceId(sourceId);
					              if(!nullEmptyBlankJudge(type)){
					               userAccountBalance.setType(Integer.parseInt(type));	 
					              }
					              userAccountBalance.setUserName(userName);
					              userAccountBalance.setCreateTime(new Date());
					              //userAccountBalance.setBalance(balance); 
					             // userAccountBalance.setStatus(status);
					              userAccountBalanceService.saveUserAccountBalance(userAccountBalance); 
					         }
			        	 }
			         }
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
         }  
    }  
  
    private ConsumerConnector createConsumer() {  
        Properties properties = new Properties();  
        properties.put("zookeeper.connect", userManagerDev.getZookeeper_connect());//声明zk  
        properties.put("group.id", userManagerDev.getKafka_group());// 必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据  
        return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));  
    }  
    
    /**
	 * 检验字符串是否为空
	 * @param str
	 * @return
	 */
	 public boolean nullEmptyBlankJudge(String str){
	        return null==str||str.isEmpty()||"".equals(str.trim());
	  }  
    public static void main(String[] args) {  
       // new KafkaConsumer("test",).start();// 使用kafka集群中创建好的主题 test   
          
    }  
       
}  