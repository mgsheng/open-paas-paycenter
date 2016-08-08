package cn.com.open.pay.platform.manager.tools;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SmsTools {

	private static final Logger log = Logger.getLogger(SmsTools.class);
	/**
	 * 发送短信
	 * @param phone
	 * @param content
	 * @return
	 */
	public static String sendSms(String phone, String content){
		return SmsTools.sendSms(phone, null, content);
		
	}
	
	/**
	 * 发送短信
	 * @param phone
	 * @param title
	 * @param content
	 * @return
	 */
	public static String sendSms(String phone,String title,String content){
		if(StringUtils.isBlank(phone) || StringUtils.isBlank(content)){
			return null;
		}
		if(!PropertiesTool.getAppPropertieByKey("sms.send").equals("true")){
		   log.info("模拟发送短信:"+content);
		   return "0";
		}
		// 定义方法  
        String method = "SmsSend";  
        // 定义服务  
        Service service = new Service();  
        // 测试1：调用HelloWorld方法，方法没有参数  
        Call call;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL("http://10.100.14.233/OpenSMSWebService/OemsSmsService.asmx"));  
			call.setUseSOAPAction(true);  
			// 第一种设置返回值类型为String的方法  
			call.setReturnType(XMLType.SOAP_STRING);  
			call.setOperationName(new QName("http://tempuri.org/", method));  
			call.setSOAPActionURI("http://tempuri.org/SmsSend");  
			SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement("http://tempuri.org/", "SmsSendSoapHeader");
			soapHeaderElement.setNamespaceURI("http://tempuri.org/");
			soapHeaderElement.addChildElement("SendSource").setValue("oesunitauth");
			soapHeaderElement.addChildElement("SendSourcePwd").setValue("oesunit-auth-pass-word-2015-1210");
			call.addHeader(soapHeaderElement);
			call.addParameter(
					new QName("http://tempuri.org/", "mobilephoneno"),// 这里的name对应参数名称  
			        XMLType.XSD_STRING, ParameterMode.IN);  
			call.addParameter(
					new QName("http://tempuri.org/", "msgTitle"),// 这里的name对应参数名称  
			        XMLType.XSD_STRING, ParameterMode.IN);  
			call.addParameter(
					new QName("http://tempuri.org/", "msgContent"),// 这里的name对应参数名称  
			        XMLType.XSD_STRING, ParameterMode.IN);  
			String retVal1 = (String) call.invoke(new Object[] {phone,title,content});  
			return retVal1;
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		return null;
	}
}
