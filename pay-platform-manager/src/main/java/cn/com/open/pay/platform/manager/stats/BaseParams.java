package cn.com.open.pay.platform.manager.stats;

import org.apache.commons.lang.StringUtils;

public class BaseParams {

	private int pageNumber;
	
	private int pageSize;
	
	private String sortBy;
	
	private String dir;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortBy() {
		return sortBy;
	}

	/**
	 * 排序方式
	 * @param sortBy ASC DESC
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getDir() {
		return dir;
	}

	/**
	 * 排序字段
	 * @param dir
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	/**
	 * 获取排序语句
	 * @return
	 */
	public String getOrderBy(){
		if(StringUtils.isBlank(sortBy) || StringUtils.isBlank(dir)){
			return "";
		}
		return " order by " + dir + " " + sortBy + " ";
	}
}
