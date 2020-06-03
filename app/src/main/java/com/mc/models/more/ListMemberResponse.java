package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListMemberResponse {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("rows")
    private List<Members> memberList = null;

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("rows")
    public List<Members> getMemberList() {
        return memberList;
    }

    @JsonProperty("rows")
    public void setMemberList(List<Members> rows) {
        this.memberList = rows;
    }
}
