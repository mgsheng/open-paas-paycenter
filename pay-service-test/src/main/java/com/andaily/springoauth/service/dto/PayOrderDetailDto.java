package com.andaily.springoauth.service.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 
 */
public class PayOrderDetailDto implements Serializable {
	
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
	
	private String payDate;
	
	private String payChannel;
	
	private String projectNumber;
	
	private String projectName;
	
	private String merchantOrderId;
	private String savePayOrderDetailUri;
	private String managerCenterName;
	
	
	public String getManagerCenterName() {
		return managerCenterName;
	}

	public void setManagerCenterName(String managerCenterName) {
		this.managerCenterName = managerCenterName;
	}
	private String appId;
	
	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?regNumber=%s&learningCenterCode=%s&learningCenterName=%s&province=%s&learningBatches=%s&learningStatus=%s&specialty=%s&level=%s&studentName=%s&cardNo=%s&studentId=%s&costAttribution=%s&exacct=%s&payAmount=%s&payCharge=%s&payDate=%s&payChannel=%s&projectNumber=%s&projectName=%s&merchantOrderId=%s&appId=%s&managerCenterName=%s",
        		savePayOrderDetailUri,regNumber,learningCenterCode,URLEncoder.encode(learningCenterName, "UTF-8"),URLEncoder.encode(province, "UTF-8"),learningBatches,URLEncoder.encode(learningStatus, "UTF-8"),URLEncoder.encode(specialty, "UTF-8"),URLEncoder.encode(level, "UTF-8"),URLEncoder.encode(studentName, "UTF-8"),cardNo,studentId,URLEncoder.encode(costAttribution, "UTF-8"),URLEncoder.encode(exacct, "UTF-8"),payAmount,payCharge,payDate,URLEncoder.encode(payChannel, "UTF-8"),projectNumber,URLEncoder.encode(projectName, "UTF-8"),merchantOrderId,appId,URLEncoder.encode(managerCenterName, "UTF-8"));  
    }
	public String getSavePayOrderDetailUri() {
		return savePayOrderDetailUri;
	}
	public void setSavePayOrderDetailUri(String savePayOrderDetailUri) {
		this.savePayOrderDetailUri = savePayOrderDetailUri;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	
	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
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