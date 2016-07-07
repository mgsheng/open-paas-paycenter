package cn.com.open.openpaas.payservice.app.tools;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SendPostMethod {
	  public  String methodPost(String url,NameValuePair[] data){ 
	         
	        String response= "";//要返回的response信息 
	        HttpClient httpClient = new HttpClient(); 
	        PostMethod postMethod = new PostMethod(url); 
	        // 将表单的值放入postMethod中 
	        postMethod.setRequestBody(data); 
	        // 执行postMethod 
	        int statusCode = 0; 
	        try { 
	            statusCode = httpClient.executeMethod(postMethod); 
	        } catch (HttpException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } 
	        // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发 
	        // 301或者302 
	        if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY 
	                || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) { 
	            // 从头中取出转向的地址 
	            Header locationHeader = postMethod.getResponseHeader("location"); 
	            String location = null; 
	            if (locationHeader != null) { 
	                location = locationHeader.getValue(); 
	                System.out.println("The page was redirected to:" + location); 
	                response= methodPost(location,data);//用跳转后的页面重新请求。 
	            } else { 
	                System.err.println("Location field value is null."); 
	            } 
	        } else { 
	            System.out.println(postMethod.getStatusLine()); 
	 
	            try { 
	                response= postMethod.getResponseBodyAsString(); 
	            } catch (IOException e) { 
	                e.printStackTrace(); 
	            } 
	            postMethod.releaseConnection(); 
	        } 
	        return response; 
	    } 
	 
	  
	    public static void main(String[] args) throws UnsupportedEncodingException { 
	         
	        String url = "http://localhost/uchome/sendapi.php";
	       
	       
	        NameValuePair name=new NameValuePair("name", "allen");
	        NameValuePair password=new NameValuePair("password", "allen");
	       
	        NameValuePair[] data = {name,password};
	       
	      
	        String response=new SendPostMethod().methodPost(url,data);
	       
	        System.out.println("********"+response);
	        
	       
	    }  

}
