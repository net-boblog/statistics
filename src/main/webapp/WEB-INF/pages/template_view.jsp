<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" params="text/html; charset=utf-8">
	<title>日志统计系统</title>
	<link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
	<script type="text/javascript" src="${ctx}/resources/js/jquery/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.form.js"></script>
	<script src="${ctx}/resources/js/bootstrap/bootstrap.min.js"></script>
	<script src="${ctx}/resources/js/highcharts/highcharts.js"></script>
	<script src="${ctx}/resources/js/highcharts/modules/exporting.js"></script>

</head>
<body>
<div >
	<form id="searchForm" class="form-horizontal">
		<div class="form-group" style="display: none">
			<label  class="col-sm-2 control-label" for="id">ID</label>
			<div class="col-sm-4">
				<input class="form-control" id="id" name="id" type="text" value="0" />
			</div>
		</div>
		<div class="form-group" style="display: none">
			<label  class="col-sm-2 control-label" for="type">类型</label>
			<div class="col-sm-4">
				<input class="form-control" id="type" name="type" type="text" value="0" />
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label" for="name">模板名</label>
			<div class="col-sm-4">
				<input class="form-control" id="name" name="name" type="text" placeholder="搜索模板名" />
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label">终端</label>
			<div class="col-sm-8">
				<c:forEach items="${terminals}" var="terminal" varStatus="status">
					<span>
						<input type="checkbox" id="terminals${status.index}" name="terminals" value="${terminal.key}">
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
						<input type="checkbox" id="channels${status.index}" name="channels" value="${channel.key}">
						<label for="channels${status.index}">${channel.description}</label>
					</span>
				</c:forEach>
			</div>
		</div>

		<div class="form-group">
			<!-- Search input-->
			<label  class="col-sm-2 control-label">来源页</label>
			<div class="col-sm-8">
				<c:forEach items="${pages}" var="page" varStatus="status">
					<span>
						<input type="checkbox" id="prefixPages${status.index}" name="prefixPages" value="${page.key}">
						<label for="prefixPages${status.index}">${page.description}</label>
					</span>
				</c:forEach>
			</div>

		</div>
		<div class="form-group">
			<!-- Search input-->
			<label  class="col-sm-2 control-label">停留页</label>
			<div class="col-sm-8">
				<c:forEach items="${pages}" var="page" varStatus="status">
					<span>
						<input type="checkbox" id="currentPages${status.index}" name="currentPages" value="${page.key}">
						<label for="currentPages${status.index}">${page.description}</label>
					</span>
				</c:forEach>
			</div>
		</div>
		<div class="form-group">
		<!-- Search input-->
		<label  class="col-sm-2 control-label">事件</label>
			<div class="col-sm-8">
				<c:forEach items="${events}" var="event" varStatus="status">
					<span>
						<input type="checkbox" id="events${status.index}" name="events" value="${event.key}">
						<label for="events${status.index}">${event.description}</label>
					</span>
				</c:forEach>
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
				<input class="form-control" id="interval" name="interval" type="text" placeholder="" value="1"/>
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label" for="unit">间隔时间单位</label>
			<div class="col-sm-4">
				<select  id="unit" name="unit" class="form-control">
					<option value="1">分钟</option>
					<option value="2">小时</option>
					<option value="3">天</option>
					<option value="4">月</option>
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
				<select  id="termsCountField" name="termsCountField" class="form-control">
					<option value="uid">用户ID</option>
					<option value="channel">渠道</option>
					<option value="terminal">终端</option>
					<option value="prefix_page">来源页</option>
					<option value="current_page">当前页</option>
					<option value="event">事件</option>
					<option value="key_word">关键字</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-4">
				<input class="form-control" type="button" value="保存" onclick="saveTemplate()" />
			</div>
		</div>
	</form>
</div>

<script type="text/javascript">
	function saveTemplate(){
		var data = {};
		var	items = $('#searchForm')[0].elements;

		for ( var i=items.length-1 ; i>=0 ; i--) {
			var item = items[i];
			var value = item.value, name = item.name;
			if (!name || !value) {
				continue ;
			}
			if (data[name] !== undefined) {
				if (!data[name].push) {//如果不是数组则转化成数组
					data[name] = [data[name]];
				}
				data[name].push(value || '');
			} else if (item.type.toLowerCase() == 'checkbox' ) {
				if ( item.checked ){

					data[name] = [value] || [];
				}
			} else if (item.type.toLowerCase() == 'radiobox' ) {
				if ( item.checked ) {

					data[name] = value || '';
				}
			} else {
				data[name] = value || '';
			}
		}
		console.debug(data);
		$.ajax({
			url:"${ctx}/template/update",
			data:"data="+JSON.stringify(data),
			type:'POST',
			error:function(e){
				alert(e);
			},
			success:function(result){
				alert("OK");
			}
		})
	}
</script>
</body>
</html>
