package cn.com.open.openpaas.payservice.app.ditch.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 */
public class DictTradePaymentDitch implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String icon;
	private String payment_value;
	private String payment_name;
	private Integer payment_type;
	private Integer payment_sort;
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getPayment_value() {
		return payment_value;
	}
	public void setPayment_value(String payment_value) {
		this.payment_value = payment_value;
	}
	public String getPayment_name() {
		return payment_name;
	}
	public void setPayment_name(String payment_name) {
		this.payment_name = payment_name;
	}
	public Integer getPayment_sort() {
		return payment_sort;
	}
	public void setPayment_sort(Integer payment_sort) {
		this.payment_sort = payment_sort;
	}
	public Integer getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(Integer payment_type) {
		this.payment_type = payment_type;
	}

	
	
	
	
}