package cn.com.open.openpaas.payservice.app.channel.tclpay.config;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * 名称：RSA签名报文换
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class SignedPacket {

	private byte[] signData;
	private PublicKey pubKey;
	private X509Certificate cert;
	private PrivateKey priKey;

	public byte[] getSignData() {
		return signData;
	}

	public void setSignData(byte[] signData) {
		this.signData = signData;
	}

	public PublicKey getPubKey() {
		return pubKey;
	}

	public void setPubKey(PublicKey pubKey) {
		this.pubKey = pubKey;
	}

	public X509Certificate getCert() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}

	public PrivateKey getPriKey() {
		return priKey;
	}

	public void setPriKey(PrivateKey priKey) {
		this.priKey = priKey;
	}

}
