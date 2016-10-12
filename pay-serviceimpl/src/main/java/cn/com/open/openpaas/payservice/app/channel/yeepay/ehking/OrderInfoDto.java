package cn.com.open.openpaas.payservice.app.channel.yeepay.ehking;

import java.io.Serializable;

public class OrderInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String scanCode;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getScanCode() {
		return scanCode;
	}
	public void setScanCode(String scanCode) {
		this.scanCode = scanCode;
	}

}
