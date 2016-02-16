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
            $.alert('请填写模板名','',tar);
            return ;
        }
        XLstats.saveTemplate(tar[0]);
    });
    $(document.body).on('click','.delTemplate',function(e){
        var id = e.currentTarget.dataset.id;
        var $tr = $(e.currentTarget).parents('tr');

        XLstats.delTemplate(id,$tr);
    });
    $(document.body).on('click','.saveTemplateAndSearch',function(e){
        var tar= $(e.currentTarget).parents('form');
        if (!tar.find('#name').val()){
            $.alert('请填写模板名','',tar);
            return ;
        }
        XLstats.saveTemplate(tar[0],true);
        XLstats.showTemplateStat(e.currentTarget.dataset.id);
    });
    $(document.body).on('click','.addDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        XLstats.addDict(tar);
    });
    $(document.body).on('click','.saveDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        XLstats.saveDict(tar);
    });
    $(document.body).on('click','.editDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        XLstats.editDict(tar);
    });
    $(document.body).on('click','.delDict',function(e){
        var id= e.currentTarget.dataset.id;
        XLstats.delDict(id);
    });
    $(document.body).on('click','.searchDictBtn',function(e){
        var form= $(e.currentTarget).parents('form');
        var data = form.serialize();
        XLstats.getDictList(data);
    });
    $(document.body).on('click','.addExtra',function(e){

        var obj= $(e.currentTarget).parents('.form-group');
        var alertContainer = obj.parents('form');
        if (!obj.find('input').first().val()) {
            $.alert('请填写附加字段描述','',alertContainer);
            return ;
        }
        XLstats.addExtra(obj);
    });
    $(document.body).on('click','.delExtra',function(e){
        $(e.currentTarget).parents('.form-group').remove();
    });
    $(document.body).on('click','.showCharts',function(e){
        var tar = e.currentTarget;
        XLstats.showTemplateStat(tar.dataset.id,tar.dataset.name);
        $('body').animate({scrollTop: $('#statResultBox').offset().top - 20}, 500);
    });
    $(document.body).on('click','.slideToggleBtn',function(e){
        var $tar = $(e.currentTarget);
        if ($tar.hasClass('active')){
            $tar.removeClass('active').html('收起');
            $tar.parent().siblings().show();
            $tar.siblings('span').html('');
        }else{
            $tar.addClass('active').html('展开');
            $tar.parent().siblings().hide();
            var str = '筛选条件概览：' + decodeURI($tar.parents('.box').find('form').serialize().replace(/&/g,' || '));
            $tar.siblings('span').html(str);
        }
    });
    $("[data-inputmask]").inputmask();
    $('#searchBytime').click(function(e){
        var from = $('#statStartTime').val();
        var to = $('#statEndTime').val();
        XLstats.showTemplateStat(e.target.dataset.id,false,from,to);
    })
    $('#funnelForm').submit(function(e){
        e.preventDefault();
        var input = e.currentTarget.elements;
        //console.debug(input);
        var ids = input[0].value ;
        var from = input[1].value ;
        var to = input[2].value ;
        XLstats.showFunnelSearch(ids,from,to);
    });
    $('#tooltip').tooltip({placement:"right",html:true,title:$('#tooltipTemp').html()});
    $('#changeTemplate').tooltip({placement:"bottom",html:true,title:$('#changeTempTemp').html()});
    $('[data-tooltip]').tooltip();
    $(document.body).on('click','.funnelAddID',function(e){
        var id = e.currentTarget.dataset.id;
        var tar = $('#funnelIds');
        var str = tar.val();
        if (str){
            str += ',' + id;
        }else{
            str = id;
        }
        tar.val(str);
    })

    $(document.body).on('input','.autoFixInput',function(e){
        var tar = e.currentTarget,
            type = tar.dataset.type,
            name = tar.dataset.name,
            val = tar.value,
            $box = $(tar).siblings('.autoFixList').children();
        $box.html('');


        if (!val) {
            return;
        }
        var r = new RegExp(val , 'i');
        var searchList = window[type];

        for (var i = searchList.length-1 ; i>=0 ; i-- ) {

            if (searchList[i].search(r)>-1) {
                var arr = searchList[i].split('&&');// desc&&id

                var a = $('<li><a href="javascript:;" data-id="'+ arr[1] +'">'+ arr[0] +'</a></li>');

                a[0].onclick = function(e){
                    var data = [{id: e.target.dataset.id ,description: e.target.textContent}];
                    $(tar.parentElement.previousElementSibling).append(template('checkboxTemp',{name:name,list:data,timestamp:(new Date()).valueOf()}));
                }
                $box.append(a);
            }

            if ( $box.html().length == 0){
                $box.html('未能检索到符合关键字的条件');
            }
        }
    })

})

var XLstats = {
    init:function(){

        this.getDictList();//更新字典列表
        this.checkHash();
        var tar = $('#resultTable').find('a[data-id]').eq(1);
        var tempId = tar.data('id');
        var tempName = tar.data('name');
        this.showTemplateStat(tempId,tempName);//默认显示第一个模板的统计结果
        //this.showFunnelSearch('80,81');
        window.PAGES_OBJ = this.arr2obj(window.PAGES);
        window.EVENTS_OBJ = this.arr2obj(window.EVENTS);

    },
    arr2obj: function(arr) {
        var objMap = {};
      for(var i=arr.length; i>0 ; i--){
          var description = arr[i-1].split('&&')[0];
          var id = arr[i-1].split('&&')[1];
          objMap[id] ={
              id : id,
              description : description
          }
      }
        return objMap;

    },
    showTemplateStat : function(tempId,tempName,from,to) {
        var _self = this;
        if (from || to){
            var postdata = {templateId : tempId, from : from , to : to}
        }else{
            var postdata = { templateId : tempId}
        }
        var $fresh = $('#statsContainer .fresh').show();//刷新图标
        $.sajax({
            url : ROOT + '/report/searchByTemplate',
            data:postdata,
            success : function(data) {
                // console.debug(data);
                _self.editTemplate(tempId,true);
                _self.showColumnChart(data.data.sectionStatResults);
                _self.showTermsResult(data.data.termsResultsMap);
                $fresh.hide();
                if (tempName){
                    $('#statTempName').html(tempName);
                }
                document.querySelector('#searchBytime').dataset.id = tempId ;
                $('#totalIp').html(data.data.totalStatResult.ip);
                $('#totalPv').html(data.data.totalStatResult.pv);
                $('#totalUv').html(data.data.totalStatResult.uv);
                $('#statStartTime').val(data.data.totalStatResult.from);
                $('#statEndTime').val(data.data.totalStatResult.to);
            }
        })
    },
    delTemplate:function(id , $tr){
        $.sajax({
            type:'POST',
            url: ROOT + '/template/del',
            data:{id:id},
            success:function(data){
                $.alert('已删除模板！','primary');
                $tr.remove();
            }
        })
    },
    showFunnelSearch:function(ids,from,to){
        var _self = this;
        if (from || to){
            var postdata = {templateIds : ids, from : from , to : to}
        }else{
            var postdata = { templateIds : ids}
        }
        var $container = $('#funnelContainer');
        $container.siblings('.fresh').show();
        $.sajax({
            url :ROOT + '/report/funnelSearch',
            data:postdata,
            success : function(data) {
                 //console.debug(data);
                var funnelData = [];
                for (key in data.data){
                    funnelData.push([key,data.data[key]]);
                }
                //console.debug(funnelData);
                $container.highcharts({
                    chart: {type: 'funnel'},
                    title: {
                        text: '转化率',
                        x: -50
                    },
                    plotOptions: {
                        series: {
                            dataLabels: {
                                enabled: true,
                                format: '<b>{point.name}</b> ({point.y:,.0f})',
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                                softConnector: true
                            },
                            neckWidth: '30%',
                            neckHeight: '25%',
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    series: [{
                        name: '模板',
                        data: funnelData
                    }]
                });
             $container.siblings('.fresh').hide();
            }
        })
    },
    showItemList: function(data,title){
        if (!data || data.length==0 ){
            return;
        }
        $('#itemsContainer').append(template('statItemListTemp',{list:data,title:title || '字段统计'}));
    },
    showTermsResult:function(data){

        var _self = this;
        var $container = $('#pieContainer');
            $container.html('');

        //来源页
        data.prefix_page_terms_agg && data.prefix_page_terms_agg.length>0 ? _self.showPieChart($container,'来源页','pages',_self.toPercent(data.prefix_page_terms_agg)) : '';
        //停留页
        data.current_page_terms_agg && data.current_page_terms_agg.length>0 ? _self.showPieChart($container,'停留页','pages',_self.toPercent(data.current_page_terms_agg)) : '';
         //终端
        data.terminal_terms_agg && data.terminal_terms_agg.length>0 ? _self.showPieChart($container,'终端','terminal',_self.toPercent(data.terminal_terms_agg)) : '';
         //渠道
        data.channel_terms_agg && data.channel_terms_agg.length>0 ? _self.showPieChart($container,'渠道','pages',_self.toPercent(data.channel_terms_agg)) : '';

        //事件 用户ID列表 附加字段
        $('#itemsContainer').html('');
        _self.showItemList(data.uid_terms_agg,'用户统计');
        _self.showItemList(data.event_terms_agg,'事件统计');
        _self.showItemList(data.extra_terms_agg,'附加字段');

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
            arr[i].y = arr[i].y;///sum;
        };
        return arr;
    },
    editDict: function ($tr){
        //var selecttemp = template('selectTemp',{dictType:$tr.data('type')});//修改 -- 编辑时type不可变
        var inputtemp = template('inputTemp',{dictDesc:$tr.data('description')});
        $tr.find('td')/*.eq(1).html(selecttemp)*/
            .eq(2).html(inputtemp)
            .next().find('.editDict').removeClass('text-greyPurple editDict').addClass('text-primary saveDict').html('保存');
    },
    delDict: function (id){
        $.sajax({
            type:'POST',
            url: ROOT + '/dict/del',
            data:{id:id},
            success:function(data){
                $.alert('已删除字典！','primary');
                XLstats.getDictList();
            }
        })
    },
     addDict: function ($tr) {
            var data = {};
            data.type = $tr.find('.dictType').val();
            data.description = $tr.find('.dictDesc').val();
            data.id = $tr.find('.dictID').val();


            if ( !data.id || !data.type || !data.description ){
                $.alert('请 填写ID！！选择字段类型！！并填写字段描述！！');
            }else{

                $.sajax({
                    type:'POST',
                    url: ROOT + '/dict/insert',
                    data:data,
                    success:function(data){
                        $.alert('已添加新字典！','primary');
                        XLstats.getDictList();
                    }
                })
            }
        },
    saveDict: function ($tr) {
            var data = {};
            //data.type = $tr.find('.dictType').val();
            data.description = $tr.find('.dictDesc').val();
            data.id = $tr.data('id');

            if ( !data.description ){
                $.alert('请填写字段描述');
            }else{
                $.sajax({
                    type:'POST',
                    url: ROOT + '/dict/update',
                    data:data,
                    success:function(){
                        $.alert('字典已更新','primary');
                        $tr.find('.dictDesc').remove()
                            .end()
                            .find('.center-block')
                            .html(data.description)
                            .end()
                            .find('.saveDict')
                            .removeClass('text-primary')
                            .addClass('text-greyPurple')
                            .html('编辑');

                        //XLstats.getDictList();
                    }
                })
            }
        },
    getDictList: function (data){
            var data = data || {};
            $('#room2 .fresh').show();
            $.sajax({
                type:'POST',
                url : ROOT + '/dict/list',
                data: data,
                success: function (data) {
                    $('#dictList').html(template('dictListTemp',{list:data.data}));
                    $('#room2 .fresh').hide();
                }
            })

        },

    editTemplate: function (id,isStat){//编辑模板 isStat==true 为展示当前统计结果的模板

            $.get(ROOT + '/template/get?id='+id,function(data){
                if (data.code == 0) {
                    var DATA = data.data;
                    var params = JSON.parse(DATA.params);

                    DATA.interval = params.interval || '';
                    DATA.termsCountField = params.termsCountField || '';
                    DATA.unit = params.unit || '';

                    if ( params.extra ) {
                        var extra = params.extra ;
                        var _arr = [];
                        for (key in extra ){
                            var obj = {};
                            obj.name = key ;
                            obj.value = extra[key] ;
                            _arr.push(obj);
                        }
                        DATA.extra = _arr;
                    }else{
                        DATA.extra = [];
                    }

                    if (isStat) {//统计视图和编辑视图
                        var form = $('#searchForm');
                        DATA.stats = true;
                    }else{
                        var form = $('#updateForm');
                    }
                    form.html(template('templateTemp',DATA));

                    var arr = ['terminals','channels','currentPages','prefixPages','events'] ;

                    for (var n=arr.length-1 ; n>=0 ; n--) {
                        if (params[arr[n]]){//params[arr[n]] 是一个id 的数组

                            var idList = params[arr[n]];

                            if ( n==0 || n==1) {
                                var checkboxs = form.find('[name="'+arr[n]+'"]');
                                for(var i=idList.length-1 ; i>=0 ;i--){
                                    checkboxs.filter('[value="'+idList[i]+'"]').attr('checked','checked');
                                }
                            }else{
                                var data = [];
                                for(var i=idList.length-1 ; i>=0 ;i--){
                                    window.PAGES_OBJ[idList[i]] ? data.push(window.PAGES_OBJ[idList[i]]) : '';
                                }
                                var html = template('checkboxTemp',{name:arr[n],list:data,timestamp:(new Date()).valueOf()});
                                form.find('#temp' + arr[n]).html(html);
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

                if (data[name] !== undefined) {//值第二次出现
                    if ( item.type.toLowerCase() == 'checkbox'){
                        if ( !item.checked ){
                            continue;
                        }
                    }
                    if (!data[name].push) {//如果不是数组则转化成数组
                        data[name] = [data[name]];
                    }
                    data[name].push(value || '');
                } else {
                    if (item.type.toLowerCase() == 'checkbox'){
                        if ( item.checked ){
                            data[name] = [value] || [];
                        }
                    }else{
                        data[name] = value || '';
                    }
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

            $.sajax({
                url:ROOT + "/template/update",
                data:"data="+JSON.stringify(data),
                type:'POST',
                error:function(e){
                    $.alert(e);
                },
                success:function(data){
                    if (isStat){
                        //更新统计视图
                        XLstats.showTemplateStat(data.data.id,data.data.name);
                    }else{
                        $('#templateModal').modal('hide');
                    }
                    //更新模板列表
                    var $tar = $('#resultTable tr[data-id="' + data.data.id + '"]');
                    if ($tar.length == 0){
                        $tar = $(template('insertTemplateTemp',{id:data.data.id,name:data.data.name})).appendTo('#resultTable tbody');
                    }else{
                        $tar.find('td')
                            .eq(1)
                            .html(data.data.name)
                            .end()
                            .find('[data-name]')
                            .data('name',data.data.name);
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
    showPieChart: function(pieContainer,title,seriesName,seriesData){
        var container = $('<div class="col-sm-6 pie"></div>').appendTo(pieContainer);
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
                pointFormat: '占比: <b>{point.percentage:.1f}%</b><br>总数:<b>{point.y:.0f}</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %<br>总数:<b>{point.y:.0f}</b>',
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

                TIME[i] = data[i].from.substr(5,5) + '至'+ data[i].to.substr(5,5);
                IP[i] = data[i].ip;
                PV[i] = data[i].pv;
                UV[i] = data[i].uv;
            };

            $('#columnContainer').highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: '概览'
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
