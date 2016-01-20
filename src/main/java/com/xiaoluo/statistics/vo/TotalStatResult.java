package com.xiaoluo.statistics.vo;

import java.util.List;

/**
 * Created by Caedmon on 2016/1/19.
 */
public class TotalStatResult {
    private List<SearchStatResult> sectionStatResults;
    private List<SearchStatResult.TermsResult> termsResults;
    private SearchStatResult totalStatResult;
    private String termsCountFiled;
    public List<SearchStatResult> getSectionStatResults() {
        return sectionStatResults;
    }

    public void setSectionStatResults(List<SearchStatResult> sectionStatResults) {
        this.sectionStatResults = sectionStatResults;
    }

    public List<SearchStatResult.TermsResult> getTermsResults() {
        return termsResults;
    }

    public void setTermsResults(List<SearchStatResult.TermsResult> termsResults) {
        this.termsResults = termsResults;
    }

    public SearchStatResult getTotalStatResult() {
        return totalStatResult;
    }

    public void setTotalStatResult(SearchStatResult totalStatResult) {
        this.totalStatResult = totalStatResult;
    }

    public String getTermsCountFiled() {
        return termsCountFiled;
    }

    public void setTermsCountFiled(String termsCountFiled) {
        this.termsCountFiled = termsCountFiled;
    }
}
