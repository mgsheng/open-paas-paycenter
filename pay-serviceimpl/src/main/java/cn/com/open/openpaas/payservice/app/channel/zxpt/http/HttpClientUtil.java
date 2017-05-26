package cn.com.open.openpaas.payservice.app.channel.zxpt.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.core.util.Assert;

public class HttpClientUtil
{
  public static final int CONNECTION_TIMEOUT_MS = 360000;
  public static final int SO_TIMEOUT_MS = 360000;
  public static final String CONTENT_TYPE_JSON_CHARSET = "application/json;charset=gbk";
  public static final String CONTENT_TYPE_XML_CHARSET = "application/xml;charset=gbk";
  public static final String CONTENT_CHARSET = "utf-8";
  public static final Charset UTF_8 = Charset.forName("utf-8");
  public static final Charset GBK = Charset.forName("gbk");
  
  public static String httpGet(String url, Map<String, String> params)
    throws ClientProtocolException, IOException, URISyntaxException
  {
    return httpGet(url, params, "utf-8");
  }
  
  public static String httpGet(String url, Map<String, String> params, String charset)
    throws ClientProtocolException, IOException, URISyntaxException
  {
    HttpClient client = buildHttpClient(false);
    HttpGet getMethod = null;
    String returnStr = "";
    try
    {
      getMethod = buildHttpGet(url, params);
      HttpResponse response = client.execute(getMethod);
      assertStatus(response);
      HttpEntity entity = response.getEntity();
      if (entity != null)
      {
        returnStr = EntityUtils.toString(entity, charset);
        return returnStr;
      }
    }
    catch (ClientProtocolException e)
    {
      throw new ClientProtocolException("发送http报文时协议错误");
    }
    catch (IOException e)
    {
      throw new IOException("发送http报文时IO错误");
    }
    catch (URISyntaxException e)
    {
      throw new URISyntaxException(url, "发送http报文时URI错误");
    }
    finally
    {
      getMethod.releaseConnection();
    }
    return returnStr;
  }
  
  public static String httpPost(String url, Map<String, String> params)
    throws Exception, URISyntaxException
  {
    return httpPost(url, params, "utf-8");
  }
  
  public static String httpPost(String url, Map<String, String> params, String charset)
    throws IOException, URISyntaxException
  {
    HttpClient client = buildHttpClient(false);
    HttpPost postMethod = null;
    String returnStr = "";
    try
    {
      postMethod = buildHttpPost(url, params);
      HttpResponse response = client.execute(postMethod);
      assertStatus(response);
      HttpEntity entity = response.getEntity();
      if (entity != null)
      {
        returnStr = EntityUtils.toString(entity, charset);
        return returnStr;
      }
    }
    catch (ClientProtocolException e)
    {
      throw new ClientProtocolException("发送http报文时协议错误");
    }
    catch (IOException e)
    {
      throw new IOException("发送http报文时IO错误");
    }
    catch (URISyntaxException e)
    {
      throw new URISyntaxException(url, "发送http报文时URI错误");
    }
    finally
    {
      postMethod.releaseConnection();
    }
    return returnStr;
  }
  
  public static HttpClient buildHttpClient(boolean isMultiThread)
  {
    CloseableHttpClient client;
    if (isMultiThread) {
      client = HttpClientBuilder.create().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
    } else {
      client = HttpClientBuilder.create().build();
    }
    return client;
  }
  
  public static HttpPost buildHttpPost(String url, Map<String, String> params)
    throws UnsupportedEncodingException, URISyntaxException
  {
//    Assert.requireNonNull(url, "构建HttpPost时,url不能为null");
    HttpPost post = new HttpPost(url);
    setCommonHttpMethod(post);
    HttpEntity he = null;
    if (params != null)
    {
      List<NameValuePair> formparams = new ArrayList();
      for (String key : params.keySet()) {
        formparams.add(new BasicNameValuePair(key, (String)params.get(key)));
      }
      he = new UrlEncodedFormEntity(formparams, UTF_8);
      post.setEntity(he);
    }
    return post;
  }
  
  public static HttpGet buildHttpGet(String url, Map<String, String> params)
    throws URISyntaxException
  {
    Assert.requireNonNull(url, "构建HttpGet时,url不能为null");
    HttpGet get = new HttpGet(buildGetUrl(url, params));
    return get;
  }
  
  private static String buildGetUrl(String url, Map<String, String> params)
  {
    StringBuffer uriStr = new StringBuffer(url);
    if (params != null)
    {
      List<NameValuePair> ps = new ArrayList();
      for (String key : params.keySet()) {
        ps.add(new BasicNameValuePair(key, (String)params.get(key)));
      }
      uriStr.append("?");
      uriStr.append(URLEncodedUtils.format(ps, UTF_8));
    }
    return uriStr.toString();
  }
  
  public static void setCommonHttpMethod(HttpRequestBase httpMethod)
  {
    httpMethod.setHeader("Content-Encoding", "utf-8");
  }
  
  public static void setContentLength(HttpRequestBase httpMethod, HttpEntity he)
  {
    if (he == null) {
      return;
    }
    httpMethod.setHeader("Content-Length", String.valueOf(he.getContentLength()));
  }
  
  public static RequestConfig buildRequestConfig()
  {
    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(360000).setConnectTimeout(360000).build();
    
    return requestConfig;
  }
  
  static void assertStatus(HttpResponse res)
    throws IOException
  {
    switch (res.getStatusLine().getStatusCode())
    {
    case 200: 
    case 201: 
    case 202: 
    case 203: 
    case 204: 
    case 205: 
    case 206: 
    case 207: 
      break;
    default: 
      throw new IOException("服务器响应状态异常." + res.getStatusLine().getStatusCode());
    }
  }
}
