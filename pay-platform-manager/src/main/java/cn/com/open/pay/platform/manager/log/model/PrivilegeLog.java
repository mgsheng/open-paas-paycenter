package cn.com.open.pay.platform.manager.log.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;
/**
 * 该类是日志的实体类
 * @author 
 *
 */
public class PrivilegeLog extends AbstractDomain{
	private static final long serialVersionUID = 1L;
	private Integer id;//ID 
	private String operator;
	private String operationContent;
	private String oneLevels;
	private String towLevels;
	private Integer operationAuthority;
	private Date operationTime;
	private String operationDescribe;
	private String operatorId;
	private String foundDate;
	
	private Integer pageSize;//每页的显示条数
	private Integer startRow;//每页的开始记录
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperationContent() {
		return operationContent;
	}
	public void setOperationContent(String operationContent) {
		this.operationContent = operationContent;
	}
	public String getOneLevels() {
		return oneLevels;
	}
	public void setOneLevels(String oneLevels) {
		this.oneLevels = oneLevels;
	}
	public String getTowLevels() {
		return towLevels;
	}
	public void setTowLevels(String towLevels) {
		this.towLevels = towLevels;
	}
	public Integer getOperationAuthority() {
		return operationAuthority;
	}
	public void setOperationAuthority(Integer operationAuthority) {
		this.operationAuthority = operationAuthority;
	}
	public Date getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
	public String getOperationDescribe() {
		return operationDescribe;
	}
	public void setOperationDescribe(String operationDescribe) {
		this.operationDescribe = operationDescribe;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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
	public String getFoundDate() {
		return foundDate;
	}
	public void setFoundDate(String foundDate) {
		this.foundDate = foundDate;
	}
	

}
