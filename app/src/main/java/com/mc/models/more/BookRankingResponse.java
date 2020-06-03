package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BookRankingResponse {
    @JsonProperty("rows")
    private List<BookRanking> bookRankings = null;


    public List<BookRanking> getBookRankings() {
        return bookRankings;
    }

    public void setBookRankings(List<BookRanking> bookRankings) {
        this.bookRankings = bookRankings;
    }

    @Override
    public String toString() {
        return "BookRankingResponse{" +
                "bookRankings=" + bookRankings +
                '}';
    }
}
