package com.mc.models.gift;

public class HistoryInfoExam {

    private String time;

    private String date;

    private String point;

    public HistoryInfoExam() {
    }

    public HistoryInfoExam(String time, String date, String point) {
        this.time = time;
        this.date = date;
        this.point = point;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
