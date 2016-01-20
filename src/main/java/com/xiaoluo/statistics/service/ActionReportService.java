package com.xiaoluo.statistics.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoluo.statistics.constant.IdentityType;
import com.xiaoluo.statistics.entity.Dict;
import com.xiaoluo.statistics.search.ElaticsearchManager;
import com.xiaoluo.statistics.entity.ActionReport;
import com.xiaoluo.statistics.entity.FullActionReport;
import com.xiaoluo.statistics.entity.SearchTemplate;
import com.xiaoluo.statistics.exception.StatisticException;
import com.xiaoluo.statistics.search.SearchParams;
import com.xiaoluo.statistics.util.DateKit;
import com.xiaoluo.statistics.vo.*;
import org.elasticsearch.Version;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Caedmon on 2015/12/24.
 */
@Service
public class ActionReportService {
    private static final Logger log= LoggerFactory.getLogger(ActionReportService.class);
    @Autowired
    private ElaticsearchManager elaticsearchManager;
    private TransportClient elaticsearchClient;
    @Autowired
    private UserService userService;
    @Autowired
    private String bindEvents;
    @Autowired
    private SearchTemplateService searchTemplateService;
    public static final String  INDEX_NAME="log",TYPE_NAME="action_report";
    private Set<String> bindEventSet=new HashSet<String>();
    private ExecutorService asyncThreadPool= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final Set<String> Dict_Field_Set=new HashSet<String>();
    @Autowired
    private DictService dictService;
    @PostConstruct
    public void init(){
        elaticsearchClient=elaticsearchManager.getElasticsearchClient();
        for(String bindEvent:bindEvents.split(",")){
            bindEventSet.add(bindEvent);
        }
        Dict_Field_Set.add("terminal");
        Dict_Field_Set.add("channel");
        Dict_Field_Set.add("prefix_page");
        Dict_Field_Set.add("current_page");
        Dict_Field_Set.add("event");
    }
    public TotalStatResult search(SearchParams params) throws Exception{
        SearchResponse response= createSearchRequestBuilder(params).get();
        SearchStatResult searchStatResult=buildStatResult(params,response);
        List<SearchStatResult.TermsResult> termsResults=getTermsResultList(params.getTermsCountField(),response);
        TotalStatResult totalStatResult=new TotalStatResult();
        totalStatResult.setTotalStatResult(searchStatResult);
        totalStatResult.setTermsResults(termsResults);
        return totalStatResult;
    }
    private List<SearchStatResult.TermsResult> getTermsResultList(String termsField,SearchResponse searchResponse){
        List<SearchStatResult.TermsResult> termsResults=new ArrayList<SearchStatResult.TermsResult>();
        if(!StringUtils.isEmpty(termsField)){
            boolean isDictField=Dict_Field_Set.contains(termsField);
            for(Terms.Bucket bucket:((StringTerms)searchResponse.getAggregations().getProperty("terms_count")).getBuckets()){
                SearchStatResult.TermsResult termsResult=new SearchStatResult.TermsResult();
                if(isDictField){
                    Dict dict=dictService.get(Integer.valueOf(bucket.getKeyAsString()));
                    termsResult.setKey(dict.getDescription());
                }else{
                    termsResult.setKey(bucket.getKeyAsString());
                }
                termsResult.setCount(bucket.getDocCount());
                termsResults.add(termsResult);
            }
        }

        return termsResults;
    }
    public SearchRequestBuilder createSearchRequestBuilder(SearchParams params){
        if(params.getFrom()==null){
            params.setFrom(new Date(System.currentTimeMillis()- DateKit.DAY_MILLS*7));
        }
        if(params.getTo()==null){
            params.setTo(new Date());
        }
        if(params.getUnit()==0){
            params.setUnit(SearchParams.SearchIntervalUnit.DAY.value);
        }
        if(params.getInterval()==0){
            params.setInterval(1);
        }
        SearchRequestBuilder searchRequestBuilder=elaticsearchClient.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME);
        searchRequestBuilder.setExplain(true);
        BoolQueryBuilder query=QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(params.getUid())){
            query.filter(QueryBuilders.matchQuery("uid", params.getUid()));
        }
        //时间
        if(params.getFrom()!=null){
            query.filter(QueryBuilders.rangeQuery("time").from(params.getFrom().getTime()));
        }
        if(params.getTo()!=null){
            query.filter(QueryBuilders.rangeQuery("time").to(params.getTo().getTime()));
        }
        if(params.getChannels()!=null&&!params.getChannels().isEmpty()){
            query.filter(QueryBuilders.termsQuery("channel", params.getChannels()));
        }
        if(params.getEvents()!=null&&params.getEvents().size()>0){
            query.filter(QueryBuilders.termsQuery("event", params.getEvents()));

        }
        if(!StringUtils.isEmpty(params.getKeyWords())){
            String[] keyWords=params.getKeyWords().split(",");
            query.filter(QueryBuilders.termsQuery("key_word", keyWords));
        }
        if(params.getPrefixPages()!=null&&params.getPrefixPages().size()>0){
            query.filter(QueryBuilders.termsQuery("prefix_page", params.getPrefixPages()));
        }
        if(params.getCurrentPages()!=null&&params.getCurrentPages().size()>0){
            query.filter(QueryBuilders.termsQuery("current_page", params.getCurrentPages()));
        }
        if(params.getTerminals()!=null&&params.getTerminals().size()>0){
            query.filter(QueryBuilders.termsQuery("terminal", params.getTerminals()));
        }

        searchRequestBuilder.setQuery(query);
        searchRequestBuilder.addAggregation(new CardinalityBuilder("ip").field("ip"));
        searchRequestBuilder.addAggregation(new CardinalityBuilder("uv").field("uid")).request();
        if(!StringUtils.isEmpty(params.getTermsCountField())){
            searchRequestBuilder.addAggregation(new TermsBuilder("terms_count").size(0).field(params.getTermsCountField()).minDocCount(params.getMinTermsCount()));
        }
        return searchRequestBuilder;
    }
    public void updateUid(final List<String> oldUids,final String newUid) throws Exception{
        Runnable task=new Runnable() {
            @Override
            public void run() {
                BoolQueryBuilder qb=QueryBuilders.boolQuery();
                qb.filter(QueryBuilders.termsQuery("uid", oldUids));
                SearchResponse scrollResp = elaticsearchClient.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
                        .setSearchType(SearchType.SCAN)
                        .setScroll(new TimeValue(60000))
                        .setQuery(qb)
                        .setSize(100).execute().actionGet();
                long start=System.currentTimeMillis();
                BulkRequestBuilder bulkRequestBuilder=elaticsearchClient.prepareBulk();
                while (true) {
                    for (SearchHit hit : scrollResp.getHits().getHits()) {
                        UpdateRequestBuilder updateRequestBuilder=elaticsearchClient.prepareUpdate(INDEX_NAME, TYPE_NAME, hit.getId());
                        updateRequestBuilder.setDoc("uid",newUid);
                        bulkRequestBuilder.add(updateRequestBuilder);

                    }
                    scrollResp = elaticsearchClient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(600000)).execute().actionGet();
                    //Break condition: No hits are returned
                    if (scrollResp.getHits().getHits().length == 0) {
                        break;
                    }
                }
                if(bulkRequestBuilder.numberOfActions()<0){
                    return;
                }
                try{
                    BulkResponse response=bulkRequestBuilder.execute().get();
                    long end=System.currentTimeMillis();
                    log.info("Scroll num {},time {}ms,result {}:", response.getItems().length, (end - start), response.getItems().toString());
                }catch (Exception e){
                    log.error("Bulk update uid error ",e);
                }


            }
        };
        asyncThreadPool.execute(task);

    }
    public TotalStatResult search(int templateId,Date from,Date to) throws Exception{
        SearchTemplate template=searchTemplateService.get(templateId);
        String params=template.getParams();
        SearchParams searchParams=JSON.parseObject(params,SearchParams.class);
        searchParams.setFrom(from);
        searchParams.setTo(to);
        TotalStatResult totalStatResult=search(searchParams);
        List<SearchStatResult> sectionStatResults=multiSearch(searchParams);
        totalStatResult.setSectionStatResults(sectionStatResults);
        totalStatResult.setTermsCountFiled(searchParams.getTermsCountField());
        return totalStatResult;
    }
    public List<SearchStatResult> multiSearch(SearchParams params) throws Exception{
        MultiSearchRequestBuilder requestBuilder=elaticsearchClient.prepareMultiSearch();
        SearchParams.SearchIntervalUnit unit= SearchParams.SearchIntervalUnit.valueOf(params.getUnit());
        int interval=params.getInterval();
        long intervalMills=0;
        switch (unit){
            case MINUTE:
                intervalMills=interval*DateKit.MINUTE_MILLS;
                break;
            case HOUR:
                intervalMills=interval*DateKit.HOUR_MILLS;
                break;
            case DAY:
                intervalMills=interval*DateKit.DAY_MILLS;
                break;
            case MONTH:
                intervalMills=interval*DateKit.MONTH_MILLS;
                break;
            default:
                break;
        }
        long distance=params.getTo().getTime()-params.getFrom().getTime();
        if(distance/intervalMills>100){
            throw new StatisticException("查询时间间隔太短,结果集超过100,会影响性能");
        }
        long prefix=params.getFrom().getTime();
        long to=params.getTo().getTime();
        List<SearchParams> searchParamsList=new ArrayList<SearchParams>();
        while(prefix<=to){
            final SearchParams itemParams=SearchParams.copyFrom(params);
            itemParams.setFrom(new Date(prefix));
            prefix=prefix+ intervalMills;
            itemParams.setTo(new Date(prefix));
            requestBuilder.add(createSearchRequestBuilder(itemParams).request());
            searchParamsList.add(itemParams);
        }
        MultiSearchResponse multiSearchResponse=requestBuilder.execute().get();
        System.out.println(multiSearchResponse.toString());
        MultiSearchResponse.Item[] items=multiSearchResponse.getResponses();
        List<SearchStatResult> results=new ArrayList<SearchStatResult>(items.length);
        int i=0;
        for(MultiSearchResponse.Item item:items){
            SearchResponse response=item.getResponse();
            if(response==null){
                log.error("MultiSearch error {}", item.getFailureMessage());
                break;
            }
            SearchStatResult result=buildStatResult(searchParamsList.get(i),response);
            results.add(result);
            i++;
        }
        return results;
    }
    private SearchStatResult buildStatResult(SearchParams searchParams,SearchResponse response){
        SearchStatResult result=new SearchStatResult();
        result.setPv(response.getHits().totalHits());
        Aggregations aggregations=response.getAggregations();
        result.setUv((Double) aggregations.get("uv").getProperty("value"));
        result.setIp((Double) aggregations.get("ip").getProperty("value"));
        result.setFrom(DateKit.YYYY_MM_DD_HH_MM_SS_FORMAT.format(searchParams.getFrom()));
        result.setTo(DateKit.YYYY_MM_DD_HH_MM_SS_FORMAT.format(searchParams.getTo()));
        return result;
    }
    public void insert(FullActionReport report) throws Exception{
        //判断事件是否为注册或登录事件
        String uid=null;
        if(this.bindEventSet.contains(report.getEvent())){
            String phone=report.getKey_word();
            UserBindResult result=userService.bindUser(phone, IdentityType.valueOf(report.getIdentity_type()), report.getIdentity_value());
            if(result.needUpdate()){
                updateUid(result.getOldUids(), result.getNewUid());
            }
            uid=result.getNewUid();
        }else{
            uid=userService.getUid(null,IdentityType.valueOf(report.getIdentity_type()),report.getIdentity_value());
        }
        report.setUid(uid);
        report.setTime(System.currentTimeMillis());
        addActionReport(report);
    }
    public void addActionReport(ActionReport report){
        IndexRequestBuilder requestBuilder=elaticsearchClient.prepareIndex(INDEX_NAME, TYPE_NAME);
        requestBuilder.setOpType(IndexRequest.OpType.CREATE);
        requestBuilder.setSource(JSON.toJSONString(report));
        IndexResponse response=null;
        try{
            response=requestBuilder.execute().get();
        }catch (Exception e){
            e.printStackTrace();
            log.error("Prepare index error ",e);
        }
    }

    public PutMappingResponse rebuild() throws Exception{
        CreateIndexRequest createRequest=new CreateIndexRequest(INDEX_NAME);
        Settings settings=Settings.settingsBuilder().put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT).build();
        createRequest.settings(settings);
        CreateIndexResponse createIndexResponse=elaticsearchClient.admin().indices().create(createRequest).actionGet();
        List<String> sources=new ArrayList();
        sources.add("prefix_page");
        sources.add("type=string,index=not_analyzed");
        sources.add("uid");
        sources.add("type=string,index=not_analyzed");
        sources.add("current_page");
        sources.add("type=string,index=not_analyzed");
        sources.add("channel");
        sources.add("type=string,index=not_analyzed");
        sources.add("terminal");
        sources.add("type=string,index=not_analyzed");
        sources.add("event");
        sources.add("type=string,index=not_analyzed");
        sources.add("event");
        sources.add("type=string,index=not_analyzed");
        sources.add("key_word");
        //sources.add("type=string,index=not_analyzed");
        sources.add("type=string,analyzer=ik");
        sources.add("time");
        sources.add("type=date,format=epoch_millis");
        sources.add("ip");
        sources.add("type=string,index=not_analyzed");
        sources.add("version");
        sources.add("type=string,index=not_analyzed");
        PutMappingRequest putMappingRequest=new PutMappingRequest(INDEX_NAME).type(TYPE_NAME).source(PutMappingRequest.buildFromSimplifiedDef(TYPE_NAME,sources.toArray()));
        PutMappingResponse putMappingResponse=elaticsearchClient.admin().indices().putMapping(putMappingRequest).get();
        return putMappingResponse;
    }

}
