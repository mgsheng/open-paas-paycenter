<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>user-dirct-pay(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="dirctPay" method="post" class="form-horizontal">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="dirctPayUri" id="dirctPayUri" value="${dirctPayUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">partner</label>

                        <div class="col-sm-10">
                            <input type="text" name="partner" id="partner"
                                   class="form-control" ng-model="partner"/>

                            <p class="help-block">合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">seller_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="seller_id" id="seller_id"
                                   class="form-control" ng-model="seller_id"/>
                           <p class="help-block">收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号</p>
                        </div>
                        
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">key</label>

                        <div class="col-sm-10">
                            <input type="text" name="key" id="key"
                                   class="form-control" ng-model="key"/>
                                    <p class="help-block">MD5密钥，安全检验码，由数字和字母组成的32位字符串</p>
                        </div>
                        
                    </div>
                    
                                     
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">notify_url</label>

                        <div class="col-sm-10">
                            <input type="text" name="notify_url" id="notify_url" class="form-control"
                                   size="50" ng-model="notify_url"/>

                            <p class="help-block">服务器异步通知页面路径</p>
                        </div>
                    </div>

					<div class="form-group">  
                        <label class="col-sm-2 control-label">return_url</label>

                        <div class="col-sm-10">
                            <input type="text" name="return_url" size="50" id="return_url" class="form-control"
                                   ng-model="return_url"/>

                            <p class="help-block">页面跳转同步通知页面路径</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">input_charset</label>

                        <div class="col-sm-10">
                            <input type="text" name="input_charset" id="input_charset" size="50" class="form-control"
                                   ng-model="input_charset"/>

                            <p class="help-block">字符编码格式 目前支持 gbk 或 utf-8</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">anti_phishing_key</label>

                        <div class="col-sm-10">
                            <input type="text" name="anti_phishing_key" id="anti_phishing_key"size="50" class="form-control"
                                   ng-model="anti_phishing_key"/>
                                    <p class="help-block">防钓鱼时间戳  若要使用请调用类文件submit中的query_timestamp函数</p>
                        </div> 
                        
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">service</label>

                        <div class="col-sm-10">
                            <input type="text" name="service" id="service" size="50" class="form-control"
                                   ng-model="service"/>

                            <p class="help-block">调用的接口名，无需修改</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">exter_invoke_ip</label>

                        <div class="col-sm-10">
                            <input type="text" name="exter_invoke_ip" id="exter_invoke_ip" size="50" class="form-control"
                                   ng-model="exter_invoke_ip"/>

                            <p class="help-block">客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">out_trade_no</label>

                        <div class="col-sm-10">
                            <input type="text" name="out_trade_no" id="out_trade_no" size="50" class="form-control"
                                   ng-model="out_trade_no"/>

                            <p class="help-block">商户订单号，商户网站订单系统中唯一订单号，必填</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">subjects</label>

                        <div class="col-sm-10">
                            <input type="text" name="subjects" id="subjects" size="50" class="form-control"
                                   ng-model="subjects"/>

                            <p class="help-block">订单名称，必填</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">total_fee</label>

                        <div class="col-sm-10">
                            <input type="text" name="total_fee" id="total_fee" size="50" class="form-control"
                                   ng-model="total_fee"/>

                            <p class="help-block">付款金额，必填    输入值以'元'为单位</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">body</label>

                        <div class="col-sm-10">
                            <input type="text" name="body" id="body" size="50" class="form-control"
                                   ng-model="body"/>

                            <p class="help-block">商品描述，可空</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">sign_type</label>

                        <div class="col-sm-10">
                            <select name="sign_type" ng-model="sign_type" id="sign_type"class="form-control">
                                <option value="MD5">是</option>
                                <option value="0">否</option>
                            </select>
                            <p class="help-block">签名方式</p>
                        </div>
                    </div> 
                       <div class="form-group">
                        <label class="col-sm-2 control-label">payment_type</label>

                        <div class="col-sm-10">
                            <select name="payment_type" ng-model="payment_type" id="payment_type"class="form-control">
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                            <p class="help-block">支付类型</p>
                        </div>
                    </div> 
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>

                        <div class="text-primary" id="regUri"> 
                        </div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用即时转账接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
    	$scope.partner="2088801478647757";
    	$scope.seller_id="2088801478647757";
    	$scope.key="peswcu2255jnvguqgudz4irhoi9rizz9";
    	$scope.notify_url="http://localhost:8080/pay-service/alipay/order/callBack";
    	$scope.return_url="http://localhost:8080/pay-service/alipay/order/callBack"
    	$scope.input_charset="utf-8";
    	$scope.anti_phishing_key="";
    	$scope.service="create_direct_pay_by_user";
    	$scope.exter_invoke_ip="";
		$scope.out_trade_no="test20160517";
		$scope.subjects="testGoodsName";
    	$scope.total_fee="0.01";
    	$scope.body="";
    	$scope.sign_type="MD5";
    	$scope.payment_type="1";
    
        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var partner=$("#partner").val();
		var seller_id=$("#seller_id").val();
		var key=$("#key").val();
		var notify_url=$("#notify_url").val();
		var return_url=$("#return_url").val();
		var input_charset=$("#input_charset").val();
		var anti_phishing_key=$("#anti_phishing_key").val();
		var service=$("#service").val();
		var exter_invoke_ip=$("#exter_invoke_ip").val();
		var out_trade_no=$("#out_trade_no").val();
		var subjects=$("#subjects").val();
		var total_fee=$("#total_fee").val();
		var body=$("#body").val();
		var sign_type=$("#sign_type").val();
		var payment_type=$("#payment_type").val();
		var dirctPayUri=$("#dirctPayUri").val();
  		
  		if(total_fee==''){
		    alert("请输入total_fee");
			return;
		}
		if(sign_type==''){
		    alert("请输入sign_type");
			return;
		}
		if(payment_type==''){
		    alert("请输入payment_type");
			return;
		}	
		if(partner==''){
		    alert("请输入partner");
			return;
		}if(seller_id==''){
		    alert("请输入seller_id");
			return;
		}
		if(key==''){
		    alert("请输入key");
			return;
		}
		if(notify_url==''){
		    alert("请输入notify_url");
			return;
		}
		if(return_url==''){
		    alert("请输入return_url");
			return;
		}
		if(input_charset==''){
		    alert("请输入input_charset");
			return;
		}/* if(anti_phishing_key==''){
		    alert("请输入anti_phishing_key");
			return;
		} */
		if(service==''){
		    alert("请选择service");
			return;
		}/* if(exter_invoke_ip==''){
		    alert("请选择exter_invoke_ip");
			return;
		} */
		if(out_trade_no==''){
		    alert("请选择out_trade_no");
			return;
		}if(subjects==''){
		    alert("请选择subjects");
			return;
		}
	    var regUri=dirctPayUri+"?"+"partner="+partner+"&seller_id="+seller_id+"&key="+key+"&notify_url="+notify_url+"&return_url="+return_url+"&sign_type="+sign_type+"&payment_type="+payment_type+"&input_charset="+input_charset+"&service="+service+"&anti_phishing_key="+anti_phishing_key+"&exter_invoke_ip="+exter_invoke_ip+"&out_trade_no="+out_trade_no+"&subjects="+subjects+"&total_fee="+total_fee+"&body="+body;
		$("#regUri").html(regUri);
	}
</script>

</body>
</html>