package com.example.com.net.p;


import com.example.com.net.v.IOkhttpActivity;

public abstract class BasePresenter<T extends IOkhttpActivity> {
    abstract void initData();
}
