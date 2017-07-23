package com.example.com.net;


import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class ResManager {

    private Resources mResources;
    private String mPkgName;
    public ResManager(Resources resources,String pkg){
        this.mResources=resources;
        this.mPkgName=pkg;
    }
    public Drawable getDrawableByresName(String name){
        try {
            return mResources.getDrawable(mResources.getIdentifier(name,"drawable",mPkgName));
        }catch (Exception e){
            return null;
        }

    }
    public ColorStateList getColorByName(String name){
        try {
            return mResources.getColorStateList(mResources.getIdentifier(name,"color",mPkgName));
        }catch (Exception e){
            return null;
        }

    }
}
