package cn.com.open.payservice.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringTools {

	/**
	 * 把任何大于0的int转换成2的n次方之和，返回和的集合
	 * @param no
	 * @return
	 */
	public static List<Integer> getParsedSum(int no){
		if(no <= 0){
//			throw new RuntimeException("The argument " + no + " should be greater than zero!");
			return null;
		}
		String str = Integer.toBinaryString(no);
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0 ,len = str.length() ; i < len; i++) {
			if(str.charAt(i)=='1'){
				list.add(1 << (len - i - 1));
			}
		}
		return list;
	}
	
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
	
	public static String bytesSubstring(String src, int start_idx, int end_idx){   
        byte[] b = src.getBytes();   
        String tgt = "";   
        for(int i=start_idx; i<=end_idx; i++){   
            tgt +=(char)b[i];   
        }   
        return tgt;   
    }
	/**
	 * 根据传递的区间返回随机值
	 * @param max
	 * @param min
	 * @return
	 */
	public static String getRandom(int max,int min){
		String value="";
		if(max>min){
			 Random random = new Random();
		     int s = random.nextInt(max)%(max-min+1) + min;	
		     value=String.valueOf(s);
		}
	       
		return value;
	}
}
