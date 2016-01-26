package com.xiaoluo.statistics.service;

import com.alibaba.fastjson.JSON;
import com.xiaoluo.statistics.constant.DictType;
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
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregator;
import org.elasticsearch.search.aggregations.bucket.nested.NestedBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Map<String,String> Terms_Agg_Name_Set=new HashMap<String,String>();
    @Autowired
    private DictService dictService;
    private static final String UID_TERMS_AGG_NAME="uid_terms_agg",
            CHANNEL_TERMS_AGG_NAME="channel_terms_agg",
            PREFIX_PAGE_TERMS_AGG_NAME="prefix_page_terms_agg",
            CURRENT_PAGE_TERMS_AGG_NAME="current_page_terms_agg",
            EXTRA_TERMS_AGG_NAME ="extra_terms_agg",
            EVENT_TERMS_AGG_NAME="event_terms_agg",
            TERMINAL_TERMS_AGG_NAME="terminal_terms_agg";
    private static final String UID_FIELD_NAME="uid",
            CHANNEL_FIELD_NAME="channel",
            PREFIX_PAGE_FIELD_NAME="prefix_page",
            CURRENT_PAGE_FIELD_NAME="current_page",
            EXTRA_FIELD_NAME="extra",
            EVENT_FIELD_NAME="event",
            TIME_FIELD_NAME="time",
            TERMINAL_FIELD_NAME="terminal",
            IP_FIELD_NAME="ip",
            VERSION_FIELD_NAME="version";
    @PostConstruct
    public void init(){
        elaticsearchClient=elaticsearchManager.getElasticsearchClient();
        for(String bindEvent:bindEvents.split(",")){
            bindEventSet.add(bindEvent);
        }
        Terms_Agg_Name_Set.put(UID_FIELD_NAME,UID_TERMS_AGG_NAME);
        Terms_Agg_Name_Set.put(CHANNEL_FIELD_NAME,CHANNEL_TERMS_AGG_NAME);
        Terms_Agg_Name_Set.put(PREFIX_PAGE_FIELD_NAME,PREFIX_PAGE_TERMS_AGG_NAME);
        Terms_Agg_Name_Set.put(CURRENT_PAGE_FIELD_NAME,CURRENT_PAGE_TERMS_AGG_NAME);
        Terms_Agg_Name_Set.put(EVENT_FIELD_NAME,EVENT_TERMS_AGG_NAME);
        Terms_Agg_Name_Set.put(TERMINAL_FIELD_NAME,TERMINAL_TERMS_AGG_NAME);
    }
    public TotalStatResult search(SearchParams params) throws Exception{
        SearchResponse response= createSearchRequestBuilder(params).get();
        SearchStatResult searchStatResult=buildStatResult(params,response);
        Map<String,List<SearchStatResult.TermsResult>> termsResultsMap= getTermsAggResult(response);
        TotalStatResult totalStatResult=new TotalStatResult();
        totalStatResult.setTotalStatResult(searchStatResult);
        totalStatResult.setTermsResultsMap(termsResultsMap);
        return totalStatResult;
    }
    private List<SearchStatResult.TermsResult> buildTermsResultList(StringTerms termsAgg){
        List<SearchStatResult.TermsResult> termsResults=new ArrayList<SearchStatResult.TermsResult>();
        String aggName=termsAgg.getName();
        for(Terms.Bucket bucket:termsAgg.getBuckets()){
            SearchStatResult.TermsResult termsResult=new SearchStatResult.TermsResult();
            if(!aggName.equals(UID_TERMS_AGG_NAME)&&!aggName.equals(EXTRA_TERMS_AGG_NAME)){
                Dict dict=dictService.get(bucket.getKeyAsString());
                if(dict==null){
                    termsResult.setValue(bucket.getKeyAsString());
                }else{
                    termsResult.setValue(dict.getDescription());
                }

            }else{
                termsResult.setValue(bucket.getKeyAsString());
            }
            termsResult.setName(aggName);
            termsResult.setCount(bucket.getDocCount());
            termsResults.add(termsResult);
        }
        return termsResults;
    }
    private Map<String,List<SearchStatResult.TermsResult>> getTermsAggResult(SearchResponse searchResponse){
        Map<String,List<SearchStatResult.TermsResult>> map=new HashMap<String, List<SearchStatResult.TermsResult>>();
        for(Aggregation aggregation:searchResponse.getAggregations().asMap().values()){
            if(aggregation instanceof StringTerms){
                List<SearchStatResult.TermsResult> termsResultList=buildTermsResultList((StringTerms) aggregation);
                map.put(aggregation.getName(),termsResultList);
            }
            if(aggregation instanceof InternalNested&&aggregation.getName().equals(EXTRA_TERMS_AGG_NAME)){
                List<SearchStatResult.TermsResult> extraTermsResults=new ArrayList<SearchStatResult.TermsResult>();
                InternalNested nested=(InternalNested)aggregation;
                for(Aggregation agg:nested.getAggregations().asList()){
                    if(agg instanceof StringTerms){
                        extraTermsResults.addAll(buildTermsResultList((StringTerms)agg));
                    }
                }
                map.put(aggregation.getName(),extraTermsResults);
            }

        }
        return map;
    }
    public List<SearchStatResult.TermsResult> funnelSearch(List<SearchParams> searchParamsList){
        List<String> uids=new ArrayList<String>();
        List<SearchStatResult.TermsResult> termsResults=null;
        for(SearchParams searchParams:searchParamsList){
            if(termsResults!=null){
                for(SearchStatResult.TermsResult termsResult:termsResults){
                    uids.add(termsResult.getValue());
                }
                searchParams.setUids(uids);
            }
            SearchResponse response=createSearchRequestBuilder(searchParams).get();
            termsResults= getTermsAggResult(response).get(UID_TERMS_AGG_NAME);
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
        if(!StringUtils.isEmpty(params.getUids())){
            query.filter(QueryBuilders.termsQuery(UID_FIELD_NAME, params.getUids()));
        }
        //时间
        if(params.getFrom()!=null){
            query.filter(QueryBuilders.rangeQuery(TIME_FIELD_NAME).from(params.getFrom().getTime()));
        }
        if(params.getTo()!=null){
            query.filter(QueryBuilders.rangeQuery(TIME_FIELD_NAME).to(params.getTo().getTime()));
        }
        if(params.getChannels()!=null&&!params.getChannels().isEmpty()){
            query.filter(QueryBuilders.termsQuery(CHANNEL_FIELD_NAME, params.getChannels()));
        }
        if(params.getEvents()!=null&&params.getEvents().size()>0){
            query.filter(QueryBuilders.termsQuery(EVENT_FIELD_NAME, params.getEvents()));

        }

        if(params.getPrefixPages()!=null&&params.getPrefixPages().size()>0){
            query.filter(QueryBuilders.termsQuery(PREFIX_PAGE_FIELD_NAME, params.getPrefixPages()));
        }
        if(params.getCurrentPages()!=null&&params.getCurrentPages().size()>0){
            query.filter(QueryBuilders.termsQuery(CURRENT_PAGE_FIELD_NAME, params.getCurrentPages()));
        }
        if(params.getTerminals()!=null&&params.getTerminals().size()>0){
            query.filter(QueryBuilders.termsQuery(TERMINAL_FIELD_NAME, params.getTerminals()));
        }
        if(params.getExtra()!=null){
            for(Map.Entry<String,String> entry:params.getExtra().entrySet()){
                TermsQueryBuilder extraTermsQueryBuilder=QueryBuilders.
                        termsQuery(EXTRA_FIELD_NAME+"."+entry.getKey(),entry.getValue().split(","));
               query.filter(QueryBuilders.nestedQuery(EXTRA_FIELD_NAME,extraTermsQueryBuilder));
            }

        }
        searchRequestBuilder.setQuery(query);
        searchRequestBuilder.addAggregation(new CardinalityBuilder("ip").field(IP_FIELD_NAME));
        searchRequestBuilder.addAggregation(new CardinalityBuilder("uv").field(UID_FIELD_NAME)).request();

        if(params.getExtra()!=null){
            NestedBuilder extraBuilder=AggregationBuilders.nested(EXTRA_TERMS_AGG_NAME).path(EXTRA_FIELD_NAME);
            for(Map.Entry<String,String> entry:params.getExtra().entrySet()){
                extraBuilder.subAggregation(new TermsBuilder(entry.getKey()).size(0).field(EXTRA_FIELD_NAME+"."+entry.getKey()).minDocCount(1));
            }
            searchRequestBuilder.addAggregation(extraBuilder);
        }


        for(Map.Entry<String,String> entry:Terms_Agg_Name_Set.entrySet()){
            searchRequestBuilder.addAggregation(new TermsBuilder(entry.getValue()).size(0).field(entry.getKey()).minDocCount(1));
        }
        return searchRequestBuilder;
    }
    public void updateUid(final List<String> oldUids,final String newUid) throws Exception{
        Runnable task=new Runnable() {
            @Override
            public void run() {
                BoolQueryBuilder qb=QueryBuilders.boolQuery();
                qb.filter(QueryBuilders.termsQuery(UID_FIELD_NAME, oldUids));
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
                        updateRequestBuilder.setDoc(UID_FIELD_NAME,newUid);
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
    public TotalStatResult fullSearch(SearchParams searchParams) throws Exception{
        TotalStatResult totalStatResult=search(searchParams);
        List<SearchStatResult> sectionStatResults=multiSearch(searchParams);
        totalStatResult.setSectionStatResults(sectionStatResults);
        return totalStatResult;
    }
    public TotalStatResult searchByTemplate(int templateId, Date from, Date to) throws Exception{
        SearchTemplate template=searchTemplateService.get(templateId);
        String params=template.getParams();
        SearchParams searchParams=JSON.parseObject(params,SearchParams.class);
        searchParams.setFrom(from);
        searchParams.setTo(to);
        return fullSearch(searchParams);
    }
    public List<SearchStatResult> multiSearch(SearchParams params) throws Exception{
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
    private Dict createIfNotExist(String id,DictType type){
        if(StringUtils.isEmpty(id)){
            return null;
        }
        Dict dict=dictService.get(id);
        if(dict==null){
            dict=new Dict();
            dict.setId(id);
            dict.setType(type.value);
            dict.setDescription(id);
            dictService.insert(dict);
        }
        return dict;
    }
    public void insert(String data) throws Exception{
        FullActionReport report=JSON.parseObject(data,FullActionReport.class);
        //判断是否存在字典,不存在则自动新增一条
        createIfNotExist(report.getChannel(),DictType.CHANNEL);
        createIfNotExist(report.getPrefix_page(),DictType.PAGE);
        createIfNotExist(report.getCurrent_page(),DictType.PAGE);
        createIfNotExist(report.getTerminal(),DictType.TERMINAL);
        createIfNotExist(report.getEvent(),DictType.EVENT);
        //判断事件是否为注册或登录事件
        String uid=null;
        if(this.bindEventSet.contains(report.getEvent())){
            String phone=report.getExtra().get("p1");
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
        sources.add(PREFIX_PAGE_FIELD_NAME);
        sources.add("type=string,index=not_analyzed");
        sources.add(UID_FIELD_NAME);
        sources.add("type=string,index=not_analyzed");
        sources.add(CURRENT_PAGE_FIELD_NAME);
        sources.add("type=string,index=not_analyzed");
        sources.add(CHANNEL_FIELD_NAME);
        sources.add("type=string,index=not_analyzed");
        sources.add(TERMINAL_FIELD_NAME);
        sources.add("type=string,index=not_analyzed");
        sources.add(EVENT_FIELD_NAME);
        sources.add("type=string,index=not_analyzed");
        sources.add(EXTRA_FIELD_NAME);
        sources.add("type=nested");
        sources.add(TIME_FIELD_NAME);
        sources.add("type=date,format=epoch_millis");
        sources.add(IP_FIELD_NAME);
        sources.add("type=string,index=not_analyzed");
        sources.add(VERSION_FIELD_NAME);
        sources.add("type=string,index=not_analyzed");
        PutMappingRequest putMappingRequest=new PutMappingRequest(INDEX_NAME).type(TYPE_NAME).source(PutMappingRequest.buildFromSimplifiedDef(TYPE_NAME,sources.toArray()));
        PutMappingResponse putMappingResponse=elaticsearchClient.admin().indices().putMapping(putMappingRequest).get();
        return putMappingResponse;
    }

}
