package com.example.tong.jiaowuxitong.entity;

/**
 * Created by TONG on 2017/1/10.
 */
public class VOCourseEvaluation {
    private int courseId;
    private int deptId;
    private String deptName;
    private float synthesisEvaDegree;
    private float deptSynthesisEvaDegree;
    private int evaStudentCount;
    private float deptPercent;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public float getSynthesisEvaDegree() {
        return synthesisEvaDegree;
    }

    public void setSynthesisEvaDegree(float synthesisEvaDegree) {
        this.synthesisEvaDegree = synthesisEvaDegree;
    }

    public float getDeptSynthesisEvaDegree() {
        return deptSynthesisEvaDegree;
    }

    public void setDeptSynthesisEvaDegree(float deptSynthesisEvaDegree) {
        this.deptSynthesisEvaDegree = deptSynthesisEvaDegree;
    }

    public int getEvaStudentCount() {
        return evaStudentCount;
    }

    public void setEvaStudentCount(int evaStudentCount) {
        this.evaStudentCount = evaStudentCount;
    }

    public float getDeptPercent() {
        return deptPercent;
    }

    public void setDeptPercent(float deptPercent) {
        this.deptPercent = deptPercent;
    }
}
