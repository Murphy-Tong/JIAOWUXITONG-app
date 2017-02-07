package com.example.tong.jiaowuxitong.entity;

/**
 * Created by TONG on 2017/1/3.
 */
public class VOEvaluation {
    private int userID;
    private String pwd;
    private int id;
    private String discribe;
    private Integer total;

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getUserID() {
        return userID;
    }

    public String getPwd() {
        return pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscribe() {
        return discribe;
    }

    public void setDiscribe(String discribe) {
        this.discribe = discribe;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VOEvaluation that = (VOEvaluation) o;

        if (id != that.id) return false;
        if (discribe != null ? !discribe.equals(that.discribe) : that.discribe != null)
            return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (discribe != null ? discribe.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        return result;
    }
}
