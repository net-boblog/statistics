package com.xiaoluo.statistics.entity;

import com.xiaoluo.statistics.search.SearchParams;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class FunnelSearch {
    private List<SearchParams> searchParamses;

    public List<SearchParams> getSearchParamses() {
        return searchParamses;
    }

    public void setSearchParamses(List<SearchParams> searchParamses) {
        this.searchParamses = searchParamses;
    }
}
