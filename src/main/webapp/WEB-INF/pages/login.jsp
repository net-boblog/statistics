<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/1/20
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
    <link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/resources/css/zxx.lib.css">
    <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.form.js"></script>
    <script src="${ctx}/resources/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<div class="container">

    <form class="form-signin" action="${ctx}/auth/dologin" method="post">
        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="name" class="sr-only">Email address</label>
        <input type="text" id="name" name="name" class="form-control" placeholder="用户名" required autofocus>
        <label for="password" class="sr-only">Password</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="密码" required>
        <div class="checkbox">
            <label>
                <input type="checkbox" value="remember-me"> Remember me
            </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登陆</button>
    </form>

</div> <!-- /container -->

</body>
</html>
