/****************************************************************************  
 * Copyright © 2016 TCL Technology . All rights reserved.
 * @Title: ResponseHead.java 
 * @Prject: cr-model
 * @Package: com.tcl.cr.score.packet.base 
 * @Description: TODO
 * @author: heqingqing   
 * @date: 2016年10月14日 下午4:32:38 
 * @version: V1.0   
 * ****************************************************************************
 */
package cn.com.open.openpaas.payservice.app.channel.zxpt.zx;

/**
 * @ClassName: ResponseHead
 * @Description: 通用响应报文
 * @author: heqingqing
 * @date: 2016年10月14日 下午4:32:38
 */
public class ResponseHead {
	
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 字符编码
	 */
	private String charSet;
	/**
	 * 报文发送方
	 */
	private String source;
	/**
	 * 报文接收方
	 */
	private String des;
	/**
	 * 接入渠道系统
	 */
	private String app;
	/**
	 * 报文交易编码
	 */
	private String tranCode;
	/**
	 * 报文唯一编码
	 */
	private String tranNo;
	/**
	 * 附加信息
	 */
	private String tranRef;
	/**
	 * 时间戳
	 */
	private String timeStamp;
	
	private String tranTime;
	/**
	 * 拓展字段
	 */
	private String reserve;
	/**
	 * 消息代码
	 */
	private String messageCode;
	/**
	 * 消息类容
	 */
	private String message;
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the charSet
	 */
	public String getCharSet() {
		return charSet;
	}
	/**
	 * @param charSet the charSet to set
	 */
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the des
	 */
	public String getDes() {
		return des;
	}
	/**
	 * @param des the des to set
	 */
	public void setDes(String des) {
		this.des = des;
	}
	/**
	 * @return the app
	 */
	public String getApp() {
		return app;
	}
	/**
	 * @param app the app to set
	 */
	public void setApp(String app) {
		this.app = app;
	}
	/**
	 * @return the tranCode
	 */
	public String getTranCode() {
		return tranCode;
	}
	/**
	 * @param tranCode the tranCode to set
	 */
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	/**
	 * @return the tranNo
	 */
	public String getTranNo() {
		return tranNo;
	}
	/**
	 * @param tranNo the tranNo to set
	 */
	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}
	/**
	 * @return the tranRef
	 */
	public String getTranRef() {
		return tranRef;
	}
	/**
	 * @param tranRef the tranRef to set
	 */
	public void setTranRef(String tranRef) {
		this.tranRef = tranRef;
	}
	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the tranTime
	 */
	public String getTranTime() {
		return tranTime;
	}
	/**
	 * @param tranTime the tranTime to set
	 */
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
	/**
	 * @return the reserve
	 */
	public String getReserve() {
		return reserve;
	}
	/**
	 * @param reserve the reserve to set
	 */
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	/**
	 * @return the messageCode
	 */
	public String getMessageCode() {
		return messageCode;
	}
	/**
	 * @param messageCode the messageCode to set
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/* (non Javadoc) 
	 * @Title: toString
	 * @Description: TODO
	 * @return 
	 * @see java.lang.Object#toString() 
	 */
	@Override
	public String toString() {
		return "ResponseHead [version=" + version + ", charSet=" + charSet + ", source=" + source + ", des=" + des
				+ ", app=" + app + ", tranCode=" + tranCode + ", tranNo=" + tranNo + ", tranRef=" + tranRef
				+ ", timeStamp=" + timeStamp + ", tranTime=" + tranTime + ", reserve=" + reserve + ", messageCode="
				+ messageCode + ", message=" + message + "]";
	}
	
}
