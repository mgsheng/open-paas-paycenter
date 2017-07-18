package cn.com.open.openpaas.payservice.app.thread;

import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;

import cn.com.open.openpaas.payservice.app.kafka.KafkaProducer;
import cn.com.open.openpaas.payservice.app.tools.PropertiesTool;


/**
 * 支付成功发送短信息通知用户
 * @author dongminghao
 *
 */
public class PaySendSmsThread implements Runnable {
	private SortedMap<Object,Object> sParaTemp;
	private KafkaProducer  kafkaProducer;
	public PaySendSmsThread(SortedMap<Object,Object> sParaTemp,KafkaProducer kafkaProducer){
	 this.sParaTemp=sParaTemp;
	 this.kafkaProducer=kafkaProducer;
	}
	@Override
	public void run() {
	String topic=	PropertiesTool.getAppPropertieByKey("kafka-topic");
	String kafkaMessage=JSONObject.toJSONString( sParaTemp);
	System.out.println("---------------------kafamessage:"+kafkaMessage);
		kafkaProducer.sendMessage(topic, kafkaMessage);
     }
}
