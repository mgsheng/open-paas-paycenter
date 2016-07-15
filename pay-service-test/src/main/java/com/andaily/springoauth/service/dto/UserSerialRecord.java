package com.andaily.springoauth.service.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户流水记录表
 */
public class UserSerialRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String appId;
	private String  serialNo;
	private String  amount;
	private Date createTime;
	private String sourceId;
	private String userName;
	private String unifyCostsUri;
	private String merchantId;
	
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getUnifyCostsUri() {
		return unifyCostsUri;
	}

	public void setUnifyCostsUri(String unifyCostsUri) {
		this.unifyCostsUri = unifyCostsUri;
	}

	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?app_id=%s&source_id=%s&user_name=%s&amount=%s&serial_no=%s&merchantId=%s",
        		unifyCostsUri,appId,sourceId,userName,amount,serialNo,merchantId);  
    }

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}