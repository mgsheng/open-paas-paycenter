package cn.com.open.pay.platform.manager.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 */
public class MerchantOrderInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id;
    
	private String channelOrderId;
	
	private Integer merchantId;
		
	private String merchantVersion;
	
	private String merchantOrderId;
	
	private String merchantOrderDate;
	
	private String merchantProductName;
	
	private String merchantProductId;
	
	private String merchantProductDesc;
	
	private Double orderAmount;
	
	private Double amount;
	
	private Double payCharge;
	
	private Double payBash;
	
	private Double payAmount;
	
	private Double payDivide;
	
	private Integer status;
	
	private String statusCode;
	
	private Date createDate = new Date();
	
	private Date dealDate;
	
	private String payOrderId;
	
	private Integer payStatus;
	
	private String payStatusCode;
	
	private Integer notifyStatus;
	
	private String notifyUrl;
	
	private String returnUrl;
	
	private String parameter1;
	
	private String appChannelId;
	
	private Integer notifyTimes;
	
	private Date notifyDate;
	
	private Integer paymentId;
	
	private Integer channelId;
	
	private Integer sourceType;
	
	private String memo;
	
	private String guid;
	
	private String userName;
	
	private String sourceUid;
	
	private String sourceUserName;
	
	private String clientostype;
	
	private String clientosversion;
	
	private String ip;
	
	private String imei;
	
	private String mac;
	
	private String appId;
	
	private String statusMessage;
	private Integer businessType;
	
	private String startDate;
	private String endDate;
	private String foundDate; //创建时间
	private String businessDate;
	private String channelName;
	private String payStatusName;
	private String paymentName;
	private String sourceTypeName;
	private Integer startRow;
	private Integer pageSize;
	
	private Double countOrderAmount;
	private String dimension;
	

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public MerchantOrderInfo(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannelOrderId() {
		return channelOrderId;
	}

	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantVersion() {
		return merchantVersion;
	}

	public void setMerchantVersion(String merchantVersion) {
		this.merchantVersion = merchantVersion;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getMerchantOrderDate() {
		return merchantOrderDate;
	}

	public void setMerchantOrderDate(String merchantOrderDate) {
		this.merchantOrderDate = merchantOrderDate;
	}

	public String getMerchantProductName() {
		return merchantProductName;
	}

	public void setMerchantProductName(String merchantProductName) {
		this.merchantProductName = merchantProductName;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Double getPayCharge() {
		return payCharge;
	}

	public void setPayCharge(Double payCharge) {
		this.payCharge = payCharge;
	}

	public Double getPayBash() {
		return payBash;
	}

	public void setPayBash(Double payBash) {
		this.payBash = payBash;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Double getPayDivide() {
		return payDivide;
	}

	public void setPayDivide(Double payDivide) {
		this.payDivide = payDivide;
	}


	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getParameter1() {
		return parameter1;
	}

	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}

	public String getAppChannelId() {
		return appChannelId;
	}

	public void setAppChannelId(String appChannelId) {
		this.appChannelId = appChannelId;
	}

	public Date getNotifyDate() {
		return notifyDate;
	}

	public void setNotifyDate(Date notifyDate) {
		this.notifyDate = notifyDate;
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getPayStatusCode() {
		return payStatusCode;
	}

	public void setPayStatusCode(String payStatusCode) {
		this.payStatusCode = payStatusCode;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSourceUid() {
		return sourceUid;
	}

	public void setSourceUid(String sourceUid) {
		this.sourceUid = sourceUid;
	}

	public String getSourceUserName() {
		return sourceUserName;
	}

	public void setSourceUserName(String sourceUserName) {
		this.sourceUserName = sourceUserName;
	}

	public String getClientostype() {
		return clientostype;
	}

	public void setClientostype(String clientostype) {
		this.clientostype = clientostype;
	}

	public String getClientosversion() {
		return clientosversion;
	}

	public void setClientosversion(String clientosversion) {
		this.clientosversion = clientosversion;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDealDate() {
		return dealDate;
	}

	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(Integer notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public Integer getNotifyTimes() {
		return notifyTimes;
	}

	public void setNotifyTimes(Integer notifyTimes) {
		this.notifyTimes = notifyTimes;
	}
	
	public void setNotifyTimes() {
		this.notifyTimes = this.notifyTimes+1;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getMerchantProductId() {
		return merchantProductId;
	}

	public void setMerchantProductId(String merchantProductId) {
		this.merchantProductId = merchantProductId;
	}

	public String getMerchantProductDesc() {
		return merchantProductDesc;
	}

	public void setMerchantProductDesc(String merchantProductDesc) {
		this.merchantProductDesc = merchantProductDesc;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFoundDate() {
		return foundDate;
	}

	public void setFoundDate(String foundDate) {
		this.foundDate = foundDate;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPayStatusName() {
		return payStatusName;
	}

	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public void setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
	}

	public Double getCountOrderAmount() {
		return countOrderAmount;
	}

	public void setCountOrderAmount(Double countOrderAmount) {
		this.countOrderAmount = countOrderAmount;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	
}