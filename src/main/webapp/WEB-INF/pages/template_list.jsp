<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="Content-Type" params="text/html; charset=utf-8">
  <title>日志统计系统</title>
  <link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
  <link rel="stylesheet" href="${ctx}/resources/css/bootstrap-theme.min.css">
  <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery-2.1.4.min.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.form.js"></script>
  <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <script src="${ctx}/resources/js/highcharts/highcharts.js"></script>
  <script src="${ctx}/resources/js/highcharts/modules/exporting.js"></script>
  <script type="text/javascript">
    </script>
</head>
<body>
<div class="table-responsive">
  <table id="resultTable" class="table table-bordered">
    <thead>
    <tr>
      <td>模板ID</td>
      <td>模板名</td>
      <td>操作</td>
    </tr>
    </thead>
    <tbody>
        <c:forEach items="${templates}" var="template">
          <tr>
            <td>${template.id}</td>
            <td>${template.name}</td>
            <td>
              <a href="#">编辑</a>
              <a href="${ctx}/report/searchByTemplate?templateId=${template.id}">搜索</a>
            </td>
          </tr>
        </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
