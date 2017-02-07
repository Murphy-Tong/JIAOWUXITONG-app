package com.example.tong.jiaowuxitong.entity;

/**
 * Created by TONG on 2017/1/3.
 */
public class VOManager extends VOUser {
    //    private int id;
    private int gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VOManager that = (VOManager) o;

        if (id != that.id) return false;
        if (gender != that.gender) return false;
        if (password != null ? !password.equals(that.password) : that.password != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + gender;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
