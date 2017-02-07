package com.example.tong.jiaowuxitong.view.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOManager;
import com.example.tong.jiaowuxitong.entity.VOStudent;
import com.example.tong.jiaowuxitong.entity.VOTeacher;
import com.example.tong.jiaowuxitong.entity.VOUser;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.IOUtil;
import com.example.tong.jiaowuxitong.net.Md5Digest;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.net.TextUtil;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.google.gson.Gson;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

@ContentView(R.layout.layout_login)
public class MainActivity extends BaseActivity {


    @ViewInject(value = R.id.type_group)
    private RadioGroup radioGroup;
    @ViewInject(R.id.pb)
    private ProgressBar pb;
    @ViewInject(R.id.error_text)
    private TextView errorText;
    @ViewInject(value = R.id.username)
    private EditText username;
    @ViewInject(R.id.host)
    private TextView host;
    @ViewInject(value = R.id.password)
    private EditText password;

    @ViewInject(value = R.id.login)
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_login);
        x.view().inject(this);
//        EventBus.getDefault().register(this);
        host.setText(GlobalResource.host);
        pb.setVisibility(View.INVISIBLE);

    }


    @Event(type = View.OnClickListener.class, value = R.id.login)
    private void tryLogin(View v) {

        String un = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        // Reset errors.
        password.setError(null);
        username.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(un)) {
            username.setError(getString(R.string.error_field_required));
            focusView = username;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (!cancel && TextUtils.isEmpty(pwd)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
            login(Integer.parseInt(un), pwd);
//            TestUtil.toast(this,"login");
        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message msg) {
        pb.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.INVISIBLE);
        radioGroup.setEnabled(true);
        this.username.setEnabled(true);
        this.password.setEnabled(true);
        login.setEnabled(true);
        if (msg != null && msg.tag == Message.LOGIN) {
            handleSuccess((String) msg.msg);
        } else if (msg == null || (msg != null && msg.tag == Message.FAILED_TAG)) {
            errorText.setVisibility(View.VISIBLE);
        }
    }

    private void handleSuccess(String msg) {
        VOUser user = new Gson().fromJson(msg, VOUser.class);
        if (user != null && user.getId() == -1) {
            username.setError(getString(R.string.user_not_found));
            return;
        }

        if (user != null && user.getId() == -2) {
            password.setError(getString(R.string.pwd_error));
            return;
        }
        if (tag == VOUser.MANAGER_TAG) {
            Intent intent = new Intent(this, ManagerActivity.class);
            intent.putExtra("user", msg);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("user", msg);
            intent.putExtra("tag", tag);
            startActivity(intent);
        }

        saveUser(user);
        ViewTool.dismissAlertFragment();
//        EventBus.getDefault().unregister(this);
        this.finishAfterTransition();
    }

    private void saveUser(VOUser user) {
        IOUtil.writeObj(this, user, "user", null);
    }

    private int tag;

    private void login(int id, String password) {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        Md5Digest md5Digest = new Md5Digest("jwxt", "MD5");
        password = md5Digest.encode(password);
        String body = null;
        if (checkedId == R.id.login_as_manager) {
            tag = VOUser.MANAGER_TAG;
            loginUrl = GlobalResource.LOGIN_MANAGE;
            VOManager voManager = new VOManager();
            voManager.setId(id);
            voManager.setPassword(password);
            voManager.setTAG(tag);
            body = GsonUtil.toJson(voManager);
        } else if (checkedId == R.id.login_as_student) {
            tag = VOUser.STUDENT_TAG;
            loginUrl = GlobalResource.LOGIN_STD;
            VOStudent voStudent = new VOStudent();
            voStudent.setId(id);
            voStudent.setTAG(tag);
            voStudent.setPassword(password);
            body = GsonUtil.toJson(voStudent);
        } else if (checkedId == R.id.login_as_teacher) {
            tag = VOUser.TEACHER_TAG;
            loginUrl = GlobalResource.LOGIN_TEACHER;
            VOTeacher voTeacher = new VOTeacher();
            voTeacher.setId(id);
            voTeacher.setTAG(tag);
            voTeacher.setPassword(password);
            body = GsonUtil.toJson(voTeacher);
        }

//
//        ViewTool.getAlertFragmentInstance(getSupportFragmentManager(), this, AlertFragment.MODE_NORMAL, false);
        pb.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.INVISIBLE);
        radioGroup.setEnabled(false);
        this.username.setEnabled(false);
        this.password.setEnabled(false);
        login.setEnabled(false);

        NetUtil.asyncPost(body, loginUrl, Message.LOGIN);
    }


    private String loginUrl;


    @Event(type = View.OnClickListener.class, value = {R.id.host})
    private void radioGroupClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("set host");
        View view1 = View.inflate(this, R.layout.edittext, null);
        final TextView textView = (TextView) view1.findViewById(R.id.text);
        builder.setView(view1);
        textView.setText(GlobalResource.host);
        builder.setPositiveButton(getString(R.string.yes_button_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String url = textView.getText().toString().trim();
                if (TextUtil.isEmpty(url)) return;

                if (!url.startsWith("http://")) {
                    url = "http://" + url;
                }
                if (!url.endsWith("/")) {
                    url += "/";
                }
                host.setText(url);
                GlobalResource.setHost(url);
                IOUtil.writeString(MainActivity.this, GlobalResource.host, "host", "host", null);
            }
        });
        builder.show();

    }


}
