<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>UserCenterDevLogin</title>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="userCenterDevLogin" method="post" class="form-horizontal">
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>
                <div ng-show="visible">
                   	<div class="form-group">
                        <label class="col-sm-2 control-label">userId</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="userId" required="required"
                                   ng-model="userId"/>

                            <p class="help-block">用户ID</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">appkey</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="appkey" required="required"
                                   ng-model="appkey"/>

                            <p class="help-block">appkey</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">appsecret</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="appsecret" required="required"
                                   ng-model="appsecret"/>

                            <p class="help-block">appsecret</p>
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">去调用登录接口</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userId = '8680770';
        $scope.appkey="4194b8dbd6624131bfccbdd6f7140776";
        $scope.appsecret="1d4d8c77108a4fd2a3c23feba0cfdccc";
        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>

</body>
</html>