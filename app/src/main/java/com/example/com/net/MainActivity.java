package com.example.com.net;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.net.v.OkhttpActivity;


public class MainActivity extends AppCompatActivity
        implements Toolbar.OnMenuItemClickListener{
    private Button send,receive;
    private EditText editText,editText2;
    private TextView tv;
    private static String IP;
    private static String Wifi_IP;

    private ListView lv;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 0:Toast.makeText(MainActivity.this,"未成功连接，请检查接受端是否开启"
                       ,Toast.LENGTH_SHORT).show();
                   break;
               case 1:
                   Toast.makeText(MainActivity.this,"发送成功"
                           ,Toast.LENGTH_SHORT).show();
                   break;
               case 3:
                   String str2=msg.obj.toString();
                   tv.setText(str2);
                   break;
               case 4:
                   Toast.makeText(MainActivity.this,"开启失败"
                           ,Toast.LENGTH_SHORT).show();
                   break;

           }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send=(Button)findViewById(R.id.send);
        receive=(Button)findViewById(R.id.receive);
        editText=(EditText)findViewById(R.id.et);
        editText2=(EditText)findViewById(R.id.text);
        tv=(TextView)findViewById(R.id.result);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);

        lv=(ListView)findViewById(R.id.listview);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setVisibility(View.GONE);
                    editText.setVisibility(View.VISIBLE);
                    editText2.setVisibility(View.VISIBLE);
                    IP= editText.getText().toString().trim();
                    String context=editText2.getText().toString().trim();
                    send_fun(context);
            }
        });
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setVisibility(View.GONE);
                editText2.setVisibility(View.GONE);
                    receive_fun();

            }
        });


    }


    private void receive_fun() {
        WifiManager wifiManager=(WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        Wifi_IP=intToIp(wifiInfo.getIpAddress());
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("连接IP为：");
        builder.setMessage(Wifi_IP);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv.setVisibility(View.VISIBLE);
                server();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();


    }



    private String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    private void send_fun(final String context) {

        new Thread(new Runnable() {
           @Override
           public void run() {
               Message message=new Message();
               message.what=SocketUitl.Client(IP,context);
               handler.sendMessage(message);
           }
       }).start();
    }

    private void server() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                String str= SocketUitl.sSocket();
                if (str.isEmpty()){
                    message.what=4;
                }else{
                    message.what=3;
                    message.obj=str;
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ac,menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.p2p:
                Toast.makeText(MainActivity.this,"p2p"
                        ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.server:
                Toast.makeText(MainActivity.this,"server"
                        ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.wating:
                Intent intent=new Intent(MainActivity.this,OkhttpActivity.class);
                MainActivity.this.startActivity(intent);
                break;

            case R.id.read_data:
                context_read_data();
                break;
        }
        return true;
    }

    /**
     * 关于contentprovider读取sqliteDemo数据库中的数据
     * 以listView的方式呈现出来
     */
    private void context_read_data() {
        Uri uri=Uri.parse("content://com.example.com.sqlitedemo/person");
        Cursor cursor=getContentResolver().query(
                uri,//指定查询某个应用程序下的一张表
                null,//指定查询的列名
                null,//指定where的约束条件
                null,//where的占位符
                null//排序方式
        );
        if (cursor!=null){
            while (cursor.moveToNext()){
                Log.e("_id:",cursor.getInt(cursor.getColumnIndex("_id"))+"");

            }
        }else{
            Log.e("cursor:","1111111111111");
        }
        Log.e("pppppppppppp","wwwwwwwwwwww");
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,
                R.layout.list_item,cursor,
                new String[]{"_id","name","age"},
                new int[]{R.id.info_id,R.id.info_name,R.id.info_age});
        lv.setAdapter(adapter);
    }
}
