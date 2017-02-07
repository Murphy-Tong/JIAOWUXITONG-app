package com.example.tong.jiaowuxitong.net;

/**
 * Created by TONG on 2017/1/6.
 */
public class Message {
    public static final int LOGIN = 3;
    public static int FAILED_TAG = 0;
    public static int SUCCESS_TAG = 1;
    public static int CHANGE_TITLE = 2;
    public static int COURSE_ALL = 4;
    public static int COURSE_DETAIL_STUDENT = 5;
    public int extra;
    public Object msg;
    public int tag;
    public static int EVA = 6;

    public Message(int tag, Object obj) {
        this.tag = tag;
        this.msg = obj;
    }

    public Message() {
    }

    public static int UPDATE_EVA = 7;

    public static int NOTIFYCHANGED = 8;
}
