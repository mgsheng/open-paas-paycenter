package cn.com.open.pay.platform.manager.tools;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URLEncoder;

public class testDemo {
	private int port;
	private String host;
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	public testDemo(String host, int port) {
		socket = new Socket();
		this.host = host;
		this.port = port;
	}
	public void sendPost() throws IOException
	{
		String path = "http://10.100.133.80:8040/api/StudentInfor/?appKey=e068197e9d924744ab6af327ef9c4a07";
		String data = URLEncoder.encode("name", "utf-8") + "=" + URLEncoder.encode("gloomyfish", "utf-8") + "&" +
						URLEncoder.encode("age", "utf-8") + "=" + URLEncoder.encode("32", "utf-8");
		// String data = "name=zhigang_jia";
		 String a="{\"UserInfoList\":[{\"username\":\"yzher\",\"sourceid\":\"315407\",\"password\":\"27CD3E53E9B1D43C\",\"key\":\"9ebada02676c4ccbbbdaeae27362896b\"}]}";
		SocketAddress dest = new InetSocketAddress(this.host, this.port);
		socket.connect(dest);
		OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
		bufferedWriter = new BufferedWriter(streamWriter);
		
		bufferedWriter.write("POST " + path + " HTTP/1.1\r\n");
		bufferedWriter.write("Host: " + this.host + "\r\n");
		bufferedWriter.write("Content-Length: " + a.length() + "\r\n");
		bufferedWriter.write("Content-Type: application/x-www-form-urlencoded\r\n");
		bufferedWriter.write("\r\n");
		bufferedWriter.write(a);
		bufferedWriter.flush();
		bufferedWriter.write("\r\n");
		bufferedWriter.flush();
		
		BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
		bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
		String line = null;
		while((line = bufferedReader.readLine())!= null)
		{
			System.out.println(line);
		}
		bufferedReader.close();
		bufferedWriter.close();
		socket.close();
	}
	
	public static void main(String[] args)
	{
		testDemo td = new testDemo("10.96.5.242",8090);
		try {
			// td.sendGet(); //send HTTP GET Request
			
			td.sendPost(); // send HTTP POST Request
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}