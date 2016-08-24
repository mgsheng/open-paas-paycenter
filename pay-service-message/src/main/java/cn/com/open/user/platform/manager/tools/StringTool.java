package cn.com.open.user.platform.manager.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {

	/**
	 * 判断是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
			if (!Character.isDigit(str.charAt(i))){
			return false;
			}
		}
		return true;
	}
	
	/**
     * 
     * 验证密码为 6～20位,字母、数字或者英文符号，最短6位，区分大小写
     * @param value
     * @return
     */
    public static int judgeInputNotNo(String value){
    	int returnValue=0;
    	if(value.length()>20||value.length()<6){
    		returnValue=1;
    		return returnValue;
    	}else{
    	//Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{5,20}"); 
    		Pattern p = Pattern.compile("[0-9A-Za-z_]*");
    	//Pattern p = Pattern.compile("^[a-zA-Z]/w{5,17}$");
    	Matcher m = p.matcher(value);
    	boolean chinaKey = m.matches();
    	if(chinaKey){
    		returnValue=0;
    	} else{
    		returnValue=1;
    		return returnValue;
    	 }
    	}
    	return  returnValue;
    }
	
	/**
	 * String 转 Date
	 * @param str
	 * @param pattern yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date stringToDate(String str, String pattern){
		SimpleDateFormat sdf=new SimpleDateFormat(pattern); 
		Date date = new Date();
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String dateToString(Date date, String pattern){
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		String str=sdf.format(date);
		return str;
	}
	
	/**
	 * 随机生成任意长度的字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789"; 
	    Random random = new Random(); 
	    StringBuffer sb = new StringBuffer(); 
	    for (int i = 0; i < length; i++) { 
	        int number = random.nextInt(base.length()); 
	        sb.append(base.charAt(number)); 
	    } 
	    return sb.toString(); 
	}
	
	public static String getRandomNum(int length) { //length表示生成字符串的长度
	    String base = "0123456789"; 
	    Random random = new Random(); 
	    StringBuffer sb = new StringBuffer(); 
	    for (int i = 0; i < length; i++) { 
	        int number = random.nextInt(base.length()); 
	        sb.append(base.charAt(number)); 
	    } 
	    return sb.toString(); 
	 }
	
	public static String bytesSubstring(String src, int start_idx, int end_idx){ 
        byte[] b = src.getBytes(); 
        String tgt = ""; 
        for(int i=start_idx; i<=end_idx; i++){ 
            tgt +=(char)b[i]; 
        } 
        return tgt; 
    }

	/**
	 * unicode 转换成 中文
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		StringBuffer outBuffer;
		try {
			char aChar;
			int len = theString.length();
			outBuffer = new StringBuffer(len);
			for (int x = 0; x < len;) {
				aChar = theString.charAt(x++);
				if (aChar == '\\') {
					aChar = theString.charAt(x++);
					if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException("Malformedencoding.");
						}
					}
					outBuffer.append((char) value);
					}
					else {
						if (aChar == 't') {
							aChar = '\t';
						}
						else if (aChar == 'r') {
							aChar = '\r';
						}
						else if (aChar == 'n') {
							aChar = '\n';
						}
						else if (aChar == 'f') {
							aChar = '\f';
						}
						outBuffer.append(aChar);
					}
				}
				else {
					outBuffer.append(aChar);
				}
			}
		} catch (Exception e) {
			outBuffer = new StringBuffer("");
//			e.printStackTrace();
		}
		return outBuffer.toString();
	}
	
	public static void main(String[] args) {
//		System.out.println(decodeUnicode("\u5317\u4eac"));
		System.out.println(getRandomNum(6));
	}
}
