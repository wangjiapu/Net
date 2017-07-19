package com.example.com.net.v;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.com.net.Bean;
import com.example.com.net.R;
import com.example.com.net.p.BasePresenter;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkhttpActivity extends IOkhttpActivity{

    private Button bt1,bt2;
    private OkHttpClient okhttpClient;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                Toast.makeText(OkhttpActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
            }else if (msg.what==1){
                Toast.makeText(OkhttpActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(OkhttpActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_get:
                doget();
                break;
            case R.id.ok_post:
                dopost();
                break;
        }
    }

    private void dopost() {
        //1.拿到okhttpClient对象
        okhttpClient=new OkHttpClient();

        RequestBody formBody=new FormBody.Builder()
                .add("name","pjw")
                .add("pwd","111")
                .build();

        //构造request
        Request.Builder bulider=new Request.Builder();
        final Request request=bulider.url("http://172.20.7.66:8080/Okhttpdemo/p")
                .post(formBody).build();
        Call call=okhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("错","cvhdsifnileos");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                // InputStream is=response.body().byteStream();//流的下载
                Log.e("hao",response.body().string());
                if (response.isSuccessful()) {
                    message.what=1;
                    message.obj=response.body().toString();
                }
                handler.sendMessage(message);
            }
        });
    }

    /**
     * okhttp的get请求
     */
    private void doget() {
        //1.拿到okhttpClient对象
        okhttpClient = new OkHttpClient();
        //2.构造request

        Request.Builder bulider = new Request.Builder();
        Request request = bulider.get().url(Bean.URL).build();
        //3.封装request对象为call
        Call call = okhttpClient.newCall(request);

        //执行call
        //同步
        //Response response=call.execute();
        //异步
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求出错
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message message = new Message();
               // InputStream is=response.body().byteStream();//流的下载
                if (!((message.what = response.isSuccessful() ? 200 : 404) > 200)) {
                    Log.e("get请求成功，成功数据为：", response.body().string());
                }
                handler.sendMessage(message);
            }
        });

    }


    @Override
    void initview() {
        bt1=(Button)findViewById(R.id.ok_get);
        bt1.setOnClickListener(this);
        bt2=(Button)findViewById(R.id.ok_post);
        bt2.setOnClickListener(this);
    }

    @Override
    int getLayout() {
        return R.layout.okhttp_layout;
    }

    @Override
    void onPrepase() {

    }

    @Override
    BasePresenter initPresent() {
        return null;
    }
}
