package com.example.tong.jiaowuxitong.view.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tong.jiaowuxitong.R;
import com.example.tong.jiaowuxitong.entity.ActionState;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.view.fragment.AlertFragment;

/**
 * Created by TONG on 2017/1/21
 * alter dialog 封装类.
 */
public class ViewTool {

    /**
     * 处理ActionState 并自动提示
     *
     * @param context
     * @param message
     * @return 成功的action return 1 否则-1
     */
    public static int handlerAction(Context context, Message message) {
        if (message == null || message.msg == null || TextUtils.isEmpty((String) message.msg)) {
            Toast.makeText(context, "error empty msg", Toast.LENGTH_SHORT).show();
        } else {
            ActionState actionState = GsonUtil.fromJson((String) message.msg, ActionState.class);
            if (actionState != null) {
//                Toast.makeText(context, actionState.tag == 1 ? "ok" : "error", Toast.LENGTH_SHORT).show();
                return actionState.tag;
            }
        }
        return -1;
    }


    /**
     * 设置alertfragment显示的text
     *
     * @param text
     */
    public static void setmAlertFragmentText(String text) {
        if (mAlertFragment != null) {
            mAlertFragment.setText(text);
        }
    }

    /**
     * 设置alertfragment error时显示的text
     * 未使用
     *
     * @param text
     */
    public static void setmAlertFragmentError(String text) {
        if (mAlertFragment != null) {
            mAlertFragment.setError();
            mAlertFragment.setText(text);
        }
    }

    private static AlertFragment mAlertFragment;

    /**
     * alertfragment 消失时的回调
     *
     * @param ondismiss
     */
    public static void setAlertFragmentCallback(AlertFragment.Ondismiss ondismiss) {
        if (mAlertFragment != null) {
            mAlertFragment.setOndismiss(ondismiss);
        }
    }

    /**
     * 获取alertfragment实例并显示
     *
     * @param fragmentManager
     * @param context
     * @param mode            显示模式
     * @param cancelAble      点击其他地方是否可以取消alert的显示
     * @return
     */
    public static AlertFragment getAlertFragmentInstance(FragmentManager fragmentManager, Context context, int mode, boolean cancelAble) {
        if (mAlertFragment != null) {
            mAlertFragment.setMode(mode);
            mAlertFragment.setCancelable(cancelAble);
            mAlertFragment.show(fragmentManager, mAlertFragment.getClass().getName());
            return mAlertFragment;
        }
        mAlertFragment = (AlertFragment) DialogFragment.instantiate(context, AlertFragment.class.getName());
        mAlertFragment.setCancelable(cancelAble);
        mAlertFragment.setMode(mode);
        mAlertFragment.show(fragmentManager, mAlertFragment.getClass().getName());
        return mAlertFragment;
    }

    /**
     * 使alertfragment消失
     */
    public static void dismissAlertFragment() {
        if (mAlertFragment != null && mAlertFragment.isVisible()) {
            mAlertFragment.dismiss();
            mAlertFragment = null;
        }
    }

    /**
     * 设置alertfragment显示时 是否可以点击界面其他地方以取消alertfragment
     *
     * @param cancelAble
     */
    public static void setmAlertFragmentCancelAble(boolean cancelAble) {
        if (mAlertFragment != null) {
            mAlertFragment.setCancelable(cancelAble);
        }
    }


    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 110) {
                dismissAlertFragment();
            }
        }
    };

    /**
     * 显示alertfragment
     *
     * @param mill alertfragment显示时长     -1 一直显示
     */
    public static void showAlertFragmentText(long mill) {
        if (mAlertFragment != null) {
            mAlertFragment.showText();

            if (mill != -1) {
                //延时 mill 毫秒使alertfragment消失
                handler.sendEmptyMessageDelayed(110, mill);
            }
        }
    }

    /**
     * 显示一个alert 对话框
     *
     * @param context
     * @param alert
     * @param pButton
     * @param nButton
     * @param callBack
     * @param canTouch
     */
    public static void showAlert(Context context, String alert, String pButton, String nButton, final CallBack callBack, boolean canTouch) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(pButton))
            builder.setPositiveButton(pButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callBack != null) {
                        callBack.onPositiveChoose();
                    }
                }
            });
        builder.setTitle(alert);
        if (!TextUtils.isEmpty(nButton))
            builder.setNegativeButton(nButton, null);
        builder.create().setCanceledOnTouchOutside(canTouch);
        builder.show();
    }

    public static void showAlert(Context context, String alert, String pButton, String nButton, final CallBack callBack) {
        showAlert(context, alert, pButton, nButton, callBack, false);
    }

    public static void showAlert(Context context, String s) {
        showAlert(context, s, null, null, null, false);
    }


    public static void showAlert(Context context, String s, boolean cancelTouch) {
        showAlert(context, s, null, null, null, cancelTouch);
    }

    private static boolean isShowingSnack = false;

    /**
     * 显示一个snackbar
     *
     * @param context
     * @param recyclerView
     * @param s
     */
    public static void showSnack(Context context, View recyclerView, String s) {
        if (!isShowingSnack) {
            Snackbar snackbar = Snackbar.make(recyclerView, s, Snackbar.LENGTH_SHORT)
                    .setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            isShowingSnack = false;
                        }
                    });
            View view = snackbar.getView();
            if (context != null && view != null) {
                view.setBackgroundColor(context.getResources().getColor(R.color.snackBarBackground));
                View v = view.findViewById(android.support.design.R.id.snackbar_text);
                if (v != null && v instanceof TextView) {
                    TextView textView = (TextView) v;
                    textView.setTextColor(context.getResources().getColor(R.color.snackBarTextColor));
                }
            }
            snackbar.show();
        }
    }

    /**
     * 显示一个toast
     *
     * @param context
     * @param s
     * @param lengthShort
     */
    public static void showToast(Context context, String s, int lengthShort) {
        Toast.makeText(context, s, lengthShort).show();
    }


    public static interface CallBack {
        void onPositiveChoose();
    }
}
