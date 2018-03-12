package com.example.administrator.diary.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Musurl {
    @SerializedName("data")
    private Mydata data;

    private String errinfo;

    public Mydata getData() {
        return data;
    }

    public String getCode() {
        return errinfo;
    }

    public  class Mydata{
        private List<MyItem> items;
        public  List<MyItem> getItem(){
            return items;
        }
    }
    public  class MyItem{
        private String vkey;
        public String getVkey(){
            return vkey;
        }
    }
}


