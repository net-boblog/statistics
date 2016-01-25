package com.xiaoluo.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoluo.statistics.entity.FullActionReport;
import com.xiaoluo.statistics.entity.SearchTemplate;
import com.xiaoluo.statistics.search.SearchParams;
import com.xiaoluo.statistics.service.ActionReportService;
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
    @Autowired
    private SearchTemplateService searchTemplateService;
    @RequestMapping("/multiSearch")
    public @ResponseBody String multiSearch(SearchParams params) throws Exception{

        List<SearchStatResult> list=actionReportService.multiSearch(params);
        List<Long> pvs=new ArrayList<Long>();
        List<Double> uvs=new ArrayList<Double>();
        List<Double> ips=new ArrayList<Double>();
        List<String> times=new ArrayList<String>();
        for(SearchStatResult result:list){
            pvs.add(result.getPv());
            uvs.add(result.getUv());
            ips.add(result.getIp());
            times.add("\""+result.getTo()+"\"");
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("pvs", Arrays.toString(pvs.toArray()));
        jsonObject.put("uvs",Arrays.toString(uvs.toArray()));
        jsonObject.put("ips",Arrays.toString(ips.toArray()));
        jsonObject.put("times", Arrays.toString(times.toArray()));
        return jsonObject.toJSONString();
    }
    @RequestMapping("/searchByTemplate")
    public String searchByTemplate(Model model,int templateId, @RequestParam(required = false) Date from, @RequestParam (required = false)Date to) throws Exception{
        TotalStatResult totalStatResult=actionReportService.searchByTemplate(templateId,from,to);
        List<Long> pvs=new ArrayList<Long>();
        List<Double> uvs=new ArrayList<Double>();
        List<Double> ips=new ArrayList<Double>();
        List<String> times=new ArrayList<String>();
        for(SearchStatResult result:totalStatResult.getSectionStatResults()){
            pvs.add(result.getPv());
            uvs.add(result.getUv());
            ips.add(result.getIp());
            times.add("\""+result.getTo()+"\"");
        }
        model.addAttribute("totalPv",totalStatResult.getTotalStatResult().getPv());
        model.addAttribute("totalUv",totalStatResult.getTotalStatResult().getUv());
        model.addAttribute("totalIp",totalStatResult.getTotalStatResult().getIp());
        model.addAttribute("termsAggMap",totalStatResult.getTermsResultsMap());
        model.addAttribute("channel_terms_agg",JSON.toJSONString(totalStatResult.getTermsResultsMap().get("channel")));
        model.addAttribute("pvs",Arrays.toString(pvs.toArray()));
        model.addAttribute("uvs",Arrays.toString(uvs.toArray()));
        model.addAttribute("ips",Arrays.toString(ips.toArray()));
        model.addAttribute("times", Arrays.toString(times.toArray()));
        return "search_result";
    }
    @RequestMapping("/rebuild")
    public @ResponseBody String rebuild(HttpServletRequest request) throws Exception{
        return actionReportService.rebuild().toString();

    }
    @RequestMapping("/funnelSearch")
    public @ResponseBody String funnelSearch(String templateIds){
        List<SearchParams> searchParamsList=new ArrayList<SearchParams>();
        for(String templateId:templateIds.split(",")){
            SearchTemplate template=searchTemplateService.get(Integer.valueOf(templateId));
            String params=template.getParams();
            SearchParams searchParams= JSON.parseObject(params,SearchParams.class);
            searchParams.setFrom(null);
            searchParams.setTo(null);
            searchParamsList.add(searchParams);

        }
        return actionReportService.funnelSearch(searchParamsList).toString();

    }
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public @ResponseBody String insert(FullActionReport report) throws Exception{
        actionReportService.insert(report);
        return "OK";
    }
}
