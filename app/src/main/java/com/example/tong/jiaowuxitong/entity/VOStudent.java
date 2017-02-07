package com.example.tong.jiaowuxitong.entity;


import java.util.List;

/**
 * Created by TONG on 2017/1/3.
 */
public class VOStudent extends VOUser {

    private int marjorId;
    private int gender;
    private int stdCrsCount;

    private List<VOStdCrs> stdCrses;

    public List<VOStdCrs> getStdCrses() {
        return stdCrses;
    }

    public void setStdCrses(List<VOStdCrs> stdCrses) {
        this.stdCrses = stdCrses;
    }

    public int getStdCrsCount() {
        return stdCrsCount;
    }

    public void setStdCrsCount(int stdCrsCount) {
        this.stdCrsCount = stdCrsCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getMarjorId() {
        return marjorId;
    }

    public void setMarjorId(int marjorId) {
        this.marjorId = marjorId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof VOStudent) {
            return ((VOStudent) o).getId() == this.id;
        }
        return false;
    }

    @Override
    public String toString() {

        return "学生:"+getName()+ " id: "+id;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + marjorId;
        result = 31 * result + gender;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }


}
