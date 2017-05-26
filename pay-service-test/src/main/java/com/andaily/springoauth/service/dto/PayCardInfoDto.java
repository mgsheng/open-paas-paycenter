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
	private String terminalId;
	private String lastLoginTerminalId;
	private String isSetPaypwd;
	private String registIp;
	private String lastloginIp;
	private String lastloginTime;
	private String requestNo;
	private String validateCode;
	private String amount;
	private String productName;
	private String bindCardConfirmUri;
	private String bindCardResendsmsUri;
	private String bindCardDirectUri;
	private String terminalNo;
	private String oricardNo;
	private String changeCardRequestUri;
	private String changeCardConfirmUri;
	private String changeCardResendsmsUri;
	private String unbindCardDirectUri;
	private String registTime;
	
	
	
	public String getUnbindCardDirectUri() {
		return unbindCardDirectUri;
	}

	public void setUnbindCardDirectUri(String unbindCardDirectUri) {
		this.unbindCardDirectUri = unbindCardDirectUri;
	}

	public String getRegistTime() {
		return registTime;
	}

	public void setRegistTime(String registTime) {
		this.registTime = registTime;
	}

	public String getChangeCardConfirmUri() {
		return changeCardConfirmUri;
	}

	public void setChangeCardConfirmUri(String changeCardConfirmUri) {
		this.changeCardConfirmUri = changeCardConfirmUri;
	}

	public String getChangeCardResendsmsUri() {
		return changeCardResendsmsUri;
	}

	public void setChangeCardResendsmsUri(String changeCardResendsmsUri) {
		this.changeCardResendsmsUri = changeCardResendsmsUri;
	}

	public String getChangeCardRequestUri() {
		return changeCardRequestUri;
	}

	public void setChangeCardRequestUri(String changeCardRequestUri) {
		this.changeCardRequestUri = changeCardRequestUri;
	}

	public String getOricardNo() {
		return oricardNo;
	}

	public void setOricardNo(String oricardNo) {
		this.oricardNo = oricardNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getBindCardConfirmUri() {
		return bindCardConfirmUri;
	}

	public void setBindCardConfirmUri(String bindCardConfirmUri) {
		this.bindCardConfirmUri = bindCardConfirmUri;
	}

	public String getBindCardResendsmsUri() {
		return bindCardResendsmsUri;
	}

	public void setBindCardResendsmsUri(String bindCardResendsmsUri) {
		this.bindCardResendsmsUri = bindCardResendsmsUri;
	}

	public String getBindCardDirectUri() {
		return bindCardDirectUri;
	}

	public void setBindCardDirectUri(String bindCardDirectUri) {
		this.bindCardDirectUri = bindCardDirectUri;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getLastLoginTerminalId() {
		return lastLoginTerminalId;
	}

	public void setLastLoginTerminalId(String lastLoginTerminalId) {
		this.lastLoginTerminalId = lastLoginTerminalId;
	}

	public String getIsSetPaypwd() {
		return isSetPaypwd;
	}

	public void setIsSetPaypwd(String isSetPaypwd) {
		this.isSetPaypwd = isSetPaypwd;
	}

	public String getRegistIp() {
		return registIp;
	}

	public void setRegistIp(String registIp) {
		this.registIp = registIp;
	}

	public String getLastloginIp() {
		return lastloginIp;
	}

	public void setLastloginIp(String lastloginIp) {
		this.lastloginIp = lastloginIp;
	}

	public String getLastloginTime() {
		return lastloginTime;
	}

	public void setLastloginTime(String lastloginTime) {
		this.lastloginTime = lastloginTime;
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
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&identityId=%s&identityType=%s&cardNo=%s&userName=%s&outTradeNo=%s&phone=%s&userId=%s&avaliabletime=%s&parameter=%s&merchantId=%s&terminalId=%s&lastLoginTerminalId=%s&isSetPaypwd=%s&registIp=%s&lastloginIp=%s&lastloginTime=%s&registTime=%s",
        		bindCardRequestUri,appId,identityId,identityType, cardNo,URLEncoder.encode(userName, "UTF-8"), outTradeNo, phone,userId,avaliabletime,parameter,merchantId,terminalId,lastLoginTerminalId,isSetPaypwd,registIp,lastloginIp,lastloginTime,registTime);  
    }
	public String getConfirmUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&outTradeNo=%s&merchantId=%s&requestNo=%s&validateCode=%s",
        		bindCardConfirmUri,appId, outTradeNo,merchantId,requestNo,validateCode);  
    }
	public String getChangeConfirmUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&outTradeNo=%s&merchantId=%s&requestNo=%s&validateCode=%s",
        		changeCardConfirmUri,appId, outTradeNo,merchantId,requestNo,validateCode);  
    }
	public String getDirectUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&outTradeNo=%s&merchantId=%s&productName=%s&parameter=%s&avaliabletime=%s&amount=%s&userId=%s&terminalNo=%s",
        		bindCardDirectUri,appId, outTradeNo,merchantId,URLEncoder.encode(productName, "UTF-8"),parameter,avaliabletime,amount,userId,terminalNo);  
    }
	public String getResendsmsUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&outTradeNo=%s&merchantId=%s&requestNo=%s",
        		bindCardResendsmsUri,appId, outTradeNo,merchantId,requestNo);  
    }
	public String getChangeResendsmsUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&outTradeNo=%s&merchantId=%s&requestNo=%s",
        		changeCardResendsmsUri,appId, outTradeNo,merchantId,requestNo);  
    }
	public String getChangeCardUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&identityId=%s&identityType=%s&oricardNo=%s&cardNo=%s&userName=%s&outTradeNo=%s&phone=%s&userId=%s&avaliabletime=%s&parameter=%s&merchantId=%s&terminalId=%s&lastLoginTerminalId=%s&isSetPaypwd=%s&registIp=%s&lastloginIp=%s&lastloginTime=%s&registTime=%s",
        		changeCardRequestUri,appId,identityId,identityType,oricardNo, cardNo,URLEncoder.encode(userName, "UTF-8"), outTradeNo, phone,userId,avaliabletime,parameter,merchantId,terminalId,lastLoginTerminalId,isSetPaypwd,registIp,lastloginIp,lastloginTime,registTime);  
    }
	public String getUnbindCardUri() throws UnsupportedEncodingException {
        return String.format("%s?appId=%s&outTradeNo=%s&merchantId=%s&cardNo=%s&identityId=%s&identityType=%s&userId=%s",
        		unbindCardDirectUri,appId, outTradeNo,merchantId,cardNo,identityId,identityType,userId);  
    }
	


	

}