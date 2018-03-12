package com.example.administrator.diary.model;

/**
 * Created by Administrator on 2017/12/31 0031.
 */

public class UserDetail {
    private String id;
    private String name;
    private String password;
    private String token;
    private String sex;
    private String signature;
    private String head_url;
    private String area;
    private String contact;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getSex() {
        return sex;
    }

    public String getArea() {
        return area;
    }

    public String getContact() {
        return contact;
    }

    public String getHead_url() {
        return head_url;
    }

    public String getSignature() {
        return signature;
    }

    public String getToken() {
        return token;
    }
}
