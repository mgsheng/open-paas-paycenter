package cn.com.open.pay.platform.manager.paychannel.model;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;

/**
 * 该类是支付渠道字典（渠道编码）实体类
 * @author lvjq
 *
 */
public class PayChannelDictionary extends AbstractDomain{
	private static final long serialVersionUID = 1L;
	private Integer id;//ID 
	private String channelName;//渠道名称
	private String channelID;//渠道ID
	private String channelStatus;//状态，1：有效 0：无效'
	private String channelCode;//渠道编码
	private Integer pageSize;//每页的显示条数
	private Integer startRow;//每页的开始记录
	
	public PayChannelDictionary() {
		super();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	public String getChannelStatus() {
		return channelStatus;
	}
	public void setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
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
		result = prime * result
				+ ((channelCode == null) ? 0 : channelCode.hashCode());
		result = prime * result
				+ ((channelID == null) ? 0 : channelID.hashCode());
		result = prime * result
				+ ((channelName == null) ? 0 : channelName.hashCode());
		result = prime * result
				+ ((channelStatus == null) ? 0 : channelStatus.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((pageSize == null) ? 0 : pageSize.hashCode());
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
		PayChannelDictionary other = (PayChannelDictionary) obj;
		if (channelCode == null) {
			if (other.channelCode != null)
				return false;
		} else if (!channelCode.equals(other.channelCode))
			return false;
		if (channelID == null) {
			if (other.channelID != null)
				return false;
		} else if (!channelID.equals(other.channelID))
			return false;
		if (channelName == null) {
			if (other.channelName != null)
				return false;
		} else if (!channelName.equals(other.channelName))
			return false;
		if (channelStatus == null) {
			if (other.channelStatus != null)
				return false;
		} else if (!channelStatus.equals(other.channelStatus))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pageSize == null) {
			if (other.pageSize != null)
				return false;
		} else if (!pageSize.equals(other.pageSize))
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
		return "PayChannelDictionary [id=" + id + ", channelName="
				+ channelName + ", channelID=" + channelID + ", channelStatus="
				+ channelStatus + ", channelCode=" + channelCode
				+ ", pageSize=" + pageSize + ", startRow=" + startRow + "]";
	}
	
}
