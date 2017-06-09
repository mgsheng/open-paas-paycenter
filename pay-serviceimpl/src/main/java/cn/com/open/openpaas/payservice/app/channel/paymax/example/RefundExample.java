package cn.com.open.openpaas.payservice.app.channel.paymax.example;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.com.open.openpaas.payservice.app.channel.paymax.exception.AuthorizationException;
import cn.com.open.openpaas.payservice.app.channel.paymax.exception.InvalidRequestException;
import cn.com.open.openpaas.payservice.app.channel.paymax.exception.InvalidResponseException;
import cn.com.open.openpaas.payservice.app.channel.paymax.model.Refund;
/**
 * Created by xiaowei.wang on 2016/4/27.
 */
public class RefundExample {

    public static void main(String[] args) {
        RefundExample ce = new RefundExample();
        String private_key="";
        String secret_key=""; 
        String paymax_public_key="";
        ce.charge(private_key,secret_key,paymax_public_key);
        ce.retrieve(private_key,secret_key,paymax_public_key);
    }

    /**
     * 创建退款订单
     */
    public void charge(String private_key,String secret_key ,String paymax_public_key) {
        Map<String, Object> refundMap = new HashMap<String, Object>();
        refundMap.put("amount", 1);
        refundMap.put("description", "description");
        Map<String, Object> extra = new HashMap<String, Object>();
        extra.put("extra_key2","extra_value2");
        extra.put("extra_key1","extra_value1");
        refundMap.put("extra",extra);
        try {
            Refund refund = Refund.create("ch_25214d51cccb602c1045fafa",refundMap,private_key,secret_key,paymax_public_key);
            printResult(refund);
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

    /**
     * 输出请求结果
     *
     * @param refund
     */
    private void printResult(Refund refund) {
        if (refund.getReqSuccessFlag()){//http请求成功
           // System.out.println(refund);
        }else {//http请求失败
            String failureCode = refund.getFailureCode();
            String failureMsg = refund.getFailureMsg();
           // System.out.println("failureCode:"+failureCode);
          //  System.out.println("failureMsg:"+failureMsg);
        }
    }

    /**
     * 退款查询订单
     */
    public void retrieve(String private_key,String secret_key,String paymax_public_key) {
        try {
            Refund refund = Refund.retrieve("ch_c090e7d9a9d9d94a6e9b0bce","re_d6586ff6e077b95985344538",private_key,secret_key,paymax_public_key);
            printResult(refund);
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
