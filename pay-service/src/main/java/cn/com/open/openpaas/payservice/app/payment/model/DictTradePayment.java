package cn.com.open.openpaas.payservice.app.payment.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 */
public class DictTradePayment implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
    
	private String paymentName;
	
	private BigDecimal paymentType;
	
	private String rate;

	public DictTradePayment() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public BigDecimal getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(BigDecimal paymentType) {
		this.paymentType = paymentType;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	
}