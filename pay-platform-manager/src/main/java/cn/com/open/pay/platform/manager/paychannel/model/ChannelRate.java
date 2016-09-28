package cn.com.open.pay.platform.manager.paychannel.model;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;
/**
 * 该类是支付渠道费率实体类
 * @author lvjq
 *
 */
public class ChannelRate extends AbstractDomain{
	private static final long serialVersionUID = 1L;
	private Integer id;//ID 
	private String merchantID;//商户ID
	private String payChannelCode;//支付渠道代码
	private String payName;//支付名称
	private String payRate;//费率
	private Integer pageSize;//每页的显示条数
	private Integer startRow;//每页的开始记录
	
	public ChannelRate(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getPayChannelCode() {
		return payChannelCode;
	}

	public void setPayChannelCode(String payChannelCode) {
		this.payChannelCode = payChannelCode;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayRate() {
		return payRate;
	}

	public void setPayRate(String payRate) {
		this.payRate = payRate;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((merchantID == null) ? 0 : merchantID.hashCode());
		result = prime * result
				+ ((pageSize == null) ? 0 : pageSize.hashCode());
		result = prime * result
				+ ((payChannelCode == null) ? 0 : payChannelCode.hashCode());
		result = prime * result + ((payName == null) ? 0 : payName.hashCode());
		result = prime * result + ((payRate == null) ? 0 : payRate.hashCode());
		result = prime * result
				+ ((startRow == null) ? 0 : startRow.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChannelRate other = (ChannelRate) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (merchantID == null) {
			if (other.merchantID != null)
				return false;
		} else if (!merchantID.equals(other.merchantID))
			return false;
		if (pageSize == null) {
			if (other.pageSize != null)
				return false;
		} else if (!pageSize.equals(other.pageSize))
			return false;
		if (payChannelCode == null) {
			if (other.payChannelCode != null)
				return false;
		} else if (!payChannelCode.equals(other.payChannelCode))
			return false;
		if (payName == null) {
			if (other.payName != null)
				return false;
		} else if (!payName.equals(other.payName))
			return false;
		if (payRate == null) {
			if (other.payRate != null)
				return false;
		} else if (!payRate.equals(other.payRate))
			return false;
		if (startRow == null) {
			if (other.startRow != null)
				return false;
		} else if (!startRow.equals(other.startRow))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Rate [id=" + id + ", merchantID=" + merchantID
				+ ", payChannelCode=" + payChannelCode + ", payName=" + payName
				+ ", payRate=" + payRate + "]";
	}
	
}
