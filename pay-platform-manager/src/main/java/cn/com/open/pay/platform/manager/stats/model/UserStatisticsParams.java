package cn.com.open.pay.platform.manager.stats.model;

import cn.com.open.pay.platform.manager.stats.BaseParams;

public class UserStatisticsParams extends BaseParams {
	
	private Integer appId;
	
	private String startDate;
	
	private String endDate;
	
	private Integer statisticScope;
	
	private String appIds;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getStatisticScope() {
		return statisticScope;
	}

	public void setStatisticScope(Integer statisticScope) {
		this.statisticScope = statisticScope;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getAppIds() {
		return appIds;
	}

	public void setAppIds(String appIds) {
		this.appIds = appIds;
	}
}
