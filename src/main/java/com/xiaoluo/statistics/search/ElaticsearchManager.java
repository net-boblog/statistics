package com.xiaoluo.statistics.search;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * Created by Caedmon on 2015/12/24.
 */
public class ElaticsearchManager {
    private TransportClient elasticsearchClient;
    private String address;
    public ElaticsearchManager(String address){
        this.address=address;

    }
    public ElaticsearchManager(){
        this("localhost:9300");
    }
    @PostConstruct
    public void init(){
        elasticsearchClient= TransportClient.builder().build();
        for(String tmp:address.split(",")){
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
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
