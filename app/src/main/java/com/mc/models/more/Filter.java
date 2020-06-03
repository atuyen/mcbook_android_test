package com.mc.models.more;

public class Filter {
    private String category;
    private String filterBook;
    private String filterTime;
    private String filterUser;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilterBook() {
        return filterBook;
    }

    public void setFilterBook(String filterBook) {
        this.filterBook = filterBook;
    }

    public String getFilterTime() {
        return filterTime;
    }

    public void setFilterTime(String filterTime) {
        this.filterTime = filterTime;
    }

    public String getFilterUser() {
        return filterUser;
    }

    public void setFilterUser(String filterUser) {
        this.filterUser = filterUser;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "category='" + category + '\'' +
                ", filterBook='" + filterBook + '\'' +
                ", filterTime='" + filterTime + '\'' +
                ", filterUser='" + filterUser + '\'' +
                '}';
    }
}
