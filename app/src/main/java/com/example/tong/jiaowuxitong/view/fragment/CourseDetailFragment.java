package com.example.tong.jiaowuxitong.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.TestUtil;
import com.example.tong.jiaowuxitong.entity.VOManager;
import com.example.tong.jiaowuxitong.entity.VOStudent;
import com.example.tong.jiaowuxitong.entity.VOTeacher;
import com.example.tong.jiaowuxitong.entity.VOUser;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by TONG on 2017/1/8.
 * 课程详情页  并没有使用
 */
public class CourseDetailFragment extends BaseFragment {
    private static CourseDetailFragment courseDetailFragment = new CourseDetailFragment();

    private static int crsId;
    private static String userStr;
    private static int tag;

    public static CourseDetailFragment InstanceCourseFragment(int crsId, String userStr, int tag) {
        CourseDetailFragment.crsId = crsId;
        CourseDetailFragment.userStr = userStr;
        CourseDetailFragment.tag = tag;
        return courseDetailFragment;
    }

    /**
     * 获取USER_TAG
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(userStr)) {
            switch (tag) {
                case VOUser.STUDENT_TAG:
                    VOStudent studentEntity = GsonUtil.fromJson(userStr, VOStudent.class);
                    break;
                case VOUser.MANAGER_TAG:
                    VOManager managerEntity = GsonUtil.fromJson(userStr, VOManager.class);
                    break;
                case VOUser.TEACHER_TAG:
                    VOTeacher teacherEntity = GsonUtil.fromJson(userStr, VOTeacher.class);
                    break;
            }
            NetUtil.asyncPost("course", GlobalResource.host + "/myapp/course?action=get&tag=" + tag + "&crsId=" + crsId, Message.COURSE_DETAIL_STUDENT);
        } else {
            //TODO
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message msg) {
        if (msg != null && msg.tag == Message.COURSE_DETAIL_STUDENT) {
            TestUtil.log("course detail : ", (String)msg.msg);
        }
    }

}
