/****************************************************************************  
 * Copyright © 2016 TCL Technology . All rights reserved.
 * @Title: ThirdScoreResponse.java 
 * @Prject: cr-model
 * @Package: com.tcl.cr.score.third.packet.response 
 * @Description: TODO
 * @author: heqingqing   
 * @date: 2016年10月31日 上午11:27:14 
 * @version: V1.0   
 * ****************************************************************************
 */
package cn.com.open.openpaas.payservice.app.channel.zxpt.zx;

import java.io.Serializable;
import java.util.Map;

/** 
 * @ClassName: ThirdScoreResponse 
 * @Description: TODO
 * @author: heqingqing
 * @date: 2016年10月31日 上午11:27:14  
 */
public class ThirdScoreResponse implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	/**
	 * 订单号(请求流水号)
	 */
	private String orderId;
	/**
	 * 征信平台返回流水号
	 */
	private String orderNo;
	/**
	 * openId 芝麻信用专用
	 */
	private String name;
	/**
	 * 授权渠道（1、芝麻信用,2 、前海征信  3、所有）
	 */
	private String scoreChannel;
	/**
	 * 授权方式(1,PC)
	 */
	private String scoreMethod;
	/**
	 * 证件类型
	*/
	private String certType;
	/**
	 * 证件号
	 */
	private String certID;
	
	/**
	 * 手机号码
	 */
	private String mobile;
	
	/**
	 * 渠道名称
	 */
	private String channelName;
	/**
	 * 拓展字段
	 */
	private String reserve;

	/**
	 * @return the reserve
	 */
	
	private Map<String ,Object> map;
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
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	

	

	public String getName() {
		return name;
	}
	

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the scoreChannel
	 */
	public String getScoreChannel() {
		return scoreChannel;
	}

	/**
	 * @param scoreChannel the scoreChannel to set
	 */
	public void setScoreChannel(String scoreChannel) {
		this.scoreChannel = scoreChannel;
	}

	/**
	 * @return the scoreMethod
	 */
	public String getScoreMethod() {
		return scoreMethod;
	}

	/**
	 * @param scoreMethod the scoreMethod to set
	 */
	public void setScoreMethod(String scoreMethod) {
		this.scoreMethod = scoreMethod;
	}

	/**
	 * @return the certType
	 */
	public String getCertType() {
		return certType;
	}

	/**
	 * @param certType the certType to set
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}

	/**
	 * @return the certID
	 */
	public String getCertID() {
		return certID;
	}

	/**
	 * @param certID the certID to set
	 */
	public void setCertID(String certID) {
		this.certID = certID;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return "ThirdScoreResponse [orderId=" + orderId + ", orderNo=" + orderNo + ", name=" + name + ", scoreChannel="
				+ scoreChannel + ", scoreMethod=" + scoreMethod + ", certType=" + certType + ", certID=" + certID
				+ ", mobile=" + mobile + ", channelName=" + channelName + ", reserve=" + reserve + ", map=" + map + "]";
	}

	/* (non Javadoc) 
	 * @Title: toString
	 * @Description: TODO
	 * @return 
	 * @see java.lang.Object#toString() 
	 */
	
	
	
}