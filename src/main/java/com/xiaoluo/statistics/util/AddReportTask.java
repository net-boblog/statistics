package com.xiaoluo.statistics.util;

import com.alibaba.fastjson.JSON;
import com.xiaoluo.statistics.service.ActionReportService;
import com.xiaoluo.statistics.vo.ActionReport;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/12/24.
 */
public class AddReportTask implements Runnable{
    private ActionReport actionReport;
    private Client client;
    private static final Logger log= LoggerFactory.getLogger(AddReportTask.class);
    public AddReportTask(Client client, ActionReport actionReport){
        this.actionReport = actionReport;
        this.client=client;
    }
    @Override
    public void run() {
        try{
            IndexResponse response=client.prepareIndex(ActionReportService.INDEX_NAME, ActionReportService.TYPE_NAME).setSource(JSON.toJSONString(actionReport)).execute().get();
            log.debug("Add report log result :{}",response.toString());
        }catch (Throwable e){
            e.printStackTrace();
            log.error("Add report log error ",e);
        }

    }
    public static void main(String[] args) throws  Exception{
        String[] channels=new String[]{"baidu","360","wandoujia","tencent","wangyi","appstore","itools"};
        String[] ips=new String[]{"127.0.0.1","10.10.87.18","112.114.113.1","180.150.186.187","111.113.112.110"};
        String[] prefixPages=new String[]{"prefixPage1","prefixPage2","prefixPage3","prefixPage4","prefixPage5","prefixPage6","prefixPage7","prefixPage8","prefixPage9"};
        String[] events=new String[]{"event1","event2","event3","event4","event5","event6","event7","event8","event9","event10"};
        String[] uids=new String[1000];
        for(int i=0;i<uids.length;i++){
            uids[i]="user"+i;
        }
        String[] terminals=new String[]{"andriod","ios","wechat","pc"};
        ExecutorService threadPool= Executors.newFixedThreadPool(50);
        TransportClient client=TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("localhost", 9300)));
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<50000;i++){
            ActionReport report=new ActionReport();
            Random random=new Random();
            report.setPrefixPage(prefixPages[random.nextInt(prefixPages.length)]);
            report.setUid(uids[random.nextInt(uids.length)]);
            report.setChannel(channels[random.nextInt(channels.length)]);
            report.setIp(ips[random.nextInt(ips.length)]);
            report.setEvent(events[random.nextInt(events.length)]);
            long startTime=DateKit.YYYY_MM_DD_HH_MM_SS_FORMAT.parse("2015-12-12 00:00:00").getTime();
            long endTime=System.currentTimeMillis();
            long time=new BigDecimal((endTime - startTime)*Math.random()).toBigInteger().longValue()+startTime;
            report.setTime(time);
            report.setIp(ips[random.nextInt(ips.length)]);
            report.setTerminal(terminals[random.nextInt(terminals.length)]);
            AddReportTask task=new AddReportTask(client,report);
            threadPool.execute(task);
        }
    }
}
