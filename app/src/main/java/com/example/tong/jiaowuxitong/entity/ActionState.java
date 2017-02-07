package com.example.tong.jiaowuxitong.entity;

/**
 * Created by TONG on 2017/1/9.
 */
public class ActionState {
    public static final int ACTION_SUCCESS = 1;
    public static final int ACTION_FAILED = 0;
    public int tag;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
