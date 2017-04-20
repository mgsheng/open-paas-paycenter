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