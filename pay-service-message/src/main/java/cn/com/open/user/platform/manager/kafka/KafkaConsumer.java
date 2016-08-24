package cn.com.open.user.platform.manager.kafka;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import net.sf.json.JSONObject;
import cn.com.open.user.platform.manager.dev.UserManagerDev;
import cn.com.open.user.platform.manager.user.model.UserAccountBalance;
import cn.com.open.user.platform.manager.user.service.UserAccountBalanceService;
public class KafkaConsumer extends Thread{  
  
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
             String message = new String(iterator.next().message());
             //保存账户表中
             //message: {"appId":1,"userId":80012553,"userName":"testsendpay11","type":"1","sourceId":"21292111111"}
            
             if(!nullEmptyBlankJudge(message)){
                 String balanceMsg=message.substring(9, message.length());
                 if(!nullEmptyBlankJudge(balanceMsg)){
                	 JSONObject reqjson = JSONObject.fromObject(balanceMsg);
                     String userId = reqjson.getString("userId");
                     String appId = reqjson.getString("appId");
                     String sourceId = reqjson.getString("sourceId");
                     String userName = reqjson.getString("userName");
                     String type = reqjson.getString("type");
                     UserAccountBalance  userAccountBalance=userAccountBalanceService.findByUserId(userId);
                     if(userAccountBalance==null){
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
                          userAccountBalanceService.saveUserAccountBalance(userAccountBalance); 
                     }
                 }
            	 
             }
             i++;
             System.out.println("接收到: " + message+","+"i="+i); 
         }  
    }  
  
    private ConsumerConnector createConsumer() {  
        Properties properties = new Properties();  
        properties.put("zookeeper.connect", "10.100.136.36:2181,10.100.136.37:2181,10.100.136.38:2181");//声明zk  
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