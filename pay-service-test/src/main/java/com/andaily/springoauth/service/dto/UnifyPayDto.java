package com.andaily.springoauth.service.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 15-5-18
 * <p/>
 * http://localhost:8080/oauth/token?client_id=unity-client&client_secret=unity&grant_type=authorization_code&code=zLl170&redirect_uri=http%3a%2f%2flocalhost%3a8080%2funity%2fdashboard.htm
 *
 * 
 */
public class UnifyPayDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String unifyPayUri;
	private String outTradeNo;
	private String userName;
    private String userId;
    private String merchantId;
    private String appId;
	private String goodsId;
	private String goodsName;
	private String goodsDesc;
	private String goodsTag;
	private String showUrl;
	private String buyerRealName;
	private String buyerCertNo;
	private String inputCharset;
	private String paymentOutTime;
	private String paymentType;
	private String paymentChannel;
	private String totalFee;
	private String feeType;
	private String clientIp;
	private String parameter;
	private String businessType;
	private String phone;
	
	
	
	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getBusinessType() {
		return businessType;
	}


	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}


	public String getUnifyPayUri() {
		return unifyPayUri;
	}


	public void setUnifyPayUri(String unifyPayUri) {
		this.unifyPayUri = unifyPayUri;
	}


	public String getOutTradeNo() {
		return outTradeNo;
	}


	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
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


	public String getGoodsId() {
		return goodsId;
	}


	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}


	public String getGoodsName() {
		return goodsName;
	}


	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}


	public String getGoodsDesc() {
		return goodsDesc;
	}


	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}


	public String getGoodsTag() {
		return goodsTag;
	}


	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}


	public String getShowUrl() {
		return showUrl;
	}


	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}


	public String getBuyerRealName() {
		return buyerRealName;
	}


	public void setBuyerRealName(String buyerRealName) {
		this.buyerRealName = buyerRealName;
	}


	public String getBuyerCertNo() {
		return buyerCertNo;
	}


	public void setBuyerCertNo(String buyerCertNo) {
		this.buyerCertNo = buyerCertNo;
	}


	public String getInputCharset() {
		return inputCharset;
	}


	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}


	public String getPaymentOutTime() {
		return paymentOutTime;
	}


	public void setPaymentOutTime(String paymentOutTime) {
		this.paymentOutTime = paymentOutTime;
	}


	public String getPaymentType() {
		return paymentType;
	}


	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}


	public String getPaymentChannel() {
		return paymentChannel;
	}


	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}


	public String getTotalFee() {
		return totalFee;
	}


	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}


	public String getFeeType() {
		return feeType;
	}


	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?outTradeNo=%s&userName=%s&userId=%s&appId=%s&merchantId=%s&goodsId=%s&goodsName=%s&goodsDesc=%s&goodsTag=%s&showUrl=%s&buyerRealName=%s&buyerCertNo=%s&inputCharset=%s&paymentOutTime=%s&paymentType=%s&paymentChannel=%s&totalFee=%s&feeType=%s&clientIp=%s&parameter=%s&businessType=%s&phone=%s",
        		unifyPayUri,outTradeNo,userName,userId,appId,merchantId,goodsId,URLEncoder.encode(goodsName, "UTF-8"),URLEncoder.encode(goodsDesc, "UTF-8"),URLEncoder.encode(goodsTag, "UTF-8"),showUrl,URLEncoder.encode(buyerRealName, "UTF-8"),buyerCertNo,inputCharset,paymentOutTime,paymentType,paymentChannel,totalFee,feeType,clientIp,URLEncoder.encode(parameter, "UTF-8"),businessType,phone);  
    }
	

}
