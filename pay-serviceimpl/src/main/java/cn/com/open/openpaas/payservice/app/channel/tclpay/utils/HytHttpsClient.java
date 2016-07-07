package cn.com.open.openpaas.payservice.app.channel.tclpay.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * 名称：Hyt http工具换
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class HytHttpsClient {

	public static HttpsURLConnection getHttpsURLConnection(String strUrl)
			throws IOException {
		URL url = new URL(strUrl);
		HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
				.openConnection();
		return httpsURLConnection;
	}

	public static String doHttpsPost(HttpsURLConnection httpsURLConnection,
			String postData, String charset) throws Exception {
		httpsURLConnection.setDoOutput(true);
		httpsURLConnection.setRequestMethod("POST");
		if (HytStringUtils.isEmpty(charset)) {
			charset = "GBK";
		}
		httpsURLConnection.setRequestProperty("Accept-Charset", charset);
		httpsURLConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		httpsURLConnection.setRequestProperty("Content-Length",
				String.valueOf(postData.length()));

		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		outputStream = httpsURLConnection.getOutputStream();
		outputStreamWriter = new OutputStreamWriter(outputStream);
        System.out.println("待发送报文--------"+postData);
		outputStreamWriter.write(postData);
		outputStreamWriter.flush();

		if (httpsURLConnection.getResponseCode() >= 300) {
			System.out.println(httpsURLConnection.getHeaderFields());
			throw new Exception(
					"HTTP Request is not success, Response code is "
							+ httpsURLConnection.getResponseCode());
		}

		inputStream = httpsURLConnection.getInputStream();
		inputStreamReader = new InputStreamReader(inputStream);
		reader = new BufferedReader(inputStreamReader);

		while ((tempLine = reader.readLine()) != null) {
			resultBuffer.append(tempLine);
		}

		if (outputStreamWriter != null) {
			outputStreamWriter.close();
		}

		if (outputStream != null) {
			outputStream.close();
		}

		if (reader != null) {
			reader.close();
		}

		if (inputStreamReader != null) {
			inputStreamReader.close();
		}

		if (inputStream != null) {
			inputStream.close();
		}
		
		httpsURLConnection.disconnect();

		return resultBuffer.toString();

	}
}
