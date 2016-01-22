package com.xiaoluo.statistics.vo;

import java.util.List;
import java.util.Map;

/**
 * Created by Caedmon on 2016/1/19.
 */
public class TotalStatResult {
    private List<SearchStatResult> sectionStatResults;
    private Map<String,List<SearchStatResult.TermsResult>> termsResultsMap;
    private SearchStatResult totalStatResult;

    public List<SearchStatResult> getSectionStatResults() {
        return sectionStatResults;
    }

    public void setSectionStatResults(List<SearchStatResult> sectionStatResults) {
        this.sectionStatResults = sectionStatResults;
    }

    public Map<String, List<SearchStatResult.TermsResult>> getTermsResultsMap() {
        return termsResultsMap;
    }

    public void setTermsResultsMap(Map<String, List<SearchStatResult.TermsResult>> termsResultsMap) {
        this.termsResultsMap = termsResultsMap;
    }

    public SearchStatResult getTotalStatResult() {
        return totalStatResult;
    }

    public void setTotalStatResult(SearchStatResult totalStatResult) {
        this.totalStatResult = totalStatResult;
    }
}
