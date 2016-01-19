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
        $(function(){
            var pvs=$.parseJSON('${pvs}');
            var uvs= $.parseJSON('${uvs}');
            var ips= $.parseJSON('${ips}');
            var times= $.parseJSON('${times}');
            $('#container').highcharts({
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
        })

    </script>
</head>
<body>
<div id="container" style="min-width: 310px; height: 600px; margin: 0 auto"></div>
</body>
</html>