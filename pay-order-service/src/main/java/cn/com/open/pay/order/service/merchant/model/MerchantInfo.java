package cn.com.open.pay.order.service.merchant.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 */
public class MerchantInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
    
	private String merchantName;
	
	private Double status;
		
	private Date createDate;
	
	private String operater;
	
	private String notifyUrl;
	
	private String returnUrl;
	
	private String payKey;
	
	private String productName;
	
	private String contact;
	
	private String phone;
	
	private String mobile;
	
	private String email;
	
	private Double dayNorm;
	
	private Double monthNorm;
	
	private Double singleNorm;

	public MerchantInfo(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
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

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getStatus() {
		return status;
	}

	public void setStatus(Double status) {
		this.status = status;
	}

	public Double getDayNorm() {
		return dayNorm;
	}

	public void setDayNorm(Double dayNorm) {
		this.dayNorm = dayNorm;
	}

	public Double getMonthNorm() {
		return monthNorm;
	}

	public void setMonthNorm(Double monthNorm) {
		this.monthNorm = monthNorm;
	}

	public Double getSingleNorm() {
		return singleNorm;
	}

	public void setSingleNorm(Double singleNorm) {
		this.singleNorm = singleNorm;
	}
		
}