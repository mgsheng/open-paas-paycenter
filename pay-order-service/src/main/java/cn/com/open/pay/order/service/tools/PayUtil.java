package cn.com.open.pay.order.service.tools;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PayUtil {
	    private static final Logger log = LoggerFactory.getLogger(PayUtil.class);
			private static String Key = "fc3db98a48a5434aaddbd2c75a7382ba";

			/**
			 * @param args
			 */
			public static void main(String[] args) {
				System.out.println(">>>模拟微信支付<<<");
				System.out.println("==========华丽的分隔符==========");
				//微信api提供的参数
				String appid = "wxd930ea5d5a258f4f";
				String mch_id = "10000100";
				String device_info = "1000";
				String body = "test";
				String nonce_str = "ibuaiVcKdpRxkhJA";
				
				SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
				parameters.put("appid", appid);
				parameters.put("mch_id", mch_id);
				parameters.put("device_info", device_info);
				parameters.put("body", body);
				parameters.put("nonce_str", nonce_str);
				
				String characterEncoding = "UTF-8";
				String weixinApiSign = "0D116A34A890046D2DEF526BB2E0EB0D";
				System.out.println("微信的签名是：" + weixinApiSign);
				String mySign = createSign(characterEncoding,parameters,Key);
				System.out.println("我     的签名是："+mySign);
				
				if(weixinApiSign.equals(mySign)){
					System.out.println("恭喜你成功了~");
				}else{
					System.out.println("注定了你是个失败者~");
				}
				//appUid=1&goodsDesc=sddsd&goodsName=test1&merchantId=10001&orderId=20160506113639952053&parameter=sddsd&paymentChannel=1&paymentType=null&timeEnd=20160506033825&totalFee=0.0&key=fc3db98a48a5434aaddbd2c75a7382ba
                //
				String userAgent = "Mozilla/5.0(iphone;CPU iphone OS 5_1_1 like Mac OS X) AppleWebKit/534.46(KHTML,like Geocko) Mobile/9B206 MicroMessenger/5.0";
				
				char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger")+15);
				
				System.out.println("微信的版本号："+new String(new char[]{agent}));
			}

			/**
			 * 微信支付签名算法sign
			 * @param characterEncoding
			 * @param parameters
			 * @return
			 */
			public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters,String key){
				log.info("----------------------------生成secert开始");
				StringBuffer sb = new StringBuffer();
				Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
				Iterator it = es.iterator();
				while(it.hasNext()) {
					Map.Entry entry = (Map.Entry)it.next();
					String k = (String)entry.getKey();
					Object v = entry.getValue();
					if(null != v && !"".equals(v)&& !"null".equals(v) 
							&& !"sign".equals(k) && !"key".equals(k)) {
						sb.append(k + "=" + v + "&");
					}
				}
				sb.append("key=" + key);
				log.info("加密参数："+sb.toString());
				String sign = MD5.Md5(sb.toString()).toUpperCase();
				log.info("加密结果："+sign);
				return sign;
			}


}
