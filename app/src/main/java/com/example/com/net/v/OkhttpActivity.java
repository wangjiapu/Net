package com.example.com.net.v;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.com.net.Bean;
import com.example.com.net.R;
import com.example.com.net.p.BasePresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkhttpActivity extends IOkhttpActivity {
    private OkHttpClient okhttpClient;
    private ImageView image;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                Toast.makeText(OkhttpActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(OkhttpActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OkhttpActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_get:
                Toast.makeText(OkhttpActivity.this, "get", Toast.LENGTH_SHORT).show();
                doget();
                break;
            case R.id.ok_post:
                Toast.makeText(OkhttpActivity.this, "post", Toast.LENGTH_SHORT).show();
                dopost();
                break;
            case R.id.ok_file:
                Toast.makeText(OkhttpActivity.this, "post文件", Toast.LENGTH_SHORT).show();
                doPostFile();
                break;
            case R.id.ok_string://post一个json字符串到服务器
                Toast.makeText(OkhttpActivity.this, "post字符串", Toast.LENGTH_SHORT).show();
                doPostString();
                break;
            case R.id.ok_upload://上传一个文件到服务器
                Toast.makeText(OkhttpActivity.this, "上传文件", Toast.LENGTH_SHORT).show();
                doUpload();
                break;
            case R.id.ok_download://下载一个文件到本地
                Toast.makeText(OkhttpActivity.this, "下载文件", Toast.LENGTH_SHORT).show();
                doDownload();
                break;
            case R.id.ok_doBitmap://下载一个文件到本地
                doBitmap();
                break;
        }
    }

    /**
     * 设置文件
     */
    private void doBitmap() {
        //1.拿到okhttpClient对象
        okhttpClient = new OkHttpClient();
        //2.构造request

        Request.Builder bulider = new Request.Builder();
        Request request = bulider.get()
                .url("http://pic.58pic.com/58pic/13/61/00/61a58PICtPr_1024.jpg")
                .build();
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
                Log.e("下载图片","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               InputStream is=response.body().byteStream();
                final Bitmap bitmap= BitmapFactory.decodeStream(is);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    /**
     * 下载文件到本地
     */
    private void doDownload() {
        //1.拿到okhttpClient对象
        okhttpClient = new OkHttpClient();
        //2.构造request

        Request.Builder bulider = new Request.Builder();
        Request request = bulider.get()
                .url("http://pic.58pic.com/58pic/13/61/00/61a58PICtPr_1024.jpg")
                .build();
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
                Log.e("下载图片","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is=response.body().byteStream();
                int len=0;
                File file=new File(Environment.getExternalStorageDirectory(),"美女.jpg");
                byte[] buf=new byte[1024];
                FileOutputStream fos=new FileOutputStream(file);

                while((len=is.read(buf))!=-1){
                    fos.write(buf,0,len);
                }
                fos.flush();
                fos.close();
                is.close();
                Log.e("下载图片","成功");
            }
        });
    }

    /**
     * 上传文件
     */
    private void doUpload() {
        okhttpClient = new OkHttpClient();
        File file = new File(Environment.getExternalStorageDirectory(), "android_plan.jpg");
        if (!file.exists()) {
            Log.e(file.getAbsolutePath() + ":", "未找到！");
            return;
        }

        //构造者模式
        MultipartBody.Builder mbuilder=new MultipartBody.Builder();
        RequestBody requestBody=mbuilder
                .addFormDataPart("uername","pjw")
                .addFormDataPart("pwd","111")
                .addFormDataPart("mphoto","android_plan.jpg",
                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://172.20.7.66:8080/Okhttpdemo/p")
                .post(requestBody)
                .build();

        Call call = okhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("文件错", "error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //doshting
            }
        });
    }

    /**
     * post一个字符串到服务器
     * （eg:json字符串）
     */
    private void doPostString() {
        okhttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody
                .create(MediaType.parse("text/plain;chaset=utf-8"), "{123:456,789:987}");
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://172.20.7.66:8080/Okhttpdemo/p")
                .post(requestBody)
                .build();

        Call call = okhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("字符串错", "error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //doshting
            }
        });
    }

    /**
     * post 一个文件到服务端
     */
    private void doPostFile() {
        okhttpClient = new OkHttpClient();
        File file = new File(Environment.getExternalStorageDirectory(), "android_plan.jpg");
        if (!file.exists()) {
            Log.e(file.getAbsolutePath() + ":", "未找到！");
            return;
        }
        //搜索mime type知道application/octet-stream流的类型
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://172.20.7.66:8080/Okhttpdemo/p")
                .post(requestBody)
                .build();

        Call call = okhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("文件错", "error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //doshting
            }
        });
    }


    /**
     * post请求
     */
    private void dopost() {
        //1.拿到okhttpClient对象
        okhttpClient = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("name", "pjw")
                .add("pwd", "111")
                .build();

        //构造request
        Request.Builder bulider = new Request.Builder();
        final Request request = bulider.url("http://172.20.7.66:8080/Okhttpdemo/p")
                .post(formBody).build();
        Call call = okhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("错", "cvhdsifnileos");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                // InputStream is=response.body().byteStream();//流的下载
                Log.e("hao", response.body().string());
                if (response.isSuccessful()) {
                    message.what = 1;
                    message.obj = response.body().toString();
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
        findViewById(R.id.ok_get).setOnClickListener(this);
        findViewById(R.id.ok_post).setOnClickListener(this);
        findViewById(R.id.ok_file).setOnClickListener(this);
        findViewById(R.id.ok_string).setOnClickListener(this);
        findViewById(R.id.ok_upload).setOnClickListener(this);
        findViewById(R.id.ok_download).setOnClickListener(this);
        findViewById(R.id.ok_doBitmap).setOnClickListener(this);
        image=(ImageView)findViewById(R.id.iv);
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
