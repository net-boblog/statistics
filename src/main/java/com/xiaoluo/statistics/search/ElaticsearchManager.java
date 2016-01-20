package com.xiaoluo.statistics.search;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * Created by Caedmon on 2015/12/24.
 */
@Component
public class ElaticsearchManager {
    private TransportClient elasticsearchClient;
    @Autowired
    private String elasticAddress;
    @PostConstruct
    public void init(){
        elasticsearchClient= TransportClient.builder().build();
        for(String tmp:elasticAddress.split(",")){
            String[] arr=tmp.split(":");
            String host=arr[0];
            int port=Integer.parseInt(arr[1]);
            elasticsearchClient.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(host,port)));
        }
    }
    public TransportClient getElasticsearchClient(){
        return elasticsearchClient;
    }

    public void setElasticsearchClient(TransportClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public String getAddress() {
        return elasticAddress;
    }

    public void setAddress(String address) {
        this.elasticAddress = address;
    }
}
