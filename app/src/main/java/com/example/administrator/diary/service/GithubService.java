package com.example.administrator.diary.service;

import com.example.administrator.diary.model.*;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface GithubService {
    @GET("/seer")
    Observable<List<String>> getMusicName(@Query("music") String musicname);


    @GET("/base/fcgi-bin/fcg_music_express_mobile3.fcg")
    Observable<String> getRepos(@QueryMap Map<String, String> map);

    @POST("/sign")
    @FormUrlEncoded
    Observable<String> createCommit(@Field("name") String name, @Field("dic") String dic);

    /***
     *
     * @param name 用户名
     * @return  该用户的所有日志文件
     */
    @GET("/getdiary")
    Observable<List<Diarymodel>> getDiary(@Query("name") String name);

    /***
     *
     * @param name 用户名
     * @param dic  json格式的日志
     * @param opt   操作："delete"为删除 "add"为添加
     * @param token  验证的密文
     * @param pos    删除的日志的位置
     * @return  json格式的日志
     */
    @POST("/diary")
    @FormUrlEncoded
    Observable<Diarymodel> AddDiary(@Field("name") String name, @Field("dic") String dic,@Field("opt") String opt,@Field("token") String token,@Field("pos") int pos);


}



