package com.example.tong.jiaowuxitong.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tong.jiaowuxitong.GlobalResource;
import com.example.tong.jiaowuxitong.R;

/**
 * Created by TONG on 2017/2/1.
 * 自定义的dialogfragment
 */
public class AlertFragment extends DialogFragment {

    private TextView textView;
    private ImageView ok;
    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setCancelable(false);
        View view = View.inflate(getContext(), R.layout.dialog_fragment_layout, null);
        textView = (TextView) view.findViewById(R.id.text);
        ok = (ImageView) view.findViewById(R.id.ok);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);
        ok.setVisibility(View.INVISIBLE);
        return view;
    }


    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public static final int MODE_NORMAL = 1;
    public static final int MODE_WITH_TEXT = 2;

    private int mMode = -1;

    public void setMode(int mode) {
        this.mMode = mode;
    }

    /**
     * 展示动画
     */
    public void showText() {

        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }else {
            textView.setText("");
        }
        float tans = ok.getX() - progressBar.getX() - (progressBar.getWidth() - ok.getWidth()) / 2;
        ok.setTranslationX(-tans);
        ok.setVisibility(View.VISIBLE);

        textView.setVisibility(View.VISIBLE);
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(progressBar, "scaleX", 1, 0);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(progressBar, "scaleY", 1, 0);
        ObjectAnimator trasition = ObjectAnimator.ofFloat(ok, "translationX", 0);
        trasition.setStartDelay(100);

        ObjectAnimator scaleAnimatorXOk = ObjectAnimator.ofFloat(ok, "scaleX", 0, 1);
        ObjectAnimator scaleAnimatorYOk = ObjectAnimator.ofFloat(ok, "scaleY", 0, 1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateInterpolator());

        if (mMode == MODE_NORMAL) {
            ObjectAnimator upOKNormal = ObjectAnimator.ofFloat(ok, "translationY", -textView.getHeight());
            upOKNormal.setInterpolator(new LinearInterpolator());
            upOKNormal.setStartDelay(300);

            ObjectAnimator textAlpha = ObjectAnimator.ofFloat(textView, "alpha", 0, 1);

            animatorSet.play(upOKNormal)
                    .after(scaleAnimatorX)
                    .after(scaleAnimatorY)
                    .after(scaleAnimatorXOk)
                    .after(scaleAnimatorYOk)
                    .before(textAlpha);

        } else if (MODE_WITH_TEXT == mMode) {
            textView.setTranslationY(-textView.getY() - textView.getHeight()-100);

            ObjectAnimator upOK = ObjectAnimator.ofFloat(ok, "translationY", -ok.getY(), 0);
            upOK.setInterpolator(new AccelerateDecelerateInterpolator());
            upOK.setStartDelay(300);

            ObjectAnimator downAnimator = ObjectAnimator.ofFloat(textView, "translationY", 0);
            downAnimator.setInterpolator(new DecelerateInterpolator());
            animatorSet.play(upOK)
                    .after(scaleAnimatorX)
                    .after(scaleAnimatorY)
                    .after(scaleAnimatorXOk)
                    .after(scaleAnimatorYOk)
                    .before(trasition)
                    .before(downAnimator);
        }
        animatorSet.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMode == MODE_WITH_TEXT) {
            getDialog().getWindow().setLayout(GlobalResource.SCREEN_WID - 20, GlobalResource.SCREEN_HIGH / 3);
        } else if (mMode == MODE_NORMAL) {
            getDialog().getWindow().setLayout((int) (GlobalResource.SCREEN_WID * 0.6f), GlobalResource.SCREEN_HIGH / 4);
        }
    }

    public void setError() {
        ok.setImageResource(R.drawable.umeng_update_close_bg_normal);
    }

    private Ondismiss ondismiss;

    public void setOndismiss(Ondismiss ondismiss) {
        this.ondismiss = ondismiss;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(ondismiss!=null){
            ondismiss.onDismiss();
        }
    }

    public static interface Ondismiss{
        void onDismiss();
    }
}
