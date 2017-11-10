package cn.com.open.openpaas.payservice.app.order.model;
import java.io.Serializable;
import java.util.Date;

public class PayLoanInfo  implements Serializable {
	
		/**
	 *易汇金分期贷款 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id; //主键	private String merchantId; //商户号	private String orderId; //订单id	private String orderAmount; //分期金额	private String repaymentMethod; //还款方式	private String terms; //分期期数	private Integer status; //放款状态	private String annualRate; //年利率	private Date createTime; //创建时间	private Date completeTime; //完成时间	private Date dueDate; //应还款日期	private Double totalAmount; //还款总金额	private Double estimatedDueInterest; //还款利息	private Double duePrincipal; //还款本金	private Date overDueDate; //逾期日期	private Date breakContractDate; //违约日期  
	private Integer appId;
	
	
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getRepaymentMethod() {
		return repaymentMethod;
	}
	public void setRepaymentMethod(String repaymentMethod) {
		this.repaymentMethod = repaymentMethod;
	}
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getAnnualRate() {
		return annualRate;
	}
	public void setAnnualRate(String annualRate) {
		this.annualRate = annualRate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getEstimatedDueInterest() {
		return estimatedDueInterest;
	}
	public void setEstimatedDueInterest(Double estimatedDueInterest) {
		this.estimatedDueInterest = estimatedDueInterest;
	}
	public Double getDuePrincipal() {
		return duePrincipal;
	}
	public void setDuePrincipal(Double duePrincipal) {
		this.duePrincipal = duePrincipal;
	}
	public Date getOverDueDate() {
		return overDueDate;
	}
	public void setOverDueDate(Date overDueDate) {
		this.overDueDate = overDueDate;
	}
	public Date getBreakContractDate() {
		return breakContractDate;
	}
	public void setBreakContractDate(Date breakContractDate) {
		this.breakContractDate = breakContractDate;
	}
	
	
}

