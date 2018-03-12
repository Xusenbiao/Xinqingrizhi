package com.example.administrator.diary.model;

/**
 * Created by Administrator on 2018/1/1 0001.
 */

public class Diarymodel {
    private String diary_id;
    private String name;
    private String diary_text;
    private String feeling;
    private String date;
    private String picurl;
    private String picurl1;
    private String id;
    private String up_id;
    private String is_public;

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDiary_id() {
        return diary_id;
    }

    public String getDiary_text() {
        return diary_text;
    }

    public String getFeeling() {
        return feeling;
    }

    public String getId() {
        return id;
    }

    public String getIs_public() {
        return is_public;
    }

    public String getPicurl() {
        return picurl;
    }
    public String getPicurl1() {
        return picurl1;
    }

    public String getUp_id() {
        return up_id;
    }
}
