package com.example.tong.jiaowuxitong.view.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VOStdCrs;
import com.example.tong.jiaowuxitong.entity.VOUser;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.example.tong.jiaowuxitong.view.fragment.CourseDegreeChartDiolagFragment;
import com.example.tong.jiaowuxitong.view.fragment.CourseDetailFragment;
import com.example.tong.jiaowuxitong.view.fragment.UserFragment;
import com.example.tong.jiaowuxitong.view.fragment.adapter.OnListFragmentInteractionListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 学生 和 教师 主界面 根据userTag
 */
@ContentView(value = R.layout.activity_user)
public class UserActivity extends BaseActivity implements OnListFragmentInteractionListener {

    private String userStr;
    private VOUser user;
    @ViewInject(R.id.container)
    private View container;
    private Fragment currFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setEnter();
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getWindow().getDecorView().setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        String userStr = getIntent().getStringExtra("user");
        if (TextUtils.isEmpty(userStr)) {
            finishAfterTransition();
        }

        user = GsonUtil.fromJson(userStr, VOUser.class);
        if (user != null) {
//            EventBus.getDefault().register(this);
            tag = getIntent().getIntExtra("tag", -1);
            user.setTAG(tag);
            if ((tag == VOUser.STUDENT_TAG || tag == VOUser.TEACHER_TAG)) {
                getSupportActionBar().setTitle(user.getName());
                Bundle bundle = new Bundle();
                bundle.putString("user", userStr);
                bundle.putInt("tag", tag);
                setContent(bundle);
            }
        }

    }


    private int tag;

    private void setContent(Bundle bundle) {

//        b_switch (tag) {
//            case VOUser.TEACHER_TAG:
//                currFragment = TeacherFragment.instantiate(this, TeacherFragment.class.getName(), bundle);
//                break;
//            case VOUser.STUDENT_TAG:
        currFragment = UserFragment.instantiate(this, UserFragment.class.getName(), bundle);
//                break;
//            case VOUser.MANAGER_TAG:
//                currFragment = ManagerFragment.instantiate(this, ManagerFragment.class.getName(), bundle);
//                break;
//        }
        getSupportFragmentManager().beginTransaction().add(R.id.container, currFragment).commit();
    }


    @Override
    public void onListFragmentInteraction(Object o, View view) {
        if (user.getTAG() == VOUser.STUDENT_TAG && o instanceof VOStdCrs) {//当前是学生界面
            VOStdCrs voStdCrs = (VOStdCrs) o;
            if (voStdCrs.getEvaDegree() <= 0) {//评教成绩<0 （未评教）
                evaluateStdCrs(voStdCrs);//去评教
            } else {
                StdCrsDetail(voStdCrs, view);//查看成绩
            }

        } else if (user.getTAG() == VOUser.TEACHER_TAG && o instanceof VOCourse) {//当前是教师界面
            VOCourse voCourse = (VOCourse) o;
            if (voCourse.getUnDegreeStudentCount() > 0) {//还有未给成绩学生
                degreeStudent(voCourse);//去打分
            } else {
                courseDetail(voCourse);//去查看课程详情
            }
        }

    }

    /**
     * 去查看课程详情
     *
     * @param voCourse
     */
    private void courseDetail(VOCourse voCourse) {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra("course", voCourse);
        startActivity(intent);
    }

    /**
     * 去未学生打分
     *
     * @param voCourse
     */
    private void degreeStudent(final VOCourse voCourse) {
        ViewTool.showAlert(this, voCourse.getUnDegreeStudentCount() + getString(R.string.degree_std_alert), getString(R.string.yes_button_text), getString(R.string.no_button_text), new ViewTool.CallBack() {
            @Override
            public void onPositiveChoose() {
                Intent intent = new Intent(getApplicationContext(), InputDegreeActivity.class);
                intent.putExtra("course", voCourse);
                startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * 学生查看成绩
     *
     * @param voStdCrs
     * @param view
     */
    private void StdCrsDetail(VOStdCrs voStdCrs, View view) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("stdcrs", voStdCrs);

        final CourseDegreeChartDiolagFragment dialogFragment = (CourseDegreeChartDiolagFragment) DialogFragment.instantiate(this, CourseDegreeChartDiolagFragment.class.getName(), bundle);
//        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_DialogWhenLarge);
//        dialogFragment.set
//        dialogFragment.getDialog().getWindow().
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        dialogFragment.show(fragmentTransaction, CourseDegreeChartDiolagFragment.class.getName());
//        dialogFragment.
//        View decorView = dialogFragment.mView;
//        decorView.setVisibility(View.INVISIBLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Animator circularReveal = ViewAnimationUtils.createCircularReveal(decorView, (view.getRight() + view.getLeft()) / 2, (view.getTop() + view.getBottom()) / 2, 0, decorView.getHeight());
//            circularReveal.setDuration(1000);
//            circularReveal.start();
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                dialogFragment.setEnterTransition(new Slide(Gravity.BOTTOM));
//            }
//            dialogFragment.show(getSupportFragmentManager(), CourseDegreeChartDiolagFragment.class.getName());
//        }
    }


    /**
     * 前往评教
     *
     * @param voStdCrs
     */
    private void evaluateStdCrs(final VOStdCrs voStdCrs) {
        ViewTool.showAlert(this, getString(R.string.eva_course_alert), getString(R.string.yes_button_text), getString(R.string.no_button_text), new ViewTool.CallBack() {
            @Override
            public void onPositiveChoose() {
                Intent intent = new Intent(getApplicationContext(), CourseEvaluationActivity.class);
                intent.putExtra("obj", voStdCrs);
                startActivity(intent);
            }
        });
    }

    /**
     * 改变当前的fragment
     *
     * @param crsId
     */
    private void changeContent(int crsId) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(currFragment);
        CourseDetailFragment courseDetailFragment = CourseDetailFragment.InstanceCourseFragment(crsId, userStr, user.TAG);
        if (!courseDetailFragment.isAdded()) {
            transaction.add(R.id.container, courseDetailFragment);
        }
        transaction.show(courseDetailFragment);
        transaction.commit();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                finish = false;
            }
        }
    };
    private boolean finish = false;

    /**
     * 此方法以抽取到BaseActivity
     */
    @Override
    public void onBackPressed() {
        if (finish) {
            this.finish();
        } else {
            finish = true;
            Toast.makeText(this, R.string.exit_app_toast, Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(1, 1000);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currFragment.onActivityResult(requestCode, resultCode, data);
    }
}
