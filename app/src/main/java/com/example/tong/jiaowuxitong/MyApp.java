package com.example.tong.jiaowuxitong;

import android.app.Application;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.tong.jiaowuxitong.net.IOUtil;
import com.example.tong.jiaowuxitong.net.TextUtil;

import org.xutils.x;

/**
 * Created by TONG on 2017/1/6.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        String s = IOUtil.readString(this,"host","host");
        if(!TextUtils.isEmpty(s)){
            GlobalResource.setHost(s);
        }
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        GlobalResource.SCREEN_WID = windowManager.getDefaultDisplay().getWidth();
        GlobalResource.SCREEN_HIGH = windowManager.getDefaultDisplay().getHeight();
    }
}
