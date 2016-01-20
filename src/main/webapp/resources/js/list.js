/**
 * Created by zoe on 2016/1/19.
 */
$(function(){
    getDictList();//更新字典列表

    $(document.body).on('click','.addTemplate',function(e){
        $('#updateForm').html(template('templateTemp',{}));
        $('#templateModal').modal('show');
    });
    $(document.body).on('click','.editTemplate',function(e){
        var tar = e.currentTarget ;
        editTemplate(tar.dataset.id);
    });
    $(document.body).on('click','.saveTemplate',function(e){
        var tar= $(e.currentTarget).parents('form');
        saveTemplate(tar[0]);
    });
    $(document.body).on('click','.addDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        saveDict(tar);
    });
    $(document.body).on('click','.editDict',function(e){
        var tar= $(e.currentTarget).parents('tr');
        editDict(tar);
    });
    $(document.body).on('click','.searchDictBtn',function(e){
        var form= $(e.currentTarget).parents('form');
        var data = form.serialize();
        getDictList(data);
    });
    checkHash();
})

function editDict($tr){
    var selecttemp = template('selectTemp',{dictType:$tr.data('type')});
    var inputtemp = template('inputTemp',{dictDesc:$tr.data('description')});
    $tr.find('td').eq(1).html(selecttemp)
        .next().html(inputtemp)
        .next().find('button').removeClass('btn-greyPurple editDict').addClass('btn-primary addDict').html('保存');
}
function saveDict($tr) {
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
}
function getDictList(data){
    var data = data || {};
    $.post(ROOT + '/dict/list',data,function(data){
        if (data.code == 0) {
            $('#dictList').html(template('dictListTemp',{list:data.data}));
        }else {
            $.alert(data.msg);
        }
    },'json')
}

function editTemplate(id){
    $.get(ROOT + '/template/get?id='+id,function(data){
        console.debug(data);
        if (data.code == 0) {
            var DATA = data.data;
            var params = JSON.parse(DATA.params);

            console.debug(params);
            DATA.interval = params.interval || '';
            DATA.termsCountField = params.termsCountField || '';
            DATA.unit = params.unit || '';

            var form = $('#updateForm');
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
                $('#templateModal').modal('show');

        }else {
            $.alert(data.msg);
        }
    },'json')
}

function saveTemplate(obj){//obj was a form

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
    $.ajax({
        url:ROOT + "/template/update",
        data:"data="+JSON.stringify(data),
        type:'POST',
        error:function(e){
            $.alert(e);
        },
        success:function(){
            location.hash = "updatedFrom";
            location.reload();
        }
    })
}

function checkHash(){
    switch (location.hash.substr(1)){
        case 'updatedFrom':
            $.alert('模板保存成功!','primary');
            break;
        default :
            break;
    }
    location.hash = '';
}
