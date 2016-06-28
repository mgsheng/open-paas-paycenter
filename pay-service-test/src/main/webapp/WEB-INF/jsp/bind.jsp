<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>bind</title>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="bind" method="post" class="form-horizontal">
                <input type="hidden" name="userBindUri" value="${userBindUri}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">user_id</label>
                        <div class="col-sm-10">
                            <input type="text" name="userId" class="form-control" ng-model="userId"/>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">real_name</label>
                        <div class="col-sm-10">
                            <input type="text" name="realName" class="form-control" ng-model="realName"/>
                        </div>
                    </div>

					<div class="form-group">
                        <label class="col-sm-2 control-label">email</label>
                        <div class="col-sm-10">
                            <input type="text" name="email" class="form-control" ng-model="email"/>
                        </div>
                    </div>

					<div class="form-group">
                        <label class="col-sm-2 control-label">phone</label>
                        <div class="col-sm-10">
                            <input type="text" name="phone" class="form-control" ng-model="phone"/>
                        </div>
                    </div>

					<div class="form-group">
                        <label class="col-sm-2 control-label">identify_type</label>
                        <div class="col-sm-10">
                            <select name="identifyType" ng-model="identifyType" class="form-control">
                                <option value="1">身份证</option>
                                <option value="2">军官证</option>
                                <option value="3">港澳台证</option>
                                <option value="4">护照</option>
                                <option value="5">其他</option>
                            </select>
                        </div>
                    </div>

					<div class="form-group">
                        <label class="col-sm-2 control-label">identify_no</label>
                        <div class="col-sm-10">
                            <input type="text" name="identifyNo" class="form-control" ng-model="identifyNo"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">client_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="clientId" required="required"
                                   class="form-control" ng-model="clientId"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">redirect_uri</label>

                        <div class="col-sm-10">
                            <input type="text" name="redirectUri" readonly="readonly" class="form-control"
                                   required="required" size="50" ng-model="redirectUri"/>
                        </div>
                    </div>

					<div class="form-group">
                        <label class="col-sm-2 control-label">error_uri</label>

                        <div class="col-sm-10">
                            <input type="text" name="errorUri" readonly="readonly" class="form-control"
                                   required="required" size="50" ng-model="errorUri"/>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                        <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>

                        <div class="text-primary">
                            {{userBindUri}}?source_id={{userId}}&email={{email}}&phone={{phone}}&real_name={{realName}}&identify_type={{identifyType}}&identity_no={{identifyNo}}&client_id={{clientId}}&redirect_uri={{redirectUri}}
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">去绑定</button>
                <span class="text-muted">将重定向到 'spring-oauth-server' 的绑定页面</span>
            </form>
			<div>${mess }</div>
        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userBindUri = '${userBindUri}';
        $scope.userId = '1';
        $scope.realName = 'xiaoli';
        $scope.email = 'li@qq.com';
        $scope.phone = '1234567890';
        $scope.identifyType = 1;
        $scope.identifyNo = '130681199209220319';

        $scope.clientId = 'abf4bdedbea446f7af10dcfce1010e4f';
        $scope.redirectUri = '${host}client_credentials';
        $scope.errorUri = '${host}bind';

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>

</body>
</html>