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
    @RequestMapping("/update")
    public @ResponseBody String update(Dict dict){
        ApiResult result=new ApiResult();
        if(dictService.update(dict)<1){
            result.setCode(StatErrorCode.UPDATE_FAIL);
        }
        return result.toString();
    }
    @RequestMapping("/del")
    public @ResponseBody String del(int type,String key){
        ApiResult result=new ApiResult();
        if(dictService.del(type, key)<1){
            result.setCode(StatErrorCode.DELETE_FAIL);
        }
        return result.toString();
    }
    @RequestMapping("/list")
    public @ResponseBody String list(int type,@RequestParam(required = false) String key,@RequestParam(required = false)String description){
        ApiResult result=new ApiResult();
        result.setData(dictService.find(type,key,description));
        return result.toString();
    }

}
