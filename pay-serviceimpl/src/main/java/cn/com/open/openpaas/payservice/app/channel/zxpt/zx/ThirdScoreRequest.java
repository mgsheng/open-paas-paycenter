/****************************************************************************  
 * Copyright © 2016 TCL Technology . All rights reserved.
 * @Title: ThirdScoreRequest.java 
 * @Prject: cr-model
 * @Package: com.tcl.cr.score.third.packet.request 
 * @Description: TODO
 * @author: heqingqing   
 * @date: 2016年10月31日 上午11:26:58 
 * @version: V1.0   
 * ****************************************************************************
 */
package cn.com.open.openpaas.payservice.app.channel.zxpt.zx;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @ClassName: ThirdScoreRequest
 * @Description: 第三方评分请求
 * @author: heqingqing
 * @date: 2016年10月31日 上午11:26:58
 */
@XStreamAlias("thirdscorerequest")
public class ThirdScoreRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 订单号(请求流水号)
	 */
	private String orderId;
	
	/**
	 * 授权渠道（1、芝麻信用,2 、前海征信 3、所有）
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
	private String certNo;
	
	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 查询原因
	 */
	private String reasonNo;
	
	/**
	 * 信息主体授权码
	 */
	private String entityAuthCode;
	
	/**
	 * 银行卡号
	 */
	private String cardNo;
	
	/**
	 * 授权日期
	 */
	private String authDate;

	/**
	 * 保留字段
	 */
	private String reserve;

	/**
	 * @return the reserve
	 */
	public String getReserve() {
		return reserve;
	}

	/**
	 * @param reserve
	 *            the reserve to set
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
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the scoreChannel
	 */
	public String getScoreChannel() {
		return scoreChannel;
	}

	/**
	 * @param scoreChannel
	 *            the scoreChannel to set
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
	 * @param scoreMethod
	 *            the scoreMethod to set
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
	 * @param certType
	 *            the certType to set
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}



	/**
	 * @return the certNo
	 */
	public String getCertNo() {
		return certNo;
	}

	/**
	 * @param certNo the certNo to set
	 */
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReasonNo() {
		return reasonNo;
	}

	public void setReasonNo(String reasonNo) {
		this.reasonNo = reasonNo;
	}

	public String getEntityAuthCode() {
		return entityAuthCode;
	}

	public void setEntityAuthCode(String entityAuthCode) {
		this.entityAuthCode = entityAuthCode;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	

	public String getAuthDate() {
		return authDate;
	}

	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}

	@Override
	public String toString() {
		return "ThirdScoreRequest [orderId=" + orderId + ", scoreChannel=" + scoreChannel + ", scoreMethod="
				+ scoreMethod + ", certType=" + certType + ", certNo=" + certNo + ", name=" + name + ", mobile="
				+ mobile + ", reasonNo=" + reasonNo + ", entityAuthCode=" + entityAuthCode + ", cardNo=" + cardNo
				+ ", authDate=" + authDate + ", reserve=" + reserve + "]";
	}

	
}
