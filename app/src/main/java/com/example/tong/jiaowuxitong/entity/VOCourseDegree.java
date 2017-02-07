package com.example.tong.jiaowuxitong.entity;

/**
 * Created by TONG on 2017/1/10.
 */
public class VOCourseDegree {
    private int id;
    private int classAStudentCount;
    private int classBStudentCount;
    private int classCStudentCount;
    private int classDStudentCount;
    private int classSStudentCount;
    private float excellentRate;
    private float unpassRate;
    private float excellentRateInDept;
    private float unpassRateInDept;
    private float studentMaxDegree;
    private float studentMinDegree;
    private String maxDegreeStudentName;
    private int maxDegreeStudentId;
    private String minDegreeStudentName;
    private int minDegreeStudentId;

    private float avDegree;

    public float getStudentMaxDegree() {
        return studentMaxDegree;
    }

    public void setStudentMaxDegree(float studentMaxDegree) {
        this.studentMaxDegree = studentMaxDegree;
    }

    public float getStudentMinDegree() {
        return studentMinDegree;
    }

    public void setStudentMinDegree(float studentMinDegree) {
        this.studentMinDegree = studentMinDegree;
    }

    public String getMaxDegreeStudentName() {
        return maxDegreeStudentName;
    }

    public void setMaxDegreeStudentName(String maxDegreeStudentName) {
        this.maxDegreeStudentName = maxDegreeStudentName;
    }

    public int getMaxDegreeStudentId() {
        return maxDegreeStudentId;
    }

    public void setMaxDegreeStudentId(int maxDegreeStudentId) {
        this.maxDegreeStudentId = maxDegreeStudentId;
    }

    public String getMinDegreeStudentName() {
        return minDegreeStudentName;
    }

    public void setMinDegreeStudentName(String minDegreeStudentName) {
        this.minDegreeStudentName = minDegreeStudentName;
    }

    public int getMinDegreeStudentId() {
        return minDegreeStudentId;
    }

    public void setMinDegreeStudentId(int minDegreeStudentId) {
        this.minDegreeStudentId = minDegreeStudentId;
    }

    public float getAvDegree() {
        return avDegree;
    }

    public void setAvDegree(float avDegree) {
        this.avDegree = avDegree;
    }

    public float getExcellentRateInDept() {
        return excellentRateInDept;
    }

    public void setExcellentRateInDept(float excellentRateInDept) {
        this.excellentRateInDept = excellentRateInDept;
    }

    public float getUnpassRateInDept() {
        return unpassRateInDept;
    }

    public void setUnpassRateInDept(float unpassRateInDept) {
        this.unpassRateInDept = unpassRateInDept;
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

    public int getId() {
        return id;
    }

    public int getClassAStudentCount() {
        return classAStudentCount;
    }

    public int getClassBStudentCount() {
        return classBStudentCount;
    }

    public int getClassCStudentCount() {
        return classCStudentCount;
    }

    public int getClassDStudentCount() {
        return classDStudentCount;
    }

    public int getClassSStudentCount() {
        return classSStudentCount;
    }

    public float getClassAStdPrct() {
        return classAStdPrct;
    }

    public float getClassBStdPrct() {
        return classBStdPrct;
    }

    public float getClassCStdPrct() {
        return classCStdPrct;
    }

    public float getClassDStdPrct() {
        return classDStdPrct;
    }

    public float getDeptSPassPrct() {
        return deptSPassPrct;
    }

    public float getDeptNoPassPrct() {
        return deptNoPassPrct;
    }

    private float classAStdPrct;
    private float classBStdPrct;
    private float classCStdPrct;
    private float classDStdPrct;

    private float deptSPassPrct;
    private float deptNoPassPrct;

    public void setId(int id) {
        this.id = id;
    }

    public void setClassAStudentCount(int classAStudentCount) {
        this.classAStudentCount = classAStudentCount;
    }

    public void setClassBStudentCount(int classBStudentCount) {
        this.classBStudentCount = classBStudentCount;
    }

    public void setClassCStudentCount(int classCStudentCount) {
        this.classCStudentCount = classCStudentCount;
    }

    public void setClassDStudentCount(int classDStudentCount) {
        this.classDStudentCount = classDStudentCount;
    }

    public void setClassSStudentCount(int classSStudentCount) {
        this.classSStudentCount = classSStudentCount;
    }
    

    public void setClassAStdPrct(float classAStdPrct) {
        this.classAStdPrct = classAStdPrct;
    }

    public void setClassBStdPrct(float classBStdPrct) {
        this.classBStdPrct = classBStdPrct;
    }

    public void setClassCStdPrct(float classCStdPrct) {
        this.classCStdPrct = classCStdPrct;
    }

    public void setClassDStdPrct(float classDStdPrct) {
        this.classDStdPrct = classDStdPrct;
    }

    public void setDeptSPassPrct(float deptSPassPrct) {
        this.deptSPassPrct = deptSPassPrct;
    }

    public void setDeptNoPassPrct(float deptNoPassPrct) {
        this.deptNoPassPrct = deptNoPassPrct;
    }
}
