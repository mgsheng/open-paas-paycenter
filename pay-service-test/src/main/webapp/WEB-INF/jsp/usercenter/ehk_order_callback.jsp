<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>alipay-order-callback(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="ehkCallBack" method="post" class="form-horizontal">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="ehkCallBackUri" id="ehkCallBackUri" value="${ehkCallBackUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">notify_time</label>

                        <div class="col-sm-10">
                            <input type="text" name="notify_time" id="notify_time"
                                   class="form-control" ng-model="notify_time"/>

                            <p class="help-block">通知的发送时间。格式为yyyy-MM-dd HH:mm:ss。</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">notify_type</label>

                        <div class="col-sm-10">
                            <input type="text" name="notify_type" id="notify_type"
                                   class="form-control" ng-model="notify_type"/>
                           <p class="help-block">通知类型 </p>
                        </div>
                        
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">notify_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="notify_id" id="notify_id"
                                   class="form-control" ng-model="notify_id"/>
                                    <p class="help-block">通知校验ID</p>
                        </div>
                        
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">sign_type</label>

                        <div class="col-sm-10">
                            <select name="sign_type" ng-model="sign_type" id="sign_type"class="form-control">
                                <option value="MD5">MD5</option>
                                <option value="RSA">RSA</option>
                                <option value="DSA">DSA</option>
                            </select>
                            <p class="help-block">DSA、RSA、MD5三个值可选，必须大写。</p>
                        </div>
                    </div>                
                    <div class="form-group">
                        <label class="col-sm-2 control-label">sign </label>

                        <div class="col-sm-10">
                            <input type="text" name="sign" id="sign" class="form-control"
                                   size="50" ng-model="sign"/>

                            <p class="help-block">签名</p>
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
                <button type="submit" class="btn btn-primary">调用支付宝回调接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
    
        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
				var notify_time=$("#notify_time").val();
				var notify_type=$("#notify_type").val();
				var notify_id=$("#notify_id").val();
	   			var sign_type=$("#sign_type").val();
	   			var sign=$("#sign").val();
	   			var aliPayCallBackUri=$("#aliPayCallBackUri").val();
	   			
				if(notify_time==''){
				    alert("请输入notify_time");
					return;
				}if(notify_type==''){
				    alert("请输入notify_type");
					return;
				}
				if(notify_id==''){
				    alert("请输入notify_id");
					return;
				}
				if(sign_type==''){
				    alert("请输入sign_type");
					return;
				}
				if(sign==''){
				    alert("请输入sign");
					return;
				}
				$.post("${contextPath}/getAlipayCallBackUrl",
					{
						notify_time:notify_time,
						notify_type:notify_type,
						notify_id:notify_id,
			   			sign_type:sign_type,
			   			sign:sign
					},
					function(data){
						$("#regUri").html(data.fullUri);
					}
		 		);
			}
</script>

</body>
</html>