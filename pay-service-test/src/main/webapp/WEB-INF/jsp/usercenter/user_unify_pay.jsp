<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>user-unify-pay(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="unifyPay" method="post" class="form-horizontal" target="_blank">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="unifyPayUri" id="unifyPayUri" value="${unifyPayUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">outTradeNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="outTradeNo" id="outTradeNo"
                                   class="form-control" ng-model="outTradeNo"/>

                            <p class="help-block">业务方唯一订单号（必填）</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">username</label>

                        <div class="col-sm-10">
                            <input type="text" name="userName" id="userName"
                                   class="form-control" ng-model="userName"/>
                           <p class="help-block">用户名（接入用户中心时可以传递）</p>
                        </div>
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">userId</label>

                        <div class="col-sm-10">
                            <input type="text" name="userId" id="userId"
                                   class="form-control" ng-model="userId"/>
                                    <p class="help-block">业务方用户Id（必传）</p>
                        </div>
                        
                    </div>
                    
                                     
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">merchantId</label>

                        <div class="col-sm-10">
                            <input type="text" name="merchantId" id="merchantId" class="form-control"
                                   size="50" ng-model="merchantId"/>

                            <p class="help-block">商户号（必传）接入时会首先确认,10001-测试商户1</p>
                        </div>
                    </div>

					<div class="form-group">  
                        <label class="col-sm-2 control-label">appId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId" size="50" id="appId" class="form-control"
                                   ng-model="appId"/>

                            <p class="help-block">应用Id,23-测试应用Id</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">goodsId</label>

                        <div class="col-sm-10">
                            <input type="text" name="goodsId" id="goodsId" size="50" class="form-control"
                                   ng-model="goodsId"/>
                            <p class="help-block">商品Id</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">goodsName</label>

                        <div class="col-sm-10">
                            <input type="text" name="goodsName" id="goodsName"size="50" class="form-control"
                                   ng-model="goodsName"/>
                                    <p class="help-block">商品名称（必传）</p>
                        </div> 
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">goodsDesc</label>

                        <div class="col-sm-10">
                            <input type="text" name="goodsDesc" id="goodsDesc" size="50" class="form-control"
                                   ng-model="goodsDesc"/>

                            <p class="help-block">商品描述</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">goodsTag</label>

                        <div class="col-sm-10">
                            <input type="text" name="goodsTag" id="goodsTag" size="50" class="form-control"
                                   ng-model="goodsTag"/>

                            <p class="help-block">商品标记（商品优惠、打折等）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">showUrl</label>

                        <div class="col-sm-10">
                            <input type="text" name="showUrl" id="showUrl" size="50" class="form-control"
                                   ng-model="showUrl"/>

                            <p class="help-block">商品展示网址</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">buyerRealName</label>

                        <div class="col-sm-10">
                            <input type="text" name="buyerRealName" id="buyerRealName" size="50" class="form-control"
                                   ng-model="buyerRealName"/>

                            <p class="help-block">买家真实姓名</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">buyerCertNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="buyerCertNo" id="buyerCertNo" size="50" class="form-control"
                                   ng-model="buyerCertNo"/>

                            <p class="help-block">买家证件号码</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">inputCharset</label>

                        <div class="col-sm-10">
                            <input type="text" name="inputCharset" id="inputCharset" size="50" class="form-control" ng-model="inputCharset"/>

                            <p class="help-block">字符集，默认为（UTF-8）</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">paymentOutTime</label>

                        <div class="col-sm-10">
                            <input type="text" name="paymentOutTime" id="paymentOutTime" size="50" class="form-control"
                                   ng-model="paymentOutTime"/>

                            <p class="help-block">支付超时时间（超过支付时间，该笔交易自动关闭）取值范围：1m～15d</p>
                        </div>
                    </div> 
                       <div class="form-group">
                        <label class="col-sm-2 control-label">paymentType</label>

                        <div class="col-sm-10">
                           <select name="paymentType" id="paymentType" class="form-control" ng-mode="paymentType">
                                <option value=""></option>
								<option value="ALIPAY">支付宝-即时到账</option>
								<option value="ALIFAF">支付宝-当面付</option>
								<option value="WEIXIN">微信-扫码支付</option>
								<option value="WECHAT_WAP">微信-公众号支付</option>
								<option value="PAYMAX">拉卡拉-网关支付</option>
								<option value="PAYMAX_WECHAT_CSB">拉卡拉-微信扫码</option>
								<option value="PAYMAX_H5">拉卡拉-移动支付</option>
								<option value="EHK_WEIXIN_PAY">易汇金-扫码支付</option>
								<option value="EHK_BANK">易汇金-收银台</option>
								<option value="YEEPAY_GW">易宝-收银台</option>
								<option value="UPOP">银联</option>
								<option value="CMB">招商银行</option>
								<option value="ICBC">工商银行</option>
								<option value="CCB">建设银行</option>
								<option value="ABC">农业银行</option>
								<option value="BOC">中国银行</option>
								<option value="BCOM">交通银行</option>
								<option value="PSBC">中国邮政银行</option>
								<option value="CGB">广发银行</option>
								<option value="SPDB">浦发银行</option>
								<option value="BOB">北京银行</option>
								<option value="CEB">中国光大银行</option>
								<option value="PAB">中国平安银行</option>
								<option value="CMBC">中国民生银行</option>
								<option value="CIB">兴业银行</option>
							</select>
                            <p class="help-block">支付方式</p>
                        </div>
                    </div> 
                    <div class="form-group">
                        <label class="col-sm-2 control-label">paymentChannel</label>

                        <div class="col-sm-10">
                            <!-- <input type="text" name="paymentChannel" id="paymentChannel" size="50" class="form-control"
                                   ng-model="paymentChannel"/> -->
							<select name="paymentChannel" id="paymentChannel" class="form-control" ng-mode="paymentChannel">
							     <option value=""></option>
								<option value="10001">支付宝</option>
								<option value="10002">微信</option>
								<option value="10005">直连银行</option>
								<option value="10003">银联</option>
								<option value="10007">拉卡拉-网关支付</option>
								<option value="10006">易汇金-扫码支付</option>
								<option value="10012">易汇金-收银台</option>
								<option value="10008">易宝-直连银行</option>
								<option value="10009">支付宝-当面付</option>
								<option value="10010">微信公众号</option>
								<option value="10011">拉卡拉-微信公众号支付</option>
								<option value="10013">拉卡拉-微信扫码支付</option>
								<option value="10014">拉卡拉-H5支付</option>
							</select>
                            <p class="help-block">支付渠道:10001-支付宝渠道   10002-微信渠道</p>
                        </div>
                    </div> 
                    <div class="form-group">
                        <label class="col-sm-2 control-label">businessType</label>

                        <div class="col-sm-10">
                            <!-- <input type="text" name="paymentChannel" id="paymentChannel" size="50" class="form-control"
                                   ng-model="paymentChannel"/> -->
							<select name="businessType" id="businessType" class="form-control" ng-mode="businessType">
								<option value="1">直消</option>
								<option value="2">充值</option>
							</select>
                            <p class="help-block">业务类型（1：直消2:充值）</p>
                        </div>
                    </div> 
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">totalFee</label>

                        <div class="col-sm-10">
                            <input type="text" name="totalFee" id="totalFee" size="50" class="form-control"
                                   ng-model="totalFee"/>

                            <p class="help-block">订单金额（单位元，精确到分）如：100元=10000分（必传）,1=0.01元</p>
                        </div>
                    </div> 
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">feeType</label>

                        <div class="col-sm-10">
                            <input type="text" name="feeType" id="feeType" size="50" class="form-control" ng-model="feeType"/>

                            <p class="help-block">货币类型（CNY）</p>
                        </div>
                    </div> 
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">clientIp</label>

                        <div class="col-sm-10">
                            <input type="text" name="clientIp" id="clientIp" size="50" class="form-control"
                                   ng-model="clientIp"/>

                            <p class="help-block">客户端Ip</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">parameter</label>

                        <div class="col-sm-10">
                            <input type="text" name="parameter" id="parameter" size="50" class="form-control"
                                   ng-model="parameter"/>

                            <p class="help-block">扩展字段（可以传递自定义参数，支付成功后会回传）</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">phone</label>

                        <div class="col-sm-10">
                            <input type="text" name="phone" id="phone" size="50" class="form-control"
                                   ng-model="phone"/>

                            <p class="help-block">手机号（用来通知用户支付成功）</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 pay-service-server的 URL:</span>
                        <br/>

                        <div class="text-primary" id="userUnifyPayUri"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用支付接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
                
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
		$scope.outTradeNo="test20160517";
		$scope.userId="36133476-3827-4188-AE4A-0B9DBFC6AC64";
		$scope.merchantId="10001";
		$scope.appId="10026";
		$scope.goodsId="1";
		$scope.businessType="1";
		$scope.goodsName="testGoodsName";
		$scope.goodsDesc="testGoodsDesc";
		$scope.paymentType="10001";
		$scope.paymentChannel="10001";
		$scope.totalFee="1";
		$scope.inputCharset="utf-8";
		$scope.feeType="CNY";

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var outTradeNo=$("#outTradeNo").val();
		var userName=$("#userName").val();
		var userId=$("#userId").val();
		var appId=$("#appId").val();
		var merchantId=$("#merchantId").val();
		var goodsId=$("#goodsId").val();
		var goodsName=$("#goodsName").val();
		var goodsTag=$("#goodsTag").val();
		var goodsDesc=$("#goodsDesc").val();
		var showUrl=$("#showUrl").val();	
		var buyerRealName=$("#buyerRealName").val();
		var buyerCertNo=$("#buyerCertNo").val();
		var inputCharset=$("#inputCharset").val();
		var paymentOutTime=$("#paymentOutTime").val();
		var paymentType=$("#paymentType").val();
		var paymentChannel=$("#paymentChannel").val();
		var totalFee=$("#totalFee").val();
		var feeType=$("#feeType").val();
		var clientIp=$("#clientIp").val();
		var parameter=$("#parameter").val();
		var userUnifyPayUri=$("#unifyPayUri").val();
		
		if(outTradeNo==''){
		    alert("请输入outTradeNo业务方唯一订单号");
			return;
		}if(userId==''){
		    alert("请输入userId业务方用户Id");
			return;
		}
		if(merchantId==''){
		    alert("请输入merchantId商户号");
			return;
		}
		if(appId==''){
		    alert("请输入appId公共参数");
			return;
		}
		if(goodsName==''){
		    alert("请输入goodsName商品名称");
			return;
		}
		if(totalFee==''){
		    alert("请输入totalFee订单金额（输入整数）");
			return;
		}
		$.post("${contextPath}/getSignature",
			{
				appId:appId
			},
			function(data){
				if(data.flag){
				    var signature=data.signature;
				    var timestamp=data.timestamp;
				    var signatureNonce=data.signatureNonce;
				    var regUri=userUnifyPayUri+"?"+"outTradeNo="+outTradeNo+"&userName="+userName+"&userId="+userId+"&appId="+appId+"&merchantId="+merchantId+"&goodsId="+goodsId+"&goodsName="+goodsName+"&goodsDesc="+goodsDesc+"&goodsTag="+goodsTag+"&showUrl="+showUrl+"&buyerRealName="+buyerRealName+"&buyerCertNo="+buyerCertNo+"&inputCharset="+inputCharset+"&paymentOutTime="+paymentOutTime+"&paymentType="+paymentType+"&paymentChannel="+paymentChannel+"&totalFee="+totalFee+"&feeType="+feeType+"&parameter="+parameter+"&clientIp="+clientIp+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#userUnifyPayUri").html(regUri);
				}else{
				    jQuery("#userUnifyPayUri").html('无效数据，请重新申请');
				}
			}
 		);
	}
	
</script>

</body>
</html>