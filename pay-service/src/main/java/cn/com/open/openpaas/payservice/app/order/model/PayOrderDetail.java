package cn.com.open.openpaas.payservice.app.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户订单详细信息表
 */
public class PayOrderDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
    
	private String regNumber;
	
	private String learningCenterCode;
		
	private String learningCenterName;
	
	private String province;
	
	private String learningBatches;
	
	private String learningStatus;
	
	private String specialty;
	private String level;
	
	private String studentName;
	
	private String cardNo;
	
	private String studentId;
	
	private String costAttribution;
	
	private String exacct;
	
	private String payAmount;
	
	private String payCharge;
	
	private Date payDate;
	
	private String payChannel;
	
	private String projectNumber;
	
	private String projectName;
	
	private String merchantOrderId;
	private String managerCenterName;
	
	private String appId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getManagerCenterName() {
		return managerCenterName;
	}
	public void setManagerCenterName(String managerCenterName) {
		this.managerCenterName = managerCenterName;
	}
	public String getRegNumber() {
		return regNumber;
	}
	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}
	public String getLearningCenterCode() {
		return learningCenterCode;
	}
	public void setLearningCenterCode(String learningCenterCode) {
		this.learningCenterCode = learningCenterCode;
	}
	public String getLearningCenterName() {
		return learningCenterName;
	}
	public void setLearningCenterName(String learningCenterName) {
		this.learningCenterName = learningCenterName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getLearningBatches() {
		return learningBatches;
	}
	public void setLearningBatches(String learningBatches) {
		this.learningBatches = learningBatches;
	}
	public String getLearningStatus() {
		return learningStatus;
	}
	public void setLearningStatus(String learningStatus) {
		this.learningStatus = learningStatus;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getCostAttribution() {
		return costAttribution;
	}
	public void setCostAttribution(String costAttribution) {
		this.costAttribution = costAttribution;
	}
	public String getExacct() {
		return exacct;
	}
	public void setExacct(String exacct) {
		this.exacct = exacct;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	public String getPayCharge() {
		return payCharge;
	}
	public void setPayCharge(String payCharge) {
		this.payCharge = payCharge;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}