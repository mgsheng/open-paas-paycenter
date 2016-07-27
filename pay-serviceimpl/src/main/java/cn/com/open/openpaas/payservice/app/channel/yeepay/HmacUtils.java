package cn.com.open.openpaas.payservice.app.channel.yeepay;

public class HmacUtils {
	/**
	 * 生成hmac方法
	 * 业务类型
	 * @param p0_Cmd
	 * 商户编号
	 * @param p1_MerId
	 * 商户订单号
	 * @param p2_Order
	 * 支付金额
	 * @param p3_Amt
	 * 交易币种
	 * @param p4_Cur
	 * 商品名称
	 * @param p5_Pid
	 * 商品种类
	 * @param p6_Pcat
	 * 商品描述
	 * @param p7_Pdesc
	 * 商户接收支付成功数据的地址
	 * @param p8_Url
	 * 送货地址
	 * @param p9_SAF
	 * 商户扩展信息
	 * @param pa_MP
	 * 银行编码
	 * @param pd_FrpId
	 * 应答机制
	 * @param pr_NeedResponse
	 * 商户密钥
	 * @param keyValue
	 * @return
	 */
	public static String getReqMd5HmacForOnlinePayment(String p0_Cmd,String p1_MerId,
			String p2_Order, String p3_Amt, String p4_Cur,String p5_Pid, String p6_Pcat,
			String p7_Pdesc,String p8_Url, String p9_SAF,String pa_MP,String pd_FrpId,
			String pr_NeedResponse,String keyValue) {
		  StringBuffer sValue = new StringBuffer();
		// 业务类型
		sValue.append(p0_Cmd);
		// 商户编号
		sValue.append(p1_MerId);
		// 商户订单号
		sValue.append(p2_Order);
		// 支付金额
		sValue.append(p3_Amt);
		// 交易币种
		sValue.append(p4_Cur);
		// 商品名称
		sValue.append(p5_Pid);
		// 商品种类
		sValue.append(p6_Pcat);
		// 商品描述
		sValue.append(p7_Pdesc);
		// 商户接收支付成功数据的地址
		sValue.append(p8_Url);
		// 送货地址
		sValue.append(p9_SAF);
		// 商户扩展信息
		sValue.append(pa_MP);
		// 银行编码
		sValue.append(pd_FrpId);
		// 应答机制
		sValue.append(pr_NeedResponse);
		
		String sNewString = null;

		sNewString = DigestUtil.hmacSign(sValue.toString(), keyValue);
		return (sNewString);
	}

	

}
