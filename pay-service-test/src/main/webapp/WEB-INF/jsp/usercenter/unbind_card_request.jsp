<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>change_card_resendsms(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="unBindCardRequest" method="post" class="form-horizontal" target="_blank">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="unbindCardDirectUri" id="unbindCardDirectUri" value="${unbindCardDirectUri}"/>
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
                        <label class="col-sm-2 control-label">cardNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="cardNo" id="cardNo" size="50" class="form-control"
                                   ng-model="cardNo"/>

                            <p class="help-block">卡号</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">identityId</label>

                        <div class="col-sm-10">
                            <input type="text" name="identityId" id="identityId" size="50" class="form-control"
                                   ng-model="identityId"/>
                            <p class="help-block">身份证号（必传）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">identityType</label>

                        <div class="col-sm-10">
                            <input type="text" name="identityType" id="identityType"size="50" class="form-control"
                                   ng-model="identityType"/>
                                    <p class="help-block">证件类型（必传）</p>
                        </div> 
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">userId</label>

                        <div class="col-sm-10">
                            <input type="text" name="userId" id="userId"size="50" class="form-control"
                                   ng-model="userId"/>
                                    <p class="help-block">用户id（必传）</p>
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
                <button type="submit" class="btn btn-primary">调用换卡重发接口</button>
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
        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var outTradeNo=$("#outTradeNo").val();
		var appId=$("#appId").val();
		var merchantId=$("#merchantId").val();
		var cardNo=$("#cardNo").val();
		var identityId=$("#identityId").val();
		var identityType=$("#identityType").val();
		var unbindCardDirectUri=$("#unbindCardDirectUri").val();
		
		if(outTradeNo==''){
		    alert("请输入outTradeNo业务方唯一订单号");
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
		$.post("${contextPath}/getSignature",
			{
				appId:appId
			},
			function(data){
				if(data.flag){
				    var signature=data.signature;
				    var timestamp=data.timestamp;
				    var signatureNonce=data.signatureNonce;
				    var regUri=unbindCardDirectUri+"?"+"outTradeNo="+outTradeNo+"&cardNo="+cardNo+"&appId="+appId+"&merchantId="+merchantId+"&identityId="+identityId+"&identityType="+identityType+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
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