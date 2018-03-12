package com.example.administrator.diary;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diary.model.Diarymodel;
import com.example.administrator.diary.service.GithubService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lenovo on 2017/12/30.
 */

public class addnewtext extends AppCompatActivity {
    ImageView sadminds;
    ImageView peaceminds;
    ImageView angerminds;
    ImageView disapointminds;
    ImageView happyminds;
    EditText inouttext;
    ImageView addimgs1;
    ImageView addimgs2;
    ImageView addimgs3;
    Button publishtext;
    int whichimags=0;
    int whichminds=0;//0不可描述，1难过，2平静，3愤怒，4失望，5开心
    String imgpaths="";

    ///////////////////////获取手机图片
    private TextView imgPath = null;//获取到图片的路径的TextView
    private final int IMAGE_CODE = 0;
    //Uri bitMapUri = null;
    private final String IMAGE_TYPE = "image/*";

    //////////////////////
    private GithubService githubService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialoglayout2);
        sadminds=(ImageView)findViewById(R.id.sads);
        peaceminds=(ImageView)findViewById(R.id.innerpeaces);
        angerminds=(ImageView)findViewById(R.id.angers);
        disapointminds=(ImageView)findViewById(R.id.dispoints);
        happyminds=(ImageView)findViewById(R.id.happys);
        inouttext=(EditText)findViewById(R.id.myinputss);
        publishtext=(Button)findViewById(R.id.publishs);
        addimgs1=(ImageView)findViewById(R.id.firstimg);
        //////////////////////////////////选择心情
        sadminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imagid=R.drawable.xin;
                int imagid2=R.drawable.xin2;
                sadminds.setImageResource(imagid);
                peaceminds.setImageResource(imagid2);
                angerminds.setImageResource(imagid2);
                disapointminds.setImageResource(imagid2);
                happyminds.setImageResource(imagid2);
                whichminds=1;
            }
        });
        peaceminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imagid=R.drawable.xin;
                int imagid2=R.drawable.xin2;
                sadminds.setImageResource(imagid2);
                peaceminds.setImageResource(imagid);
                angerminds.setImageResource(imagid2);
                disapointminds.setImageResource(imagid2);
                happyminds.setImageResource(imagid2);
                whichminds=2;
            }
        });
        angerminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imagid=R.drawable.xin;
                int imagid2=R.drawable.xin2;
                sadminds.setImageResource(imagid2);
                peaceminds.setImageResource(imagid2);
                angerminds.setImageResource(imagid);
                disapointminds.setImageResource(imagid2);
                happyminds.setImageResource(imagid2);
                whichminds=3;
            }
        });
        disapointminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imagid=R.drawable.xin;
                int imagid2=R.drawable.xin2;
                sadminds.setImageResource(imagid2);
                peaceminds.setImageResource(imagid2);
                angerminds.setImageResource(imagid2);
                disapointminds.setImageResource(imagid);
                happyminds.setImageResource(imagid2);
                whichminds=4;
            }
        });
        happyminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imagid=R.drawable.xin;
                int imagid2=R.drawable.xin2;
                sadminds.setImageResource(imagid2);
                peaceminds.setImageResource(imagid2);
                angerminds.setImageResource(imagid2);
                disapointminds.setImageResource(imagid2);
                happyminds.setImageResource(imagid);
                whichminds=5;
            }
        });
        /////////////////////////////访问系统相册
        addimgs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichimags=1;
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivityForResult(getAlbum, IMAGE_CODE);
            }

        });
        /////////////////////////////提交
        Retrofit GithubRetrofit = ServiceFactory.createRetrofit("http://zllink.applinzi.com/");
        githubService = GithubRetrofit.create(GithubService.class);
        publishtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson=new Gson();
                HashMap<String,String> paramsMap=new HashMap<String,String>();
                paramsMap.put("name","Me");
                paramsMap.put("diary_text",inouttext.getText().toString());
                paramsMap.put("head_url","null");
                paramsMap.put("feeling",String.valueOf(whichminds));
                paramsMap.put("date","20181216");
                paramsMap.put("picurl","null");
                paramsMap.put("is_public","public");
                String diary_json = gson.toJson(paramsMap);

                githubService.AddDiary("Me",diary_json,"add","123",0)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Diarymodel>() {
                            @Override
                            public void onCompleted() {
                                Toast.makeText(addnewtext.this, "获取成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(addnewtext.this,e.getMessage()+"请确认搜索的用户存在", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(Diarymodel musicmodels) {



                            }
                        });//subscribe










                Toast.makeText(addnewtext.this,imgpaths,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(addnewtext.this,MainActivity.class);
                intent.putExtra("minds",String.valueOf(whichminds));
                intent.putExtra("texts",inouttext.getText().toString());
                intent.putExtra("imagepath",imgpaths);
                startActivity(intent);
            }
        });
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;
        ContentResolver resolver = getContentResolver();
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Toast.makeText(addnewtext.this, "失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == IMAGE_CODE) {
            try {
                Uri originalUri = data.getData();                                               //获得图片的uri
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);                   //得到bitmap图片
                String[] proj = {MediaStore.Images.Media.DATA};                                 //android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = getContentResolver().query(originalUri, proj, null, null, null);//获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//将光标移至开头 ，不然容易引起越界
                cursor.moveToFirst();
                //最后根据索引值获取图片路径
                String path = cursor.getString(column_index);
                imgpaths=originalUri.getPath();
                if(whichimags==1){
                    addimgs1.setImageURI(originalUri);
                }
                else if(whichimags==2){
                    addimgs2.setImageURI(originalUri);
                }
                else{
                    addimgs3.setImageURI(originalUri);
                }
            } catch (IOException e) {
                Toast.makeText(addnewtext.this, "失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
