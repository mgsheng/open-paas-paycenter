package cn.com.open.pay.platform.manager.department.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;
/**
 * 该类是渠道的实体类
 * @author 
 *
 */
public class DictTradeChannel extends AbstractDomain{
	private static final long serialVersionUID = 1L;
	private Integer id;//ID 
	private String channelName;
	private Double channelStatus;
	private Double priority;
	private Date createDate;
	private String memo;
	private String merId;
	private String keyValue;
	private String notifyUrl;
	private String other;
	private Integer paymentType;
	private String backUrl;
	private Integer type;
	private String sighType;
	private String inputCharset;
	private String paymentChannel;
	
	private Integer pageSize;//每页的显示条数
	private Integer startRow;//每页的开始记录
	private String foundDate;
	private String  channelStatusName;
	private String priorityName;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getSighType() {
		return sighType;
	}
	public void setSighType(String sighType) {
		this.sighType = sighType;
	}
	public String getInputCharset() {
		return inputCharset;
	}
	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getStartRow() {
		return startRow;
	}
	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
	public String getFoundDate() {
		return foundDate;
	}
	public void setFoundDate(String foundDate) {
		this.foundDate = foundDate;
	}
	public String getChannelStatusName() {
		return channelStatusName;
	}
	public void setChannelStatusName(String channelStatusName) {
		this.channelStatusName = channelStatusName;
	}
	public String getPriorityName() {
		return priorityName;
	}
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	
	
	
	
}
