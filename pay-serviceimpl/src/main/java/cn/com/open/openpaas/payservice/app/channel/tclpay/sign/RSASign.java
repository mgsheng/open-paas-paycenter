package cn.com.open.openpaas.payservice.app.channel.tclpay.sign;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.bouncycastle.util.encoders.Base64;

import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.CAP12CertTool;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HexStringByte;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytStringUtils;

/**
 * 名称：RSA工具类
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class RSASign {

	private String certFilePath = null;
	private String password = null;
	private String hexCert = null;
	private String rootCertPath = null;

	public RSASign(String certFilePath, String password) {
		this.certFilePath = certFilePath;
		this.password = password;
	}
	public RSASign(String rootCertPath)
	  {
	    this.rootCertPath = rootCertPath;
	  }

	public String sign(String inData, String charset) throws Exception {
		String singData = null;

		if (HytStringUtils.isEmpty(charset)) {
			charset = HytConstants.CHARSET_GBK;
		}
		CAP12CertTool c = new CAP12CertTool(this.certFilePath, this.password);
		X509Certificate cert = c.getCert();
		byte[] si = c.getSignData(inData.getBytes(charset));
		byte[] cr = cert.getEncoded();
		this.hexCert = HexStringByte.byteToHex(cr);
		singData = HexStringByte.byteToHex(si);
		return singData;
	}

	 

	private X509Certificate getCertFromHexString(String hexCert)
			throws SecurityException {
		ByteArrayInputStream bIn = null;
		X509Certificate certobj = null;
		try {
			byte[] cert = HexStringByte.hexToByte(hexCert.getBytes());
			CertificateFactory fact = CertificateFactory.getInstance("X.509");
			bIn = new ByteArrayInputStream(cert);
			certobj = (X509Certificate) fact.generateCertificate(bIn);
			bIn.close();
			bIn = null;
		} catch (CertificateException e) {
			e.printStackTrace();
			try {
				if (bIn != null)
					bIn.close();
			} catch (IOException localIOException1) {
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				if (bIn != null)
					bIn.close();
			} catch (IOException localIOException2) {
			}
		} finally {
			try {
				if (bIn != null)
					bIn.close();
			} catch (IOException localIOException3) {
			}
		}
		return certobj;
	}

	public static byte[] checkPEM(byte[] paramArrayOfByte) {
		String str1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/+= \r\n-";
		for (int i = 0; i < paramArrayOfByte.length; i++)
			if (str1.indexOf(paramArrayOfByte[i]) == -1)
				return null;
		StringBuffer localStringBuffer = new StringBuffer(
				paramArrayOfByte.length);
		String str2 = new String(paramArrayOfByte);
		for (int j = 0; j < str2.length(); j++)
			if ((str2.charAt(j) != ' ') && (str2.charAt(j) != '\r')
					&& (str2.charAt(j) != '\n'))
				localStringBuffer.append(str2.charAt(j));
		return localStringBuffer.toString().getBytes();
	}
	 public boolean verify(String oridata, String signData, String svrCert, String encoding)
	  {
	    boolean res = false;

	    if (isEmpty(encoding))
	    {
	      encoding = "GBK";
	    }
	    try
	    {
	      byte[] signDataBytes = HexStringByte.hexToByte(signData.getBytes());
	      byte[] inDataBytes = oridata.getBytes(encoding);

	      byte[] signaturepem = checkPEM(signDataBytes);
	      if (signaturepem != null) {
	        signDataBytes = Base64.decode(signaturepem);
	      }
	      X509Certificate cert = getCertFromHexString(svrCert);
	      if (cert != null) {
	        PublicKey pubKey = cert.getPublicKey();
	        Signature signet = Signature.getInstance("SHA1WITHRSA");
	        signet.initVerify(pubKey);
	        signet.update(inDataBytes);
	        res = signet.verify(signDataBytes);
	      }

	    }
	    catch (InvalidKeyException e)
	    {
	      e.printStackTrace();
	    }
	    catch (NoSuchAlgorithmException e) {
	      e.printStackTrace();
	    }
	    catch (SignatureException e) {
	      e.printStackTrace();
	    }
	    catch (SecurityException e) {
	      e.printStackTrace();
	    }
	    catch (UnsupportedEncodingException e) {
	      e.printStackTrace();
	    }

	    return res;
	  }
	 public static boolean isEmpty(String s)
	  {
	    return (s == null) || ("".equals(s.trim()));
	  }
	public String getCertInfo() {
		return this.hexCert;
	}
	 

  
}
