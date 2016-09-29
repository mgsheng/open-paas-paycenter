package cn.com.open.pay.platform.manager.department.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;
/**
 * 该类是商户的实体类
 * @author 
 *
 */
public class MerchantInfo extends AbstractDomain{
	private static final long serialVersionUID = 1L;
	private Integer id;//ID 
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
	private String memo;
	
	private String foundDate;
	private String statusName;
	

	private Integer pageSize;//每页的显示条数
	private Integer startRow;//每页的开始记录
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
	public Double getStatus() {
		return status;
	}
	public void setStatus(Double status) {
		this.status = status;
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	

}
