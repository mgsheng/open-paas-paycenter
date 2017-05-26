package cn.com.open.openpaas.payservice.app.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户卡号信息表
 */
public class PayCardInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String appId;
	private Integer id;
	private String identityId;
	private String identityType;
	private String cardNo;
	private String userName;
	private String oricardNo;
	private String phone;
	private Date createTime;
	private Date updateTime;
	private Integer status;//1:
	private String userId;
	private String terminalId;
	private String lastLoginTerminalId;
	private String isSetPaypwd;
	private String registIp;
	private String lastloginIp;
	private Date   lastloginTime;
	private Date registTime;
	
	
	
	
	public Date getRegistTime() {
		return registTime;
	}
	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}
	public String getRegistIp() {
		return registIp;
	}
	public void setRegistIp(String registIp) {
		this.registIp = registIp;
	}
	public String getLastloginIp() {
		return lastloginIp;
	}
	public void setLastloginIp(String lastloginIp) {
		this.lastloginIp = lastloginIp;
	}
	public Date getLastloginTime() {
		return lastloginTime;
	}
	public void setLastloginTime(Date lastloginTime) {
		this.lastloginTime = lastloginTime;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getLastLoginTerminalId() {
		return lastLoginTerminalId;
	}
	public void setLastLoginTerminalId(String lastLoginTerminalId) {
		this.lastLoginTerminalId = lastLoginTerminalId;
	}
	public String getIsSetPaypwd() {
		return isSetPaypwd;
	}
	public void setIsSetPaypwd(String isSetPaypwd) {
		this.isSetPaypwd = isSetPaypwd;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOricardNo() {
		return oricardNo;
	}
	public void setOricardNo(String oricardNo) {
		this.oricardNo = oricardNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	

}