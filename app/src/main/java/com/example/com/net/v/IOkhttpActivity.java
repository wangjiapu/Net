package com.example.com.net.v;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.com.net.R;
import com.example.com.net.p.BasePresenter;


public abstract class IOkhttpActivity <T extends BasePresenter>extends Activity
    implements View.OnClickListener{

    protected T basePresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initview();
    }
    abstract void initview();
    abstract int getLayout();
    abstract void onPrepase();
    abstract T initPresent();
}
