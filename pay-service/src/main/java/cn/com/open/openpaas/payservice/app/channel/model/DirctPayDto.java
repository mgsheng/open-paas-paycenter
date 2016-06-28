package cn.com.open.openpaas.payservice.app.channel.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 15-5-18
 * <p/>
 * http://localhost:8080/oauth/token?client_id=unity-client&client_secret=unity&grant_type=authorization_code&code=zLl170&redirect_uri=http%3a%2f%2flocalhost%3a8080%2funity%2fdashboard.htm
 *
 * 
 */
public class DirctPayDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String partner;
	private String seller_id;
    private String key;
    private String notify_url;
    private String return_url;
    private String sign_type;
    private String payment_type;
    private String input_charset;
    private String service;  
    private String anti_phishing_key;    
    private String exter_invoke_ip;    
    private String out_trade_no;    
    private String subjects;    
    private String total_fee;
    private String body;
    private String dirctPayUri;
   
	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getInput_charset() {
		return input_charset;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAnti_phishing_key() {
		return anti_phishing_key;
	}

	public void setAnti_phishing_key(String anti_phishing_key) {
		this.anti_phishing_key = anti_phishing_key;
	}

	public String getExter_invoke_ip() {
		return exter_invoke_ip;
	}

	public void setExter_invoke_ip(String exter_invoke_ip) {
		this.exter_invoke_ip = exter_invoke_ip;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDirctPayUri() {
		return dirctPayUri;
	}

	public void setDirctPayUri(String dirctPayUri) {
		this.dirctPayUri = dirctPayUri;
	}

	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?partner=%s&seller_id=%s&key=%s&notify_url=%s&return_url=%s&sign_type=%s&payment_type=%s&input_charset=%s&service=%s&anti_phishing_key=%s&exter_invoke_ip=%s&out_trade_no=%s&subjects=%s&total_fee=%s&body=%s",
        		dirctPayUri,partner,seller_id,key, notify_url, return_url, sign_type, payment_type, input_charset,service,anti_phishing_key,exter_invoke_ip,out_trade_no,subjects,total_fee,body);  
    }
	

}
