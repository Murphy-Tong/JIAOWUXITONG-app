package com.example.tong.jiaowuxitong.entity;

import java.io.Serializable;

/**
 * Created by TONG on 2017/1/3.
 */
public class VOCourse implements Serializable {
    private int id;
    private String name;
    private String teacherName;
    private String departmentName;
    private int studentCount;
    private int unDegreeStudentCount;
    private int departmentId;
    private int teacherId;
    private float degree;
    private float excellentRate;
    private float unpassRate;

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public float getExcellentRate() {
        return excellentRate;
    }

    public void setExcellentRate(float excellentRate) {
        this.excellentRate = excellentRate;
    }

    public float getUnpassRate() {
        return unpassRate;
    }

    public void setUnpassRate(float unpassRate) {
        this.unpassRate = unpassRate;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getUnDegreeStudentCount() {
        return unDegreeStudentCount;
    }

    public void setUnDegreeStudentCount(int unDegreeStudentCount) {
        this.unDegreeStudentCount = unDegreeStudentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof VOCourse) {
            return ((VOCourse) o).getId() == this.id;
        }
        return false;
    }

    @Override
    public String toString() {

        return "课程:" + getName() + " id: " + id;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
