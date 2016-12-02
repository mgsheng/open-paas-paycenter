package cn.com.open.pay.order.service.statement.model;

import java.io.Serializable;

/**
 * 
 */
public class PayOrderStatement implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String merchantOrderId;
	private String payOrderId;
	private Double orderAmount;
	private String feeType;
	private String createOrderDate;
	private String payOrderDate;
	private String parmenter1;
	private Double payCharge;
	private String statementStatus;
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
	public String getPayOrderId() {
		return payOrderId;
	}
	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getCreateOrderDate() {
		return createOrderDate;
	}
	public void setCreateOrderDate(String createOrderDate) {
		this.createOrderDate = createOrderDate;
	}
	public String getPayOrderDate() {
		return payOrderDate;
	}
	public void setPayOrderDate(String payOrderDate) {
		this.payOrderDate = payOrderDate;
	}
	public String getParmenter1() {
		return parmenter1;
	}
	public void setParmenter1(String parmenter) {
		this.parmenter1 = parmenter;
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
	public String getStatementStatus() {
		return statementStatus;
	}
	public void setStatementStatus(String statementStatus) {
		this.statementStatus = statementStatus;
	}
	


}