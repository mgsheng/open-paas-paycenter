
package cn.com.open.openpaas.payservice.app.kafka;
import java.util.Properties;

import cn.com.open.openpaas.payservice.app.tools.PropertiesTool;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
//单例对象的初始化同步
public class GlobalConfig {
    private static GlobalConfig instance = null;
    private Producer producer=null;
    private GlobalConfig() {
      //Load configuration information from DB or file
      //Set values for properties
    	 Properties properties = new Properties();  
    	 properties.put("zookeeper.connect",PropertiesTool.getAppPropertieByKey("zookeeper-connect"));
    	 properties.put("serializer.class", StringEncoder.class.getName());
    	 properties.put("metadata.broker.list",PropertiesTool.getAppPropertieByKey("metadata-broker-list"));
    	 producer=new Producer<Integer, String>(new ProducerConfig(properties));
    }
    private static synchronized void syncInit() {
      if (instance == null) {
        instance = new GlobalConfig();
      }
    }
    public static GlobalConfig getInstance() {
      if (instance == null) {
        syncInit();
      }
      return instance;
    }
	public Producer getProducer() {
		return producer;
	}
    
  }