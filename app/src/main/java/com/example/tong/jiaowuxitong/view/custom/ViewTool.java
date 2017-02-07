package com.example.tong.jiaowuxitong.view.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.tong.jiaowuxitong.entity.ActionState;
import com.example.tong.jiaowuxitong.net.GsonUtil;
import com.example.tong.jiaowuxitong.net.Message;
import com.example.tong.jiaowuxitong.view.fragment.AlertFragment;

/**
 * Created by TONG on 2017/1/21.
 */
public class ViewTool {

    public static void showActionResultWindow(Context context, boolean flag) {


    }

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

    public static void setmAlertFragmentText(String text) {
        if (mAlertFragment != null) {
            mAlertFragment.setText(text);
        }
    }

    public static void setmAlertFragmentError(String text) {
        if (mAlertFragment != null) {
            mAlertFragment.setError();
            mAlertFragment.setText(text);
        }
    }

    private static AlertFragment mAlertFragment;

    public static void setAlertFragmentCallback(AlertFragment.Ondismiss ondismiss) {
        if (mAlertFragment != null) {
            mAlertFragment.setOndismiss(ondismiss);
        }
    }

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

    public static void dismissAlertFragment() {
        if (mAlertFragment != null && mAlertFragment.isVisible()) {
            mAlertFragment.dismiss();
            mAlertFragment = null;
        }
    }

    public static void setmAlertFragmentCancelAble(boolean cancelAble) {
        if (mAlertFragment != null) {
            mAlertFragment.setCancelable(cancelAble);
        }
    }


    public static void setmAlertFragmentOnPositiveListenr() {


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

    public static void showAlertFragmentText(long mill) {
        if (mAlertFragment != null) {
            mAlertFragment.showText();

            if (mill != -1) {
                handler.sendEmptyMessageDelayed(110, mill);
            }
        }
    }

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

    public static void showSnack(@Nullable Context context, View recyclerView, String s) {
        if (!isShowingSnack) {
            Snackbar.make(recyclerView, s, Snackbar.LENGTH_SHORT)
                    .setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            isShowingSnack = false;
                        }
                    })
                    .show();
        }
    }

    public static void showToast(Context context, String s, int lengthShort) {
        Toast.makeText(context, s, lengthShort).show();
    }


    public static interface CallBack {
        void onPositiveChoose();
    }
}
