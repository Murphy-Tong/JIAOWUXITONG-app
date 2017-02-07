package com.example.tong.jiaowuxitong.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TONG on 2017/2/4.
 */
public class Query implements Serializable {
    public int tag;
    public String key;
    public int queryTag;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VOCourse> mResults = new ArrayList<>();
    public List<VOTeacher> tResults = new ArrayList<>();
    public List<VOStudent> sResults = new ArrayList<>();


    public Query(int tag, int queryTag, String key) {
        this.tag = tag;
        this.key = key;
        this.queryTag = queryTag;
    }

    public int getQueryTag() {
        return queryTag;
    }

    public void setQueryTag(int queryTag) {
        this.queryTag = queryTag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<VOCourse> getmResults() {
        return mResults;
    }

    public void setmResults(List<VOCourse> mResults) {
        this.mResults = mResults;
    }

    public List<VOTeacher> gettResults() {
        return tResults;
    }

    public void settResults(List<VOTeacher> tResults) {
        this.tResults = tResults;
    }

    public List<VOStudent> getsResults() {
        return sResults;
    }

    public void setsResults(List<VOStudent> sResults) {
        this.sResults = sResults;
    }

    public int size() {
        int c = 0;
        if (this.mResults != null) {
            c += this.mResults.size();
        }
        if (this.tResults != null) {
            c += this.tResults.size();
        }
        if (this.sResults != null) {
            c += this.sResults.size();
        }
        return c;
    }

    public void addAll(Query query) {
        if (query != null) {
            if (this.mResults != null) {
                this.mResults.addAll(query.mResults);
            }
            if (this.tResults != null) {
                this.tResults.addAll(query.tResults);
            }

            if (this.sResults != null) {
                this.sResults.addAll(query.sResults);
            }

        }
    }
}
