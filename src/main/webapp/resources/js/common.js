/**
 * Created by Administrator on 2016/1/19.
 */
$.cookie = {
    get:function(n){
        var m = document.cookie.match(new RegExp( "(^| )"+n+"=([^;]*)(;|$)"));
        return !m ? "":decodeURIComponent(m[2]);
    },
    set:function(name,value,domain,path,hour){
        var expire = new Date();
        expire.setTime(expire.getTime() + (hour?3600000 * hour:30*24*60*60*1000));

        document.cookie = name + "=" + value + "; " + "expires=" + expire.toGMTString()+"; path="+ (path ? path :"/")+ "; "  + (domain ? ("domain=" + domain + ";") : "");
    },
    del : function(name, domain, path) {
        document.cookie = name + "=; expires=Mon, 26 Jul 1997 05:00:00 GMT; path="+ (path ? path :"/")+ "; " + (domain ? ("domain=" + domain + ";") : "");
    }
};

$.bom = {
    query:function(n){
        var m = window.location.search.match(new RegExp( "(\\?|&)"+n+"=([^&]*)(&|$)"));
        return !m ? "":decodeURIComponent(m[2]);
    }
};

$.fajax = function(opt){
    var layerIndex = null;
    var post_data = {};
    if(opt.animate){
        var animateOption = $.extend({
            shade: false,
            type: 2,
            time: 15,
            shadeClose: false
        }, opt.animate);
        layerIndex = layer.open(animateOption);
    }
    $.ajax({
        type: opt.type || 'GET',
        url: opt.req ? (CGI_PATH + opt.req) : opt.url,
        dataType: opt.dataType || 'json',
        data: opt.postType && opt.postType == 'String' ?  opt.data :$.extend(post_data,(opt.data || {})) ,
        async:opt.hasOwnProperty('async')? opt.async:true,
        success : function(json){

                if (json.code == 0){
                    opt.success(json);
                }

                if(json.code == 0 && opt.animate && opt.animate.success){
                    if(layerIndex !==null) layer.close(layerIndex);
                }else{
                    setTimeout(function(){
                        if(layerIndex !==null) layer.close(layerIndex);
                    },50);
                }

                if (json.code != 0) {
                    layer.open({
                        content : json.msg,
                        time : 2
                    });
                }
        },
        timeout:10000,
        error : function(err){
            if(layerIndex !==null) layer.close(layerIndex);
            layer.open({
                content : "服务器没有响应 %>_<%",
                time : 2
            });
            if(opt.errorCb){
                opt.errorCb()
            }
        }
    });
};

template.helper("filterDictType",function(type){

    switch(type){
        case 1:
            return '页面';
        case 2:
            return '事件';
        case 3:
            return '渠道';
        case 4:
            return '终端';
        default:
            return '未知';
    }
});