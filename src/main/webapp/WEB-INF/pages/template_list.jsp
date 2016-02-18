<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
  <meta http-equiv="Content-Type" params="text/html; charset=utf-8">
  <title>日志统计系统</title>
  <link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
  <link rel="stylesheet" href="${ctx}/resources/css/zxx.lib.css">
  <link rel="stylesheet" href="${ctx}/resources/css/font-awesome-4.5.0/css/font-awesome.min.css"/>

  <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery-2.1.4.min.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.form.js"></script>
  <script src="${ctx}/resources/js/bootstrap/bootstrap.min.js"></script>
    <script>
        var ROOT = '${ctx}';
        var DEFAULT_TEMP_ID = '';
    </script>
    <style>
        .pie{height: 300px;padding: 0;}input.time-input{border: 0 none;border-bottom: 1px solid #eee;color: #6ccb93;text-align: center;outline:none;}
        .fresh{background-color:#fff;position:absolute;top:0;bottom:0;left:0;right:0;text-align: center;z-index:2;display: flex;align-items: center;justify-content: center;}
        .tab-pane{min-height: 600px;}.autoFixList li{display:inline-block;padding:2px;border: 1px solid #eee;margin-right: 5px 5px 0 0;}.autoFixList ul{padding: 0;margin: 0;}
        .autoFixInput{margin:10px 0;}
        #itemsContainer>.col-sm-4{max-height: 400px;overflow: auto;}
    </style>
</head>
<body>
    <div class="container">
      <div class="page-header">
        <h1>
            日志统计系统
            <a href="javascript:;" class="pull-right" id="toggleSetting">
                <i class="fa fa-cog fa-1x" data-tooltip data-toggle="tooltip" data-placement="left" title="点击修改配置"></i>
            </a>
        </h1>
      </div>
        <%--统计系统功能标签页 start--%>
        <div class="box" id="settingBox" style="display:none;">
            <div class="box-header rel">
                <ul class="tag-group">
                    <li class="tag tag-default active" data-toggle="tab" data-target="#room1">模板列表</li>
                    <li class="tag tag-default" data-toggle="tab" data-target="#room2">字典列表</li>
                </ul>
                <a href="javascript:;" onclick="document.querySelector('#settingBox').style.display='none'" class="abs t10 r10">隐藏</a>
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
                        <tbody id="tempListTable">
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
                    <div class="rel">
                        <div class="fresh hidden"><i class="fa fa-refresh fa-spin fa-5x"></i></div>
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
        <%--统计系统功能标签页 end--%>

        <%--统计结果 图表部分 start--%>
      <div class="box" id="statResultBox">
          <div class="box-header">
              <h4>
                  名称: <a href="javascript:;" id="changeTemplate"><i class="fa fa-modx fa-1x" ></i></a>
                  <span id="statTempName" class="text-primary pr20 w50">&nbsp;&nbsp;&nbsp;&nbsp;</span>
                  总PV: <span id="totalPv" class="text-primary pr20 w50">&nbsp;&nbsp;&nbsp;&nbsp;</span>
                  总UV: <span id="totalUv" class="text-primary pr20 w50">&nbsp;&nbsp;&nbsp;&nbsp;</span>
                  起止时间:
                  <input type="text" id="statStartTime" data-inputmask="'mask': 'y-m-d h:s:s'" class="time-input"/>
                  <span class="text-primary">到</span>
                  <input type="text" id="statEndTime" data-inputmask="'mask': 'y-m-d h:s:s'" class="time-input"/>
                  <a href="javascript:;" id="searchBytime">查询</a>
              </h4>
          </div>
          <%--当前统计结果对应的模板 start--%>

          <div class="box-body bbe rel">
              <form id="searchForm" class="form-horizontal rel">
              </form>
              <span></span>
              <a href="javascript:;" class="slideToggleBtn abs r0 b0 db p5">收起</a>
          </div>

          <%--当前统计结果对应的模板 end--%>
          <div id="statsContainer" class="box-body rel">
              <div class="fresh"><i class="fa fa-refresh fa-spin fa-5x"></i></div>
              <div id="columnContainer" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
              <div id="pieContainer" class="row pb20"></div>
              <div id="itemsContainer" class="row bte pt20"></div>
          </div>
      </div>
        <%--统计结果 图表部分 end--%>

        <%--统计  漏斗图--%>
        <div class="box">
            <div class="box-header rel">
                <ul class="tag-group">
                    <li class="tag tag-default active" data-toggle="tab" data-target="#room3">漏斗图</li>
                </ul>
            </div>
            <div class="box-body tab-content ">
                <div class="tab-pane rel active in" id="room3">
                    <form id="funnelForm">
                        <label for="funnelIds">模板ID&nbsp;&nbsp;&nbsp;</label>
                        <input type="text" id="funnelIds" name="templateIds" class="time-input" placeholder="多个ID可用半角逗号隔开" style="width:292px;"/>
                        <a href="javascript:;" id="tooltip" data-toggle="tooltip" data-content="das"><i class="fa fa-plus-circle fa-2x"></i></a>
                        <br/>
                        <br/>
                        <label>起始时间 </label>
                        <input type="text" name="from" data-inputmask="'mask': 'y-m-d h:s:s'" class="time-input"/>
                        <span class="text-primary">—</span>
                        <input type="text" name="to" data-inputmask="'mask': 'y-m-d h:s:s'" class="time-input"/>
                        <button class="btn btn-primary btn-xs">查询</button>
                    </form>
                    <div class="fresh hidden"><i class="fa fa-refresh fa-spin fa-5x"></i></div>
                    <div id="funnelContainer"></div>
                </div>
            </div>
        </div>
        <%--统计  漏斗图end--%>

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

    <script type="text/html" id="statItemListTemp">
        <div class="col-sm-4">
            <table class="table table-bordered">
                <caption>{{ title }}</caption>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>count</th>
                    </tr>
                </thead>
                <tbody>
                {{each list as item}}
                    <tr>
                        <td class="ell" style="max-width:200px;">{{item.value}}</td>
                        <td>{{item.count}}</td>
                    </tr>
                {{/each}}
                </tbody>
            </table>
        </div>
    </script>
    <div id="tooltipTemp" class="hidden">
        <c:forEach items="${templates}" var="template">
            <a href="javascript:;" class="funnelAddID" data-id="${template.id}"><b>${template.id}</b> ${template.name}</a><br/>
        </c:forEach>
    </div>
<script id="insertTemplateTemp" type="text/html">
        <tr data-id="{{ id }}">
            <td>{{ id }}</td>
            <td>{{ name }}</td>
            <td>
                <a href="javascript:;" data-id="{{ id }}" class="editTemplate">编辑</a>
                <a href="javascript:;" data-id="{{ id }}" data-name="{{ name }}" class="showCharts">查看统计</a>
                <a href="javascript:;" data-id="{{ id }}" class="text-danger delTemplate">删除</a>
            </td>
        </tr>
</script>
<script type="text/html" id="dictListTemp">
    <tr>
        <td>
            <input type="text" placeholder="ID" class="form-control center-block text-center w200 dictID"/>
        </td>
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
            <div class="center-block" style="width: 200px;">
                <input type="text" class="form-control text-center dictDesc" placeholder="字段描述"/>
            </div>
        </td>
        <td>
            <button class="btn btn-primary addDict">添加</button>
        </td>
    </tr>
  {{each list as item}}
    <tr data-id="{{ item.id}}" data-type="{{ item.type}}" data-description="{{ item.description}}">
      <td class="ell" style="max-width:400px;">{{ item.id}}</td>
      <td>{{ item.type | filterDictType }}</td>
      <td class="ell" style="max-width:400px;">{{ item.description }}</td>
      <td>
          <a href="javascript:;" class="text-greyPurple editDict">编辑</a>
          <a href="javascript:;" class="text-danger delDict" data-id="{{ item.id}}">删除</a>
      </td>
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
         <input class="form-control" id="id" name="id" type="text" value="{{ id }}" />
       </div>
     </div>
     <div class="form-group" style="display: none">
       <label  class="col-sm-2 control-label" for="type">类型</label>
       <div class="col-sm-4">
         <input class="form-control" id="type" name="type" type="text" value="{{ type || 0 }}" />
       </div>
     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label" for="name"><span class="text-pinkRed">*</span>模板名</label>
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
       <div class="col-sm-8" id="tempprefixPages"><label class="control-label">暂未限定来源页</label></div>
       <div class="col-sm-8 col-sm-offset-2" class="autoFixBox">
           <input type="text" class="form-control autoFixInput w200" data-type="PAGES" data-name="prefixPages" placeholder="输入关键字检索,点击添加"/>
           <div class="autoFixList"><ul></ul></div>
       </div>
     </div>
     <div class="form-group">
       <label  class="col-sm-2 control-label">停留页</label>
       <div class="col-sm-8" id="tempcurrentPages"><label class="control-label">暂未限定停留页</label></div>
       <div class="col-sm-8 col-sm-offset-2" class="autoFixBox">
           <input type="text" class="form-control autoFixInput w200" data-type="PAGES" data-name="currentPages" placeholder="输入关键字检索,点击添加"/>
           <div class="autoFixList"><ul></ul></div>
       </div>
     </div>

     <div class="form-group">
       <label  class="col-sm-2 control-label">事件</label>
       <div class="col-sm-8" id="tempevents"><label class="control-label">暂未限定事件</label></div>
       <div class="col-sm-8 col-sm-offset-2" class="autoFixBox">
           <input type="text" class="form-control autoFixInput w200" data-type="EVENTS" data-name="events" placeholder="输入关键字检索,点击添加"/>
           <div class="autoFixList"><ul></ul></div>
       </div>
     </div>

     <div class="form-group">
       <!-- Search input-->
       <label  class="col-sm-2 control-label" for="interval">间隔时间</label>
       <div class="col-sm-3">
         <input class="form-control" id="interval" name="interval" type="text" placeholder="间隔时间" value="{{ interval || 0 }}"/>
       </div>
         <%--<label  class="col-sm-3 control-label" for="unit">时间单位</label>--%>
         <div class="col-sm-3">
             <select  id="unit" name="unit" class="form-control">
                 <option value="0" {{if unit=="0" }} selected='selected' {{/if}}>请选择</option>
                 <option value="1" {{if unit=="1" }} selected='selected' {{/if}}>分钟（默认展示最近1小时）</option>
                 <option value="2" {{if unit=="2" }} selected='selected' {{/if}}>小时（默认展示最近48小时）</option>
                 <option value="3" {{if unit=="3" }} selected='selected' {{/if}}>天（默认展示最近一周）</option>
                 <option value="4" {{if unit=="4" }} selected='selected' {{/if}}>月（默认展示最近12个月）</option>
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
     {{ if stats}}
     <div class="form-group">
       <div class="col-sm-offset-2 col-sm-4">
         <input class="btn btn-block btn-primary saveTemplateAndSearch" type="button" data-id='{{ id }}' data-name="{{ name }}" value="保存并查看统计结果" />
       </div>
     </div>
     {{else}}
     <div class="form-group">
       <div class="col-sm-offset-2 col-sm-4">
         <input class="btn btn-block btn-greyPurple saveTemplate" type="button" value="保存" />
       </div>
     </div>
    {{/if}}
</script>
    <script id="checkboxTemp" type="text/html">
        {{ each list as item index}}
        <span>
			<input type="checkbox" id="{{ timestamp }}{{ index }}" name="{{ name }}" value="{{ item.id }}" checked="checked">
			<label for="{{ timestamp }}{{ index }}">{{ item.description }}</label>
		</span>
        {{/each}}
    </script>
    <script id="changeTempTemp" type="text/html">
        {{ each list as template }}
            <a href="javascript:;" data-id="{{template.id}}" data-name="{{template.name}}" class="showCharts">{{template.name}}</a><br/>
        {{/each}}
        <a href="javascript:;" class="addTemplate"><i class="fa fa-plus-square"></i> 新模板</a>
    </script>
    <script id="tempListTableTemp" type="text/html">
        {{ each list as template }}
            <tr data-id="{{template.id}}">
                <td>{{template.id}}</td>
                <td>{{template.name}}</td>
                <td>
                    <a href="javascript:;" data-id="{{template.id}}" class="editTemplate">编辑</a>
                    <a href="javascript:;" data-id="{{template.id}}" data-name="{{template.name}}" class="showCharts">查看统计</a>
                    <a href="javascript:;" data-id="{{template.id}}" class="text-danger delTemplate">删除</a>
                </td>
            </tr>
        {{/each}}
    </script>
    <script>
        window.PAGES = [<c:forEach items="${pages}" var="page" varStatus="status">'${page.description}&&${page.id}',</c:forEach>];
        window.EVENTS = [<c:forEach items="${events}" var="event" varStatus="status">'${event.description}&&${event.id}',</c:forEach>];
        window.TEMPLIST = [<c:forEach items="${templates}" var="template">{id:'${template.id}',name:'${template.name}'},</c:forEach>];
    </script>
    <script src="${ctx}/resources/js/highcharts/highcharts.js"></script>
    <script src="${ctx}/resources/js/highcharts/modules/exporting.js"></script>
    <script src="${ctx}/resources/js/highcharts/modules/funnel.js"></script>
    <script src="${ctx}/resources/js/art-template.js"></script>
    <script src="${ctx}/resources/js/input-mask3/jquery.inputmask.min.js"></script>
    <script src="${ctx}/resources/js/input-mask3/inputmask.min.js"></script>
    <script src="${ctx}/resources/js/input-mask3/inputmask.date.extensions.min.js"></script>
    <script src="${ctx}/resources/js/common.js"></script>
    <script src="${ctx}/resources/js/list.js"></script>
</body>
</html>
