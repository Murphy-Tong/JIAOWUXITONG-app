package com.example.tong.jiaowuxitong.entity;

import java.io.Serializable;

/**
 * Created by TONG on 2017/1/5.
 */
public class VOUser implements Serializable{
    public static final int MANAGER_TAG = 0;
    public static final int TEACHER_TAG = 1;
    public static final int STUDENT_TAG = 2;

    public int TAG;
    protected int id;
    protected String password;
    private String name;


    public VOUser(){}

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId(){
        return  id;
    }

    public void setTAG(int t) {
        this.TAG = t;
    }

    public int getTAG() {
        return TAG;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
