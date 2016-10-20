package cn.com.open.pay.platform.manager.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 */
public class MerchantOrderRefund implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
    
	private String merchantOrderId;
	
	private Integer merchantId;
	
	private Double refundMoney;
	
	private String appId;
	
	private String remark;
	
	private Date createTime = new Date();
	
	private String sourceUid;
	
	private String sourceUserName;
	
	private String realName;
	
	private String phone;
	
	private String GoodsId;
	
	private Integer startRow;
	private Integer pageSize;
	
	/*private String foundDate; //创建时间
	private String appName;
	private String channelName;
	private String merchantName;
	private String bankName;
	
	private Date startDate;//查询缴费日期开始
	private Date endDate;//查询缴费日期结束
*/
	public MerchantOrderRefund(){
		
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}


	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getSourceUid() {
		return sourceUid;
	}

	public void setSourceUid(String sourceUid) {
		this.sourceUid = sourceUid;
	}

	public String getSourceUserName() {
		return sourceUserName;
	}

	public void setSourceUserName(String sourceUserName) {
		this.sourceUserName = sourceUserName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public String getGoodsId() {
		return GoodsId;
	}

	public void setGoodsId(String goodsId) {
		GoodsId = goodsId;
	}

	public Double getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}