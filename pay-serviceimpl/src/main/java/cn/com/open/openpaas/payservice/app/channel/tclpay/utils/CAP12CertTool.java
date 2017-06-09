package cn.com.open.openpaas.payservice.app.channel.tclpay.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import cn.com.open.openpaas.payservice.app.channel.tclpay.config.SignedPacket;


/**
 * 名称：CAP12证书工具类
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class CAP12CertTool {

	private static SignedPacket signedPacket;

	public CAP12CertTool(InputStream fileInputStream, String keyPass)
			throws Exception {
		signedPacket = getP12(fileInputStream, keyPass);
	}

	public CAP12CertTool(String path, String keyPass) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(new File(path));

		signedPacket = getP12(fileInputStream, keyPass);
	}

	@SuppressWarnings("rawtypes")
	private SignedPacket getP12(InputStream fileInputStream, String keyPass)
			throws Exception {
		SignedPacket sp = new SignedPacket();
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			char[] nPassword = (char[]) null;
			if ((keyPass == null) || (keyPass.trim().equals("")))
				nPassword = (char[]) null;
			else {
				nPassword = keyPass.toCharArray();
			}
			ks.load(fileInputStream, nPassword);
			Enumeration enum2 = ks.aliases();
			String keyAlias = null;
			if (enum2.hasMoreElements()) {
				keyAlias = (String) enum2.nextElement();
			}

			PrivateKey priKey = (PrivateKey) ks.getKey(keyAlias, nPassword);
			//System.out.println(priKey);
			Certificate cert = ks.getCertificate(keyAlias);
			PublicKey pubKey = cert.getPublicKey();
			sp.setCert((X509Certificate) cert);
			sp.setPubKey(pubKey);
			sp.setPriKey(priKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (fileInputStream != null)
				try {
					fileInputStream.close();
				} catch (IOException localIOException) {
					throw new Exception(localIOException.getMessage());
				}
		}
		return sp;
	}

	public X509Certificate getCert() {
		return signedPacket.getCert();
	}

	public PublicKey getPublicKey() {
		return signedPacket.getPubKey();
	}

	public PrivateKey getPrivateKey() {
		return signedPacket.getPriKey();
	}

	public byte[] getSignData(byte[] inData) throws SecurityException {
		byte[] res = (byte[]) null;
		try {
			Signature signet = Signature.getInstance("SHA1WITHRSA");
			signet.initSign(getPrivateKey());
			signet.update(inData);
			res = signet.sign();
		} catch (InvalidKeyException e) {
			throw new SecurityException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException(e.getMessage());
		} catch (SignatureException e) {
			throw new SecurityException(e.getMessage());
		}
		return res;
	}
}
