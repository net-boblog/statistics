package com.xiaoluo.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoluo.statistics.constant.DictType;
import com.xiaoluo.statistics.entity.Dict;
import com.xiaoluo.statistics.entity.FullActionReport;
import com.xiaoluo.statistics.entity.SearchTemplate;
import com.xiaoluo.statistics.search.SearchParams;
import com.xiaoluo.statistics.service.ActionReportService;
import com.xiaoluo.statistics.service.DictService;
import com.xiaoluo.statistics.service.SearchTemplateService;
import com.xiaoluo.statistics.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Caedmon on 2015/12/24.
 */
@Controller
@RequestMapping("/report")
public class ActionReportController extends RestBaseController{
    @Autowired
    private ActionReportService actionReportService;
    @RequestMapping("/multiSearch")
    public @ResponseBody String multiSearch(String data) throws Exception{
        SearchParams params=JSON.parseObject(data,SearchParams.class);

        return JSON.toJSONString(actionReportService.fullSearch(params));
    }
    @RequestMapping("/searchByTemplate")
    @ResponseBody
    public String searchByTemplate(Model model,int templateId,
                                   @RequestParam(required = false) Date from,
                                   @RequestParam (required = false)Date to) throws Exception{
        TotalStatResult totalStatResult=actionReportService.searchByTemplate(templateId,from,to);
        ApiResult apiResult=new ApiResult(totalStatResult);

        return apiResult.toString();
    }
    @RequestMapping("/rebuild")
    public @ResponseBody String rebuild(HttpServletRequest request) throws Exception{
        return actionReportService.rebuild().toString();

    }
    @RequestMapping("/funnelSearch")
    public @ResponseBody String funnelSearch(String templateIds ,
                                             @RequestParam(required=false) Date from,
                                             @RequestParam (required = false)Date to){
        List<Integer> ids=new ArrayList<Integer>();
        for(String templateId:templateIds.split(",")){
            ids.add(Integer.parseInt(templateId));
        }
        ApiResult result=new ApiResult(actionReportService.funnelSearch(ids,from,to));
        return result.toString();

    }
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public @ResponseBody String insert(String report) throws Exception{
        actionReportService.insert(report);
        return "OK";
    }

    public static void main(String[] args) {
        FullActionReport report=new FullActionReport();
        report.setIdentity_type("pc_cookie");
        report.setIdentity_value("UKAcFc5y1g8Br537WKcAS");
        report.setPrefix_page("1");
        report.setCurrent_page("2");
        report.setChannel("1");
        report.setTerminal("ios");

        Map<String,String> extra=new HashMap<String, String>();
        extra.put("p1","test1");
        extra.put("p2","test2");
        report.setExtra(extra);
        System.out.println(JSON.toJSONString(report));
    }
}
