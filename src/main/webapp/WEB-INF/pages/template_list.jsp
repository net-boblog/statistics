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
  <script src="${ctx}/resources/js/bootstrap/bootstrap.min.js"></script>
  <script src="${ctx}/resources/js/highcharts/highcharts.js"></script>
  <script src="${ctx}/resources/js/highcharts/modules/exporting.js"></script>
  <script type="text/javascript">
    </script>
</head>
<body>
    <div class="container">
      <div class="page-header">
        <h1>日志统计系统</h1>
      </div>
      <div class="box">
        <div class="box-header">
          <ul class="tag-group">
            <li class="tag tag-default active" data-toggle="tab" data-target="#room1">模板列表</li>
            <li class="tag tag-default" data-toggle="tab" data-target="#room2">新增模板</li>
            <li class="tag tag-default" data-toggle="tab" data-target="#room3">3</li>
            <li class="tag tag-default" data-toggle="tab" data-target="#room4">4</li>
          </ul>
        </div>
        <div class="box-body tab-content">
            <div class="tab-pane fade active in" id="room1">
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

            <div class="tab-pane fade" id="room2">
              <iframe src="${ctx}/template/view" style="width:100%;height:100%;"></iframe>
            </div>
            <div class="tab-pane fade" id="room3">
              <table>

              </table>
            </div>
            <div class="tab-pane fade" id="room4">4</div>

        </div>
        <div class="box-footer">
            分页
        </div>
      </div>
    </div>


</body>
</html>
