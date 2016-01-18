package com.xiaoluo.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.xiaoluo.domain.DataPage;
import com.xiaoluo.statistics.dao.filter.SearchTemplateFilter;
import com.xiaoluo.statistics.entity.SearchTemplate;
import com.xiaoluo.statistics.service.SearchTemplateService;
import com.xiaoluo.statistics.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Caedmon on 2016/1/15.
 */
@Controller
@RequestMapping("/template")
public class SearchTemplateController {
    @Autowired
    private SearchTemplateService searchTemplateService;
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public @ResponseBody String update(String data){
        SearchTemplate template=JSON.parseObject(data, SearchTemplate.class);
        searchTemplateService.update(template);
        return ApiResult.EMPTY_RESULT.toString();
    }
    @RequestMapping("/find")
    public @ResponseBody
    String find(SearchTemplateFilter filter){
        DataPage<SearchTemplate> searchTemplateDataPage=searchTemplateService.find(filter);
        ApiResult result=new ApiResult();
        result.setData(searchTemplateDataPage);
        return result.toString();
    }
    @RequestMapping("/get")
    public @ResponseBody String get(int id){
       return new ApiResult(searchTemplateService.get(id)).toString();
    }

}
