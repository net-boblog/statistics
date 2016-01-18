<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" params="text/html; charset=utf-8">
	<title>日志统计系统</title>
	<link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
	<link rel="stylesheet" href="${ctx}/resources/css/bootstrap-theme.min.css">
	<script type="text/javascript" src="${ctx}/resources/js/jquery/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.form.js"></script>
	<script src="${ctx}/resources/js/highcharts/highcharts.js"></script>
	<script src="${ctx}/resources/js/highcharts/modules/exporting.js"></script>
	<script type="text/javascript">
		function loadData(){
			$('#searchForm').ajaxSubmit(function(data){
				data= $.parseJSON(data);
				var pvs= $.parseJSON(data.pvs);
				var uvs=$.parseJSON(data.uvs);
				var ips=$.parseJSON(data.ips);
				var times=$.parseJSON(data.times);
				$('#container').highcharts({
					title: {
						text: '统计曲线',
						x: -20 //center
					},
					subtitle: {
						text: 'Source: xiaoluo.com',
						x: -20
					},
					xAxis: {
						categories: times
					},
					yAxis: {
						title: {
							text: ''
						},
						plotLines: [{
							value: 0,
							width: 1,
							color: '#808080'
						}]
					},
					tooltip: {
						valueSuffix: ''
					},
					legend: {
						layout: 'vertical',
						align: 'right',
						verticalAlign: 'middle',
						borderWidth: 0
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
				})
			});
		}
		function saveTemplate(){
			var params = {};
			var a = $('#searchForm').serializeArray();
			$.each(a, function() {
				if (params[this.name] !== undefined) {
					if (!params[this.name].push) {
						params[this.name] = [params[this.name]];
					}
					params[this.name].push(this.value || '');
				} else {
					params[this.name] = this.value || '';
				}
			});
			$.ajax({
				url:"${ctx}/template/update",
				data:params,
				type:post,
				error:function(e){
					alert(e);
				},
				success:function(result){

				}
			})
		}
	</script>
</head>
<body>
<%--private int id;--%>
<%--private int type;--%>
<%--private String name;--%>
<%--private int interval;--%>
<%--private int unit;--%>
<%--private String params;--%>
<div >
	<form id="searchForm" class="form-horizontal">
		<div class="form-group">
			<label  class="col-sm-2 control-label" for="name">模板名</label>
			<div class="col-sm-4">
				<input class="form-control" id="name" name="name" type="text" placeholder="搜索模板名" />
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label" for="terminals">终端</label>
			<div class="col-sm-4">
				<input class="form-control" id="terminals" name="terminals" type="text" placeholder="终端类型" />
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label" for="channels">渠道</label>
			<div class="col-sm-4">
				<input class="form-control" id="channels" name="channels" type="text" placeholder="请输入渠道,多个用逗号分隔" />
			</div>
		</div>
		<div class="form-group">
			<!-- Search input-->
			<label  class="col-sm-2 control-label" for="prefixPages">来源页</label>
			<div class="col-sm-4">
				<input class="form-control" id="prefixPages" name="prefixPages" type="text" placeholder="请输入来源页,多个用逗号分隔" />
			</div>
		</div>
		<div class="form-group">
			<!-- Search input-->
			<label  class="col-sm-2 control-label" for="currentPages">停留页</label>
			<div class="col-sm-4">
				<input class="form-control" id="currentPages" name="currentPages" type="text" placeholder="请输入停留页,多个用逗号分隔" />
			</div>
		</div>
		<div class="form-group">
		<!-- Search input-->
		<label  class="col-sm-2 control-label" for="events">事件</label>
		<div class="col-sm-4">
			<input class="form-control" id="events" name="events" type="text" placeholder="请输入事件,多个用逗号分隔" />
		</div>
	</div>
		<div class="form-group">
			<!-- Search input-->
			<label  class="col-sm-2 control-label" for="keyWords">关键字</label>
			<div class="col-sm-4">
				<input class="form-control" id="keyWords" name="keyWords" type="text" placeholder="" />
			</div>
		</div>
		<%--<div class="form-group">--%>
			<%--<label  class="col-sm-2 control-label" for="from">起止时间</label>--%>
			<%--<div class="col-sm-4">--%>
				<%--<div class="input-group">--%>
					<%--<input class="form-control" id="from" name="from" type="text" placeholder="yyyy-MM-dd HH:mm:ss" />--%>
					<%--<div class="input-group-btn">--%>
						<%--<label  class="col-sm-1 control-label" for="to" style="font-size: 14px;">到</label>--%>
					<%--</div>--%>
					<%--<input class="form-control" id="to" name="to" type="text" placeholder="yyyy-MM-dd HH:mm:ss"/>--%>
				<%--</div>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="form-group">
			<!-- Search input-->
			<label  class="col-sm-2 control-label" for="interval">间隔时间</label>
			<div class="col-sm-4">
				<input class="form-control" id="interval" name="interval" type="text" placeholder="" />
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label" for="unit">间隔时间单位</label>
			<div class="col-sm-4">
				<select  id="unit" name="unit" class="form-control">
					<option value="0">分钟</option>
					<option value="1">小时</option>
					<option value="2">天</option>
					<option value="3">月</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<!-- Search input-->
			<label  class="col-sm-2 control-label" for="minTermsCount">最少出现次数</label>
			<div class="col-sm-4">
				<input class="form-control" id="minTermsCount" name="minTermsCount" type="text" placeholder="" />
			</div>
		</div>
		<div class="form-group">
			<!-- Search input-->
			<label  class="col-sm-2 control-label" for="termsCountField">统计字段</label>
			<div class="col-sm-4">
				<input class="form-control" id="termsCountField" name="termsCountField" type="text" placeholder="" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-4">
				<input class="form-control" type="button" value="保存" onclick="saveTemplate()" />
			</div>
		</div>
	</form>
</div>
<div id="container" style="min-width: 310px; height: 600px; margin: 0 auto"></div>

</body>
</html>
