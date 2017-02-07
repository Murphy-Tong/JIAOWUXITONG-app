package com.example.tong.jiaowuxitong.entity;

import java.io.Serializable;

/**
 * Created by TONG on 2017/1/8.
 */
public class VOOpinion implements Serializable {
    private int TAG;
    private int uId;
    private String pwd;
    private int courseId;
    private int stdCrsId;
    private float opt1;
    private float opt2;
    private float opt3;
    private float opt4;
    private float opt5;
    private float opt10;
    private float opt11;
    private String opinion;
    private int id;
    private float opt6;
    private float opt7;
    private float opt8;
    private float opt9;

    private float avOpt1;
    private float avOpt2;
    private float avOpt3;
    private float avOpt4;
    private float avOpt5;
    private float avOpt10;
    private float avOpt11;
    private float avOpt6;
    private float avOpt7;
    private float avOpt8;
    private float avOpt9;

    private float optTotal1;
    private float optTotal2;
    private float optTotal3;
    private float optTotal4;
    private float optTotal5;
    private float optTotal10;
    private float optTotal11;
    private float optTotal6;
    private float optTotal7;
    private float optTotal8;
    private float optTotal9;


    public float getOpt(int position) {
        switch ((position) % 11) {
            case 0:
                return getOpt1();
            case 1:
                return getOpt2();
            case 2:
                return getOpt3();
            case 3:
                return getOpt4();
            case 4:
                return getOpt5();
            case 5:
                return getOpt6();
            case 6:
                return getOpt7();
            case 7:
                return getOpt8();
            case 8:
                return getOpt9();
            case 9:
                return getOpt10();
            case 10:
                return getOpt11();
        }
        return 0;
    }

    public float getTotal(int position) {
        switch ((position) % 11) {
            case 0:
                return getOptTotal1();
            case 1:
                return getOptTotal2();
            case 2:
                return getOptTotal3();
            case 3:
                return getOptTotal4();
            case 4:
                return getOptTotal5();
            case 5:
                return getOptTotal6();
            case 6:
                return getOptTotal7();
            case 7:
                return getOptTotal8();
            case 8:
                return getOptTotal9();
            case 9:
                return getOptTotal10();
            case 10:
                return getOptTotal11();
        }
        return 0;
    }

    public float getAv(int position) {
        switch ((position) % 11) {
            case 0:
                return getAvOpt1();
            case 1:
                return getAvOpt2();
            case 2:
                return getAvOpt3();
            case 3:
                return getAvOpt4();
            case 4:
                return getAvOpt5();
            case 5:
                return getAvOpt6();
            case 6:
                return getAvOpt7();
            case 7:
                return getAvOpt8();
            case 8:
                return getAvOpt9();
            case 9:
                return getAvOpt10();
            case 10:
                return getAvOpt11();
        }
        return 0;
    }

    public float getOptTotal1() {
        return optTotal1;
    }

    public void setOptTotal1(float optTotal1) {
        this.optTotal1 = optTotal1;
    }

    public float getOptTotal2() {
        return optTotal2;
    }

    public void setOptTotal2(float optTotal2) {
        this.optTotal2 = optTotal2;
    }

    public float getOptTotal3() {
        return optTotal3;
    }

    public void setOptTotal3(float optTotal3) {
        this.optTotal3 = optTotal3;
    }

    public float getOptTotal4() {
        return optTotal4;
    }

    public void setOptTotal4(float optTotal4) {
        this.optTotal4 = optTotal4;
    }

    public float getOptTotal5() {
        return optTotal5;
    }

    public void setOptTotal5(float optTotal5) {
        this.optTotal5 = optTotal5;
    }

    public float getOptTotal10() {
        return optTotal10;
    }

    public void setOptTotal10(float optTotal10) {
        this.optTotal10 = optTotal10;
    }

    public float getOptTotal11() {
        return optTotal11;
    }

    public void setOptTotal11(float optTotal11) {
        this.optTotal11 = optTotal11;
    }

    public float getOptTotal6() {
        return optTotal6;
    }

    public void setOptTotal6(float optTotal6) {
        this.optTotal6 = optTotal6;
    }

    public float getOptTotal7() {
        return optTotal7;
    }

    public void setOptTotal7(float optTotal7) {
        this.optTotal7 = optTotal7;
    }

    public float getOptTotal8() {
        return optTotal8;
    }

    public void setOptTotal8(float optTotal8) {
        this.optTotal8 = optTotal8;
    }

    public float getOptTotal9() {
        return optTotal9;
    }

    public void setOptTotal9(float optTotal9) {
        this.optTotal9 = optTotal9;
    }

    public float getAvOpt1() {
        return avOpt1;
    }

    public void setAvOpt1(float avOpt1) {
        this.avOpt1 = avOpt1;
    }

    public float getAvOpt2() {
        return avOpt2;
    }

    public void setAvOpt2(float avOpt2) {
        this.avOpt2 = avOpt2;
    }

    public float getAvOpt3() {
        return avOpt3;
    }

    public void setAvOpt3(float avOpt3) {
        this.avOpt3 = avOpt3;
    }

    public float getAvOpt4() {
        return avOpt4;
    }

    public void setAvOpt4(float avOpt4) {
        this.avOpt4 = avOpt4;
    }

    public float getAvOpt5() {
        return avOpt5;
    }

    public void setAvOpt5(float avOpt5) {
        this.avOpt5 = avOpt5;
    }

    public float getAvOpt10() {
        return avOpt10;
    }

    public void setAvOpt10(float avOpt10) {
        this.avOpt10 = avOpt10;
    }

    public float getAvOpt11() {
        return avOpt11;
    }

    public void setAvOpt11(float avOpt11) {
        this.avOpt11 = avOpt11;
    }

    public float getAvOpt6() {
        return avOpt6;
    }

    public void setAvOpt6(float avOpt6) {
        this.avOpt6 = avOpt6;
    }

    public float getAvOpt7() {
        return avOpt7;
    }

    public void setAvOpt7(float avOpt7) {
        this.avOpt7 = avOpt7;
    }

    public float getAvOpt8() {
        return avOpt8;
    }

    public void setAvOpt8(float avOpt8) {
        this.avOpt8 = avOpt8;
    }

    public float getAvOpt9() {
        return avOpt9;
    }

    public void setAvOpt9(float avOpt9) {
        this.avOpt9 = avOpt9;
    }

    public int getTAG() {
        return TAG;
    }

    public void setTAG(int TAG) {
        this.TAG = TAG;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getStdCrsId() {
        return stdCrsId;
    }

    public void setStdCrsId(int stdCrsId) {
        this.stdCrsId = stdCrsId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setOpt1(Integer opt1) {
        this.opt1 = opt1;
    }

    public void setOpt2(Integer opt2) {
        this.opt2 = opt2;
    }

    public void setOpt3(Integer opt3) {
        this.opt3 = opt3;
    }

    public void setOpt4(Integer opt4) {
        this.opt4 = opt4;
    }

    public void setOpt5(Integer opt5) {
        this.opt5 = opt5;
    }

    public void setOpt6(Integer opt6) {
        this.opt6 = opt6;
    }

    public void setOpt7(Integer opt7) {
        this.opt7 = opt7;
    }

    public void setOpt8(Integer opt8) {
        this.opt8 = opt8;
    }

    public void setOpt9(Integer opt9) {
        this.opt9 = opt9;
    }

    public void setOpt10(Integer opt10) {
        this.opt10 = opt10;
    }

    public void setOpt11(Integer opt11) {
        this.opt11 = opt11;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getOpt1() {
        return opt1;
    }

    public void setOpt1(float opt1) {
        this.opt1 = opt1;
    }

    public float getOpt2() {
        return opt2;
    }

    public void setOpt2(float opt2) {
        this.opt2 = opt2;
    }

    public float getOpt3() {
        return opt3;
    }

    public void setOpt3(float opt3) {
        this.opt3 = opt3;
    }

    public float getOpt4() {
        return opt4;
    }

    public void setOpt4(float opt4) {
        this.opt4 = opt4;
    }

    public float getOpt5() {
        return opt5;
    }

    public void setOpt5(float opt5) {
        this.opt5 = opt5;
    }

    public float getOpt10() {
        return opt10;
    }

    public void setOpt10(float opt10) {
        this.opt10 = opt10;
    }

    public float getOpt11() {
        return opt11;
    }

    public void setOpt11(float opt11) {
        this.opt11 = opt11;
    }

    public String getOpinion() {
        return opinion;
    }

    public float getOpt6() {
        return opt6;
    }

    public void setOpt6(float opt6) {
        this.opt6 = opt6;
    }

    public float getOpt7() {
        return opt7;
    }

    public void setOpt7(float opt7) {
        this.opt7 = opt7;
    }

    public float getOpt8() {
        return opt8;
    }

    public void setOpt8(float opt8) {
        this.opt8 = opt8;
    }

    public float getOpt9() {
        return opt9;
    }

    public void setOpt9(float opt9) {
        this.opt9 = opt9;
    }

    public int getId() {
        return id;
    }

    private int unEvaedStdCount;
    public int getUnEvaedStdCount() {
        return unEvaedStdCount;
    }
}
