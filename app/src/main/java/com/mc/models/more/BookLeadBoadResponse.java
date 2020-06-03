package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BookLeadBoadResponse {
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("rows")
    private List<BookLeadBoad> bookLeadBoads = null;

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("rows")
    public List<BookLeadBoad> getBookLeadBoads() {
        return bookLeadBoads;
    }

    @JsonProperty("rows")
    public void setBookLeadBoads(List<BookLeadBoad> rows) {
        this.bookLeadBoads = rows;
    }

}
