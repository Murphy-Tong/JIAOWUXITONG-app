package com.example.tong.jiaowuxitong.view.views;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOCourse;
import com.example.tong.jiaowuxitong.entity.VODepartment;
import com.example.tong.jiaowuxitong.entity.VOStudent;
import com.example.tong.jiaowuxitong.entity.VOTeacher;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Md5Digest;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.net.TextUtil;
import com.example.tong.jiaowuxitong.view.LoadingWhat;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.example.tong.jiaowuxitong.view.fragment.AlertFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

@ContentView(R.layout.activity_add)
public class AddActivity extends BaseActivity {

    private static final int ADD = 760;
    private static final int ADD_COURSE = 761;
    private static final int ADD_TEACHER = 762;
    private static final int ADD_STD = 763;
    private static final int ADD_DEPT = 764;
    @ViewInject(R.id.add_t_name)
    private EditText tname;
    @ViewInject(R.id.add_t_password)
    private EditText tpwd;
    @ViewInject(R.id.add_t_password_confrim)
    private EditText tpwdCfm;
    @ViewInject(R.id.add_t_alter_button)
    private ImageButton talter;
    @ViewInject(R.id.add_t_result)
    private TextView talterResult;
    @ViewInject(R.id.add_t_sex_radiogroup)
    private RadioGroup tradioGroup;

    @ViewInject(R.id.add_c_name)
    private EditText cname;
    @ViewInject(R.id.add_c_alter_button)
    private ImageButton calter;
    @ViewInject(R.id.add_c_result)
    private TextView calterResult;
    @ViewInject(R.id.add_c_result_extra)
    private TextView calterResultExtra;

    @ViewInject(R.id.add_s_name)
    private EditText sname;
    @ViewInject(R.id.add_s_password)
    private EditText spwd;
    @ViewInject(R.id.add_s_password_confrim)
    private EditText spwdCfm;
    @ViewInject(R.id.add_s_sex_radiogroup)
    private RadioGroup sradioGroup;

    @ViewInject(R.id.add_d_name)
    private EditText dname;

    @ViewInject(R.id.fab)
    private FloatingActionButton fab;

    @ViewInject(R.id.student)
    private View stdView;

    @ViewInject(R.id.dept)
    private View deptView;
    @ViewInject(R.id.course)
    private View courseView;
    @ViewInject(R.id.teacher)
    private View thrView;
    @ViewInject(R.id.toolBar)
    private Toolbar toolbar;
    private int curentAction;

    private VODepartment choosedDept;
    private VOTeacher choosedTeacher;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setExit = false;
//        setEnter();
        setShareEnter();
        setShareExit();
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setSupportActionBar(toolbar);

//        getWindow().getDecorView().setVisibility(View.VISIBLE);
        parseIntent();

    }


    private int action = -1;
    private int mTag;
    private VOCourse course;

    private void parseIntent() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            action = bundle.getInt(LoadingWhat.ACTION);
            curentAction = bundle.getInt(LoadingWhat.LOADING_WHAT);
            Serializable serializable = bundle.getSerializable("body");
            if (curentAction == LoadingWhat.COURSES) {
                courseView.setVisibility(View.VISIBLE);
                setUpToorBar(getString(R.string.add_crs));
                if (serializable != null && serializable instanceof VOTeacher) {
                    choosedTeacher = (VOTeacher) serializable;
                    calterResult.setText(choosedTeacher.getName());
                    calterResultExtra.setText(choosedTeacher.getDepartmentName());
                    calter.setEnabled(false);
                    calter.setEnabled(false);
                }
            } else if (curentAction == LoadingWhat.STUDENTS) {
                stdView.setVisibility(View.VISIBLE);
                setUpToorBar(getString(R.string.add_std));
            } else if (curentAction == LoadingWhat.TEACHERS) {
                thrView.setVisibility(View.VISIBLE);
                setUpToorBar(getString(R.string.add_thr));
            } else if (curentAction == LoadingWhat.DEPTS) {
                deptView.setVisibility(View.VISIBLE);
                setUpToorBar(getString(R.string.add_dept));
            }

            if (action == LoadingWhat.ALTER) {
                mTag = bundle.getInt(LoadingWhat.LOADING_WHAT);
                if (serializable != null && LoadingWhat.COURSES == mTag && serializable instanceof VOCourse) {
                    course = (VOCourse) serializable;
                    cname.setText(course.getName());
                    calterResult.setText(course.getTeacherName());
                    calterResultExtra.setText(course.getDepartmentName());
                    choosedTeacher = new VOTeacher();
                    choosedTeacher.setId(course.getTeacherId());
                    choosedTeacher.setDepartmentId(course.getDepartmentId());
                    choosedTeacher.setName(course.getTeacherName());
                    choosedTeacher.setDepartmentName(course.getDepartmentName());
                }
            }
        }

    }

    @Event(type = View.OnClickListener.class, value = {R.id.fab, R.id.add_c_alter_button, R.id.add_t_alter_button})
    private void tryReg(View v) {
        if (v.getId() == R.id.fab) {
            switch (curentAction) {
                case LoadingWhat.STUDENTS:
                    addStd();
                    break;
                case LoadingWhat.COURSES:
                    addCourse();
                    break;
                case LoadingWhat.DEPTS:
                    addDept();
                    break;
                case LoadingWhat.TEACHERS:
                    addTeacher();
                    break;
            }
        } else if (v.getId() == R.id.add_c_alter_button) {
            chooseTeacher();
        } else if (v.getId() == R.id.add_t_alter_button) {
            chooseDept();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void chooseDept() {
        Intent intent = new Intent(this, ListSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.DEPTS);
        bundle.putInt(LoadingWhat.ACTION, LoadingWhat.FORRESULT);
        bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_DEPT_PAGE);
        intent.putExtra("bundle", bundle);
//        startActivityForResult(intent,1, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        startActivityForResult(intent, 1);

    }

    private void chooseTeacher() {
        Intent intent = new Intent(this, ListSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(LoadingWhat.LOADING_WHAT, LoadingWhat.TEACHERS);
        bundle.putInt(LoadingWhat.ACTION, LoadingWhat.FORRESULT);
        bundle.putString(LoadingWhat.LOAD_URL, GlobalResource.GET_ALLTEACHER_PAGE);
        intent.putExtra("bundle", bundle);
//        startActivityForResult(intent,1, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        startActivityForResult(intent, 1);

    }


    private void addCourse() {
        String name = cname.getText().toString().trim();
        cname.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            cname.setError(getString(R.string.error_field_required));
            focusView = cname;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return;
        }
        if (choosedTeacher == null) {
            ViewTool.showSnack(null, courseView, getString(R.string.pls_choose_thr));
            return;
        }

        VOCourse voCourse = new VOCourse();
        voCourse.setName(name);
        voCourse.setTeacherId(choosedTeacher.getId());
        showAlert();
        NetUtil.asyncPost(GsonUtil.toJson(voCourse), GlobalResource.ADD_COURSE, ADD_COURSE);

    }

    private void addStd() {
        String name = sname.getText().toString().trim();
        sname.setError(null);
        String pwd = spwd.getText().toString();
        String pwdcfm = spwdCfm.getText().toString();
        spwdCfm.setError(null);
        spwd.setError(null);


        if (TextUtils.isEmpty(name)) {
            sname.setError(getString(R.string.error_field_required));
            sname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            spwd.setError(getString(R.string.error_field_required));
            spwd.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(pwdcfm)) {
            spwdCfm.setError(getString(R.string.error_field_required));
            spwdCfm.requestFocus();
            return;
        }

        if (!pwd.equals(pwdcfm)) {
            spwdCfm.setError(getString(R.string.pwd_not_same));
            spwdCfm.requestFocus();
            return;
        }

        View viewById = sradioGroup.findViewById(sradioGroup.getCheckedRadioButtonId());
        if (viewById == null || !(viewById instanceof RadioButton)) {
            sradioGroup.requestFocus();
            ViewTool.showSnack(null, stdView, getString(R.string.pls_choose_gender));
            return;
        }

        VOStudent voStudent = new VOStudent();
        voStudent.setName(name);
        voStudent.setPassword(new Md5Digest("jwxt", "MD5").encode(pwd));
        voStudent.setGender(Integer.parseInt(sradioGroup.findViewById(sradioGroup.getCheckedRadioButtonId()).getTag().toString().trim()));
        showAlert();
        NetUtil.asyncPost(GsonUtil.toJson(voStudent), GlobalResource.ADD_STD, ADD_STD);
    }

    private void addDept() {
        dname.setError(null);
        String name = dname.getText().toString().trim();
        if (TextUtil.isEmpty(name)) {
            dname.setError(getString(R.string.error_field_required));
            dname.requestFocus();
            return;
        }
        VODepartment voDepartment = new VODepartment();
        voDepartment.setName(name);
        showAlert();
        NetUtil.asyncPost(GsonUtil.toJson(voDepartment), GlobalResource.ADD_DEPT, ADD_DEPT);
    }


    private void showAlert() {
        ViewTool.getAlertFragmentInstance(getSupportFragmentManager(), this, AlertFragment.MODE_WITH_TEXT, false);
    }

    private void addTeacher() {
        String name = tname.getText().toString().trim();
        tname.setError(null);
        String pwd = tpwd.getText().toString();
        String pwdcfm = tpwdCfm.getText().toString();
        tpwdCfm.setError(null);
        tpwd.setError(null);


        if (TextUtils.isEmpty(name)) {
            tname.setError(getString(R.string.error_field_required));
            tname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            tpwd.setError(getString(R.string.error_field_required));
            tpwd.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(pwdcfm)) {
            tpwdCfm.setError(getString(R.string.error_field_required));
            tpwdCfm.requestFocus();
            return;
        }

        if (!pwd.equals(pwdcfm)) {
            tpwdCfm.setError(getString(R.string.pwd_not_same));
            tpwdCfm.requestFocus();
            return;
        }

        View viewById = tradioGroup.findViewById(tradioGroup.getCheckedRadioButtonId());
        if (viewById == null || !(viewById instanceof RadioButton)) {
            tradioGroup.requestFocus();
            ViewTool.showSnack(null, thrView, getString(R.string.pls_choose_gender));
            return;
        }

        if (choosedDept == null) {
            talter.requestFocus();
            ViewTool.showSnack(null, thrView, getString(R.string.pls_choose_dept));
            return;
        }

        VOTeacher voTeacher = new VOTeacher();
        voTeacher.setName(name);
        voTeacher.setPassword(new Md5Digest("jwxt", "MD5").encode(pwd));
        voTeacher.setGender(Integer.parseInt(tradioGroup.findViewById(tradioGroup.getCheckedRadioButtonId()).getTag().toString().trim()));
        voTeacher.setDepartmentId(choosedDept.getId());
        showAlert();
        NetUtil.asyncPost(GsonUtil.toJson(voTeacher), GlobalResource.ADD_TEACHER, ADD_TEACHER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            Object o = data.getSerializableExtra("body");
            if (o == null) return;
            if (curentAction == LoadingWhat.COURSES && o instanceof VOTeacher) {
                choosedTeacher = (VOTeacher) o;
                calterResult.setText(choosedTeacher.getName());
                calterResultExtra.setText(choosedTeacher.getDepartmentName());
            } else if (curentAction == LoadingWhat.TEACHERS && o instanceof VODepartment) {
                choosedDept = (VODepartment) o;
                talterResult.setText(choosedDept.getName());
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message message) {
        if (message != null && message.tag == ADD_COURSE) {
            final VOCourse voCourse = GsonUtil.fromJson((String) message.msg, VOCourse.class);

            ViewTool.setmAlertFragmentText(getString(R.string.add_succ));
            ViewTool.showAlertFragmentText(-1);
            ViewTool.setmAlertFragmentCancelAble(true);
            ViewTool.setAlertFragmentCallback(new AlertFragment.Ondismiss() {
                @Override
                public void onDismiss() {
                    if (action == LoadingWhat.FORRESULT) {
                        onResult(voCourse);
                    }
                }
            });
        } else if (message != null && message.tag == ADD_DEPT) {
            final VODepartment voDepartment = GsonUtil.fromJson((String) message.msg, VODepartment.class);

            ViewTool.setmAlertFragmentText(getString(R.string.add_succ));
            ViewTool.showAlertFragmentText(-1);
            ViewTool.setmAlertFragmentCancelAble(true);
            ViewTool.setAlertFragmentCallback(new AlertFragment.Ondismiss() {
                @Override
                public void onDismiss() {
                    if (action == LoadingWhat.FORRESULT) {
                        onResult(voDepartment);
                    }
                }
            });
        } else if (message != null && message.tag == ADD_STD) {
            final VOStudent voStudent = GsonUtil.fromJson((String) message.msg, VOStudent.class);

            if (voStudent.getId() != -1) {
                ViewTool.setmAlertFragmentText("add success!\t\n id:" + voStudent.getId());
            } else {
                ViewTool.setmAlertFragmentText(getString(R.string.add_fail));
            }
            ViewTool.showAlertFragmentText(-1);
            ViewTool.setmAlertFragmentCancelAble(true);
            ViewTool.setAlertFragmentCallback(new AlertFragment.Ondismiss() {
                @Override
                public void onDismiss() {
                    if (action == LoadingWhat.FORRESULT) {
                        onResult(voStudent);
                    }
                }
            });
        } else if (message != null && message.tag == ADD_TEACHER) {
            final VOTeacher voTeacher = GsonUtil.fromJson((String) message.msg, VOTeacher.class);

            if (voTeacher.getId() != -1) {
                ViewTool.setmAlertFragmentText("add success!\t\n id:" + voTeacher.getId());
            } else {
                ViewTool.setmAlertFragmentText(getString(R.string.add_fail));
            }
            ViewTool.showAlertFragmentText(-1);
            ViewTool.setmAlertFragmentCancelAble(true);
            ViewTool.setAlertFragmentCallback(new AlertFragment.Ondismiss() {
                @Override
                public void onDismiss() {
                    if (action == LoadingWhat.FORRESULT) {
                        onResult(voTeacher);
                    }
                }
            });
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onResult(Serializable serializable) {
        if (serializable != null) {
            Intent intent = new Intent();
            intent.putExtra("body", serializable);
            setResult(2, intent);
            finishAfterTransition();
        }
    }
}
