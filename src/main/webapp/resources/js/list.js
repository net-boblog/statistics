/**
 * Created by zoe on 2016/1/19.
 */
$(function(){

    XLstats.init();

    $(document.body).on('click','.addTemplate',function(e){
        $('#updateForm').html(template('templateTemp',{}));
        $('#templateModal').modal('show');
    });
    $(document.body).on('click','.editTemplate',function(e){
        var tar = e.currentTarget ;
        XLstats.editTemplate(tar.dataset.id);
    });
    $(document.body).on('click','.saveTemplate',function(e){
        var tar= $(e.currentTarget).parents('form');
        if (!tar.find('#name').val()){
            $.alert('请填写模板名','','#updateForm');
            return ;
        }
        XLstats.saveTemplate(tar[0]);
    });
    $(document.body).on('click','.saveTemplateAndSearch',function(e){
        var tar= $(e.currentTarget).parents('form');
        if (!tar.find('#name').val()){
            $.alert('请填写模板名','','#updateForm');
            return ;
        }
        XLstats.saveTemplate(tar[0],true);
    });
    $(document.body).on('click','.addDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        XLstats.saveDict(tar);
    });
    $(document.body).on('click','.editDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        XLstats.editDict(tar);
    });
    $(document.body).on('click','.searchDictBtn',function(e){
        var form= $(e.currentTarget).parents('form');
        var data = form.serialize();
        XLstats.getDictList(data);
    });
    $(document.body).on('click','.addExtra',function(e){

        var obj= $(e.currentTarget).parents('.form-group');
        if (!obj.find('input').first().val()) {
            $.alert('请填写附加字段描述','','#updateForm');
            return ;
        }
        XLstats.addExtra(obj);
    });
    $(document.body).on('click','.delExtra',function(e){
        $(e.currentTarget).parents('.form-group').remove();
    });
    $(document.body).on('click','.showCharts',function(e){
        var id = e.currentTarget.dataset.id;
        XLstats.showTemplateStat(id);
    });
})

var XLstats = {
    init:function(){

        this.getDictList();//更新字典列表
        this.checkHash();
        var tempId = $('#resultTable').find('a[data-id]').first().data('id');
        this.showTemplateStat(tempId);//默认显示第一个模板的统计结果

    },
    showTemplateStat : function(tempId) {
        $.sajax({
            url : '/report/searchByTemplate',
            data:{ templateId : tempId},
            success : function(data) {
                console.debug(data);
                XLstats.editTemplate(tempId,true);
                XLstats.showTotal(data.data.totalStatResult);
                XLstats.showColumnChart(data.data.sectionStatResults);
                XLstats.showTermsResult(data.data.termsResultsMap);
            }
        })
    },
    showTermsResult:function(data){
        var _self = this;
        //来源页 to 停留页 漏斗图
        //var pagesContainer = $('<div class="col-sm-4 pie"></div>').appendTo('#pieContainer');
        //_self.showPieChart(pagesContainer,'页面来源','pages',_self.toPercent(data.prefix_page_terms_agg));

        //终端
        var terminalContainer = $('<div class="col-sm-4 pie"></div>').appendTo('#pieContainer');
        _self.showPieChart(terminalContainer,'终端','terminal',_self.toPercent(data.terminal_terms_agg));
        //渠道
        var channelContainer = $('<div class="col-sm-4 pie"></div>').appendTo('#pieContainer');
        _self.showPieChart(channelContainer,'渠道','pages',_self.toPercent(data.channel_terms_agg));
        //事件 列表
        //附加字段

    },
    toPercent: function (data){
        var sum = 0;
        var arr =[];
        for (var i = data.length - 1; i >= 0; i--) {
            var item ={};
            sum += data[i].count;
            item.name = data[i].value;
            item.y = data[i].count;
            arr.push(item);
        };
        for (var i = arr.length - 1; i >= 0; i--) {
            arr[i].y = arr[i].y/sum;
        };
        return arr;
    },
    showTotal: function (data){
        $('#totalIp').html(data.ip);
        $('#totalPv').html(data.pv);
        $('#totalUv').html(data.uv);
        $('#statsTime').html(data.from +' - ' + data.to);
    },
    editDict: function ($tr){
        var selecttemp = template('selectTemp',{dictType:$tr.data('type')});
        var inputtemp = template('inputTemp',{dictDesc:$tr.data('description')});
        $tr.find('td').eq(1).html(selecttemp)
            .next().html(inputtemp)
            .next().find('button').removeClass('btn-greyPurple editDict').addClass('btn-primary addDict').html('保存');
    },
     saveDict: function ($tr) {
            var data = {};
            data.type = $tr.find('.dictType').val();
            data.description = $tr.find('.dictDesc').val();
            if ($tr.data('id')){
                data.id = $tr.data('id');
            }

            if ( !data.type || !data.description ){
                $.alert('请选择字段类型并填写字段描述');
            }else{
                $.post(ROOT + '/dict/update',data,function(data){
                    console.debug(data);
                    if (data.code == 0) {
                        getDictList();
                    }
                },'json');
            }
        },
    getDictList: function (data){
            var data = data || {};
            $.post(ROOT + '/dict/list',data,function(data){
                if (data.code == 0) {
                    $('#dictList').html(template('dictListTemp',{list:data.data}));
                }else {
                    $.alert(data.msg);
                }
            },'json')
        },

    editTemplate: function (id,isStat){
            $.get(ROOT + '/template/get?id='+id,function(data){
                console.debug(data);
                if (data.code == 0) {
                    var DATA = data.data;
                    var params = JSON.parse(DATA.params);

                    console.debug(params);
                    DATA.interval = params.interval || '';
                    DATA.termsCountField = params.termsCountField || '';
                    DATA.unit = params.unit || '';
                    DATA.extra = params.extra || [];

                    if (isStat) {//统计视图和编辑视图
                        var form = $('#searchForm');
                        DATA.stats = true;
                    }else{
                        var form = $('#updateForm');
                    }
                    form.html(template('templateTemp',DATA));

                    var arr = ['terminals','channels','currentPages','prefixPages','events'] ;

                    for (var n=arr.length-1 ; n>=0 ; n--) {
                        if (params[arr[n]]){
                            var checkboxs = form.find('[name="'+arr[n]+'"]');
                            for(var i=params[arr[n]].length-1 ; i>=0 ;i--){
                                checkboxs.filter('[value="'+params[arr[n]][i]+'"]').attr('checked','checked');
                            }
                        }
                    }
                    if (!isStat) {
                        $('#templateModal').modal('show');
                    }

                }else {
                    $.alert(data.msg);
                }
            },'json')
        },

    saveTemplate: function (obj,isStat){//obj was a form

            var data = {};
            var	items = obj.elements;

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

            data['extra']={};
            $(obj).find('.extra').each(function(index,el){
                var name = $(el).find('.extraName').val();
                var value = $(el).find('.extraKey').val();
                if (name){
                    data.extra[name] = value;
                }
            });

            $.ajax({
                url:ROOT + "/template/update",
                data:"data="+JSON.stringify(data),
                type:'POST',
                error:function(e){
                    $.alert(e);
                },
                success:function(){
                    if (isStat){
                        //更新统计视图
                    }else{
                        $('#templateModal').modal('hide');
                    }
                    $.alert('保存成功!','primary');
                }
            })
        },

    checkHash: function () {
            switch (location.hash.substr(1)){
                case 'updatedFrom':
                    $.alert('模板保存成功!','primary');
                    break;
                default :
                    break;
            }
            location.hash = '';
        },

    addExtra: function ($obj){//$obj is a div.form-group
            $obj.clone()
                .insertAfter($obj)
                .find('label').html('')
                .siblings().last().find('.addExtra')
                .removeClass('addExtra').addClass('delExtra')
                .html('删除');
            $obj.find('input').val('');
        },
    showPieChart: function(container,title,seriesName,seriesData){
        $(container).highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: title
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    }
                }
            },
            series: [{
                name: seriesName,
                colorByPoint: true,
                data: seriesData
            }]
        });
    },

    showColumnChart: function (data){
            var TIME = [] , IP = [] , PV = [], UV = [];
            for (var i = data.length - 1; i >= 0; i--) {
                TIME[i] = data[i].from + data[i].to;
                IP[i] = data[i].ip;
                PV[i] = data[i].pv;
                UV[i] = data[i].uv;
            };

            $('#columnContainer').highcharts({
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
                    categories: TIME,
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
                    data: PV
                }, {
                    name: 'UV',
                    data: UV
                }, {
                    name: 'IP',
                    data: IP
                }]
            });
        },
}
