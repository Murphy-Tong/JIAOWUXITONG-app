package com.example.tong.jiaowuxitong.entity;

import java.io.Serializable;

/**
 * Created by TONG on 2017/1/3.
 */
public class VOStdCrs implements Serializable {
    private int id;
    private String name;
    private int studentCount;
    private float degree;
    private int thrId;
    private String thrName;
    private String studentName;
    private int studentId;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    private String deptName;
    private int deptId;
    private float evaDegree;
    private int courseId;
    private int unEvaluatedCount;
    private float evaPercent;
    private float degreePercent;

    public float getEvaDegree() {
        return evaDegree;
    }

    public void setEvaDegree(float evaDegree) {
        this.evaDegree = evaDegree;
    }

    public int getUnEvaluatedCount() {
        return unEvaluatedCount;
    }

    public void setUnEvaluatedCount(int unEvaluatedCount) {
        this.unEvaluatedCount = unEvaluatedCount;
    }


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setThrId(int thrId) {
        this.thrId = thrId;
    }

    public void setThrName(String thrName) {
        this.thrName = thrName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public float getEvaPercent() {
        return evaPercent;
    }

    public void setEvaPercent(float evaPercent) {
        this.evaPercent = evaPercent;
    }

    public float getDegreePercent() {
        return degreePercent;
    }

    public void setDegreePercent(float degreePercent) {
        this.degreePercent = degreePercent;
    }

    public int getThrId() {
        return thrId;
    }

    public String getThrName() {
        return thrName;
    }


    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof VOStdCrs) {
            return (((VOStdCrs) o).getCourseId() == this.courseId) && (((VOStdCrs) o).getStudentId() == this.studentId);
        }
        return super.equals(o);
    }
}
