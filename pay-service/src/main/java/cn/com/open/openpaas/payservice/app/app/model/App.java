package cn.com.open.openpaas.payservice.app.app.model;

import java.io.Serializable;

/**
 * 
 */
public class App implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
    
	private Integer applyuser;
	
	private Integer user;
	
	private Long createTime;
	
	private Long applyTime;
	
	private String appStauts;
	
	private String appkey;
	
	private String applyStatus;
	
	private String appsecret;
	
	private String description;
	
	private String idkey;
	
	private String loginType;
	
	private String name;
	
	private String status;
	
	private String type;
	
	private String webServerRedirectUri;
	private String authorizedGrantTypes;
	private String appAuthorities;
	private String scope;
	private String authorities;
	private Integer accessTokenValidity;
	private Integer refreshTokenValidity;
	private Integer archived;
	private Integer trusted;
	private String resourceIds;
	private String additionalInformation;
	private String icon;
	
	private String callbackUrl;

    public App() {
    	
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApplyuser() {
		return applyuser;
	}

	public void setApplyuser(Integer applyuser) {
		this.applyuser = applyuser;
	}

	public Integer getUser() {
		return user;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}

	public String getAppStauts() {
		return appStauts;
	}

	public void setAppStauts(String appStauts) {
		this.appStauts = appStauts;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdkey() {
		return idkey;
	}

	public void setIdkey(String idkey) {
		this.idkey = idkey;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWebServerRedirectUri() {
		return webServerRedirectUri;
	}

	public void setWebServerRedirectUri(String webServerRedirectUri) {
		this.webServerRedirectUri = webServerRedirectUri;
	}

	public String getAuthorizedGrantTypes() {
		return authorizedGrantTypes;
	}

	public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Integer getAccessTokenValidity() {
		return accessTokenValidity;
	}

	public void setAccessTokenValidity(Integer accessTokenValidity) {
		this.accessTokenValidity = accessTokenValidity;
	}

	public Integer getRefreshTokenValidity() {
		return refreshTokenValidity;
	}

	public void setRefreshTokenValidity(Integer refreshTokenValidity) {
		this.refreshTokenValidity = refreshTokenValidity;
	}

	public Integer getArchived() {
		return archived;
	}

	public void setArchived(Integer archived) {
		this.archived = archived;
	}

	public Integer getTrusted() {
		return trusted;
	}

	public void setTrusted(Integer trusted) {
		this.trusted = trusted;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getAppAuthorities() {
		return appAuthorities;
	}

	public void setAppAuthorities(String appAuthorities) {
		this.appAuthorities = appAuthorities;
	}

}