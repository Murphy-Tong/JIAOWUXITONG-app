package com.example.tong.jiaowuxitong;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by TONG on 2017/1/5.
 */
public class TestUtil {
    public  static  void print(String s){
        System.out.print("\n"+s+"\n");
    }

    public static void log(String tag,String s){
        Log.i(tag,s);
    }

    public static void toast(Context c,String s){
        Toast.makeText(c,s,Toast.LENGTH_SHORT).show();
    }

}
