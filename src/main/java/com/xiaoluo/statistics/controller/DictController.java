package com.xiaoluo.statistics.controller;

import com.xiaoluo.statistics.constant.StatErrorCode;
import com.xiaoluo.statistics.entity.Dict;
import com.xiaoluo.statistics.service.DictService;
import com.xiaoluo.statistics.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Caedmon on 2016/1/14.
 */
@Controller
@RequestMapping("/dict")
public class DictController extends RestBaseController{
    @Autowired
    private DictService dictService;
    @RequestMapping("/insert")
    public @ResponseBody String insert(Dict dict){
        ApiResult result=new ApiResult();
        if(dictService.insert(dict)<1){
            result.setCode(StatErrorCode.UPDATE_FAIL);
        }
        return result.toString();
    }
    @RequestMapping("/update")
    public @ResponseBody String update(String id,String description){
        ApiResult result=new ApiResult();
        if(dictService.update(id,description)<1){
            result.setCode(StatErrorCode.UPDATE_FAIL);
        }
        return result.toString();
    }
    @RequestMapping("/del")
    public @ResponseBody String del(int id){
        ApiResult result=new ApiResult();
        if(dictService.del(id)<1){
            result.setCode(StatErrorCode.DELETE_FAIL);
        }
        return result.toString();
    }
    @RequestMapping("/list")
    public @ResponseBody String list(@RequestParam(required = false)String ids,
                                     @RequestParam(required = false,defaultValue ="0") Integer type,
                                     @RequestParam(required = false)String description){
        ApiResult result=new ApiResult();
        result.setData(dictService.find(ids,type,description));
        return result.toString();
    }

}
