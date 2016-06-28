<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>register</title>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="register" method="post" class="form-horizontal">
                <input type="hidden" name="userRegisterUri" value="${userRegisterUri}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">response_type</label>

                        <div class="col-sm-10">
                            <input type="text" name="responseType" readonly="readonly"
                                   class="form-control" ng-model="responseType"/>

                            <p class="help-block">固定值 'code'</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">scope</label>

                        <div class="col-sm-10">
                            <select name="scope" ng-model="scope" class="form-control">
                                <option value="read">read</option>
                                <option value="write">write</option>
                                <option value="read,write">read,write</option>
                            </select>
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

                            <p class="help-block">
                                redirect_uri 是在 'UserController.java' 中定义的一个 URI, 用于检查server端返回的
                                'code'与'state',并发起对 access_token 的调用</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">state</label>

                        <div class="col-sm-10">
                            <input type="text" name="state" size="50" class="form-control" required="required"
                                   ng-model="state"/>

                            <p class="help-block">一个随机值, spring-oauth-server 将原样返回,用于检测是否为跨站请求(CSRF)等</p>
                        </div>
                    </div>

                    <div class="well well-sm">
                        <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>

                        <div class="text-primary">
                            {{userRegisterUri}}?response_type={{responseType}}&scope={{scope}}&client_id={{clientId}}&redirect_uri={{redirectUri}}&state={{state}}
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">去注册</button>
                <span class="text-muted">将重定向到 'spring-oauth-server' 的注册页面</span>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userRegisterUri = '${userRegisterUri}';
        $scope.responseType = 'code';
        $scope.scope = 'read,write';

        $scope.clientId = 'abf4bdedbea446f7af10dcfce1010e4f';
        $scope.redirectUri = '${host}authorization_code_callback';
        $scope.state = '${state}';

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>

</body>
</html>