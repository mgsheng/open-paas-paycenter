package cn.com.open.openpaas.payservice.app.channel.zxpt.zx;

/**
 * 
 * @author xiaoshan
 *
 */
public class BqsFraudlistRequest {
	
	/**
	 * 请求订单号
	 */
    private String orderId;
	
	/**
	 * 授权渠道
	 */
    private String channelNo;
	
	/**
	 * 证件号 ：目前只支持身份证号码查询
	 */
	private String certNo;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 授权码（白骑士）
	 */
	private String entityAuthCode;

	/**
	 * 授权时间（白骑士）
	 */
	private String entityAuthDate;
	
	/**
	 * 保留字段
	 */
	private String resv;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEntityAuthCode() {
		return entityAuthCode;
	}

	public void setEntityAuthCode(String entityAuthCode) {
		this.entityAuthCode = entityAuthCode;
	}

	public String getEntityAuthDate() {
		return entityAuthDate;
	}

	public void setEntityAuthDate(String entityAuthDate) {
		this.entityAuthDate = entityAuthDate;
	}

	public String getResv() {
		return resv;
	}

	public void setResv(String resv) {
		this.resv = resv;
	}
	
	
	
	
	
	

}
