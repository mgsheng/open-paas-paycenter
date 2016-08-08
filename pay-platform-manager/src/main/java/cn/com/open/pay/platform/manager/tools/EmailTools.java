package cn.com.open.pay.platform.manager.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EmailTools {
	
	/**
	 * 发送邮件
	 * @param toAddress 收件人邮箱地址
	 * @param title 邮件标题
	 * @param content  邮件正文
	 * @return
	 */
	public static boolean sendMail(String toAddress,String title,String content){
		//获取配置信息
		String mailServerHost = PropertiesTool.getAppPropertieByKey("mail.server.host");
		String mailServerPort = PropertiesTool.getAppPropertieByKey("mail.server.port");
		String fromAddress = PropertiesTool.getAppPropertieByKey("mail.administrator.address");
		String password = PropertiesTool.getAppPropertieByKey("mail.administrator.password");
		// 设置邮件服务器信息
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(mailServerHost);
		mailInfo.setMailServerPort(mailServerPort);
		mailInfo.setValidate(true);
		// 邮箱用户名
		mailInfo.setUserName(fromAddress);
		// 邮箱密码
		mailInfo.setPassword(password);
		// 发件人邮箱
		mailInfo.setFromAddress(fromAddress);
		// 收件人邮箱
		mailInfo.setToAddress(toAddress);
		// 邮件标题
		mailInfo.setSubject(title);
		// 邮件内容
		mailInfo.setContent(content);
		// 发送邮件：文体格式
//		SimpleMailSender.sendTextMail(mailInfo);
		// 发送邮件：html格式
		return SimpleMailSender.sendHtmlMail(mailInfo);
	}
	
	/**
	 * 发送邮件
	 * @param mailServerHost  发件人邮箱服务器地址，如：smtp.126.com
	 * @param mailServerPort 发件人邮箱服务端口号
	 * @param fromAddress 发件人邮箱地址
	 * @param password  发件人邮箱密码
	 * @param toAddress 收件人邮箱地址
	 * @param title 邮件标题
	 * @param content  邮件正文
	 * @return
	 */
	public static boolean sendMail(String mailServerHost,String mailServerPort,String fromAddress,String password,String toAddress,String title,String content){
		// 设置邮件服务器信息
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(mailServerHost);
		mailInfo.setMailServerPort(mailServerPort);
		mailInfo.setValidate(true);
		// 邮箱用户名
		mailInfo.setUserName(fromAddress);
		// 邮箱密码
		mailInfo.setPassword(password);
		// 发件人邮箱
		mailInfo.setFromAddress(fromAddress);
		// 收件人邮箱
		mailInfo.setToAddress(toAddress);
		// 邮件标题
		mailInfo.setSubject(title);
		// 邮件内容
		mailInfo.setContent(content);
		// 发送邮件：文体格式
//		SimpleMailSender.sendTextMail(mailInfo);
		// 发送邮件：html格式
		return SimpleMailSender.sendHtmlMail(mailInfo);
	}
	
	/**
	 * 模版形式发邮件
	 * @param toAddress			收件人邮箱地址
	 * @param title				邮件标题
	 * @param templateName		模版名称，在emailtemplate包中必须存在[名称.html]的模版 假设abc.html 那么名称为abc
	 * @param templateParams	模版参数，模版中的通配符${参数名}，将对应的参数名替换值
	 * @return
	 */
	public static boolean sendTemplateMail(String toAddress,String title,String templateName, Map<String,String> templateParams){
		File file = new File(EmailTools.class.getClassLoader().getResource("/emailtemplate/"+templateName+".html").getFile());
		if(!file.exists() || !file.isFile()){
			//模版不存在
			return false;
		}
		StringBuffer strBuffer = new StringBuffer();
		try {
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			for (String temp = null; (temp = bufReader.readLine()) != null; temp = null) {
				//每行替换通配符
				for(String key : templateParams.keySet()){
					if(temp.indexOf("${"+key+"}")!=-1){
						temp = temp.replaceAll("\\$\\{"+key+"\\}", templateParams.get(key));
					}
				}
				strBuffer.append(temp);
				strBuffer.append(System.getProperty("line.separator"));//行与行之间的分割
			}
			bufReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendMail(toAddress, title, strBuffer.toString());
	}
	
}

/*********************************发送邮件相关类****************************************/

class MailSenderInfo{
	// 发送邮件的服务器的IP(或主机地址)
	 private String mailServerHost;
	 // 发送邮件的服务器的端口
	 private String mailServerPort = "25";
	 // 发件人邮箱地址
	 private String fromAddress;
	 // 收件人邮箱地址
	 private String toAddress;
	 // 登陆邮件发送服务器的用户名
	 private String userName;
	 // 登陆邮件发送服务器的密码
	 private String password;
	 // 是否需要身份验证
	 private boolean validate = false;
	 // 邮件主题
	 private String subject;
	 // 邮件的文本内容
	 private String content;
	 // 邮件附件的文件名
	 private String[] attachFileNames;
	 
	 public Properties getProperties() {
	  Properties p = new Properties();
	  p.put("mail.smtp.host", this.mailServerHost);
	  p.put("mail.smtp.port", this.mailServerPort);
	  p.put("mail.smtp.auth", validate ? "true" : "false");
	  return p;
	 }
	 public String getMailServerHost() {
	  return mailServerHost;
	 }
	 public void setMailServerHost(String mailServerHost) {
	  this.mailServerHost = mailServerHost;
	 }
	 public String getMailServerPort() {
	  return mailServerPort;
	 }
	 public void setMailServerPort(String mailServerPort) {
	  this.mailServerPort = mailServerPort;
	 }
	 public boolean isValidate() {
	  return validate;
	 }
	 public void setValidate(boolean validate) {
	  this.validate = validate;
	 }
	 public String[] getAttachFileNames() {
	  return attachFileNames;
	 }
	 public void setAttachFileNames(String[] fileNames) {
	  this.attachFileNames = fileNames;
	 }
	 public String getFromAddress() {
	  return fromAddress;
	 }
	 public void setFromAddress(String fromAddress) {
	  this.fromAddress = fromAddress;
	 }
	 public String getPassword() {
	  return password;
	 }
	 public void setPassword(String password) {
	  this.password = password;
	 }
	 public String getToAddress() {
	  return toAddress;
	 }
	 public void setToAddress(String toAddress) {
	  this.toAddress = toAddress;
	 }
	 public String getUserName() {
	  return userName;
	 }
	 public void setUserName(String userName) {
	  this.userName = userName;
	 }
	 public String getSubject() {
	  return subject;
	 }
	 public void setSubject(String subject) {
	  this.subject = subject;
	 }
	 public String getContent() {
	  return content;
	 }
	 public void setContent(String textContent) {
	  this.content = textContent;
	 }
}

class MyAuthenticator extends Authenticator{
	/**
     * 用户名（登录邮箱）
     */
    private String username;
    /**
     * 密码
     */
    private String password;
 
    public MyAuthenticator(){
    	
    }
    /**
     * 初始化邮箱和密码
     * 
     * @param username 邮箱
     * @param password 密码
     * @return 
     */
    public MyAuthenticator(String username, String password) {
	    this.username = username;
	    this.password = password;
    }
 
    String getPassword() {
    return password;
    }
 
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
    	return new PasswordAuthentication(username, password);
    }
 
    String getUsername() {
    	return username;
    }
 
    public void setPassword(String password) {
    	this.password = password;
    }
 
    public void setUsername(String username) {
    	this.username = username;
    }
}

class SimpleMailSender{
	private static final Logger log = Logger.getLogger(SimpleMailSender.class);
	
	public static boolean sendTextMail(MailSenderInfo mailInfo) {
		  // 判断是否需要身份认证
		  MyAuthenticator authenticator = null;
		  Properties pro = mailInfo.getProperties();
		  if (mailInfo.isValidate()) {
			   // 如果需要身份认证，则创建一个密码验证器
			   authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		  }
		  // 根据邮件会话属性和密码验证器构造一个发送邮件的session
		  Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		  try {
			   // 根据session创建一个邮件消息
			   Message mailMessage = new MimeMessage(sendMailSession);
			   // 创建邮件发送者地址
			   Address from = new InternetAddress(mailInfo.getFromAddress());
			   // 设置邮件消息的发送者
			   mailMessage.setFrom(from);
			   // 创建邮件的接收者地址，并设置到邮件消息中
			   Address to = new InternetAddress(mailInfo.getToAddress());
			   mailMessage.setRecipient(Message.RecipientType.TO, to);
			   // 设置邮件消息的主题
			   mailMessage.setSubject(mailInfo.getSubject());
			   // 设置邮件消息发送的时间
			   mailMessage.setSentDate(new Date());
			   // 设置邮件消息的主要内容
			   String mailContent = mailInfo.getContent();
			   mailMessage.setText(mailContent);
			   // 发送邮件
			   if(PropertiesTool.getAppPropertieByKey("mail.send").equals("true")){
				   Transport.send(mailMessage);
			   }
			   else{
				   log.info("模拟发送邮件 from:"+mailInfo.getFromAddress()+" to:"+mailInfo.getToAddress()+" content:"+mailInfo.getContent());
			   }
			   return true;
		  } catch (MessagingException ex) {
			  ex.printStackTrace();
		  }
		  return false;
	 }
	 
	 public static boolean sendHtmlMail(MailSenderInfo mailInfo){
		  // 判断是否需要身份认证
		  MyAuthenticator authenticator = null;
		  Properties pro = mailInfo.getProperties();
		  // 如果需要身份认证，则创建一个密码验证器
		  if (mailInfo.isValidate()) {
		   authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		  }
		  // 根据邮件会话属性和密码验证器构造一个发送邮件的session
//		  Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		  Session sendMailSession = Session.getInstance(pro, authenticator);
		  try {
			   // 根据session创建一个邮件消息
			   Message mailMessage = new MimeMessage(sendMailSession);
			   // 创建邮件发送者地址
			   Address from = new InternetAddress(mailInfo.getFromAddress());
			   // 设置邮件消息的发送者
			   mailMessage.setFrom(from);
			   // 创建邮件的接收者地址，并设置到邮件消息中
			   Address to = new InternetAddress(mailInfo.getToAddress());
			   // Message.RecipientType.TO属性表示接收者的类型为TO
			   mailMessage.setRecipient(Message.RecipientType.TO, to);
			   // 设置邮件消息的主题
			   mailMessage.setSubject(mailInfo.getSubject());
//			   try {
//				   mailMessage.setSubject(MimeUtility.encodeText(mailInfo.getSubject(),"gb2312","B"));
//			   } catch (UnsupportedEncodingException e) {
//				   // TODO Auto-generated catch block
//				   e.printStackTrace();
//			   }
			   // 设置邮件消息发送的时间
			   mailMessage.setSentDate(new Date());
			   // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			   Multipart mainPart = new MimeMultipart();
			   // 创建一个包含HTML内容的MimeBodyPart
			   BodyPart html = new MimeBodyPart();
			   // 设置HTML内容
			   html.setContent(mailInfo.getContent(), "text/html; charset=gb2312");
			   mainPart.addBodyPart(html);
			   // 将MiniMultipart对象设置为邮件内容
			   mailMessage.setContent(mainPart);
			   // 发送邮件
			   if(PropertiesTool.getAppPropertieByKey("mail.send").equals("true")){
				   Transport.send(mailMessage);
			   }
			   else{
				   log.info("模拟发送邮件 from:"+mailInfo.getFromAddress()+" to:"+mailInfo.getToAddress()+" content:"+mailInfo.getContent());
			   }
			   log.info("邮件发送完毕");
			   return true;
		  } catch (MessagingException ex) {
			  ex.printStackTrace();
		  }
		  return false;
	 }
	 
}




