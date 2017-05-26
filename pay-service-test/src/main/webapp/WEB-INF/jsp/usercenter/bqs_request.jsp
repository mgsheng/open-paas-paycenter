<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>bqs-request(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="bqsRequest" method="post" class="form-horizontal" target="_blank">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="bqsUri" id="bqsUri" value="${bqsUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">outTradeNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="merchantOrderId" id="merchantOrderId"
                                   class="form-control" ng-model="merchantOrderId"/>

                            <p class="help-block">业务方唯一订单号（必填）</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">certNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="certNo" id="certNo"
                                   class="form-control" ng-model="certNo"/>
                           <p class="help-block">身份证号（必传）</p>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-sm-2 control-label">channelId</label>

                        <div class="col-sm-10">
							<select name="scoreChannel" id="scoreChannel" class="form-control" ng-mode="scoreChannel">
								<option value="1">白骑士</option>
								<option value="2">前海征信</option>
							</select>
                            <p class="help-block">授权渠道（必传）</p>
                        </div>
                    </div>
                                     
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">userName</label>

                        <div class="col-sm-10">
                            <input type="text" name="userName" id="userName" class="form-control"
                                   size="50" ng-model="userName"/>

                            <p class="help-block">用户姓名（必传）</p>
                        </div>
                    </div>

					<div class="form-group">  
                        <label class="col-sm-2 control-label">appId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId" size="50" id="appId" class="form-control"
                                   ng-model="appId"/>

                            <p class="help-block">应用Id,23-测试应用Id（必传）</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">phone</label>

                        <div class="col-sm-10">
                            <input type="text" name="phone" id="phone" size="50" class="form-control"
                                   ng-model="phone"/>
                            <p class="help-block">手机号</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">merchantId</label>

                        <div class="col-sm-10">
                            <input type="text" name="merchantId" id="merchantId" size="50" class="form-control"
                                   ng-model="merchantId"/>

                            <p class="help-block">商户id（必传）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">parameter</label>

                        <div class="col-sm-10">
                            <input type="text" name="parameter" id="parameter" size="50" class="form-control"
                                   ng-model="parameter"/>

                            <p class="help-block">备用参数</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 pay-service-server的 URL:</span>
                        <br/>

                        <div class="text-primary" id="bqsUrl"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用白骑士接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
                
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
		$scope.merchantOrderId="test201705161408";
		$scope.merchantId="10001";
		$scope.userName="张四";
		$scope.certNo="420922198509103814";
		$scope.channelId="10026";
		$scope.phone="13535413805";
		$scope.reasonNo="10026";
		$scope.cardNo="62222744552211111112";
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
		var certNo=$("#certNo").val();
		var appId=$("#appId").val();
		var merchantId=$("#merchantId").val();
		var channelId=$("#channelId").val();
		var mobile=$("#phone").val();
		var bqsUri=$("#bqsUri").val();
		
		if(outTradeNo==''){
		    alert("请输入outTradeNo业务方唯一订单号");
			return;
		}if(userName==''){
		    alert("请输入用户名");
			return;
		}
		if(merchantId==''){
		    alert("请输入merchantId商户号");
			return;
		}
		if(mobile==''){
		    alert("请输入mobile商户号");
			return;
		}
		if(appId==''){
		    alert("请输入appId公共参数");
			return;
		}
		if(certNo==''){
		    alert("请输入身份证号");
			return;
		}
		if(channelId==''){
		    alert("请输入渠道ID");
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
				    var regUri=bqsUri+"?"+"outTradeNo="+outTradeNo+"&userName="+userName+"&userId="+userId+"&appId="+appId+"&merchantId="+merchantId+"&certNo="+certNo+"&channelId="+channelId+"&mobile="+mobile+"&parameter="+parameter+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#bqsUrl").html(regUri);
				}else{
				    jQuery("#bqsUrl").html('无效数据，请重新申请');
				}
			}
 		);
	}
	
</script>

</body>
</html>