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


$.alert = function(msg,color,id){
    if (id){
       var box = $(id).find('.alertBox');
        if (box.length == 0){
            box = $('<div class="alert-box"></div>');
            $(id).append(box);
        }
    }else{
        var box = $('#alertBox');
        if ( box.length == 0 ) {
            box = $('<div id="alertBox"></div>');
            $(document.body).append(box);
        }
    }

    var color = color || 'pinkRed';
    var msg = $('<div class="bg-'+color+' alert-msg">'+msg+'</div>');
    box.append(msg);
    setTimeout(function(){
       msg.fadeOut('normal',function(){
           $(this).remove();
       });
    },3000)
}
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