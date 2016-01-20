<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
              <li class="tag tag-default" data-toggle="tab" data-target="#room3">3</li>
              <li class="tag tag-default " data-toggle="tab" data-target="#room4">4</li>
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



            <div class="tab-pane fade" id="room3">
                3
            </div>
            <div class="tab-pane fade" id="room4">4</div>

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
            <form id="updateForm" class="form-horizontal">

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
        <td>?</td>
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
       <div class="col-sm-8">
         <input class="form-control" id="interval" name="interval" type="text" placeholder="间隔时间" value="{{ interval || 0 }}"/>
       </div>
     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label" for="unit">间隔时间单位</label>
       <div class="col-sm-8">
         <select  id="unit" name="unit" class="form-control">
           <option value="1" {{if unit=="1" }} selected='selected' {{/if}}>分钟</option>
           <option value="2" {{if unit=="2" }} selected='selected' {{/if}}>小时</option>
           <option value="3" {{if unit=="3" }} selected='selected' {{/if}}>天</option>
           <option value="4" {{if unit=="4" }} selected='selected' {{/if}}>月</option>
         </select>
       </div>
     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label" for="minTermsCount">最少出现次数</label>
       <div class="col-sm-8">
         <input class="form-control" id="minTermsCount" name="minTermsCount" type="text" placeholder="" />
       </div>
     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label" for="termsCountField">统计字段</label>
       <div class="col-sm-8">
         <select  id="termsCountField" name="termsCountField" class="form-control">
           <option value="uid" {{if termsCountField=="uid" }} selected='selected' {{/if}}>用户ID</option>
           <option value="channel" {{if termsCountField=="channel" }} selected='selected' {{/if}}>渠道</option>
           <option value="terminal" {{if termsCountField=="terminal" }} selected='selected' {{/if}}>终端</option>
           <option value="prefix_page" {{if termsCountField=="prefix_page" }} selected='selected' {{/if}}>来源页</option>
           <option value="current_page" {{if termsCountField=="current_page" }} selected='selected' {{/if}}>当前页</option>
           <option value="event" {{if termsCountField=="event" }} selected='selected' {{/if}}>事件</option>
           <option value="key_word" {{if termsCountField=="key_word" }} selected='selected' {{/if}}>关键字</option>
         </select>
       </div>
     </div>
     <div class="form-group">
       <div class="col-sm-offset-2 col-sm-4">
         <input class="btn btn-block btn-greyPurple saveTemplate" type="button" value="保存" />
       </div>
     </div>
</script>

    <script src="${ctx}/resources/js/highcharts/highcharts.js"></script>
    <script src="${ctx}/resources/js/highcharts/modules/exporting.js"></script>
    <script src="${ctx}/resources/js/art-template.js"></script>
    <script src="${ctx}/resources/js/layer/layer.js"></script>
    <script src="${ctx}/resources/js/URI.min.js"></script>
    <script src="${ctx}/resources/js/common.js"></script>
    <script src="${ctx}/resources/js/list.js"></script>
</body>
</html>
