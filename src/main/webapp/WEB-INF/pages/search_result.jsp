<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
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
        var loadPvUv=function(){
            var pvs=$.parseJSON('${pvs}');
            var uvs= $.parseJSON('${uvs}');
            var ips= $.parseJSON('${ips}');
            var times= $.parseJSON('${times}');
            $('#pvuvContainer').highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: '柱状图'
                },
                subtitle: {
                    text: 'Source: WorldClimate.com'
                },
                xAxis: {
                    categories: times,
                    crosshair: true
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ''
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: [{
                    name: 'PV',
                    data: pvs
                }, {
                    name: 'UV',
                    data: uvs
                }, {
                    name: 'IP',
                    data: ips
                }]
            });
        };
        var loadChannel=function(){
            var chanel_terms_agg=$.parseJSON('${channel_terms_agg}');
            var channelData=new Array();
            for(var i = 0 ,l = chanel_terms_agg.length; i< l; i++){
                var result=new Object();
                result.name=chanel_terms_agg[i].key;
                result.y=chanel_terms_agg[i].count;

                channelData.push(result);
            }
            $('#channelContainer').highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type: 'pie'
                },
                title: {
                    text: '渠道分布'
                },
                subtitle: {
                    text: 'www.xiaoluo.com'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: false
                        },
                        showInLegend: true
                    }
                },
                series: [
                    {
                        name: 'Brands',
                        colorByPoint: true,
                        data: channelData
                    }
                ]
            });
        };
        var loadTerminal=function(){

        }
        $(function(){
            loadPvUv();
            loadChannel();
        })

    </script>
</head>
<body>
<div class="container">
    <div class="page-header">
        <h1>日志统计结果 <small>总PV:${totalPv} 总UV:${totalUv} 总IP:${totalIp}</small></h1>
    </div>
    <div class="box">
        <div class="box-header">
            <h4>统计模板信息</h4>
        </div>
        <div class="box-body">
            <form id="updateForm" class="form-horizontal">

            </form>
        </div>
    </div>
    <div class="box">
        <div class="box-header">
            <ul class="tag-group">
                <li class="tag tag-default active" data-toggle="tab" data-target="#room1">PV UV IP</li>
                <li class="tag tag-default" data-toggle="tab" data-target="#room2">渠道统计</li>
                <li class="tag tag-default" data-toggle="tab" data-target="#room3">事件统计</li>
            </ul>
        </div>
        <div class="box-body tab-content">
            <div class="tab-pane fade active in" id="room1">
                <div id="pvuvContainer" style="min-width: 310px; height: 600px; margin: 0 auto"></div>
            </div>
            <div class="tab-pane fade" id="room2">
                <div id="channelContainer" style="min-width: 310px; height: 600px; margin: 0 auto"></div>
            </div>
            <div class="tab-pane fade" id="room3">
                <c:forEach items="${termsAggMap}" var="termsAggEntry">
                    <table id="${termsAggEntry}.key" class="table table-bordered">
                        <thead>
                        <tr>
                            <td>
                                <c:if test="${termsAggEntry.key=='uid'}">用户ID标识</c:if>
                                <c:if test="${termsAggEntry.key=='event'}">事件</c:if>
                                <c:if test="${termsAggEntry.key=='channel'}">渠道</c:if>
                                <c:if test="${termsAggEntry.key=='prefix_page'}">来源页</c:if>
                                <c:if test="${termsAggEntry.key=='current_page'}">停留页</c:if>
                                <c:if test="${termsAggEntry.key=='extra'}">关键字</c:if>
                                <c:if test="${termsAggEntry.key=='terminal'}">终端</c:if>
                                <c:if test="${termsAggEntry.key=='version'}">版本号</c:if>
                            </td>
                            <td>计数</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${termsAggEntry.value}" var="result">
                            <tr>
                                <td>${result.key}</td>
                                <td>${result.count}</td>
                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
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
        <div class="col-sm-2">
            <select  id="unit" name="unit" class="form-control">
                <option value="0" {{if unit=="0" }} selected='selected' {{/if}}>请选择</option>
                <option value="1" {{if unit=="1" }} selected='selected' {{/if}}>分钟</option>
                <option value="2" {{if unit=="2" }} selected='selected' {{/if}}>小时</option>
                <option value="3" {{if unit=="3" }} selected='selected' {{/if}}>天</option>
                <option value="4" {{if unit=="4" }} selected='selected' {{/if}}>月</option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-4">
            <input class="btn btn-block btn-greyPurple saveTemplate" type="button" value="保存" />
        </div>
    </div>
</script>
<script src="${ctx}/resources/js/art-template.js"></script>
<script src="${ctx}/resources/js/common.js"></script>
<script>
    var templateId = '${templateId}';
    var ROOT = '${ctx}';
    $(function(){
        getTemplate(templateId);
    })
    function getTemplate(id){
        if (id){
            $.get(ROOT + '/template/get?id='+id,function(data){
                console.debug(data);
                if (data.code == 0) {
                    var DATA = data.data;
                    var params = JSON.parse(DATA.params);

                    console.debug(params);
                    DATA.interval = params.interval || '';
                    DATA.termsCountField = params.termsCountField || '';
                    DATA.unit = params.unit || '';

                    $('#updateForm').html(template('templateTemp',DATA));

                    var arr = ['terminals','channels','currentPages','prefixPages','events'] ;

                    for (var n=arr.length-1 ; n>=0 ; n--) {
                        if (params[arr[n]]){
                            var checkboxs = form.find('[name="'+arr[n]+'"]');
                            for(var i=params[arr[n]].length-1 ; i>=0 ;i--){
                                checkboxs.filter('[value="'+params[arr[n]][i]+'"]').attr('checked','checked');
                            }
                        }
                    }

                }else {
                    $.alert(data.msg);
                }
            },'json')
        }else{
            $('#updateForm').html(template('templateTemp',{}));
        }
    }

</script>

</body>
</html>
