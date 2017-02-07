package com.example.tong.jiaowuxitong.net;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by TONG on 2017/1/9.
 */
public class GsonUtil {
    public static String toJson(Object o) {
        if(o==null) return null;
        String s =new Gson().toJson(o);
//        TestUtil.print("gson out :"+s);
        return s;

    }

    public static String toJson(Object o, Type type) {
        if(o==null) return null;
        if (type == null) return toJson(o);
        return new Gson().toJson(o, type);
    }

    public static <T>  T fromJson(String s,Class<T> t){
        return (T)new Gson().fromJson(s,t);
    }

    public static <T>  T fromJson(String s,Type type){
        return (T)new Gson().fromJson(s,type);
    }


}
