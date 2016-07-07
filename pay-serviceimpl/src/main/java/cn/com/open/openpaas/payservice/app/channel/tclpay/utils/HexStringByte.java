package cn.com.open.openpaas.payservice.app.channel.tclpay.utils;

/**
 * 名称：Hex进制转换
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class HexStringByte {

	public static String byteToHex(byte[] b) {
		String hs = "";
		String tmp = "";
		for (int n = 0; n < b.length; n++) {
			tmp = Integer.toHexString(b[n] & 0xFF);
			if (tmp.length() == 1)
				hs = hs + "0" + tmp;
			else {
				hs = hs + tmp;
			}
		}
		tmp = null;
		return hs.toUpperCase();
	}

	public static byte[] hexToByte(byte[] b) {
		if (b.length % 2 != 0) {
			throw new IllegalArgumentException(
					"input data length not even number");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);

			b2[(n / 2)] = ((byte) Integer.parseInt(item, 16));
		}
		b = (byte[]) null;
		return b2;
	}
}
