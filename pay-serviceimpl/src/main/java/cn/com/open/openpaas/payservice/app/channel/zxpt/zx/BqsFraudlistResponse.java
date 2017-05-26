package cn.com.open.openpaas.payservice.app.channel.zxpt.zx;

/**
 * @ClassName BqsBlacklistResponse
 * @author xiaoshan
 *
 */
public class BqsFraudlistResponse {
	/**
	 * 业务平台订单号
	 */
	private String orderId;
	/**
	 * 征信平台订单号
	 */
	private String orderNo;
	/**
	 * 错误代码
	 */
	private String errorCode;
	/**
	 * 错误消息
	 */
	private String errorMessage;
	/**
	 * 风险得分
	 */
	private String score;
	/**
	 * 决策结果
	 */
	private String decision;
	/**
	 * 明细
	 */
	private String resultMap;
	/**
	 * 保留字段
	 */
	private String resv;
	
	
	
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public String getResultMap() {
		return resultMap;
	}
	public void setResultMap(String resultMap) {
		this.resultMap = resultMap;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getResv() {
		return resv;
	}
	public void setResv(String resv) {
		this.resv = resv;
	}
	
}
