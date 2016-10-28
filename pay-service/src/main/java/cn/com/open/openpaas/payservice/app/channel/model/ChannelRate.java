package cn.com.open.openpaas.payservice.app.channel.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 */
public class ChannelRate implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private Integer id;
    private String  merchantId;
    private String  payChannelCode;
    private String  payName;
    private String  payRate;
    private String  channelId;
    private Integer sourceType;
    
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getPayChannelCode() {
		return payChannelCode;
	}
	public void setPayChannelCode(String payChannelCode) {
		this.payChannelCode = payChannelCode;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public String getPayRate() {
		return payRate;
	}
	public void setPayRate(String payRate) {
		this.payRate = payRate;
	}
    
	
}