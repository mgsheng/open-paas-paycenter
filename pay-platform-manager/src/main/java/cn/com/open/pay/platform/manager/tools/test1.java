package cn.com.open.pay.platform.manager.tools;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpStatus;

import sun.misc.BASE64Encoder;

public class test1 {
	public static JSONObject post(String url,JSONObject json){  
		       HttpClient client = new DefaultHttpClient();  
		       
		        HttpPost post = new HttpPost(url);  
		       JSONObject response = null;  
		       try {  
	            StringEntity s = new StringEntity(json.toString());  
		            s.setContentEncoding("UTF-8");  
		           s.setContentType("application/json");  
		           post.setEntity(s); 
		          /* int hour = DateTime.Now.Hour;
		            int day = DateTime.Now.Day;
		            int dayOfYear = DateTime.Now.DayOfYear;
		            string value = "asdf1.23" + hour.ToString() + day.ToString() +dayOfYear.ToString();
*/
		           Calendar cal = Calendar.getInstance();
			         int year = cal.get(Calendar.DAY_OF_YEAR);//获取年份
			         int day=cal.get(Calendar.DATE);//获取日
			        // int day1=cal.get(Calendar.DAY_OF_YEAR);
			         int hour=cal.get(Calendar.HOUR_OF_DAY);//小时
		           String value = "asdf1.23" + String.valueOf(hour) +String.valueOf(day) +String.valueOf(year);
		           String md5value= MD5.Md5(value);
		           post.setHeader("key", md5value.toUpperCase());
		           HttpResponse res = client.execute(post);  
		           if(res.getStatusLine().getStatusCode() == HttpStatus.OK.value()){  
		               HttpEntity entity = res.getEntity();  
		               BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
		               String lines;
		   		    StringBuffer sb = new StringBuffer();
		   		    while((lines = reader.readLine()) != null){
		   		        lines = new String(lines.getBytes(), "utf-8");
		   		        sb.append(lines);
		   		    }
		   		    reader.close();
		   		    System.out.println(sb);
		              // response = new JSONObject(new JSONTokener(new InputStreamReader(entity.getContent(),charset)));  
		          }  
		      } catch (Exception e) {  
		            throw new RuntimeException(e);  
		        }  
		        return response;  
		   }
	
	 public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		//确定计算方法
		        MessageDigest md5=MessageDigest.getInstance("MD5");
		        BASE64Encoder base64en = new BASE64Encoder();
		//加密后的字符串
		        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
		    }
	public static void main(String[] args) {

	/*	double   f   =  1; 
		BigDecimal   b   =   new   BigDecimal(f); 
		double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		  System.out.println(reqjson);*/
	}

}
