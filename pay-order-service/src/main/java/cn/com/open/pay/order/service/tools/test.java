package cn.com.open.pay.order.service.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

public class test {
	public static void main(String[] arg) {
		String url="http://localhost:8080/spring-oauth-server/user/synUserName?";
		StringBuffer data=null;
		//urlConnectionPost(url,data);
		//http://localhost:8080/spring-oauth-server/user/synUserName?username=testwangs19&client_id=91d921e029d0470b9eb41e39d895a0e0&access_token=98722d3a-d8d7-46ec-8240-5d74fd41ef4c&grant_type=client_credentials&scope=read,write&source_id=3333333333333
		doPost(url);

	}

	private static void doPost(String addurl) {
		URL url;
		try
		{
		    //创建连接
		    url = new URL(addurl);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
		    conn.setDoInput(true);
		    conn.setUseCaches(false);
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-type","application/x-www-form-urlencoded");
		    conn.setRequestProperty("accpt", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:27.0) Gecko/20100101 Firefox/27.0");
		    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		    conn.setRequestProperty("Connection", "keep-alive");
		    conn.setRequestProperty("Cookie", "SP.NET_SessionId=rtznindc2qwycf45ixyji2rr");
		    conn.connect();
		    //json参数
		    //DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		    Map<String ,Object> map=new HashMap<String,Object>();
		    List<Map<String,String>> allInfoList= new LinkedList<Map<String,String>>();
		    List<Map<String,String>> infoList= null;
		    JSONObject args = new JSONObject();
		    //allInfoList.add(infoList.get(0));
		    map.put("UserInfoList", allInfoList);
		   
		    JSONObject args1= JSONObject.fromObject(map);
		   /* JSONObject args1 = new JSONObject();
		    args1*/
		    args.put("UserInfoList","1111");
		    args.put("username","1");
		    args.put("password","123");
		    
		    String a="username=testwangs19&client_id=91d921e029d0470b9eb41e39d895a0e0&access_token=98722d3a-d8d7-46ec-8240-5d74fd41ef4c&grant_type=client_credentials&scope=read,write&source_id=3333333333333";
		    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		    writer.write(a);
		    
		    /* out.writeBytes(a);
		    out.flush();
		    out.close();*/
		    InputStream in = conn.getInputStream();
		    //获取响应
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
		    String lines;
		    StringBuffer sb = new StringBuffer();
		    while((lines = reader.readLine()) != null){
		        lines = new String(lines.getBytes(), "utf-8");
		        sb.append(lines);
		    }
		    reader.close();
		    System.out.println(sb);
		    // 关闭连接
			conn.disconnect();
		}
		catch

		(Exception e) {

		    e.printStackTrace();

		}
	}
    private static  StringBuffer urlConnectionPost(String tourl,StringBuffer data) {  
        StringBuffer sb = null;  
        BufferedReader reader = null;  
        OutputStreamWriter wr = null;  
        URL url;  
        try {  
            url = new URL(tourl);  
            URLConnection conn = url.openConnection();  
            conn.setDoOutput(true);  
            conn.setConnectTimeout(1000 * 5);  
                //当存在post的值时，才打开OutputStreamWriter  
            if(data!=null && data.toString().trim().length()>0){  
                wr = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");  
                wr.write(data.toString());  
                wr.flush();  
            }  
             
            // Get the response  
            reader = new BufferedReader(new InputStreamReader(conn  
                    .getInputStream(),"UTF-8"));  
            sb = new StringBuffer();  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                sb.append(line + "/n");  
            }  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }finally{  
            try {  
                if(wr!=null){  
                    wr.close();  
                }  
                if(reader!=null){  
                    reader.close();  
                }  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }             
        }  
        return sb;  
    }  
}
