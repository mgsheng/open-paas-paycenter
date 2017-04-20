package com.andaily.springoauth.service.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 用户卡号信息表
 */
public class PayCardInfoDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String appId;
	private String identityId;
	private String identityType;
	private String cardNo;
	private String userName;
	private String outTradeNo;
	private String phone;
	private String userId;
	private String avaliabletime;
	private String parameter;
	private String bindCardRequestUri;
	private String merchantId;
	
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&identityId=%s&identityType=%s&cardNo=%s&userName=%s&outTradeNo=%s&phone=%s&userId=%s&avaliabletime=%s&parameter=%s&merchantId=%s",
        		bindCardRequestUri,appId,identityId,identityType, cardNo,URLEncoder.encode(userName, "UTF-8"), outTradeNo, phone,userId,avaliabletime,parameter,merchantId);  
    }
	
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAvaliabletime() {
		return avaliabletime;
	}
	public void setAvaliabletime(String avaliabletime) {
		this.avaliabletime = avaliabletime;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getBindCardRequestUri() {
		return bindCardRequestUri;
	}
	public void setBindCardRequestUri(String bindCardRequestUri) {
		this.bindCardRequestUri = bindCardRequestUri;
	}
	


	

}