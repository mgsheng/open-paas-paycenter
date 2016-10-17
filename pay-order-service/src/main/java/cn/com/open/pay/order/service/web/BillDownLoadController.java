package cn.com.open.pay.order.service.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;

@Controller
@RequestMapping("/bill/")
public class BillDownLoadController {
	
    @RequestMapping("downLoad")
	public String downLoad(HttpServletRequest request,HttpServletResponse response,Model model){
    	return "";
		
	}
    public static void main(String[] args) {
    	
     	
    	AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","peswcu2255jnvguqgudz4irhoi9rizz9","your private_key","json","GBK","alipay_public_key");
    	AlipayDataDataserviceBillDownloadurlQueryRequest request1 = new AlipayDataDataserviceBillDownloadurlQueryRequest();
    	request1.setBizContent("{" +
    	"    \"bill_type\":\"trade\"," +
    	"    \"bill_date\":\"2016-04-05\"" +
    	"  }");
    	AlipayDataDataserviceBillDownloadurlQueryResponse response1 = null;
		try {
			response1 = alipayClient.execute(request1);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(response1.isSuccess()){
    	System.out.println("调用成功");
    	} else {
    	System.out.println("调用失败");
    	}
	}

}
