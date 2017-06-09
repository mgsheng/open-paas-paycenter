package cn.com.open.pay.order.service.paymax.model;

import java.io.IOException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.open.pay.order.service.paymax.config.PaymaxConfig;
import cn.com.open.pay.order.service.paymax.exception.AuthorizationException;
import cn.com.open.pay.order.service.paymax.exception.InvalidRequestException;
import cn.com.open.pay.order.service.paymax.exception.InvalidResponseException;

/**
 * @Author shane
 * @Time 2016/10/20 21:39
 * @Email baohua.shan@zhulebei.com
 * @Desc ...
 */
public class StatementDownload extends PaymaxDownLoad {

    public static String download(Map<String,Object> params,String secert_key,String private_key,String paymax_public_key) throws AuthorizationException, InvalidResponseException, InvalidRequestException, IOException {
        return request(PaymaxConfig.API_BASE_URL+ PaymaxConfig.STATEMENT_DOWNLOAD, JSONObject.toJSONString(params), String.class,secert_key,private_key,paymax_public_key);
    }

}
