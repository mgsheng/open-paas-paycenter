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
        chargeMap.put("channel", "lakala_web");
        chargeMap.put("client_ip", "127.0.0.1");
        chargeMap.put("app", "app_52a8zBD2Erp56D8s");
        chargeMap.put("currency","CNY");
        chargeMap.put("description","abc");
	    //请根据渠道要求确定是否需要传递extra字段
        Map<String, Object> extra = new HashMap<String, Object>();
        extra.put("user_id","13810613847");
        extra.put("return_url","http://132.123.221.22/333/kad");
        chargeMap.put("extra",extra);
        ce.charge(chargeMap);
        //ce.retrieve();
    }

    /**
     * 创建充值订单
     */
    public String charge(Map<String, Object> chargeMap) {
    	String returnValue="";
	    try {
            Charge charge = Charge.create(chargeMap);
            returnValue=  printResult(charge);
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
    private static String printResult(Charge charge) {
    	String returnValue="";
        if (charge.getReqSuccessFlag()){//http请求成功
        	 System.out.println(JSON.toJSONString(charge));
            Map<String, Object> credential=charge.getCredential();
            String lakala_web=credential.get("lakala_web").toString();
            returnValue=lakala_web;
            System.out.println(returnValue);
        }else {//http请求失败
	        System.out.println(JSON.toJSONString(charge));
            String failureCode = charge.getFailureCode();
            String failureMsg = charge.getFailureMsg();
            System.out.println("failureCode:"+failureCode);
            System.out.println("failureMsg:"+failureMsg);
        }
        return returnValue;
    }

    /**
     * 查询充值订单
     */
    public void retrieve() {
        try {
            Charge charge = Charge.retrieve("ch_bd88045a391aade151476221");
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
