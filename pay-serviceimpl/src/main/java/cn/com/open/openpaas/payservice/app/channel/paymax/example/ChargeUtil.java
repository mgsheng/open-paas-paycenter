package cn.com.open.openpaas.payservice.app.channel.paymax.example;

import cn.com.open.openpaas.payservice.app.channel.paymax.exception.AuthorizationException;
import cn.com.open.openpaas.payservice.app.channel.paymax.exception.InvalidRequestException;
import cn.com.open.openpaas.payservice.app.channel.paymax.exception.InvalidResponseException;
import cn.com.open.openpaas.payservice.app.channel.paymax.model.Charge;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

/**
 * Created by xiaowei.wang on 2016/4/27.
 */
public class ChargeUtil {

    public static void main(String[] args) {
    	ChargeUtil ce = new ChargeUtil();
    	Map<String, Object> chargeMap=new HashMap<String, Object>();
        chargeMap.put("amount", 1);
        chargeMap.put("subject", "Your Subject");
        chargeMap.put("body", "Your Body");
        chargeMap.put("order_no", UUID.randomUUID());
        chargeMap.put("channel", "apple_pay_app");
        chargeMap.put("client_ip", "127.0.0.1");
        chargeMap.put("app", "app_52a8zBD2Erp56D8s");
        chargeMap.put("currency","CNY");
        chargeMap.put("description","abc");
        String private_key="";
        String secret_key="";
        String paymax_public_key="";
	    //请根据渠道要求确定是否需要传递extra字段
        Map<String, Object> extra = new HashMap<String, Object>();
//        extra.put("user_id","13810613847");
//        extra.put("return_url","http://132.123.221.22/333/kad");
        extra.put("open_id","obc-jswk25IUGp3q8RPTYu083rmk");
        chargeMap.put("extra",extra);
        ce.charge(chargeMap, private_key, secret_key,paymax_public_key);
        //ce.retrieve();
    }

    /**
     * 创建充值订单
     */
    public Map<String, Object> charge(Map<String, Object> chargeMap,String private_key,String secret_key,String paymax_public_key) {
    	Map<String, Object> returnValue=new HashMap<String, Object>();
	    try {
            Charge charge = Charge.create(chargeMap,private_key,secret_key,paymax_public_key);
            returnValue=  printResult(charge);
            System.out.println(returnValue+"===returnValue");
        } catch (AuthorizationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        }
		return returnValue;

    }

    /**
     * 输出请求结果
     *
     * @param charge
     */
    private static Map<String, Object> printResult(Charge charge) {
    	Map<String, Object>returnValue=new HashMap<String, Object>();
        if (charge.getReqSuccessFlag()){//http请求成功
        	 //System.out.println(JSON.toJSONString(charge));
             Map<String, Object> credential=charge.getCredential();
        	if(charge.getChannel().equals("wechat_wap")){
        	    String wechat_wap=credential.get("wechat_wap").toString();
        	    JSONObject jsApiParamsjson = JSONObject.fromObject(wechat_wap);
        	    String jsApiParams=jsApiParamsjson.getString("jsApiParams");
                returnValue.put("jsApiParams",jsApiParams);
                returnValue.put("status", "ok");	
        	}else if(charge.getChannel().equals("lakala_web")){
        		  String lakala_web=credential.get("lakala_web").toString();
                  returnValue.put("lakala_web",lakala_web);
                  returnValue.put("status", "ok");	
        	}else if(charge.getChannel().equals("wechat_csb")){
      		  String wechat_csb=credential.get("wechat_csb").toString();
              returnValue.put("wechat_csb",wechat_csb);
              returnValue.put("status", "ok");	
    	      }
        	else if(charge.getChannel().equals("lakala_web_fast")){
        		  String lakala_web_fast=credential.get("lakala_web_fast").toString();
                returnValue.put("lakala_web_fast",lakala_web_fast);
                returnValue.put("status", "ok");	
      	      }
        	else if(charge.getChannel().equals("lakala_h5")){
        		  String wechat_csb=credential.get("lakala_h5").toString();
                returnValue.put("wechat_csb",wechat_csb);
                returnValue.put("status", "ok");	
      	      }
           // System.out.println(returnValue);
        }else {//http请求失败
	       // System.out.println(JSON.toJSONString(charge));
            String failureCode = charge.getFailureCode();
            String failureMsg = charge.getFailureMsg();
            returnValue.put("failureCode",failureCode);
            returnValue.put("failureMsg",failureMsg);
            returnValue.put("status", "error");
          //  System.out.println("failureCode:"+failureCode);
           // System.out.println("failureMsg:"+failureMsg);
        }
        return returnValue;
    }


    /**
     * 查询充值订单
     */
    public void retrieve(String private_key,String secret_key,String paymax_public_key) {
        try {
            Charge charge = Charge.retrieve("ch_bd88045a391aade151476221",private_key,secret_key,paymax_public_key);
            printResult(charge);
        } catch (AuthorizationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        }
    }
}
