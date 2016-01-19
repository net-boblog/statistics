package com.xiaoluo.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoluo.domain.DataPage;
import com.xiaoluo.statistics.constant.DictType;
import com.xiaoluo.statistics.dao.filter.SearchTemplateFilter;
import com.xiaoluo.statistics.entity.Dict;
import com.xiaoluo.statistics.entity.SearchTemplate;
import com.xiaoluo.statistics.exception.StatisticException;
import com.xiaoluo.statistics.service.DictService;
import com.xiaoluo.statistics.service.SearchTemplateService;
import com.xiaoluo.statistics.vo.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by Caedmon on 2016/1/15.
 */
@Controller
@RequestMapping("/template")
public class SearchTemplateController {
    @Autowired
    private SearchTemplateService searchTemplateService;
    @Autowired
    private DictService dictService;
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public @ResponseBody String update(String data){
        JSONObject jsonObject=JSONObject.parseObject(data);

        SearchTemplate template=new SearchTemplate();
        template.setId(Integer.valueOf(jsonObject.remove("id").toString()));
        if(!jsonObject.containsKey("name")){
            throw new StatisticException("名称不能为空");
        }
        template.setName(jsonObject.remove("name").toString());
        template.setType(Integer.valueOf(jsonObject.remove("type").toString()));
        template.setParams(jsonObject.toJSONString());
        if(template.getId()>0){
            searchTemplateService.update(template);
        }else{
            searchTemplateService.insert(template);
        }

        return new ApiResult(template).toString();
    }
    @RequestMapping("/list")
    public String find(SearchTemplateFilter filter,Model model){
        DataPage<SearchTemplate> searchTemplateDataPage=searchTemplateService.find(filter);
        model.addAttribute("templates",searchTemplateDataPage.getContent());
        model.addAttribute("pageIndex",searchTemplateDataPage.getPageIndex());
        model.addAttribute("total",searchTemplateDataPage.getTotal());
        model.addAttribute("pageSize",searchTemplateDataPage.getPageSize());
        model.addAttribute("lastPage",searchTemplateDataPage.getLastPage());

        List<Dict> pages=dictService.find(DictType.PAGE.value,null);
        List<Dict> events=dictService.find(DictType.EVENT.value,null);
        List<Dict> channels=dictService.find(DictType.CHANNEL.value,null);
        List<Dict> terminals=dictService.find(DictType.TERMINAL.value,null);
        model.addAttribute("pages",pages);
        model.addAttribute("events",events);
        model.addAttribute("channels",channels);
        model.addAttribute("terminals",terminals);

        return "template_list";
    }
    @RequestMapping("/get")
    public @ResponseBody String get(int id){
        return new ApiResult(searchTemplateService.get(id)).toString();
    }

    @RequestMapping("/view")
    public String view(Model model){
        List<Dict> pages=dictService.find(null,DictType.PAGE.value,null);
        List<Dict> events=dictService.find(null,DictType.EVENT.value,null);
        List<Dict> channels=dictService.find(null,DictType.CHANNEL.value,null);
        List<Dict> terminals=dictService.find(null,DictType.TERMINAL.value,null);
        model.addAttribute("pages",pages);
        model.addAttribute("events",events);
        model.addAttribute("channels",channels);
        model.addAttribute("terminals",terminals);
        return "template_view";
    }
}
