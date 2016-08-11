package cn.com.open.pay.platform.manager.stats.model;

import java.util.Date;

public class UserStatistics  {

	private static final long serialVersionUID = 1L;
	
	//统计维度
	public static int STATISTIC_SCOPE_DAY = 1;		//日
	public static int STATISTIC_SCOPE_WEEK = 2;		//周
	public static int STATISTIC_SCOPE_MONTH = 3;	//月
	public static int STATISTIC_SCOPE_YEAR = 4;		//年
	
	private int appId;
	
	private Date statisticDate;//统计日期
	
	private int statisticScope;//统计维度 1:天 2:周 3:月 4:年
	
	private int addUserTotal;//新增用户数
	
	private int userActiveTotal;//用户活跃数
	
	private int userTotal;//总用户数

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public Date getStatisticDate() {
		return statisticDate;
	}

	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}

	public int getStatisticScope() {
		return statisticScope;
	}

	public void setStatisticScope(int statisticScope) {
		this.statisticScope = statisticScope;
	}

	public int getAddUserTotal() {
		return addUserTotal;
	}

	public void setAddUserTotal(int addUserTotal) {
		this.addUserTotal = addUserTotal;
	}

	public int getUserActiveTotal() {
		return userActiveTotal;
	}

	public void setUserActiveTotal(int userActiveTotal) {
		this.userActiveTotal = userActiveTotal;
	}

	public int getUserTotal() {
		return userTotal;
	}

	public void setUserTotal(int userTotal) {
		this.userTotal = userTotal;
	}
}
