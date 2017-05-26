package com.andaily.springoauth.service.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 
 */
public class PayZxptInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id ;
	private String scoreChannel;
	private String certNo;
	private String certType;
	private String userName;
	private String phone;
	private String reasonNo;
	private String cardNo;
	private String scoreMethod;
	private String entityAuthCode;
	private String authDate;
	private String reserve;
	private String zxptOrderNo;
	private String zxptRequestStatus;
	private String appId;
	private String credooScore;
	private String decision;
	private String merchantOrderId;
	private String merchantId;
	private String thirdScoreUri;
	private String bqsUri;
	
	public String getThirdScoreRequestUri() throws UnsupportedEncodingException {
	        return String.format("%s?appId=%s&outTradeNo=%s&merchantId=%s&certNo=%s&channelId=%s&userName=%s&mobile=%s&reasonNo=%s&cardNo=%s",
	        		thirdScoreUri,appId, merchantOrderId,merchantId,certNo,scoreChannel,URLEncoder.encode(userName, "UTF-8"),phone,reasonNo,cardNo);  
	    }
	public String getBqsRequestUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&outTradeNo=%s&merchantId=%s&certNo=%s&channelId=%s&userName=%s&mobile=%s",
        		bqsUri,appId, merchantOrderId,merchantId,certNo,scoreChannel,URLEncoder.encode(userName, "UTF-8"),phone);  
    }
	public String getThirdScoreUri() {
		return thirdScoreUri;
	}
	public void setThirdScoreUri(String thirdScoreUri) {
		this.thirdScoreUri = thirdScoreUri;
	}
	public String getBqsUri() {
		return bqsUri;
	}
	public void setBqsUri(String bqsUri) {
		this.bqsUri = bqsUri;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScoreChannel() {
		return scoreChannel;
	}
	public void setScoreChannel(String scoreChannel) {
		this.scoreChannel = scoreChannel;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
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
	public String getReasonNo() {
		return reasonNo;
	}
	public void setReasonNo(String reasonNo) {
		this.reasonNo = reasonNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getScoreMethod() {
		return scoreMethod;
	}
	public void setScoreMethod(String scoreMethod) {
		this.scoreMethod = scoreMethod;
	}
	public String getEntityAuthCode() {
		return entityAuthCode;
	}
	public void setEntityAuthCode(String entityAuthCode) {
		this.entityAuthCode = entityAuthCode;
	}
	
	public String getAuthDate() {
		return authDate;
	}
	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}
	public String getReserve() {
		return reserve;
	}
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	public String getZxptOrderNo() {
		return zxptOrderNo;
	}
	public void setZxptOrderNo(String zxptOrderNo) {
		this.zxptOrderNo = zxptOrderNo;
	}
	public String getZxptRequestStatus() {
		return zxptRequestStatus;
	}
	public void setZxptRequestStatus(String zxptRequestStatus) {
		this.zxptRequestStatus = zxptRequestStatus;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCredooScore() {
		return credooScore;
	}
	public void setCredooScore(String credooScore) {
		this.credooScore = credooScore;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	
	
}