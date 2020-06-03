package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListFriendResponse {
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("rows")
    private List<Friend> listFriendList = null;

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("rows")
    public List<Friend> getListFriendList() {
        return listFriendList;
    }

    @JsonProperty("rows")
    public void setListFriendList(List<Friend> rows) {
        this.listFriendList = rows;
    }

}
