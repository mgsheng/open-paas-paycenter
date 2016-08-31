package cn.com.open.pay.order.service.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * 支付公共方法类
 * 
 * @author lirenjuan
 * 
 */
public class SysUtil {
	private static final DecimalFormat df = new DecimalFormat("0.00");
	// 随机字符串范围
	private final static String RAND_RANGE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private final static String RAND_RANGE_NUM = "1234567890";

	private final static char[] CHARS = RAND_RANGE.toCharArray();
	private final static char[] CHARS_NUM = RAND_RANGE_NUM.toCharArray();

	/**
	 * 格式化指定数值
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatAmount(double amount) {
		String result = null;
		try {
			result = df.format(amount);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 格式化指定数值
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatAmount(long amount) {
		String result = null;
		try {
			result = df.format(amount);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 格式化指定数值
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatAmount(Object amount) {
		String result = null;
		try {
			result = df.format(amount);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 较验请求参数
	 * 
	 * @param inStr
	 * @return
	 */
	public static String checkStr(String inStr) {
		String result = null;
		try {
			if (inStr == null || inStr.toUpperCase().equals("NULL")) {
				result = "";
			} else {
				result = inStr.trim();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 将Object转换成int
	 * 
	 * @param obj
	 * @return
	 */
	public static int toInt(Object obj) {
		int result = -1;
		try {
			String str = checkStr(String.valueOf(obj));
			result = Integer.parseInt(str);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 将Object转换成float
	 * 
	 * @param obj
	 * @return
	 */
	public static float toFloat(Object obj) {
		float result = -1;
		try {
			String str = checkStr(String.valueOf(obj));
			result = Float.parseFloat(str);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 将Object转换成String
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		String result = "";
		try {
			result = String.valueOf(obj);
			result = checkStr(result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 将str转换成Integer
	 * 
	 * @param obj
	 * @return
	 */
	public static Integer toInteger(String str) {
		Integer result = -1;
		try {
			result = Integer.parseInt(str);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 将String转换成Long
	 * 
	 * @param obj
	 * @return
	 */
	public static Long toLong(String str) {
		Long result = -1L;
		try {
			result = Long.parseLong(str);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 将String转换成Long
	 * 
	 * @param obj
	 * @return
	 */
	public static Double toDouble(String str) {
		double result = -1;
		try {
			result = Double.parseDouble(str);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 获得指定格式的系统日期
	 * 
	 * @return
	 */
	public static String getSysDate(String dateFormat) {
		String result = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date date = new Date();
			result = sdf.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * 生成指定长度的验证码
	 * 
	 * @return
	 */
	public static String getRandomStr(int CODE_LENGTH) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < CODE_LENGTH; i++)
			sb.append(CHARS[random.nextInt(CHARS.length)]);
		return sb.toString();
	}

	/**
	 * 生成指定长度的随机数
	 * 
	 * @param len
	 * @return
	 */
	public static String getRandomNum(int CODE_LENGTH) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < CODE_LENGTH; i++)
			sb.append(CHARS_NUM[random.nextInt(CHARS_NUM.length)]);
		return sb.toString();
	}
	/**
	 * 生成唯一订单号
	 */
	public static String careatePayOrderId() {
		String result = "";
		try {
			String dateStr = SysUtil.getSysDate("yyyyMMddHHmmss");
			String snStr = SysUtil.getRandomNum(6);
			result = dateStr + snStr;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
