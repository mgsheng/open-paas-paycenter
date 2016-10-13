package cn.com.open.openpaas.payservice.app.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * 网络工具
 * @author dongminghao
 *
 */
public class NetworkUtil {

	/**
	 * 获取本地内网IP地址
	 * @param request
	 * @return
	 */
	public final static String getIpAddress(HttpServletRequest request){
		//获取内网IP地址
		return getLocalIp(request);
		
		//获取外网IP地址，内部需要请求外网http://checkip.dyndns.org/获取到外网IP，需要等待回传，速度较慢，【奥鹏网络目前没有开放该网址的访问权限】
//		return getExtranetIP();
	}
	
	/**
	 * 获取外网IP地址
	 * @return
	 */
	public final static String getExtranetIP(){  
		// 输入流
        InputStream in = null;
        // 到外网提供者的Http连接
        HttpURLConnection httpConn = null;
        try {
            // 打开连接
            URL url = new URL("http://checkip.dyndns.org/");
            httpConn = (HttpURLConnection) url.openConnection();         
            // 连接设置
            HttpURLConnection.setFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");

            // 获取连接的输入流
            in = httpConn.getInputStream();
            byte[] bytes=new byte[1024];// 此大小可根据实际情况调整 
            // 读取到数组中
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                   && (numRead=in.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            } 
            // 将字节转化为为UTF-8的字符串       
            String receivedString=new String(bytes,"UTF-8");    
            // 返回
            return parse(receivedString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	if(in != null){
            		in.close();
            	}
                if(httpConn != null){
                	httpConn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // 出现异常则返回空
        return null;
    }  
	
	/**
	 * 获取内网IP地址
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public final static String getLocalIp(HttpServletRequest request) {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
		String ip = "";
		try {
			ip = request.getHeader("X-Forwarded-For");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("Proxy-Client-IP");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("WL-Proxy-Client-IP");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("HTTP_CLIENT_IP");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getRemoteAddr();
				}
 				ip = ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
 				ip = ip.equals("0:0:0:0:0:0:0:1%0")?"127.0.0.1":ip;
			}
			else if (ip.length() > 15) {
				String[] ips = ip.split(",");
				for (int index = 0; index < ips.length; index++) {
					String strIp = (String) ips[index];
					if (!("unknown".equalsIgnoreCase(strIp))) {
						ip = strIp;
						break;
					}
				}
			}
		} catch (Exception e) {
			ip = "";
//			e.printStackTrace();
		}
		return ip;
	}
	
	/**
	 * 根据ip地址查询地区
	 * @param ip
	 * @return
	 */
	public final static String getIpLookup(String ip){
		if(StringUtils.isBlank(ip)){
			return "";
		}
		else if(ip.equals("127.0.0.1")){
			return "本机";
		}
		else{
			//获取地区信息
//			新浪IP
//	    	http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=61.135.169.125
//			成功：
//	    	{"ret":1,"start":-1,"end":-1,"country":"\u4e2d\u56fd","province":"\u5317\u4eac","city":"\u5317\u4eac","district":"","isp":"","type":"","desc":""}
//			失败：
//	    	{"ret":-1,"ip":"10.100.134.92"}
	    	try {
				Map<String,String> parameters = new LinkedHashMap<String, String>();
				parameters.put("format", "json");
				parameters.put("ip", ip);
				String result = HttpTools.doGet("http://int.dpool.sina.com.cn/iplookup/iplookup.php", parameters, "utf-8");
				JSONObject obj = JSONObject.fromObject(result);
				if(obj.get("ret")!=null && obj.get("ret").toString().equals("1")){
					return StringTool.decodeUnicode(obj.get("province").toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	//暂没有做126的解析，以后备用
//	    	126IP（不推荐）
//	    	http://ip.ws.126.net/ipquery?ip=61.135.169.125
//	    	成功：
//	    	var lo="北京市", lc="房山区"; var localAddress={city:"房山区", province:"北京市"}
//	    	失败：（地址失败全都是返回的广东省）
//	    	var lo="广东省", lc="广州市"; var localAddress={city:"广州市", province:"广东省"}
		}
		return "";
	}
	
	
	private static String parse(String html){
        try {
			Pattern pattern=Pattern.compile("(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})", Pattern.CASE_INSENSITIVE);   
			Matcher matcher=pattern.matcher(html);       
			while(matcher.find()){
			    return matcher.group(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }
}
