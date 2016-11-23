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
			         String saveUserInfoUrl = reqjson.getString("saveUserInfoUrl");
			         boolean flag = false;
			         if(messageType != null){
			        	 if((ConstantMessage.MESSAGE_CREATE_PAYACCOUNT).equals(messageType)){//创建支付账户
			        		 String userId = reqjson.getString("userId");
					         String appId = reqjson.getString("appId");
					         String sourceId = reqjson.getString("sourceId");
					         // Double balance=0.0;
					         String userName = reqjson.getString("userName");
					         String type = reqjson.getString("type");
					         //int  status=1;
					         createPayAccount(appId,userId,sourceId,type,userName);
			        	 }else if((ConstantMessage.MESSAGE_SYNC_ACCOUNT).equals(messageType)){//同步旧平台账户
			        		 String appUid = reqjson.getString("appUid");
					         String id = reqjson.getString("id");
							 String guid = reqjson.getString("guid");
							 String clientId = reqjson.getString("client_id");
						     String sourceId = reqjson.getString("source_id");
							 String username = reqjson.getString("username");
							 String password = reqjson.getString("password");
							 String phone = reqjson.getString("phone");
							 String cardNo = reqjson.getString("card_no");
							 String email = reqjson.getString("email");
							 String methodName = reqjson.getString("methordName");
							 SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
							 sParaTemp.put("appUid", appUid);
							 sParaTemp.put("id", id);
							 sParaTemp.put("guid", guid);
							 sParaTemp.put("client_id", clientId);
					         sParaTemp.put("source_id", sourceId);
							 sParaTemp.put("username",username);
							 sParaTemp.put("password", password);
							 sParaTemp.put("phone",phone);
							 sParaTemp.put("card_no",cardNo);
							 sParaTemp.put("email", email);
							 sParaTemp.put("methordName", methodName);
							 sendPost(saveUserInfoUrl,sParaTemp);
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
          
    public void createPayAccount(String appId,String userId,String sourceId,String type,String userName){
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
    
    /** 
     * 发送POST请求 
     *  
     * @param url 
     *            目的地址 
     * @param parameters 
     *            请求参数，Map类型。 
     * @return 远程响应结果 
     */  
    public static String sendPost(String url, SortedMap<Object,Object> sParaTemp) {  
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null;  
        StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params="";
        try {  
		Set es = sParaTemp.entrySet();//所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v) 
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		  }
		   String temp_params = sb.toString(); 
		   params = temp_params.substring(0, temp_params.length() - 1);  
            // 创建URL对象  
            java.net.URL connURL = new java.net.URL(url);  
            // 打开URL连接  
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
                    .openConnection();  
            // 设置通用属性  
            httpConn.setRequestProperty("Accept", "*/*");  
            httpConn.setRequestProperty("Connection", "Keep-Alive");  
            httpConn.setRequestProperty("User-Agent",  
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  
            // 设置POST方式  
            httpConn.setDoInput(true);  
            httpConn.setDoOutput(true);  
            // 获取HttpURLConnection对象对应的输出流  
            out = new PrintWriter(httpConn.getOutputStream());  
            // 发送请求参数  
            //out.write(params); 
            out.write(params);  
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn  
                    .getInputStream(), "utf-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
                
            }  
            System.out.println(result);
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
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