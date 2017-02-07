package com.example.tong.jiaowuxitong.entity;

import java.util.List;

/**
 * Created by TONG on 2017/1/22.
 */
public class VOCourseSys {
    private int id;
    private String name;

    private int studentCount;
    private int evaedStudentCount;
    private List<Float> degreesOfStudent;
    private List<Float> excellentPertOfDept;
    private List<Float> unpassPercentOfDept;
    private List<VOCourse> courses;

    public List<VOCourse> getCourses() {
        return courses;
    }

    public void setCourses(List<VOCourse> courses) {
        this.courses = courses;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEvaedStudentCount() {
        return evaedStudentCount;
    }

    public void setEvaedStudentCount(int evaedStudentCount) {
        this.evaedStudentCount = evaedStudentCount;
    }

    public List<Float> getDegreesOfStudent() {
        return degreesOfStudent;
    }

    public void setDegreesOfStudent(List<Float> degreesOfStudent) {
        this.degreesOfStudent = degreesOfStudent;
    }

    public List<Float> getExcellentPertOfDept() {
        return excellentPertOfDept;
    }

    public void setExcellentPertOfDept(List<Float> excellentPertOfDept) {
        this.excellentPertOfDept = excellentPertOfDept;
    }

    public List<Float> getUnpassPercentOfDept() {
        return unpassPercentOfDept;
    }

    public void setUnpassPercentOfDept(List<Float> unpassPercentOfDept) {
        this.unpassPercentOfDept = unpassPercentOfDept;
    }
}
