package com.andaily.springoauth.service.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 */
public class OrderRefund implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
    
	private String merchantOrderId;
	private String merchantId;
	private String refundMoney;
	private String appId;
	private String remark;
	private Date createTime;
	private String sourceUid;
	private String sourceUsername;
	private String realName;
	private String phone;
	private String goodsId;
	private String orderRefundUri;
	
	

	public String getOrderRefundUri() {
		return orderRefundUri;
	}

	public void setOrderRefundUri(String orderRefundUri) {
		this.orderRefundUri = orderRefundUri;
	}

	public OrderRefund(){
		
	}

	public OrderRefund(String merchantOrderId, String merchantId, String refundMoney, String appId, String remark, Date createTime, String sourceUid,String sourceUsername,String realName,String phone,String goodsId ) {
	        this.merchantOrderId = merchantOrderId;
	        this.merchantId = merchantId;
	        this.refundMoney = refundMoney;
	        this.appId = appId;
	        this.remark = remark;
	        this.createTime = createTime;
	        this.sourceUid = sourceUid;
	        this.sourceUsername = sourceUsername;
	        this.realName = realName;
	        this.phone = phone;
	        this.goodsId = goodsId;
	    }
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?outTradeNo=%s&merchantId=%s&refundMoney=%s&appId=%s&remark=%s&userid=%s&username=%s&realName=%s&phone=%s&goodsId=%s",
        		orderRefundUri,merchantOrderId,merchantId,refundMoney,appId,remark,sourceUid,sourceUsername,realName,phone,goodsId);  
    }

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getMerchantOrderId() {
		return merchantOrderId;
	}



	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}



	public String getMerchantId() {
		return merchantId;
	}



	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}






	public String getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
	}

	public String getAppId() {
		return appId;
	}



	public void setAppId(String appId) {
		this.appId = appId;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public String getSourceUid() {
		return sourceUid;
	}



	public void setSourceUid(String sourceUid) {
		this.sourceUid = sourceUid;
	}



	public String getSourceUsername() {
		return sourceUsername;
	}



	public void setSourceUsername(String sourceUsername) {
		this.sourceUsername = sourceUsername;
	}



	public String getRealName() {
		return realName;
	}



	public void setRealName(String realName) {
		this.realName = realName;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getGoodsId() {
		return goodsId;
	}



	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}


		
}