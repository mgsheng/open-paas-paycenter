package cn.com.open.openpaas.payservice.app.channel.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 */
public class DictTradeChannel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
    
	private String channelName;
	
	private Double channelStatus;
	
	private Double priority;
	
	private Long createDate;
	
	private String memo;
	
	private String merid;
	
	private String keyValue;
	
	private String annulannulcardrequrl;
	
	private String other;
	
	private String backurl;
	
	private Integer type;
    private String notifyUrl;
    private String paymentType;
    private String signType;
    private String inputCharset;
    private String paymentChannel;
    
    
	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public DictTradeChannel(){
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Double getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(Double channelStatus) {
		this.channelStatus = channelStatus;
	}

	public Double getPriority() {
		return priority;
	}

	public void setPriority(Double priority) {
		this.priority = priority;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMerid() {
		return merid;
	}

	public void setMerid(String merid) {
		this.merid = merid;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getAnnulannulcardrequrl() {
		return annulannulcardrequrl;
	}

	public void setAnnulannulcardrequrl(String annulannulcardrequrl) {
		this.annulannulcardrequrl = annulannulcardrequrl;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getBackurl() {
		return backurl;
	}

	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}