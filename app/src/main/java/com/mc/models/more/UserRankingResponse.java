package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserRankingResponse {
    @JsonProperty("rows")
    private List<UserRanking> members = null;

    public List<UserRanking> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "UserRankingResponse{" +
                "members=" + members +
                '}';
    }

    public void setMembers(List<UserRanking> members) {
        this.members = members;
    }
}
