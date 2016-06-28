package cn.com.open.openpaas.payservice.app.appUser.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
public class AppUser implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer appUid;
    
	private Integer appId;
	
	private Integer userId;
	
	private Integer isCache;
	
	private String sourceId;
	
	private Long createTime = new Date().getTime();
	
	private Long lastloginTime;
	
	private Integer loginTimes;

    public AppUser() {
    	
    }

	public AppUser(Integer appId, Integer userId,String sourceId) {
		this.appId = appId;
		this.userId = userId;
		this.sourceId = sourceId;
	}
	
	public AppUser(Integer userId,String sourceId) {
		this.userId = userId;
		this.sourceId = sourceId;
	}

	public Integer appUid() {
		return appUid;
	}

	public Integer appId() {
		return appId;
	}

	public Integer userId() {
		return userId;
	}
	
	public Integer isCache() {
		return isCache;
	}

	public String sourceId() {
		return sourceId;
	}

	public Long createTime() {
		return createTime;
	}

	public Long lastloginTime() {
		return lastloginTime;
	}

	public Integer loginTimes() {
		return loginTimes;
	}

	public void appUid(Integer appUid) {
		this.appUid = appUid;
	}

	public void appId(Integer appId) {
		this.appId = appId;
	}

	public void userId(Integer userId) {
		this.userId = userId;
	}
	
	public void isCache(Integer isCache) {
		this.isCache = isCache;
	}

	public void sourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public void createTime(Long createTime) {
		this.createTime = createTime;
	}

	public void lastloginTime(Long lastloginTime) {
		this.lastloginTime = lastloginTime;
	}

	public void loginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

}