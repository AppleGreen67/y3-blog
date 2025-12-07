package ru.ythree.blog.model;

import java.util.List;

public class SearchFilter {
    private List<String> tags;
    private String searchStr;

    public SearchFilter() {
    }

    public SearchFilter(List<String> tags, String searchStr) {
        this.tags = tags;
        this.searchStr = searchStr;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }
}
