package com.example.administrator.diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.example.administrator.diary.model.Diarymodel;
import com.example.administrator.diary.service.GithubService;

/***************************************************************************************
 **************************************************************************************/
public class MainActivity extends AppCompatActivity {
    ///////////////////////////////////////////////////
    RecyclerView mRcyclerView;//所有日志
    RecyclerView mRcyclerView2;//我的日志
    RecyclerView mRcyclerView3;//关心日志
    RecyclerView mRcyclerView4;//搜索结果
    List<Map<String,Object>> listItems;//所有日志清单
    List<Map<String,Object>> listItems2;//我的日志清单
    List<Map<String,Object>> listItems3;//关心日志清单
    List<Map<String,Object>> listItems4;//搜索清单
    cardAdapter cardAdapters;
    cardAdapter cardAdapters2;
    cardAdapter cardAdapters3;
    cardAdapter cardAdapters4;
    ImageView firstaddmy;
    TextView firstaddtext;
    ArrayList temlist = new ArrayList();
    SearchView mSearchView;
    int whichpage=1;//判断当前是那一页
    int frompage=1;//判断来自哪一页
    private GithubService githubService;
    SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    String[] allminds={"一般","难过","平静","愤怒","失望","开心"};
    //////////////////////列表切换///////////////////////////////////////////////////////////////////////////////////////
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //搜索列表清零
                    for(int temid=0;temid<listItems4.size();temid=temid+1){
                        listItems4.remove(0);
                    }
                    if(!listItems4.isEmpty()){
                        listItems4.remove(0);
                    }
                    cardAdapters4 = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems4) {
                        @Override
                        protected void convert(myViewHolder holder, Map<String, Object> o) {
                            TextView name = holder.getView(R.id.thename);
                            name.setText(o.get("name").toString());
                            TextView ids = holder.getView(R.id.texts);
                            ids.setText(o.get("texts").toString());
                            TextView blogs = holder.getView(R.id.dates);
                            blogs.setText(o.get("dates").toString());
                            TextView mymindss=holder.getView(R.id.minds);
                            mymindss.setText(o.get("minds").toString());
                            ImageView imgs = holder.getView(R.id.touxiang);
                            Glide.with(mcontext).load("http://oexlud4si.bkt.clouddn.com/"+o.get("name")+"_head_url").into(imgs);
                            ImageView publicimgs=holder.getView(R.id.mypictures);
                            Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(publicimgs);
                            TextView iffavs = holder.getView(R.id.iffavv);
                            iffavs.setText(o.get("iffav").toString());
                        }
                    };
                    mRcyclerView4.setAdapter(cardAdapters4);
                    mRcyclerView.setVisibility(View.VISIBLE);
                    mRcyclerView2.setVisibility(View.INVISIBLE);
                    mRcyclerView3.setVisibility(View.INVISIBLE);
                    mRcyclerView4.setVisibility(View.INVISIBLE);
                    mSearchView.setVisibility(View.VISIBLE);
                    firstaddtext.setVisibility(View.INVISIBLE);
                    firstaddmy.setVisibility(View.VISIBLE);
                    whichpage=1;//所有日志页
                    frompage=1;
                    return true;
                case R.id.navigation_dashboard:
                    //搜索列表清零
                    for(int temid=0;temid<listItems4.size();temid=temid+1){
                        listItems4.remove(0);
                    }
                    if(!listItems4.isEmpty()){
                        listItems4.remove(0);
                    }
                    cardAdapters4 = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems4) {
                        @Override
                        protected void convert(myViewHolder holder, Map<String, Object> o) {
                            TextView name = holder.getView(R.id.thename);
                            name.setText(o.get("name").toString());
                            TextView ids = holder.getView(R.id.texts);
                            ids.setText(o.get("texts").toString());
                            TextView blogs = holder.getView(R.id.dates);
                            blogs.setText(o.get("dates").toString());
                            TextView mymindss=holder.getView(R.id.minds);
                            mymindss.setText(o.get("minds").toString());
                            ImageView imgs = holder.getView(R.id.touxiang);
                            Glide.with(mcontext).load("http://oexlud4si.bkt.clouddn.com/"+o.get("name")+"_head_url").into(imgs);
                            ImageView publicimgs=holder.getView(R.id.mypictures);
                            Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(publicimgs);
                            TextView iffavs = holder.getView(R.id.iffavv);
                            iffavs.setText(o.get("iffav").toString());
                        }
                    };
                    mRcyclerView4.setAdapter(cardAdapters4);
                    mRcyclerView.setVisibility(View.INVISIBLE);
                    mRcyclerView2.setVisibility(View.VISIBLE);
                    mRcyclerView3.setVisibility(View.INVISIBLE);
                    mRcyclerView4.setVisibility(View.INVISIBLE);
                    mSearchView.setVisibility(View.VISIBLE);
                    firstaddmy.setVisibility(View.VISIBLE);
                    if(listItems2.size()==0){
                        firstaddtext.setVisibility(View.VISIBLE);
                    }
                    else firstaddtext.setVisibility(View.INVISIBLE);
                    whichpage=2;//显示我的日志页
                    frompage=2;//来自我的日志页
                    return true;
                case R.id.navigation_notifications:
                    //搜索列表清零
                    for(int temid=0;temid<listItems4.size();temid=temid+1){
                        listItems4.remove(0);
                    }
                    if(!listItems4.isEmpty()){
                        listItems4.remove(0);
                    }
                    cardAdapters4 = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems4) {
                        @Override
                        protected void convert(myViewHolder holder, Map<String, Object> o) {
                            TextView name = holder.getView(R.id.thename);
                            name.setText(o.get("name").toString());
                            TextView ids = holder.getView(R.id.texts);
                            ids.setText(o.get("texts").toString());
                            TextView blogs = holder.getView(R.id.dates);
                            blogs.setText(o.get("dates").toString());
                            TextView mymindss=holder.getView(R.id.minds);
                            mymindss.setText(o.get("minds").toString());
                            ImageView imgs = holder.getView(R.id.touxiang);
                            Glide.with(mcontext).load("http://oexlud4si.bkt.clouddn.com/"+o.get("name")+"_head_url").into(imgs);
                            ImageView publicimgs=holder.getView(R.id.mypictures);
                            Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(publicimgs);
                            TextView iffavs = holder.getView(R.id.iffavv);
                            iffavs.setText(o.get("iffav").toString());
                        }
                    };
                    mRcyclerView4.setAdapter(cardAdapters4);
                    mRcyclerView.setVisibility(View.INVISIBLE);
                    mRcyclerView2.setVisibility(View.INVISIBLE);
                    mRcyclerView3.setVisibility(View.VISIBLE);
                    mRcyclerView4.setVisibility(View.INVISIBLE);
                    mSearchView.setVisibility(View.VISIBLE);
                    firstaddtext.setVisibility(View.INVISIBLE);
                    firstaddmy.setVisibility(View.VISIBLE);
                    whichpage=3;//关心日志页
                    frompage=3;
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       //////////////////////////////////日志初始化/////////////////////////////////////////////
        mRcyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        mRcyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRcyclerView.setItemAnimator(new DefaultItemAnimator());//增加或删除条目动画
        mRcyclerView2=(RecyclerView)findViewById(R.id.recyclerview2);
        mRcyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mRcyclerView2.setItemAnimator(new DefaultItemAnimator());//增加或删除条目动画
        mRcyclerView3=(RecyclerView)findViewById(R.id.recyclerview3);
        mRcyclerView3.setLayoutManager(new LinearLayoutManager(this));
        mRcyclerView3.setItemAnimator(new DefaultItemAnimator());//增加或删除条目动画
        mRcyclerView4=(RecyclerView)findViewById(R.id.searchresult);
        mRcyclerView4.setLayoutManager(new LinearLayoutManager(this));
        mRcyclerView4.setItemAnimator(new DefaultItemAnimator());//增加或删除条目动画
        mRcyclerView4.setVisibility(View.INVISIBLE);
        mRcyclerView3.setVisibility(View.INVISIBLE);
        mRcyclerView2.setVisibility(View.INVISIBLE);
        listItems=new ArrayList<>();
        listItems2=new ArrayList<>();
        listItems3=new ArrayList<>();
        listItems4=new ArrayList<>();
        firstaddmy=(ImageView)findViewById(R.id.firstaddmy);
        firstaddtext=(TextView)findViewById(R.id.firstaddtext);
        firstaddtext.setVisibility(View.INVISIBLE);
        firstaddmy.setVisibility(View.VISIBLE);
        ////////////////////////////搜索
        mSearchView=(SearchView) findViewById(R.id.mysearchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//按下搜索
            @Override
            public boolean onQueryTextSubmit(String query) {
                //////////////////////////清空搜索列表///////////////////////////////
                for(int temid=0;temid<listItems4.size();temid=temid+1){
                    listItems4.remove(0);
                }
                if(!listItems4.isEmpty()){
                    listItems4.remove(0);
                }
                ///////////////////////////////////////
                if(frompage==1){
                    for(int temid=0;temid<listItems.size();temid=temid+1){
                        Map<String, Object> temnode=listItems.get(temid);
                        if(temnode.get("texts").toString().indexOf(query)>=0){
                            listItems4.add(temnode);
                            //Toast.makeText(MainActivity.this,temnode.get("texts").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    for(int temid=0;temid<listItems.size();temid++){
                        Map<String, Object> temnode=listItems.get(temid);
                        if(temnode.get("name").toString().indexOf(query)>=0){
                            listItems4.add(temnode);
                        }
                    }
                }
                else if(frompage==2){
                    for(int temid=0;temid<listItems2.size();temid=temid+1){
                        Map<String, Object> temnode=listItems2.get(temid);
                        if(temnode.get("texts").toString().indexOf(query)>=0){
                            listItems4.add(temnode);
                            //Toast.makeText(MainActivity.this,temnode.get("texts").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    for(int temid=0;temid<listItems2.size();temid++){
                        Map<String, Object> temnode=listItems2.get(temid);
                        if(temnode.get("name").toString().indexOf(query)>=0){
                            listItems4.add(temnode);
                        }
                    }
                }
                else {
                    for(int temid=0;temid<listItems3.size();temid=temid+1){
                        Map<String, Object> temnode=listItems3.get(temid);
                        if(temnode.get("texts").toString().indexOf(query)>=0){
                            listItems4.add(temnode);
                            //Toast.makeText(MainActivity.this,temnode.get("texts").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    for(int temid=0;temid<listItems3.size();temid++){
                        Map<String, Object> temnode=listItems3.get(temid);
                        if(temnode.get("name").toString().indexOf(query)>=0){
                            listItems4.add(temnode);
                        }
                    }
                }
                cardAdapters4 = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems4) {
                    @Override
                    protected void convert(myViewHolder holder, Map<String, Object> o) {
                        TextView name = holder.getView(R.id.thename);
                        name.setText(o.get("name").toString());
                        TextView ids = holder.getView(R.id.texts);
                        ids.setText(o.get("texts").toString());
                        TextView blogs = holder.getView(R.id.dates);
                        blogs.setText(o.get("dates").toString());
                        TextView mymindss=holder.getView(R.id.minds);
                        mymindss.setText(o.get("minds").toString());
                        ImageView imgs = holder.getView(R.id.touxiang);
                        Glide.with(mcontext).load("http://oexlud4si.bkt.clouddn.com/"+o.get("name")+"_head_url").into(imgs);
                        ImageView publicimgs=holder.getView(R.id.mypictures);
                        Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(publicimgs);
                        TextView iffavs = holder.getView(R.id.iffavv);
                        iffavs.setText(o.get("iffav").toString());
                        //Glide.with(MainActivity.this).load(github.getAvatar_url().toString()).placeholder(R.drawable.loading).error(R.mipmap.ic_launcher).into(imgs);
                    }
                };
                mSearchView.setIconified(true);
                cardAdapters4.setOnItemClickListener(new cardAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.dialoglayout,
                                (ViewGroup) findViewById(R.id.dialoglayout));
                        final ImageView tembutton = (ImageView) layout.findViewById(R.id.favourite);
                        final ImageView datouxiang = (ImageView) layout.findViewById(R.id.datouxiang);
                        final TextView manminds=(TextView)layout.findViewById(R.id.manminds);
                        final TextView mypubtext=(TextView)layout.findViewById(R.id.inputtexts);
                        mypubtext.setText(listItems4.get(position).get("texts").toString());
                        final ImageView mypubimg=(ImageView)layout.findViewById(R.id.justimg);
                        Glide.with(MainActivity.this).load(listItems4.get(position).get("Avatar_url").toString()).into(mypubimg);
                        manminds.setText(listItems4.get(position).get("minds").toString());
                        Glide.with(MainActivity.this).load("http://oexlud4si.bkt.clouddn.com/"+listItems4.get(position).get("name")+"_head_url").into(datouxiang);
                        tembutton.setImageResource(R.drawable.xin);
                        AlertDialog.Builder alterimg = new AlertDialog.Builder(MainActivity.this);
                        AlertDialog alterimg2 = alterimg.create();
                        alterimg2.setView(layout);
                        alterimg2.show();
                        //改变弹框大小
                        WindowManager.LayoutParams params = alterimg2.getWindow().getAttributes();
                        params.width = 1000;
                        params.height = 1100;
                        alterimg2.getWindow().setAttributes(params);
                    }
                    @Override
                    public void onLongClick(int position) {
                    }
                });
                mRcyclerView4.setAdapter(cardAdapters4);
                ///////////////////////////////////////////////////////////
                mRcyclerView4.setVisibility(View.VISIBLE);
                mRcyclerView.setVisibility(View.INVISIBLE);
                mRcyclerView2.setVisibility(View.INVISIBLE);
                mRcyclerView3.setVisibility(View.INVISIBLE);
                firstaddtext.setVisibility(View.INVISIBLE);
                firstaddmy.setVisibility(View.INVISIBLE);
                whichpage=4;
                return true;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if(whichpage==1){
                    if (!TextUtils.isEmpty(newText)){
                        mRcyclerView4.setVisibility(View.VISIBLE);
                        mRcyclerView.setVisibility(View.INVISIBLE);
                        mRcyclerView2.setVisibility(View.INVISIBLE);
                        mRcyclerView3.setVisibility(View.INVISIBLE);
                        navigation.setVisibility(View.INVISIBLE);

                    }else{
                        mRcyclerView4.setVisibility(View.INVISIBLE);
                        mRcyclerView.setVisibility(View.VISIBLE);
                        mRcyclerView2.setVisibility(View.INVISIBLE);
                        mRcyclerView3.setVisibility(View.INVISIBLE);
                        navigation.setVisibility(View.VISIBLE);
                    }
                }
                else if(whichpage==2){
                    if (!TextUtils.isEmpty(newText)){
                        mRcyclerView4.setVisibility(View.VISIBLE);
                        mRcyclerView.setVisibility(View.INVISIBLE);
                        mRcyclerView2.setVisibility(View.INVISIBLE);
                        mRcyclerView3.setVisibility(View.INVISIBLE);
                        firstaddtext.setVisibility(View.INVISIBLE);
                        firstaddmy.setVisibility(View.VISIBLE);
                        navigation.setVisibility(View.INVISIBLE);

                    }else{
                        mRcyclerView4.setVisibility(View.INVISIBLE);
                        mRcyclerView.setVisibility(View.INVISIBLE);
                        mRcyclerView2.setVisibility(View.VISIBLE);
                        mRcyclerView3.setVisibility(View.INVISIBLE);
                        if(listItems2.isEmpty()){
                            firstaddtext.setVisibility(View.VISIBLE);
                            firstaddmy.setVisibility(View.VISIBLE);
                        }
                        else {
                            firstaddtext.setVisibility(View.INVISIBLE);
                            firstaddmy.setVisibility(View.VISIBLE);
                        }
                        navigation.setVisibility(View.VISIBLE);
                    }
                }
                else if(whichpage==3){
                    if (!TextUtils.isEmpty(newText)){
                        mRcyclerView4.setVisibility(View.VISIBLE);
                        mRcyclerView.setVisibility(View.INVISIBLE);
                        mRcyclerView2.setVisibility(View.INVISIBLE);
                        mRcyclerView3.setVisibility(View.INVISIBLE);
                        navigation.setVisibility(View.INVISIBLE);

                    }else{
                        mRcyclerView4.setVisibility(View.INVISIBLE);
                        mRcyclerView.setVisibility(View.INVISIBLE);
                        mRcyclerView2.setVisibility(View.INVISIBLE);
                        mRcyclerView3.setVisibility(View.VISIBLE);
                        navigation.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    if (!TextUtils.isEmpty(newText)){
                        mRcyclerView4.setVisibility(View.VISIBLE);
                        mRcyclerView.setVisibility(View.INVISIBLE);
                        mRcyclerView2.setVisibility(View.INVISIBLE);
                        mRcyclerView3.setVisibility(View.INVISIBLE);
                        navigation.setVisibility(View.INVISIBLE);

                    }else{
                        mRcyclerView4.setVisibility(View.VISIBLE);
                        mRcyclerView.setVisibility(View.INVISIBLE);
                        mRcyclerView2.setVisibility(View.INVISIBLE);
                        mRcyclerView3.setVisibility(View.INVISIBLE);
                        navigation.setVisibility(View.VISIBLE);
                    }
                }

                return true;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(frompage==1){
                    //搜索列表清零
                    for(int temid=0;temid<listItems4.size();temid=temid+1){
                        listItems4.remove(0);
                    }
                    if(!listItems4.isEmpty()){
                        listItems4.remove(0);
                    }
                    cardAdapters4 = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems4) {
                        @Override
                        protected void convert(myViewHolder holder, Map<String, Object> o) {
                            TextView name = holder.getView(R.id.thename);
                            name.setText(o.get("name").toString());
                            TextView ids = holder.getView(R.id.texts);
                            ids.setText(o.get("texts").toString());
                            TextView blogs = holder.getView(R.id.dates);
                            blogs.setText(o.get("dates").toString());
                            TextView mymindss=holder.getView(R.id.minds);
                            mymindss.setText(o.get("minds").toString());
                            ImageView imgs = holder.getView(R.id.touxiang);
                            Glide.with(mcontext).load("http://oexlud4si.bkt.clouddn.com/"+o.get("name")+"_head_url").into(imgs);
                            ImageView publicimgs=holder.getView(R.id.mypictures);
                            Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(publicimgs);
                            TextView iffavs = holder.getView(R.id.iffavv);
                            iffavs.setText(o.get("iffav").toString());
                        }
                    };
                    mRcyclerView4.setAdapter(cardAdapters4);
                    mRcyclerView.setVisibility(View.VISIBLE);
                    mRcyclerView2.setVisibility(View.INVISIBLE);
                    mRcyclerView3.setVisibility(View.INVISIBLE);
                    mRcyclerView4.setVisibility(View.INVISIBLE);
                    mSearchView.clearFocus();
                }
                else if(frompage==2){
                    //搜索列表清零
                    for(int temid=0;temid<listItems4.size();temid=temid+1){
                        listItems4.remove(0);
                    }
                    if(!listItems4.isEmpty()){
                        listItems4.remove(0);
                    }
                    cardAdapters4 = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems4) {
                        @Override
                        protected void convert(myViewHolder holder, Map<String, Object> o) {
                            TextView name = holder.getView(R.id.thename);
                            name.setText(o.get("name").toString());
                            TextView ids = holder.getView(R.id.texts);
                            ids.setText(o.get("texts").toString());
                            TextView blogs = holder.getView(R.id.dates);
                            blogs.setText(o.get("dates").toString());
                            TextView mymindss=holder.getView(R.id.minds);
                            mymindss.setText(o.get("minds").toString());
                            ImageView imgs = holder.getView(R.id.touxiang);
                            Glide.with(mcontext).load("http://oexlud4si.bkt.clouddn.com/"+o.get("name")+"_head_url").into(imgs);
                            ImageView publicimgs=holder.getView(R.id.mypictures);
                            Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(publicimgs);
                            TextView iffavs = holder.getView(R.id.iffavv);
                            iffavs.setText(o.get("iffav").toString());
                        }
                    };
                    mRcyclerView4.setAdapter(cardAdapters4);
                    mRcyclerView.setVisibility(View.INVISIBLE);
                    mRcyclerView2.setVisibility(View.VISIBLE);
                    mRcyclerView3.setVisibility(View.INVISIBLE);
                    mRcyclerView4.setVisibility(View.INVISIBLE);
                }
                else {
                    //搜索列表清零
                    for(int temid=0;temid<listItems4.size();temid=temid+1){
                        listItems4.remove(0);
                    }
                    if(!listItems4.isEmpty()){
                        listItems4.remove(0);
                    }
                    cardAdapters4 = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems4) {
                        @Override
                        protected void convert(myViewHolder holder, Map<String, Object> o) {
                            TextView name = holder.getView(R.id.thename);
                            name.setText(o.get("name").toString());
                            TextView ids = holder.getView(R.id.texts);
                            ids.setText(o.get("texts").toString());
                            TextView blogs = holder.getView(R.id.dates);
                            blogs.setText(o.get("dates").toString());
                            TextView mymindss=holder.getView(R.id.minds);
                            mymindss.setText(o.get("minds").toString());
                            ImageView imgs = holder.getView(R.id.touxiang);
                            Glide.with(mcontext).load("http://oexlud4si.bkt.clouddn.com/"+o.get("name")+"_head_url").into(imgs);
                            ImageView publicimgs=holder.getView(R.id.mypictures);
                            Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(publicimgs);
                            TextView iffavs = holder.getView(R.id.iffavv);
                            iffavs.setText(o.get("iffav").toString());
                        }
                    };
                    mRcyclerView4.setAdapter(cardAdapters4);
                    mRcyclerView.setVisibility(View.INVISIBLE);
                    mRcyclerView2.setVisibility(View.INVISIBLE);
                    mRcyclerView3.setVisibility(View.VISIBLE);
                    mRcyclerView4.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

        //////////////////////测试///////////////////////////////////////////////////////////////////////////////////////////


        //创建Retrofit对象
        Retrofit GithubRetrofit = ServiceFactory.createRetrofit("http://zllink.applinzi.com/");
        githubService = GithubRetrofit.create(GithubService.class);

        //创建访问接口并调用接口函数获取数据
        githubService.getDiary("Me")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Diarymodel>>() {
                    @Override
                    public void onCompleted() {
                        //Toast.makeText(MainActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this,e.getMessage()+"请确认搜索的用户存在", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(final List<Diarymodel> diarymodelList) {
                        //Toast.makeText(MainActivity.this, github.getLogin().toString(), Toast.LENGTH_SHORT).show();
                        for(Diarymodel diarymodel:diarymodelList){
                            Map<String, Object> tmp = new LinkedHashMap<>();
                            tmp.put("name", diarymodel.getName());
                            tmp.put("texts", diarymodel.getDiary_text());
                            tmp.put("dates", diarymodel.getDate());
                            tmp.put("Avatar_url", diarymodel.getPicurl1());
                            tmp.put("iffav", 2);
                            tmp.put("minds",allminds[ Integer.parseInt(diarymodel.getFeeling())]);
                            //Toast.makeText(MainActivity.this,diarymodel.getPicurl1(), Toast.LENGTH_SHORT).show();
                            listItems.add(tmp);
                        }
                        //            }
                        //       });
                        /////////////////////////////加入所有日志列表///////////////////////////////////
                        cardAdapters = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems) {
                            @Override
                            protected void convert(myViewHolder holder, Map<String, Object> o) {
                                TextView name = holder.getView(R.id.thename);
                                name.setText(o.get("name").toString());
                                TextView ids = holder.getView(R.id.texts);
                                ids.setText(o.get("texts").toString());
                                TextView blogs = holder.getView(R.id.dates);
                                blogs.setText(o.get("dates").toString());
                                TextView mymindss=holder.getView(R.id.minds);
                                mymindss.setText(o.get("minds").toString());
                                ImageView imgs = holder.getView(R.id.touxiang);
                                Glide.with(mcontext).load("http://oexlud4si.bkt.clouddn.com/"+o.get("name")+"_head_url").into(imgs);
                                ImageView publicimgs=holder.getView(R.id.mypictures);
                                Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(publicimgs);
                                TextView iffavs = holder.getView(R.id.iffavv);
                                iffavs.setText(o.get("iffav").toString());
                            }
                        };
                        /////////////////////////////查看详情///////////////////////////////////
                        cardAdapters.setOnItemClickListener(new cardAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(final int position) {
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.dialoglayout,
                                        (ViewGroup) findViewById(R.id.dialoglayout));
                                final ImageView tembutton = (ImageView) layout.findViewById(R.id.favourite);
                                final ImageView datouxiang = (ImageView) layout.findViewById(R.id.datouxiang);
                                final TextView manminds=(TextView)layout.findViewById(R.id.manminds);
                                final TextView mypubtext=(TextView)layout.findViewById(R.id.inputtexts);
                                mypubtext.setText(listItems.get(position).get("texts").toString());
                                final ImageView mypubimg=(ImageView)layout.findViewById(R.id.justimg);
                                Glide.with(MainActivity.this).load(listItems.get(position).get("Avatar_url").toString()).into(mypubimg);
                                manminds.setText(listItems.get(position).get("minds").toString());
                                Glide.with(MainActivity.this).load("http://oexlud4si.bkt.clouddn.com/"+listItems.get(position).get("name")+"_head_url").into(datouxiang);
                                if (listItems.get(position).get("iffav").toString().equals("1")) {
                                    tembutton.setImageResource(R.drawable.xin2);
                                } else {
                                    tembutton.setImageResource(R.drawable.xin);
                                }
                                AlertDialog.Builder alterimg = new AlertDialog.Builder(MainActivity.this);
                                AlertDialog alterimg2 = alterimg.create();
                                alterimg2.setView(layout);
                                alterimg2.show();
                                //改变弹框大小
                                WindowManager.LayoutParams params = alterimg2.getWindow().getAttributes();
                                params.width = 1000;
                                params.height = 1100;
                                alterimg2.getWindow().setAttributes(params);
                                /////////////////////////////添加到关心///////////////////////////////////
                                tembutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (listItems.get(position).get("iffav").toString().equals("1")) {
                                            tembutton.setImageResource(R.drawable.xin);
                                        }
                                        Toast.makeText(MainActivity.this, "已添加到关心日志", Toast.LENGTH_SHORT).show();
                                        listItems.get(position).put("iffav", "2");
                                        Map<String, Object> temitem = listItems.get(position);
                                        Map<String, Object> favadd = new LinkedHashMap<>();
                                        favadd.put("name", temitem.get("name").toString());
                                        favadd.put("texts", temitem.get("texts").toString());
                                        favadd.put("dates", temitem.get("dates").toString());
                                        favadd.put("Avatar_url", temitem.get("Avatar_url").toString());
                                        favadd.put("iffav", temitem.get("iffav").toString());
                                        favadd.put("minds",temitem.get("minds"));
                                        listItems3.add(favadd);
                                        /////////////////////////////加入关心日志列表///////////////////////////////////
                                        cardAdapters3 = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems3) {
                                            @Override
                                            protected void convert(myViewHolder holder, Map<String, Object> o) {
                                                TextView name = holder.getView(R.id.thename);
                                                name.setText(o.get("name").toString());
                                                TextView ids = holder.getView(R.id.texts);
                                                ids.setText(o.get("texts").toString());
                                                TextView blogs = holder.getView(R.id.dates);
                                                blogs.setText(o.get("dates").toString());
                                                TextView mymindss=holder.getView(R.id.minds);
                                                mymindss.setText(o.get("minds").toString());
                                                ImageView imgs = holder.getView(R.id.touxiang);
                                                Glide.with(mcontext).load("http://oexlud4si.bkt.clouddn.com/"+o.get("name")+"_head_url").into(imgs);
                                                ImageView publicimgs=holder.getView(R.id.mypictures);
                                                Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(publicimgs);
                                                //Glide.with(MainActivity.this).load(github.getAvatar_url().toString()).placeholder(R.drawable.loading).error(R.mipmap.ic_launcher).into(imgs);
                                            }
                                        };
                                        cardAdapters3.setOnItemClickListener(new cardAdapter.OnItemClickListener() {
                                            @Override
                                            public void onClick(int position) {
                                                LayoutInflater inflater = getLayoutInflater();
                                                View layout = inflater.inflate(R.layout.dialoglayout,
                                                        (ViewGroup) findViewById(R.id.dialoglayout));
                                                final ImageView tembutton = (ImageView) layout.findViewById(R.id.favourite);
                                                final ImageView datouxiang = (ImageView) layout.findViewById(R.id.datouxiang);
                                                final TextView manminds=(TextView)layout.findViewById(R.id.manminds);
                                                final TextView mypubtext=(TextView)layout.findViewById(R.id.inputtexts);
                                                mypubtext.setText(listItems3.get(position).get("texts").toString());
                                                final ImageView mypubimg=(ImageView)layout.findViewById(R.id.justimg);
                                                Glide.with(MainActivity.this).load(listItems3.get(position).get("Avatar_url").toString()).into(mypubimg);
                                                manminds.setText(listItems3.get(position).get("minds").toString());
                                                Glide.with(MainActivity.this).load("http://oexlud4si.bkt.clouddn.com/"+listItems3.get(position).get("name")+"_head_url").into(datouxiang);
                                                tembutton.setImageResource(R.drawable.xin);
                                                AlertDialog.Builder alterimg = new AlertDialog.Builder(MainActivity.this);
                                                AlertDialog alterimg2 = alterimg.create();
                                                alterimg2.setView(layout);
                                                alterimg2.show();
                                                //改变弹框大小
                                                WindowManager.LayoutParams params = alterimg2.getWindow().getAttributes();
                                                params.width = 1000;
                                                params.height = 1100;
                                                alterimg2.getWindow().setAttributes(params);
                                            }

                                            @Override
                                            public void onLongClick(int position) {
                                                cardAdapters3.Remove(position);
                                            }
                                        });
                                        mRcyclerView3.setAdapter(cardAdapters3);
                                        mRcyclerView3.setVisibility(View.INVISIBLE);

                                    }
                                });
                            }

                            @Override
                            public void onLongClick(int position) {
                                cardAdapters.Remove(position);
                            }
                        });
                        mRcyclerView.setAdapter(cardAdapters);
                        cardAdapters.notifyDataSetChanged();

                        ///////////////////
                    }
                    });
            //////////////////

       //////////////////////////////////////发布日志部分
        firstaddmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent =new Intent(MainActivity.this,addnewtext.class);
                startActivity(myintent);
            }
        });
        ///////////////////////////////////接收自己发布的日志
        final String getminds=getIntent().getStringExtra("minds");
        final String gettexts=getIntent().getStringExtra("texts");
        final String getimagepath=getIntent().getStringExtra("imagepath");
        if(gettexts!=null){
            Map<String, Object> mytextlist = new LinkedHashMap<>();
            mytextlist.put("name", "用户名");
            mytextlist.put("texts", gettexts);
            mytextlist.put("dates", "12-21");
            mytextlist.put("Avatar_url", getimagepath);
            mytextlist.put("iffav", "1");
            mytextlist.put("minds",allminds[Integer.parseInt(getminds)]);
            listItems2.add(mytextlist);
            cardAdapters2 = new cardAdapter<Map<String, Object>>(MainActivity.this, R.layout.cardview, listItems2) {
                @Override
                protected void convert(myViewHolder holder, Map<String, Object> o) {
                    TextView name = holder.getView(R.id.thename);
                    name.setText(o.get("name").toString());
                    TextView ids = holder.getView(R.id.texts);
                    ids.setText(o.get("texts").toString());
                    TextView blogs = holder.getView(R.id.dates);
                    blogs.setText(o.get("dates").toString());
                    TextView mymindss=holder.getView(R.id.minds);
                    mymindss.setText(o.get("minds").toString());
                    ImageView imgs = holder.getView(R.id.touxiang);
                    Uri uris= Uri.parse(o.get("Avatar_url").toString());
                    Glide.with(mcontext).load(o.get("Avatar_url").toString()).into(imgs);
                    imgs.setImageURI(uris);
                    //Glide.with(MainActivity.this).load(github.getAvatar_url().toString()).placeholder(R.drawable.loading).error(R.mipmap.ic_launcher).into(imgs);
                }
            };
            cardAdapters2.setOnItemClickListener(new cardAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialoglayout,
                            (ViewGroup) findViewById(R.id.dialoglayout));
                    final ImageView tembutton = (ImageView) layout.findViewById(R.id.favourite);
                    final ImageView datouxiang = (ImageView) layout.findViewById(R.id.datouxiang);
                    final TextView manminds=(TextView)layout.findViewById(R.id.manminds);
                    manminds.setText(listItems2.get(position).get("minds").toString());
                    Glide.with(MainActivity.this).load(listItems2.get(position).get("Avatar_url").toString()).into(datouxiang);
                    final TextView mypubtext=(TextView)layout.findViewById(R.id.inputtexts);
                    mypubtext.setText(listItems2.get(position).get("texts").toString());

                    tembutton.setImageResource(R.drawable.xin);
                    AlertDialog.Builder alterimg = new AlertDialog.Builder(MainActivity.this);
                    AlertDialog alterimg2 = alterimg.create();
                    alterimg2.setView(layout);
                    alterimg2.show();
                    //改变弹框大小
                    WindowManager.LayoutParams params = alterimg2.getWindow().getAttributes();
                    params.width = 1000;
                    params.height = 1100;
                    alterimg2.getWindow().setAttributes(params);
                }

                @Override
                public void onLongClick(int position) {
                    cardAdapters2.Remove(position);
                }
            });


            mRcyclerView2.setAdapter(cardAdapters2);
        }
        //////////////////////////////下拉刷新
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新获取数据
                //获取完成
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
    //////////////////////////////////////////配置OkHttp对象
    private static OkHttpClient createOkHttp(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//链接超时
                .readTimeout(30,TimeUnit.SECONDS)//读超时
                .writeTimeout(10,TimeUnit.SECONDS)//写超时
                .build();
        return okHttpClient;
    }
    ////////////////////////////////////////构建Retrofit对象进行网络访问
    private static Retrofit createRetrofit(String baseulr){
        return new Retrofit.Builder()
                .baseUrl(baseulr)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }
    /////////////////////////////////////////清空搜索列表
}
