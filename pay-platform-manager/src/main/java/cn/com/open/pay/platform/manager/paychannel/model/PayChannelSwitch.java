package cn.com.open.pay.platform.manager.paychannel.model;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;

/**
 * 该类是支付渠道
 * @author lvjq
 *
 */
public class PayChannelSwitch extends AbstractDomain{
	private static final long serialVersionUID = 1L;
	private Integer id;//ID 
	private String channelName;//渠道名称
	private String channelValue;//状态，1：有效 0：无效'
	private Integer pageSize;//每页的显示条数
	private Integer startRow;//每页的开始记录
	
	public PayChannelSwitch() {
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

	public String getChannelValue() {
		return channelValue;
	}

	public void setChannelValue(String channelValue) {
		this.channelValue = channelValue;
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
	
	
	
}
