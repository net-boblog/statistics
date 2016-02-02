package com.xiaoluo.statistics.service;

import com.xiaoluo.domain.DataPage;
import com.xiaoluo.statistics.dao.SearchTemplateDao;
import com.xiaoluo.statistics.dao.filter.SearchTemplateFilter;
import com.xiaoluo.statistics.entity.SearchTemplate;
import com.xiaoluo.statistics.exception.StatisticException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Caedmon on 2016/1/15.
 */
@Service
public class SearchTemplateService {
    @Autowired
    private SearchTemplateDao searchTemplateDao;
    public int update(SearchTemplate template){
        return searchTemplateDao.update(template);
    }
    public SearchTemplate insert(SearchTemplate template){
        return searchTemplateDao.insert(template);
    }
    public DataPage<SearchTemplate> find(SearchTemplateFilter filter){
        return searchTemplateDao.find(filter);
    }
    public SearchTemplate get(int id){
        return searchTemplateDao.find(id);
    }
    public int delete(int id){
        return searchTemplateDao.delete(id);
    }
}
