package com.xiaoluo.statistics.service;

import com.alibaba.fastjson.JSON;
import com.xiaoluo.statistics.elaticsearch.ElaticsearchManager;
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
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;

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
    public static final String  INDEX_NAME="log",TYPE_NAME="action_report";
    private Set<String> bindEventSet=new HashSet<String>();
    @PostConstruct
    public void init(){
        elaticsearchClient=elaticsearchManager.getElasticsearchClient();
        for(String bindEvent:bindEvents.split(",")){
            bindEventSet.add(bindEvent);
        }
    }
    public Map<String,Object> search(SearchParams params) throws Exception{
        SearchResponse response= createSearchRequestBuilder(params).get();
        Map<String,Object> result=new HashMap<String, Object>();
        result.put("pv", response.getHits().totalHits());
        result.put("uv", response.getAggregations().get("uv").getProperty("value"));
        result.put("cost", response.getTookInMillis());
        return result;
    }
    public SearchRequestBuilder createSearchRequestBuilder(SearchParams params){
        SearchRequestBuilder searchRequestBuilder=elaticsearchClient.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME);
        searchRequestBuilder.setExplain(true);
        BoolQueryBuilder query=QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(params.getUid())){
            query.filter(QueryBuilders.matchQuery("uid",params.getUid()));
        }
        //时间
        if(params.getFrom()!=null){
            query.filter(QueryBuilders.rangeQuery("time").from(params.getFrom().getTime()));
        }
        if(params.getTo()!=null){
            query.filter(QueryBuilders.rangeQuery("time").to(params.getTo().getTime()));
        }
        if(!StringUtils.isEmpty(params.getChannels())){
            String[] channels=params.getChannels().split(",");
            query.filter(QueryBuilders.termsQuery("channel", channels));
        }
        if(!StringUtils.isEmpty(params.getEvents())){
            String[] events=params.getEvents().split(",");
            query.filter(QueryBuilders.termsQuery("event", events));

        }
        if(!StringUtils.isEmpty(params.getKeyWords())){
            String[] keyWords=params.getKeyWords().split(",");
            query.filter(QueryBuilders.termsQuery("keyWords", keyWords));
        }
        if(!StringUtils.isEmpty(params.getPrefixPages())){
            String[] prefixPages=params.getPrefixPages().split(",");
            query.filter(QueryBuilders.termsQuery("prefixPage", prefixPages));
        }
        if(!StringUtils.isEmpty(params.getCurrentPages())){
            String[] currentPages=params.getCurrentPages().split(",");
            query.filter(QueryBuilders.termsQuery("currentPage", currentPages));
        }
        if(!StringUtils.isEmpty(params.getTerminals())){
            String[] terminals=params.getTerminals().split(",");
            query.filter(QueryBuilders.termsQuery("terminal", terminals));
        }

        searchRequestBuilder.setQuery(query);
        searchRequestBuilder.addAggregation(new CardinalityBuilder("ip").field("ip"));
        searchRequestBuilder.addAggregation(new CardinalityBuilder("uv").field("uid")).request();
        searchRequestBuilder.addAggregation(new TermsBuilder("active_count").size(0).field("uid").minDocCount(params.getMinActiveCount()));
        return searchRequestBuilder;
    }
    public void updateUid(List<String> oldUids, String newUid) throws Exception{
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
        BulkResponse response=bulkRequestBuilder.execute().get();
        long end=System.currentTimeMillis();
        log.info("Scroll num {},time {}ms,result {}:", response.getItems().length, (end - start), response.getItems().toString());
    }
    public List<SimpleStatResult> multiSearch(SearchParams params) throws Exception{
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
            throw new IllegalArgumentException("Search interval is too short,result set greater than 100 ");
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
        List<SimpleStatResult> results=new ArrayList<SimpleStatResult>(items.length);
        int i=0;
        for(MultiSearchResponse.Item item:items){
            SearchResponse response=item.getResponse();
            SimpleStatResult result=new SimpleStatResult();
            if(response==null){
                System.out.println(item.getFailureMessage());
                break;
            }
            result.setPv(response.getHits().totalHits());
            Aggregations aggregations=response.getAggregations();
            result.setUv((Double) aggregations.get("uv").getProperty("value"));
            result.setIp((Double) aggregations.get("ip").getProperty("value"));
            long activeCount=((StringTerms)aggregations.getProperty("active_count")).getBuckets().size();
            result.setActiveCount(activeCount);
            result.setCost(response.getTookInMillis());
            result.setFrom(DateKit.YYYY_MM_DD_HH_MM_SS_FORMAT.format(searchParamsList.get(i).getFrom()));
            result.setTo(DateKit.YYYY_MM_DD_HH_MM_SS_FORMAT.format(searchParamsList.get(i).getTo()));
            results.add(result);
            i++;
        }
        return results;
    }
    public void insert(FullActionReport report) throws Exception{
        //判断事件是否为注册或登录事件
        String uid=null;
        if(this.bindEventSet.contains(report.getEvent())){
            String phone=report.getKeyWord();
            UserBindResult result=userService.bindUser(phone, report.getProperty(), report.getValue());
            if(result.needUpdate()){
                updateUid(result.getOldUids(), result.getNewUid());
            }
            uid=result.getNewUid();
        }else{
            uid=userService.getUid(null,report.getProperty(),report.getValue());
        }
        report.setUid(uid);
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
        sources.add("prefixPage");
        sources.add("type=string,index=not_analyzed");
        sources.add("uid");
        sources.add("type=string,index=not_analyzed");
        sources.add("currentPage");
        sources.add("type=string,index=not_analyzed");
        sources.add("channel");
        sources.add("type=string,index=not_analyzed");
        sources.add("terminal");
        sources.add("type=string,index=not_analyzed");
        sources.add("event");
        sources.add("type=string,index=not_analyzed");
        sources.add("event");
        sources.add("type=string,index=not_analyzed");
        sources.add("keyword");
        //sources.add("type=string,index=not_analyzed");
        sources.add("type=string,analyzer=ik");
        sources.add("time");
        sources.add("type=date,format=epoch_millis");
        sources.add("ip");
        sources.add("type=string,index=not_analyzed");
        PutMappingRequest putMappingRequest=new PutMappingRequest(INDEX_NAME).type(TYPE_NAME).source(PutMappingRequest.buildFromSimplifiedDef(TYPE_NAME,sources.toArray()));
        PutMappingResponse putMappingResponse=elaticsearchClient.admin().indices().putMapping(putMappingRequest).get();
        return putMappingResponse;
    }
}
