package com.example.tong.jiaowuxitong.entity;

/**
 * Created by TONG on 2017/1/3.
 */
public class VOTeacher extends VOUser {
//    private String name;
    private int gender;
    private String departmentName;
    private int departmentId;
    private int courseCount;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

    public int getCourseCount() {
        return courseCount;
    }

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
        if (o != null && o instanceof VOTeacher) {
            return ((VOTeacher) o).getId() == this.id;
        }
        return false;
    }

    @Override
    public String toString() {

        return "教师:"+getName()+ " id: "+id;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + gender;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
