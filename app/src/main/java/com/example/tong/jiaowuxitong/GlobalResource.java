package com.example.tong.jiaowuxitong;

/**
 * Created by TONG on 2017/1/6.
 */
public class GlobalResource {
    public static int SCREEN_HIGH;
    public static String host = "http://192.168.191.1:8080/myapp/";
    public static String LOGIN_URL = host;
    public static String GET_ALL_STD_PAGE = "s-getAllStudentByPage.do";
    public static String GET_ALL_STD_OF_COURSE_PAGE = "s-getAllStudentOfCourseByPage.do";
    public static String GET_ALL_STD_OF_DPT_PAGE = "s-getAllStudentOfDeptByPage.do";
    public static String UPDATE_STD = "s-update.do";
    public static String DELETE_STD = "s-delete.do";
    public static String DELETE_STD_LIST = "s-deleteList.do";
    public static String FIND_STD = "s-find.do";
    public static String ADD_STD = "s-add.do";
    public static String LOGIN_STD = "s-login.do";
    public static String GET_ALLTEACHER_PAGE = "t-getAllTeacherPage.do";

    public static String GET_ALLTEACHER = "t-getAllTeacher.do";
    public static String GET_ALLTEACHER_OF_DEPT = "t-getAllTeacherOfDept.do";
    public static String GET_ALLTEACHER_OF_DEPT_PAGE = "t-getAllTeacherOfDeptByPage.do";
    public static String UPDATE_TEACHER = "t-update.do";
    public static String DELETE_TEACHER = "t-delete.do";
    public static String ADD_TEACHER = "t-add.do";
    public static String LOGIN_TEACHER = "t-login.do";
    public static String FIND_TEACHER = "t-find.do";
    public static String DELETE_TEACHER_LIST = "t-deleteList.do";

    public static String GET_CLASS_DETAIL = "c-getClassDetailOfCourse.do";
    public static String GET_DEGREE_EVA_OF_COURSE = "c-getDegreeEva.do";
    public static String GET_EVA_RESULT = "c-getEvaresult.do";
    public static String GET_ALL_STD_OF_COURSE = "c-getAllStudentOfCourse.do";
    public static String GET_ALL_COURSE_OF_TEACHER = "c-getAllCourseOfTeacher.do";
    public static String GET_ALL_COURSE_PAGE = "c-getAllCourseByPage.do";
    public static String GET_ALL_COURSE = "c-getAllCourse.do";
    public static String ALL_LIST = "c-addList.do";
    public static String UPDATE_COURSE = "c-update.do";
    public static String DELETE_COURSE = "c-delete.do";
    public static String DELETE_COURSE_LIST = "c-deleteList.do";
    public static String ADD_COURSE = "c-add.do";
    public static String FIND_COURSE = "c-find.do";

    public static String GET_DEPT_PAGE = "d-getByPage.do";
    public static String UPDATE_DEPT = "d-update.do";
    public static String ADD_DEPT = "d-add.do";
    public static String DELETE_DEPT = "d-delete.do";
    public static String FIND_DEPT = "d-find.do";

    public static String GET_ALL_EVA_ITEMS = "e-getEvaluations.do";
    public static String GET_EVA_DETAIL_OF_COURSE = "e-getEvaDetailOfCourse.do";
    public static String UPDATE_EVA_OF_COURSE = "e-updateEvaluationOfCourse.do";
    public static String UPDATE_EVA = "e-update.do";
    public static String DELETE_EVA = "e-delete.do";
    public static String ADD_EVA = "e-add.do";
    public static String FIND_EVA = "e-find.do";

    public static String GET_ALL_MANAGE_PAGE = "m-getAllByPage.do";
    public static String ADD_MANAGE = "m-add.do";
    public static String DELETE_MANAGE = "m-delete.do";
    public static String UPDATE_MANAGE = "m-update.do";
    public static String FIND_MANAGE = "m-find.do";
    public static String LOGIN_MANAGE = "m-login.do";

    public static String GET_ALL_COURSE_OF_STD = "sc-getAllCourseOfStudent.do";
    public static String GET_EVA_DETAIL = "sc-getEvaDetail.do";
    public static String DEGREE_DETAIL = "sc-degreeDetail.do";
    public static String UPDATE_DEGREE_ITEM = "sc-updateDegreeItem.do";
    public static String UPDATE_DEGREE_LIST = "sc-updateDegree.do";
    public static String GET_UNDEGREE_STD_OF_COURSE = "sc-getUndgreedStudentOfCourse.do";
    public static String DELETE_STDCRS_LIST = "sc-deleteList.do";
    public static String GET_STDCRS_OF_STD = "sc-getStdCrsOfStudent.do";
    public static String GET_ALL_EVA_DEGREE_OF_COURSE = "sc-getAllEvaDegreeOfCourse.do";
    public static String GET_ALL_DEGREE_OF_COURSE = "sc-getAllDegreeOfCourse.do";
    public static String ADD_STDCRS_LIST = "sc-addList.do";
    public static String DELETE_STDCRS = "sc-delete.do";
    public static String FIND_STDCRS = "sc-find.do";
    public static String QUERY = "m-query.do";
    public static String QUERY_GET = "m-queryGet.do";
    public static String EVALUATIONS = "evaluation";
    public static int SCREEN_WID;


    public static void setHost(String host) {
        GlobalResource.host= host;
        GET_ALL_STD_PAGE = host + "s-getAllStudentByPage.do";
        GET_ALL_STD_OF_COURSE_PAGE = host + "s-getAllStudentOfCourseByPage.do";
        GET_ALL_STD_OF_DPT_PAGE = host + "s-getAllStudentOfDeptByPage.do";
        UPDATE_STD = host + "s-update.do";
        DELETE_STD = host + "s-delete.do";
        DELETE_STD_LIST = host + "s-deleteList.do";
        FIND_STD = host + "s-find.do";
        ADD_STD = host + "s-add.do";
        LOGIN_STD = host + "s-login.do";
        GET_ALLTEACHER_PAGE = host + "t-getAllTeacherPage.do";

        GET_ALLTEACHER = host + "t-getAllTeacher.do";
        GET_ALLTEACHER_OF_DEPT = host + "t-getAllTeacherOfDept.do";
        GET_ALLTEACHER_OF_DEPT_PAGE = host + "t-getAllTeacherOfDeptByPage.do";
        UPDATE_TEACHER = host + "t-update.do";
        DELETE_TEACHER = host + "t-delete.do";
        ADD_TEACHER = host + "t-add.do";
        LOGIN_TEACHER = host + "t-login.do";
        FIND_TEACHER = host + "t-find.do";
        DELETE_TEACHER_LIST = host + "t-deleteList.do";

        GET_CLASS_DETAIL = host + "c-getClassDetailOfCourse.do";
        GET_DEGREE_EVA_OF_COURSE = host + "c-getDegreeEva.do";
        GET_EVA_RESULT = host + "c-getEvaresult.do";
        GET_ALL_STD_OF_COURSE = host + "c-getAllStudentOfCourse.do";
        GET_ALL_COURSE_OF_TEACHER = host + "c-getAllCourseOfTeacher.do";
        GET_ALL_COURSE_PAGE = host + "c-getAllCourseByPage.do";
        GET_ALL_COURSE = host + "c-getAllCourse.do";
        ALL_LIST = host + "c-addList.do";
        UPDATE_COURSE = host + "c-update.do";
        DELETE_COURSE = host + "c-delete.do";
        DELETE_COURSE_LIST = host + "c-deleteList.do";
        ADD_COURSE = host + "c-add.do";
        FIND_COURSE = host + "c-find.do";

        GET_DEPT_PAGE = host + "d-getByPage.do";
        UPDATE_DEPT = host + "d-update.do";
        ADD_DEPT = host + "d-add.do";
        DELETE_DEPT = host + "d-delete.do";
        FIND_DEPT = host + "d-find.do";

        GET_ALL_EVA_ITEMS = host + "e-getEvaluations.do";
        GET_EVA_DETAIL_OF_COURSE = host + "e-getEvaDetailOfCourse.do";
        UPDATE_EVA_OF_COURSE = host + "e-updateEvaluationOfCourse.do";
        UPDATE_EVA = host + "e-update.do";
        DELETE_EVA = host + "e-delete.do";
        ADD_EVA = host + "e-add.do";
        FIND_EVA = host + "e-find.do";

        GET_ALL_MANAGE_PAGE = host + "m-getAllByPage.do";
        ADD_MANAGE = host + "m-add.do";
        DELETE_MANAGE = host + "m-delete.do";
        UPDATE_MANAGE = host + "m-update.do";
        FIND_MANAGE = host + "m-find.do";
        LOGIN_MANAGE = host + "m-login.do";

        GET_ALL_COURSE_OF_STD = host + "sc-getAllCourseOfStudent.do";
        GET_EVA_DETAIL = host + "sc-getEvaDetail.do";
        DEGREE_DETAIL = host + "sc-degreeDetail.do";
        UPDATE_DEGREE_ITEM = host + "sc-updateDegreeItem.do";
        UPDATE_DEGREE_LIST = host + "sc-updateDegree.do";
        GET_UNDEGREE_STD_OF_COURSE = host + "sc-getUndgreedStudentOfCourse.do";
        DELETE_STDCRS_LIST = host + "sc-deleteList.do";
        GET_STDCRS_OF_STD = host + "sc-getStdCrsOfStudent.do";
        GET_ALL_EVA_DEGREE_OF_COURSE = host + "sc-getAllEvaDegreeOfCourse.do";
        GET_ALL_DEGREE_OF_COURSE = host + "sc-getAllDegreeOfCourse.do";
        ADD_STDCRS_LIST = host + "sc-addList.do";
        DELETE_STDCRS = host + "sc-delete.do";
        FIND_STDCRS = host + "sc-find.do";
        QUERY = host + "m-query.do";
        QUERY_GET = host + "m-queryGet.do";
        EVALUATIONS = "evaluation";

    }

}

