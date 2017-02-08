package com.example.tong.jiaowuxitong.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import com.example.tong.jiaowuxitong.net.Message;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by TONG on 2017/2/2.
 * fragment 基类 用于自动处理过长动画以及 EVENTBUS 的状态绑定
 */
public class BaseFragment extends Fragment {
    //是否需要注册eventbus 需要在super.oncreate()之前设置
    protected boolean registEventbus = true;
    protected Context mContext;
    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
        mActivity = getActivity();
        //检查版本 设置动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setAllowReturnTransitionOverlap(false);
            setAllowEnterTransitionOverlap(false);
            setEnterTransition(new Slide(Gravity.RIGHT));
            setExitTransition(new Slide(Gravity.BOTTOM));
            setReenterTransition(new Slide(Gravity.BOTTOM));
            setReturnTransition(new Slide(Gravity.RIGHT));

            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setDuration(1000);
            changeBounds.setInterpolator(new LinearInterpolator());
            setSharedElementReturnTransition(changeBounds);
            setSharedElementEnterTransition(changeBounds);
        }
    }

    /**
     * 检查版本 自动开启动画
     * @param intent
     * @param requestCode
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.startActivityForResult(intent, requestCode, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity).toBundle());
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 同上
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity).toBundle());
        } else {
            super.startActivity(intent);
        }
    }

    /**
     * 同上
     */
    public void finishAfterTransition() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getActivity().finishAfterTransition();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
        mActivity = null;
        if (registEventbus) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext = getContext();
        mActivity = getActivity();
        if (registEventbus) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    /**
     * eventbus 数据回调方法
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message message) {
    }

}
