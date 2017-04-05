package com.example.tong.jiaowuxitong.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.ActionState;
import com.example.tong.jiaowuxitong.entity.VOOpinion;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.IOUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.net.NetUtil;
import com.example.tong.jiaowuxitong.view.custom.StringUtils;
import com.example.tong.jiaowuxitong.view.custom.ViewTool;
import com.example.tong.jiaowuxitong.view.views.BaseActivity;
import com.example.tong.jiaowuxitong.view.views.CourseEvaluationActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by TONG on 2017/1/16.
 * 学生评教结果提交页
 */
@ContentView(R.layout.eva_submit_layout)
public class EvaSubmitFragment extends BaseFragment {

    private static EvaSubmitFragment evaSubmitFragment;

    public static EvaSubmitFragment getInstance() {
        if (EvaSubmitFragment.evaSubmitFragment == null) {
            EvaSubmitFragment.evaSubmitFragment = new EvaSubmitFragment();
        }
        return EvaSubmitFragment.evaSubmitFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private VOOpinion voOpinion;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            //获取评教总分
            voOpinion = (VOOpinion) bundle.getSerializable("opts");
            if (voOpinion == null) finishAfterTransition();
            float total = 0.0f;
            total += voOpinion.getOpt1();
            total += voOpinion.getOpt2();
            total += voOpinion.getOpt3();
            total += voOpinion.getOpt4();
            total += voOpinion.getOpt5();
            total += voOpinion.getOpt6();
            total += voOpinion.getOpt7();
            total += voOpinion.getOpt8();
            total += voOpinion.getOpt9();
            total += voOpinion.getOpt10();
            total += voOpinion.getOpt11();
            if (tv != null)
                tv.setText(String.format(getResources().getString(R.string.your_evaluation_total), StringUtils.formatFloat(total,3)));
            //本地是否有保存评教额外信息
            String s = IOUtil.readString(mContext, OPT_EXTRA, OPT_EXTRA + voOpinion.getStdCrsId());
            if (!TextUtils.isEmpty(s) && et != null) {
                et.setText(s);
            }
        }
    }

    @ViewInject(value = R.id.eva_total)
    private TextView tv;
    @ViewInject(value = R.id.extra_opt)
    private EditText et;
/*
    @ViewInject(value = R.id.eva_submit)
    private Button submit;
*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
/*

        View view = inflater.inflate(R.layout.eva_submit_layout,container,false);
        submit = (Button) view.findViewById(R.id.eva_submit);
        et = (EditText) view.findViewById(R.id.extra_opt);
        tv = (TextView) view.findViewById(R.id.eva_total);
        submit.setOnClickListener(this);
*/
        View view = x.view().inject(this, inflater, container);
        return view;
    }

    private final int EVA_UPDATE_TAG = 67;

    @Event(type = View.OnClickListener.class, value = {R.id.eva_submit, R.id.eva_save, R.id.eva_clear})
    private void onClick(View v) {
        if (et != null) {
            voOpinion.setOpinion(et.getText().toString().trim());
        }
        if (v.getId() == R.id.eva_submit) {
//            TestUtil.toast(context, "submit");
            ViewTool.getAlertFragmentInstance(getChildFragmentManager(), mContext, AlertFragment.MODE_WITH_TEXT, false);
            NetUtil.asyncPost(GsonUtil.toJson(voOpinion), GlobalResource.UPDATE_EVA_OF_COURSE, EVA_UPDATE_TAG);

        } else if (v.getId() == R.id.eva_clear) {
            clearChoose();
            finishAfterTransition();
        } else if (v.getId() == R.id.eva_save) {
            writeFile();
//            TestUtil.toast(context, "save");
            ViewTool.showSnack(mContext, getView(), mContext.getString(R.string.save_succ));
        }
    }

    private void clearChoose() {
        for (int i = 0; i < CourseEvaluationActivity.choose.length; i++) {
            CourseEvaluationActivity.choose[i] = "";
        }
        IOUtil.delete(mContext, CourseEvaluationActivity.OPTSFILE, CourseEvaluationActivity.EVA_CHOOSE + voOpinion.getId());
        IOUtil.delete(mContext,  OPT_EXTRA, OPT_EXTRA + voOpinion.getStdCrsId());
//        IOUtil.writeStringSet(mContext, CourseEvaluationActivity.choose, CourseEvaluationActivity.OPTSFILE, CourseEvaluationActivity.EVA_CHOOSE + voOpinion.getStdCrsId(), null);
    }


    private final static String OPT_EXTRA = "OPT_EXTRA";

    /**
     * 保存评教信息
     */
    private void writeFile() {
        String s = et.getText().toString().trim();
        if (!TextUtils.isEmpty(s)) {
            IOUtil.writeString(mContext, s, OPT_EXTRA, OPT_EXTRA + voOpinion.getStdCrsId(), null);
        }
        IOUtil.writeStringSet(mContext, CourseEvaluationActivity.choose, CourseEvaluationActivity.OPTSFILE, CourseEvaluationActivity.EVA_CHOOSE + voOpinion.getStdCrsId(), null);
    }


    /**
     * 提交后，处理服务器返回的处理结果
     * @param msg
     */
    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message msg) {
        if (msg != null && msg.tag == EVA_UPDATE_TAG) {
            ActionState actionState = GsonUtil.fromJson((String) msg.msg, ActionState.class);
            ViewTool.setmAlertFragmentCancelAble(true);
//            ViewTool.showAlertFragmentText(-1);
            if (actionState != null) {
                if (actionState.tag == ActionState.ACTION_SUCCESS) {
                    final Message message = new Message(CourseEvaluationActivity.CHANGE_CONTENT_TAG_RESULT, null);
                    ViewTool.setmAlertFragmentText(mContext.getString(R.string.success));
                    ViewTool.setAlertFragmentCallback(new AlertFragment.Ondismiss() {
                        @Override
                        public void onDismiss() {

                            EventBus.getDefault().post(message);
                        }
                    });
                    ViewTool.setmAlertFragmentCancelAble(true);
                    ViewTool.showAlertFragmentText(-1);
                    clearChoose();
                } else if (actionState.tag == ActionState.ACTION_FAILED) {
                    ViewTool.showAlert(mContext, mContext.getString(R.string.submit_failed));
                    ViewTool.setmAlertFragmentText(mContext.getString(R.string.failed));
                    ViewTool.setmAlertFragmentCancelAble(true);
                    ViewTool.showAlertFragmentText(-1);
                }
            }else {
                ViewTool.dismissAlertFragment();
            }

        }
    }


}
