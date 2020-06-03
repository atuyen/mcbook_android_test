package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CategoryLeadBoadResponse {
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("rows")
    private List<CategoryLeadBoad> categoryLeadBoads = null;

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("rows")
    public List<CategoryLeadBoad> getCategoryLeadBoads() {
        return categoryLeadBoads;
    }

    @JsonProperty("rows")
    public void setCategoryLeadBoads(List<CategoryLeadBoad> rows) {
        this.categoryLeadBoads = rows;
    }
}
