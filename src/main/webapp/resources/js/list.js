/**
 * Created by zoe on 2016/1/19.
 */
$(function(){

    XLstats.init();

/*-----------------------------------------------------------
 *   模板相关操作
 *-----------------------------------------------------------
 */
    //弹窗 新增模板
    $(document.body).on('click','.addTemplate',function(e){

        $('#updateForm').html(template('templateTemp',{}));
        $('#templateModal').modal('show');
    });

    //弹窗 编辑模板
    $(document.body).on('click','.editTemplate',function(e){
        var tar = e.currentTarget ;
        XLstats.editTemplate(tar.dataset.id);
    });

    //保存模板
    $(document.body).on('click','.saveTemplate',function(e){
        var tar= $(e.currentTarget).parents('form');
        if (!tar.find('#name').val()){
            $.alert('请填写模板名','',tar);
            return ;
        }
        if (e.currentTarget.dataset.saveas == 1){
            tar.find('#id').val('');//将id设置为空 保存为新模板
        }
        XLstats.saveTemplate(tar[0]);
    });

    //删除模板
    $(document.body).on('click','.delTemplate',function(e){
        var id = e.currentTarget.dataset.id;
        var $tr = $(e.currentTarget).parents('tr');
        XLstats.delTemplate(id,$tr);
    });

    //保存并查看统计
    $(document.body).on('click','.saveTemplateAndSearch',function(e){
        var tar= $(e.currentTarget).parents('form');
        if (!tar.find('#name').val()){
            $.alert('请填写模板名','',tar);
            return ;
        }
        XLstats.saveTemplate(tar[0],true);
    });

    //添加附加字段
    $(document.body).on('click','.addExtra',function(e){
        var obj= $(e.currentTarget).parents('.form-group');
        var alertContainer = obj.parents('form');
        if (!obj.find('input').first().val()) {
            $.alert('请填写附加字段描述','',alertContainer);
            return ;
        }
        XLstats.addExtra(obj);
    });

    //删除附加字段
    $(document.body).on('click','.delExtra',function(e){
        $(e.currentTarget).parents('.form-group').remove();
    });
    //收起 展开模板设置
    $(document.body).on('click','.slideToggleBtn',function(e){
        var $tar = $(e.currentTarget);
        if ($tar.hasClass('active')){
            $tar.removeClass('active').html('收起');
            $tar.siblings('form').show();
            $tar.siblings('span').html('');
        }else{
            $tar.addClass('active').html('展开');
            $tar.siblings('form').hide();
            var str = '筛选条件概览：' + decodeURI($tar.siblings('form').serialize().replace(/&/g,' || '));
            $tar.siblings('span').html(str);
        }
    });

 /*-----------------------------------------------------------
  *   字典相关操作
  *-----------------------------------------------------------
  */
    //新增字典
    $(document.body).on('click','.addDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        XLstats.addDict(tar);
    });
    //保存字典修改
    $(document.body).on('click','.saveDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        XLstats.saveDict(tar);
    });
    //编辑字典
    $(document.body).on('click','.editDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        XLstats.editDict(tar);
    });
    //删除字典
    $(document.body).on('click','.delDict',function(e){
        var id= e.currentTarget.dataset.id;
        XLstats.delDict(id,$(e.currentTarget).parents('tr'));
    });
    //查询字典
    $('#searchDict').submit(function(e){
        e.preventDefault();
        e.stopPropagation();
        var form= $(e.currentTarget);
        var data = form.serialize();
        XLstats.getDictList(data);
    });
    //筛选字典
    $('#searchDict button[data-val]').click(function(e){
        var $tar = $(e.currentTarget);
        if ($tar.hasClass('btn-primary')){//选中时取消
            $tar.removeClass('btn-primary').addClass('btn-default')
                .siblings('input').val('')
                .parents('form')
                .submit();
        }else{
            $tar.removeClass('btn-default').addClass('btn-primary')
                .siblings('.btn-primary').removeClass('btn-primary').addClass('btn-default')
                .end()
                .siblings('input').val(e.currentTarget.dataset.val)
                .parents('form')
                .submit();
        }
    });

    /*-----------------------------------------------------------
     *   统计相关操作
     *-----------------------------------------------------------
     */
    //快速切换模板
    $(document.body).on('click','.showCharts',function(e){
        var tar = e.currentTarget;
        XLstats.showTemplateStat(tar.dataset.id,tar.dataset.name);
        $('body').animate({scrollTop: $('#statResultBox').offset().top - 20}, 500);
    });
    $(document.body).on('click','.SearchByParams',function(e){
        var tar = e.currentTarget;
        var data = XLstats.transformTempData($(tar).parents('form')[0]);
        XLstats.searchByParams(data);
    });

    //漏斗图
    $('#funnelForm').submit(function(e){
        e.preventDefault();
        var input = e.currentTarget.elements;
        XLstats.showFunnelSearch(input[0].value,input[1].value,input[2].value);
    });
    //添加模板到漏斗
    $('#tooltip').tooltip({placement:"right",html:true,title:$('#tooltipTemp').html()});
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
        if (!val) {return;}
        var r = new RegExp(val , 'i');
        var searchList = window[type];
        for (var i = searchList.length-1 ; i>=0 ; i-- ) {

            if (searchList[i].search(r)>-1) {
                var arr = searchList[i].split('&&');
                var a = $('<li><a href="javascript:;" data-id="'+ arr[1] +'">'+ arr[0] +'</a></li>');
                a[0].onclick = function(e){
                    var data = [{id: e.target.dataset.id ,description: e.target.textContent}];
                    $(tar.parentElement.previousElementSibling).append(template('checkboxTemp',{name:name,list:data,timestamp:(new Date()).valueOf()}));
                }
                $box.append(a);
            }
        }
        if ( $box.html().length == 0){
            $box.html('未能检索到符合关键字的条件');
        }
    })

    //全局设置按钮
    $('#toggleSetting').click(function(){
        $('#settingBox').toggle();
    })
    $('[data-tooltip]').tooltip();
    $("[data-inputmask]").inputmask();

})

var XLstats = {
    init:function(){

        this.freshTempList();
        this.getDictList();//更新字典列表
        this.showTemplateStat(window.TEMPLIST[0].id,window.TEMPLIST[0].name);//默认显示第一个模板的统计结果
        //this.showFunnelSearch('80,81');
        window.PAGES_OBJ = this.dictList2Map(window.PAGES);
        window.EVENTS_OBJ = this.dictList2Map(window.EVENTS);
        this.checkHash();

    },
    freshTempList:function(){
        $('#changeTemplate').tooltip('destroy')
            .tooltip({placement:"bottom",html:true,title:template('changeTempTemp',{ list:window.TEMPLIST })});
        $('#tempListTable').html(template('tempListTableTemp',{ list:window.TEMPLIST }));
    },
    dictList2Map: function(arr) {//把字典列表转化成map
        var objMap = {} , n = arr.length;
      for(var i=0; i<n ; i++){
          if (arr[i] != undefined) {
              var description = arr[i].split('&&')[0];
              var id = arr[i].split('&&')[1];
              objMap[id] ={
                  id : id,
                  description : description
              }
          }
      }
        return objMap;

    },
    dictMap2List: function(obj){//把字典map转换为list 给自动补全使用
        var arr = [];
        for(key in obj){
            if (obj[key] != undefined) {
                arr.push(obj[key].description + '&&' + obj[key].id);
            }
        }
        return arr;
    },
    showTemplateStat : function(tempId,tempName,from,to) {
        var _self = this;
        if (from || to){
            var postdata = {templateId : tempId, from : from , to : to}
        }else{
            var postdata = { templateId : tempId}
        }
        //保存查询条件
        window.TEMPDATA = XLstats.transformTempData(document.querySelector('#searchForm'));
        var $fresh = $('#statsContainer .fresh').show();//刷新图标
        $.sajax({
            url : ROOT + '/report/searchByTemplate',
            data:postdata,
            success : function(data) {
                window.TEMPDATA.from = data.data.totalStatResult.from;
                window.TEMPDATA.to = data.data.totalStatResult.to;
                _self.editTemplate(tempId,true);
                _self.showLineChart(data.data.sectionStatResults);
                _self.showTermsResult(data.data.termsResultsMap);
                $fresh.hide();
                if (tempName){
                    $('#statTempName').html(tempName);
                }
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
                var arr=[],n=window.TEMPLIST.length;
                for (var i = 0 ; i<n ;i++){
                    if (window.TEMPLIST[i].id != id){
                        arr.push(window.TEMPLIST[i]);
                    }
                }
                window.TEMPLIST = arr ;
                XLstats.freshTempList();
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

        //停留页
        data.current_page_terms_agg && data.current_page_terms_agg.length>0 ? _self.showPieChart($container,'停留页','current_page',_self.toPercent(data.current_page_terms_agg)) : '';
        //来源页
        data.prefix_page_terms_agg && data.prefix_page_terms_agg.length>0 ? _self.showPieChart($container,'来源页','prefix_page',_self.toPercent(data.prefix_page_terms_agg)) : '';
        //终端
        data.terminal_terms_agg && data.terminal_terms_agg.length>0 ? _self.showPieChart($container,'终端','terminal',_self.toPercent(data.terminal_terms_agg)) : '';
         //渠道
        data.channel_terms_agg && data.channel_terms_agg.length>0 ? _self.showPieChart($container,'渠道','channel',_self.toPercent(data.channel_terms_agg)) : '';

        //事件 用户ID列表 附加字段
        $('#itemsContainer').html('');
        _self.showItemList(data.uid_terms_agg,'用户统计');
        _self.showItemList(data.event_terms_agg,'事件统计');
        _self.showItemList(data.extra_terms_agg,'附加字段');

    },
    searchByParams:function(data){
        var _self = this;
        var $fresh = $('#statsContainer .fresh').show();//刷新图标
        window.TEMPDATA = data;
        $.sajax({
            url : ROOT + '/report/searchByParams',
            data: "data="+JSON.stringify(data),
            success: function (data) {
                window.TEMPDATA.from = data.data.totalStatResult.from;
                window.TEMPDATA.to = data.data.totalStatResult.to;
                _self.showLineChart(data.data.sectionStatResults);
                _self.showTermsResult(data.data.termsResultsMap);
                $fresh.hide();
                $('#totalIp').html(data.data.totalStatResult.ip);
                $('#totalPv').html(data.data.totalStatResult.pv);
                $('#totalUv').html(data.data.totalStatResult.uv);
                $('#statStartTime').val(data.data.totalStatResult.from);
                $('#statEndTime').val(data.data.totalStatResult.to);

            }
        })
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
    delDict: function (id,$tr){
        $.sajax({
            type:'POST',
            url: ROOT + '/dict/del',
            data:{id:id},
            success:function(){
                $.alert('已删除字典！','primary');
                //XLstats.getDictList();
                $tr.remove();
                if (window.PAGES_OBJ[id]){
                    window.PAGES_OBJ[id] = undefined;
                    window.PAGES = XLstats.dictMap2List(window.PAGES_OBJ);
                }else if (window.EVENTS_OBJ[id]){
                    window.EVENTS_OBJ[id] = undefined;
                    window.EVENTS = XLstats.dictMap2List(window.EVENTS_OBJ);
                }
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
                    success:function(){
                        $.alert('已添加新字典！','primary');
                        $('#searchDict').submit();
                        //更新数据
                        if (data.type == 1){
                            window.PAGES.push(data.description + '&&' + data.id);
                            window.PAGES_OBJ = XLstats.dictList2Map(window.PAGES);
                        }else if(data.type == 2){
                            window.EVENTS.push(data.description + '&&' + data.id);
                            window.EVENTS_OBJ = XLstats.dictList2Map(window.EVENTS);
                        }
                    }
                })
            }
        },
    saveDict: function ($tr) {
            var data = {};
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
                        //更新数据
                        if (window.PAGES_OBJ[data.id]){
                            window.PAGES_OBJ[data.id].description = data.description;
                            window.PAGES = XLstats.dictMap2List(window.PAGES_OBJ);
                        }else if(window.EVENTS_OBJ[data.id]){
                            window.EVENTS_OBJ[data.id].description = data.description;
                            window.EVENTS = XLstats.dictMap2List(window.EVENTS_OBJ);
                        }
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
                    return data;
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

                            if ( n==0 || n==1) {//chebox的选中态
                                var checkboxs = form.find('[name="'+arr[n]+'"]');
                                for(var i=idList.length-1 ; i>=0 ;i--){
                                    checkboxs.filter('[value="'+idList[i]+'"]').attr('checked','checked');
                                }
                            }else{
                                var data = [],objList = window[ n==4 ? "EVENTS_OBJ":"PAGES_OBJ"];
                                for(var i=idList.length-1 ; i>=0 ;i--){
                                    objList[idList[i]] ? data.push(objList[idList[i]]) : '';
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
    transformTempData: function(obj){
        var data = {};
        var	items = obj.elements;
        //转换数据格式
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
        //附加字段
        data['extra']={};
        $(obj).find('.extra').each(function(index,el){
            var name = $(el).find('.extraName').val();
            var value = $(el).find('.extraKey').val();
            if (name){
                data.extra[name] = value;
            }
        });
        return data;
    },

    saveTemplate: function (obj,isStat){//obj was a form

            var data = this.transformTempData(obj);

            var tempName = data.name,tempId = data.id;
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
                        var from = $('#statStartTime').val();
                        var to = $('#statEndTime').val();
                        XLstats.showTemplateStat(data.data.id,data.data.name,from,to);
                    }else{
                        $('#templateModal').modal('hide');
                    }

                    //更新模板列表
                    if (tempId == undefined){//新增
                        window.TEMPLIST.push({id:data.data.id,name:data.data.name});
                    }else if(tempName != data.name){//修改
                        for (var i = window.TEMPLIST.length-1; i>=0 ; i--){
                            if (window.TEMPLIST[i].id == tempId){
                                window.TEMPLIST[i].name = data.name;
                                break;
                            }
                        }
                    }
                    XLstats.freshTempList();
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
        var container = $('<div class="rel"><p class="pie-uv abs t35 r10 p20 zx2 bde"></p><div class="pb20 pie"></div></div>').appendTo(pieContainer).find('div');
        var UVbox = container.siblings('p');
        var UVs = {};//存储动态获得的UV
        var from = window.TEMPDATA.from;
        var to = window.TEMPDATA.to;
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
                pointFormat: '占比: <b>{point.percentage:.1f}%</b><br>PV:<b>{point.y:.0f}</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %<br>PV:<b>{point.y:.0f}</b>',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    },
                    events:{
                        click:function(e){
                            console.debug(e.point);
                            var id = e.point.name;
                            if (UVs[id]){
                                UVbox.html(template('pieTips',UVs[id]));
                            }else{
                                $('<div class="fresh"><i class="fa fa-refresh fa-spin fa-5x"></i></div>').appendTo(UVbox);
                                var pieData = {
                                    from : from,
                                    to : to,
                                    name : id,
                                    percent:e.point.percentage,
                                    pv: e.point.total
                                };
                                $.sajax({
                                    url : ROOT + '/report/getUvByField',
                                    data : "data="+JSON.stringify(window.TEMPDATA)+"&field="+seriesName+"&condition="+id,
                                    success:function(data){
                                        pieData.uv = data.data;
                                        UVs[id] = pieData;
                                        UVbox.html(template('pieTips',pieData));
                                        UVbox.show('normal');
                                    }
                                })
                            }
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

                //TIME[i] = data[i].from.substr(5,5) + '至'+ data[i].to.substr(5,5);
                TIME[i] = data[i].from + '至'+ data[i].to;
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

    showLineChart: function (data){
            var TIME = [] , IP = [] , PV = [], UV = [];
            for (var i = data.length - 1; i >= 0; i--) {

                //TIME[i] = data[i].from.substr(5,5) + '至'+ data[i].to.substr(5,5);
                TIME[i] = data[i].from + '至'+ data[i].to;
                IP[i] = data[i].ip;
                PV[i] = data[i].pv;
                UV[i] = data[i].uv;
            };

            $('#columnContainer').highcharts({
                chart: {
                    type: 'line'
                },
                title: {
                    text: 'PV UV IP 概览'
                },
                subtitle: {
                    text: 'Source: xiaoluo.com'
                },
                xAxis: {
                    categories: TIME,
                    crosshair: true
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'times'
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
                    line: {
                        dataLabels: {
                            enabled: true
                        }
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
