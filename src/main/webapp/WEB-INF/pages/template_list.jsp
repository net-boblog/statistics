<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <meta http-equiv="Content-Type" params="text/html; charset=utf-8">
  <title>日志统计系统</title>
  <link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
  <link rel="stylesheet" href="${ctx}/resources/css/zxx.lib.css">
  <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery-2.1.4.min.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.form.js"></script>
  <script src="${ctx}/resources/js/bootstrap/bootstrap.min.js"></script>
    <script>
        var ROOT = '${ctx}';
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
              <li class="tag tag-default" data-toggle="tab" data-target="#room2">字典列表</li>
          </ul>
        </div>
        <div class="box-body tab-content">
            <div class="tab-pane fade active in" id="room1">
              <div class="clearfix pb20">
                  <button class="btn btn-primary addTemplate pull-right">+ 新增模板</button>
              </div>
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
                      <a href="javascript:;" data-id="${template.id}" class="editTemplate">编辑</a>
                      <a href="${ctx}/report/searchByTemplate?templateId=${template.id}">搜索</a>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>




            <div class="tab-pane fade" id="room2">
                <form class="form-horizontal" id="searchDict">
                    <div class="form-group">
                        <label for="searchDictID" class="col-sm-1 control-label">ID</label>
                        <div class="col-sm-1">
                            <input type="text" id="searchDictID" class="form-control" name="ids"/>
                        </div>
                        <label for="searchDictType" class="col-sm-1 control-label">类型</label>
                        <div class="col-sm-2">
                            <select name="type" id="searchDictType" class="form-control">
                                <option value="">请选择</option>
                                <option value="1">页面</option>
                                <option value="2">事件</option>
                                <option value="3">渠道</option>
                                <option value="4">终端</option>
                            </select>
                        </div>
                        <label for="searchDictDesc" class="col-sm-1 control-label">描述</label>
                        <div class="col-sm-2">
                            <input type="text" id="searchDictDesc" class="form-control" name="description"/>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="btn btn-primary searchDictBtn">搜索</button>
                        </div>
                    </div>
                </form>

                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <td>ID</td>
                        <td>字段类型</td>
                        <td>字段描述</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody id="dictList">

                    </tbody>
                </table>
            </div>

        </div>
      </div>
    </div>

    <div class="modal fade" id="templateModal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">编辑模板</h4>
          </div>
          <div class="modal-body">
            <form id="updateForm" class="form-horizontal rel">

            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    

<script type="text/html" id="dictListTemp">
    <tr>
        <td><input type="text" placeholder="ID" class="form-control text-center w100"/></td>
        <td>
            <div class="center-block" style="width: 150px;">
                <select name="dictType" class="form-control dictType">
                    <option value="">请选择</option>
                    <option value="1">页面</option>
                    <option value="2">事件</option>
                    <option value="3">渠道</option>
                    <option value="4">终端</option>
                </select>
            </div>
        </td>
        <td>
            <div class="center-block" style="width: 150px;">
                <input type="text" class="form-control text-center dictDesc" placeholder="字段描述"/>
            </div>
        </td>
        <td>
            <button class="btn btn-primary addDict">添加</button>
        </td>
    </tr>
  {{each list as item}}
    <tr data-id="{{ item.id}}" data-type="{{ item.type}}" data-description="{{ item.description}}">
      <td>{{ item.id}}</td>
      <td>{{ item.type | filterDictType }}</td>
      <td>{{ item.description }}</td>
      <td><button class="btn btn-greyPurple editDict">编辑</button></td>
    </tr>
  {{/each}}
</script>

<script type="text/html" id="selectTemp">
    <div class="center-block" style="width: 150px;">
        <select name="dictType" class="form-control dictType">
            <option value="">请选择</option>
            <option value="1" {{if dictType=="1" }} selected='selected' {{/if}}>页面</option>
            <option value="2" {{if dictType=="2" }} selected='selected' {{/if}}>事件</option>
            <option value="3" {{if dictType=="3" }} selected='selected' {{/if}}>渠道</option>
            <option value="4" {{if dictType=="4" }} selected='selected' {{/if}}>终端</option>
        </select>
    </div>
</script>
<script type="text/html" id="inputTemp">
    <div class="center-block" style="width: 150px;">
        <input type="text" class="form-control text-center dictDesc" placeholder="字段描述" value="{{ dictDesc }}"/>
    </div>
</script>

<script type="text/html" id="templateTemp">
     <div class="form-group" style="display: none">
       <label  class="col-sm-2 control-label" for="id">ID</label>
       <div class="col-sm-4">
         <input class="form-control" id="id" name="id" type="text" value="{{ id || 0 }}" />
       </div>
     </div>
     <div class="form-group" style="display: none">
       <label  class="col-sm-2 control-label" for="type">类型</label>
       <div class="col-sm-4">
         <input class="form-control" id="type" name="type" type="text" value="{{ type || 0 }}" />
       </div>
     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label" for="name">模板名</label>
       <div class="col-sm-4">
         <input class="form-control" id="name" name="name" type="text" value="{{ name }}" placeholder="搜索模板名" />
       </div>
     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label">终端</label>
       <div class="col-sm-8">
         <c:forEach items="${terminals}" var="terminal" varStatus="status">
					<span>
						<input type="checkbox" id="terminals${status.index}" name="terminals" value="${terminal.id}">
						<label for="terminals${status.index}">${terminal.description}</label>
					</span>
         </c:forEach>
       </div>
     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label">渠道</label>
       <div class="col-sm-8">
         <c:forEach items="${channels}" var="channel" varStatus="status">
					<span>
						<input type="checkbox" id="channels${status.index}" name="channels" value="${channel.id}">
						<label for="channels${status.index}">${channel.description}</label>
					</span>
         </c:forEach>
       </div>
     </div>

     <div class="form-group">
       <label  class="col-sm-2 control-label">来源页</label>
       <div class="col-sm-8">
         <c:forEach items="${pages}" var="page" varStatus="status">
					<span>
						<input type="checkbox" id="prefixPages${status.index}" name="prefixPages" value="${page.id}">
						<label for="prefixPages${status.index}">${page.description}</label>
					</span>
         </c:forEach>
       </div>

     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label">停留页</label>
       <div class="col-sm-8">
         <c:forEach items="${pages}" var="page" varStatus="status">
					<span>
						<input type="checkbox" id="currentPages${status.index}" name="currentPages" value="${page.id}">
						<label for="currentPages${status.index}">${page.description}</label>
					</span>
         </c:forEach>
       </div>
     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label">事件</label>
       <div class="col-sm-8">
         <c:forEach items="${events}" var="event" varStatus="status">
					<span>
						<input type="checkbox" id="events${status.index}" name="events" value="${event.id}">
						<label for="events${status.index}">${event.description}</label>
					</span>
         </c:forEach>
       </div>
     </div>
     <div class="form-group">
       <!-- Search input-->
       <label  class="col-sm-2 control-label" for="keyWords">关键字</label>
       <div class="col-sm-8">
         <input class="form-control" id="keyWords" name="keyWords" type="text" placeholder="关键字" />
       </div>
     </div>
     <div class="form-group">
       <!-- Search input-->
       <label  class="col-sm-2 control-label" for="interval">间隔时间</label>
       <div class="col-sm-2">
         <input class="form-control" id="interval" name="interval" type="text" placeholder="间隔时间" value="{{ interval || 0 }}"/>
       </div>
         <label  class="col-sm-3 control-label" for="unit">时间单位</label>
         <div class="col-sm-3">
             <select  id="unit" name="unit" class="form-control">
                 <option value="0" {{if unit=="0" }} selected='selected' {{/if}}>请选择</option>
                 <option value="1" {{if unit=="1" }} selected='selected' {{/if}}>分钟</option>
                 <option value="2" {{if unit=="2" }} selected='selected' {{/if}}>小时</option>
                 <option value="3" {{if unit=="3" }} selected='selected' {{/if}}>天</option>
                 <option value="4" {{if unit=="4" }} selected='selected' {{/if}}>月</option>
             </select>
         </div>
     </div>
     <div class="form-group extra">
         <label  class="col-sm-2 control-label">附加字段</label>
         <div class="col-sm-3">
             <input class="form-control extraName" type="text" placeholder="描述" value=""/>
         </div>
         <div class="col-sm-3">
             <input class="form-control extraKey" type="text" placeholder="关键字" value=""/>
         </div>
         <div class="col-sm-1">
             <button type="button" class="btn btn-link addExtra"> 添加 </button>
         </div>
     </div>
     {{ each extra as item }}
     <div class="form-group extra">
         <label  class="col-sm-2 control-label"></label>
         <div class="col-sm-3">
             <input class="form-control extraName" type="text" placeholder="描述" value="{{item.name}}"/>
         </div>
         <div class="col-sm-3">
             <input class="form-control extraKey" type="text" placeholder="关键字" value="{{item.value}}"/>
         </div>
         <div class="col-sm-1">
             <button type="button" class="btn btn-link delExtra"> 删除 </button>
         </div>
     </div>
     {{/each}}
     <div class="form-group">
       <div class="col-sm-offset-2 col-sm-4">
         <input class="btn btn-block btn-greyPurple saveTemplate" type="button" value="保存" />
       </div>
     </div>
</script>

    <script src="${ctx}/resources/js/highcharts/highcharts.js"></script>
    <script src="${ctx}/resources/js/highcharts/modules/exporting.js"></script>
    <script src="${ctx}/resources/js/art-template.js"></script>
    <script src="${ctx}/resources/js/common.js"></script>
    <script src="${ctx}/resources/js/list.js"></script>
</body>
</html>
