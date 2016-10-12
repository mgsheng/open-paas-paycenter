package cn.com.open.pay.platform.manager.department.model;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;
/**
 * 该类是支付银行的实体类
 * @author 
 *
 */
public class DictTradePayment extends AbstractDomain{
	private static final long serialVersionUID = 1L;
	private Integer id;//ID 
	private String paymentName;
	private Double paymentType;
	private String rate;
	private String remark;
	private String icon;
	private Integer paymentSort;
	private Integer paymentStatus;
	
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
	public Double getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Double paymentType) {
		this.paymentType = paymentType;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getPaymentSort() {
		return paymentSort;
	}
	public void setPaymentSort(Integer paymentSort) {
		this.paymentSort = paymentSort;
	}
	public Integer getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
}
