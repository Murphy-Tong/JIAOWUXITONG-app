package com.example.tong.jiaowuxitong.view.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.VOOpinion;
import com.example.tong.jiaowuxitong.entity.VOStdCrs;
import com.example.tong.jiaowuxitong.net.IOUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.example.tong.jiaowuxitong.view.fragment.EvaResultFragment;
import com.example.tong.jiaowuxitong.view.fragment.EvaSubmitFragment;
import com.example.tong.jiaowuxitong.view.fragment.EvaluationFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.EventBus;

@ContentView(value = R.layout.activity_course_evaluation)
public class CourseEvaluationActivity extends BaseActivity {
    @ViewInject(value = R.id.toolBar)
    private Toolbar toolbar;
    @ViewInject(value = R.id.fab)
    private FloatingActionButton floatingActionButton;

    public static String[] choose;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    private Fragment mainFragment;
    public static VOStdCrs voStdCrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setSupportActionBar(toolbar);
        setUpToorBar(getString(R.string.course_eva));

        voStdCrs = (VOStdCrs) getIntent().getExtras().get("obj");
        Bundle bundle = new Bundle();
        bundle.putSerializable("obj", voStdCrs);

        mainFragment = EvaluationFragment.instantiate(getApplicationContext(), EvaluationFragment.class.getName(), bundle);
        currentFragment = mainFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.container, currentFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.eva_save) {//保存选项
            ViewTool.showSnack(this, getWindow().getDecorView(),getString(R.string.save_ok));
            saveChoose();
            return true;
        } /*else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void saveChoose() {
        IOUtil.writeStringSet(this, choose, OPTSFILE, EVA_CHOOSE + voStdCrs.getId(), null);
        finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        //当前页面是学生评教提交页面  按下了返回键 提示是否要保存一下信息
        if (currentFragment.getClass().getName().equals(EvaSubmitFragment.class.getName()) && choose != null && choose.length > 0 && !TextUtils.isEmpty(choose[0]) && Float.parseFloat(choose[0]) >= 0) {
            showAlert();
            return;
        }
        if (currentFragment.getClass().getName().equals(EvaluationFragment.class.getName()) && choose != null && choose.length > 0 && !TextUtils.isEmpty(choose[0]) && Float.parseFloat(choose[0]) >= 0) {
            showAlert();
            return;
        } else if (currentFragment.getClass().getName().equals(EvaResultFragment.class.getName())) {//当前是学生评教结果页面 发送一个stickyMessage以便回到主界面后更新信息
            Message message = new Message(Message.NOTIFYCHANGED, voStdCrs);
            EventBus.getDefault().postSticky(message);
            EventBus.getDefault().unregister(this);
            finishAfterTransition();
            return;
        }
        super.onBackPressed();

    }

    public static final String EVA_CHOOSE = "EVA_CHOOSE";
    public static final String OPTSFILE = "opts";

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_now);
        builder.setPositiveButton(R.string.save, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewTool.showToast(CourseEvaluationActivity.this, getString(R.string.save_ok), Toast.LENGTH_SHORT);
                IOUtil.writeStringSet(getApplicationContext(), choose, OPTSFILE, EVA_CHOOSE + voStdCrs.getId(), null);
                finishAfterTransition();
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAfterTransition();
                    }
                }).show();
    }


    public final static int CHANGE_CONTENT_TAG = 130;
    public final static int CHANGE_CONTENT_TAG_RESULT = 132;

    private Fragment currentFragment;


    @Override
    public void onGet(Message msg) {
        if (msg != null && msg.tag == CHANGE_CONTENT_TAG) {//切换到提交界面
            Bundle bundle = new Bundle();
            if (msg.msg != null) {
                //TODO
                VOOpinion voOpinion = (VOOpinion) msg.msg;
                bundle.putSerializable("opts", voOpinion);
                Fragment fragment = EvaSubmitFragment.instantiate(getApplicationContext(), EvaSubmitFragment.class.getName(), bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(currentFragment);
                if (!fragment.isAdded()) {
                    fragmentTransaction.add(R.id.container, fragment);
                }
//                fragmentTransaction.addToBackStack("b");
                fragmentTransaction.show(fragment);
                fragmentTransaction.commit();
                currentFragment = fragment;
            }
        }

        if (msg != null && msg.tag == CHANGE_CONTENT_TAG_RESULT) {//切换的结果界面

            VOOpinion voOpinion = (VOOpinion) msg.msg;
            Bundle bundle = new Bundle();
            bundle.putSerializable("body", voStdCrs);
            Fragment fragment = EvaResultFragment.instantiate(getApplicationContext(), EvaResultFragment.class.getName(), bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (!fragment.isAdded()) {
                fragmentTransaction.add(R.id.container, fragment);
            }
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();
            currentFragment = fragment;
        }
    }


}
