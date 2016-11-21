package cn.com.open.user.platform.manager.kafka;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import net.sf.json.JSONObject;
import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.domain.model.UserCenterRegDto;
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.user.platform.manager.constant.ConstantMessage;
import cn.com.open.user.platform.manager.dev.UserManagerDev;
import cn.com.open.user.platform.manager.redis.impl.RedisClientTemplate;
import cn.com.open.user.platform.manager.redis.impl.RedisConstant;
import cn.com.open.user.platform.manager.user.model.UserAccountBalance;
import cn.com.open.user.platform.manager.user.service.UserAccountBalanceService;
public class KafkaConsumer extends Thread{  
	private static final Logger log = Logger.getLogger(KafkaConsumer.class);
    private UserAccountBalanceService userAccountBalanceService;
    private UserManagerDev userManagerDev; 
	@Autowired
	private UserService userService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AppService appService; 
	@Autowired
	private UserserviceDev userserviceDev;
	@Autowired
	private RedisClientTemplate redisClient;
	
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
			         App app=new App();
			         UserCenterRegDto userCenterReg=new UserCenterRegDto();
			         String messageType = reqjson.getString("messageType");//小主题类型
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
			        	 }else{
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
							 userCenterReg.setAppUid(appUid);
							 userCenterReg.setId(id);
							 userCenterReg.setGuid(guid);
							 userCenterReg.setClient_id(clientId);
							 userCenterReg.setSource_id(sourceId);
							 userCenterReg.setUsername(username);
							 userCenterReg.setPassword(password);
							 userCenterReg.setPhone(phone);
							 userCenterReg.setCard_no(cardNo);
							 userCenterReg.setEmail(email);
							 userCenterReg.setMethordName(methodName);
							 app = (App) redisClient.getObject(RedisConstant.APP_INFO+userCenterReg.getClient_id());
					         if(app==null)
							 {
								 app=appService.findIdByClientId(userCenterReg.getClient_id());
								 redisClient.setObject(RedisConstant.APP_INFO+userCenterReg.getClient_id(), app);
							 }
					         if((ConstantMessage.MESSAGE_CREATE_ACCOUNT).equals(messageType)){//同步创建新用户平台账户
				        		 flag = registerUser(app,userCenterReg);
				        		 if(flag){
				        			 User user = userService.findByGuid(guid);
							         createPayAccount(user.getAppId()+"",id,sourceId,user.getUserType()+"",username);
				        		 }
				        	 }else if((ConstantMessage.MESSAGE_SYNC_ACCOUNT).equals(messageType)){//同步旧版用户数据
				        		 flag = sysUserInfo(app,userCenterReg);
				        	 }else if((ConstantMessage.MESSAGE_BIND_ACCOUNT).equals(messageType)){//绑定用户数据
				        		 flag = bindUserInfo(app,userCenterReg);
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
    
    public  boolean registerUser( App app,UserCenterRegDto userCenterReg){
		boolean flag=false;
		User user=new User(userCenterReg.getUsername(),userCenterReg.getPassword(),userCenterReg.getPhone(),userCenterReg.getEmail(),"","","");
		AppUser appUser=null;
			String aesPassword="";
			try {
				aesPassword=AESUtil.encrypt(userCenterReg.getPassword(), userserviceDev.getAes_userCenter_key());
			} catch (Exception e1) {
				log.info("aes加密出错："+userCenterReg.getPassword());
				e1.printStackTrace();
			}
			user.setAesPassword(aesPassword);
			user.setAppId(app.getId());
			if(!nullEmptyBlankJudge(userCenterReg.getId())){
			  user.setId(Integer.parseInt(userCenterReg.getId()));	
			}if(!nullEmptyBlankJudge(userCenterReg.getGuid())){
			  user.guid(userCenterReg.getGuid());	
			}
			user.cardNo(userCenterReg.getCard_no());
			user.setEmailActivation(User.ACTIVATION_NO);
			user.userState("0");
			Boolean f=userService.save(user);
			if(f){
			if(null==userCenterReg.getSource_id()||"".equals(userCenterReg.getSource_id().trim())){
				appUser=new AppUser(app.getId(),user.getId(),user.guid());
			}else{
				appUser=new AppUser(app.getId(),user.getId(),userCenterReg.getSource_id());
			}
			if(!nullEmptyBlankJudge(userCenterReg.getAppUid())){
				appUser.appUid(Integer.parseInt(userCenterReg.getAppUid()));
			}
			flag =appUserService.saveAppUser(appUser);
		 }
			return flag;
	}
	public  boolean sysUserInfo( App app,UserCenterRegDto userCenterReg){
		boolean flag=false;
       if(!nullEmptyBlankJudge(userCenterReg.getId()))
       {
           User user = userService.findUserById(Integer.parseInt(userCenterReg.getId()));
           if (null != user) {
               if (!nullEmptyBlankJudge(userCenterReg.getUsername())) {
               user.setUsername(userCenterReg.getUsername());
               }
               if (!nullEmptyBlankJudge(userCenterReg.getPassword())) {
               	String aesPassword="";
				try {
					aesPassword=AESUtil.encrypt(userCenterReg.getPassword(), userserviceDev.getAes_userCenter_key());
				} catch (Exception e1) {
					log.info("aes加密出错："+userCenterReg.getPassword());
					e1.printStackTrace();
				}
				user.setAesPassword(aesPassword);
               }
               if(!nullEmptyBlankJudge(userCenterReg.getPhone())){
               user.setPhone(userCenterReg.getPhone());
               }if (!nullEmptyBlankJudge(userCenterReg.getEmail())) {
                 user.setEmail(userCenterReg.getEmail());
               }if (!nullEmptyBlankJudge(userCenterReg.getCard_no())) {
                 user.setCardNo(userCenterReg.getCard_no());
               }  if(nullEmptyBlankJudge(userCenterReg.getPassword())|| ("null").equals(userCenterReg.getPassword())){
                   user.userType(1);
               } user.userState("1");
               if(!nullEmptyBlankJudge(userCenterReg.getGuid())){
                  user.guid(userCenterReg.getGuid());
                }
               flag=userService.updateUser(user);
           }
           else{
        	   log.info("~~~~~~~~~~~~~~sysUserInfo:not found user,userId:"+userCenterReg.getId()+",appId:"+app.getId());
           }  
       }
       return flag;
	}
	public  boolean bindUserInfo( App app,UserCenterRegDto userCenterReg){
    	boolean flag=false;
    	AppUser appUser=null;
           if(!nullEmptyBlankJudge(userCenterReg.getId()))
           {
        	   appUser=new AppUser(app.getId(),Integer.parseInt(userCenterReg.getId()),userCenterReg.getSource_id());
			if(!nullEmptyBlankJudge(userCenterReg.getAppUid())){
				appUser.appUid(Integer.parseInt(userCenterReg.getAppUid()));
			}
			flag=appUserService.saveAppUser(appUser);
			if(flag){
				if(!nullEmptyBlankJudge(userCenterReg.getCard_no())){
					userService.updateUserCardNoById(Integer.parseInt(userCenterReg.getId()),userCenterReg.getCard_no());
				}
				if(!nullEmptyBlankJudge(userCenterReg.getPhone())){
					userService.updatePhoneById(Integer.parseInt(userCenterReg.getId()), userCenterReg.getPhone());
				}
			}
           }
           return flag;
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