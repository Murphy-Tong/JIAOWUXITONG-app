package com.example.tong.jiaowuxitong.view.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;

import com.example.tong.jiaowuxitong.net.Message;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean registEventbus = true;
    /*   protected boolean setShareEnter = false;
       protected boolean setShareExit = false;*/
    protected boolean setEnter = true;
    protected boolean setExit = true;
    private long during = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setAllowEnterTransitionOverlap(false);
//            getWindow().setSharedElementsUseOverlay(false);
            getWindow().setAllowReturnTransitionOverlap(false);
        }
        if (setEnter)
            setEnter();
        super.onCreate(savedInstanceState);

        if (registEventbus) {
            EventBus.getDefault().register(this);
        }
    }

    protected void setUpToorBar(@Nullable String add) {
        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
            if (!TextUtils.isEmpty(add)) {
                getSupportActionBar().setTitle(add);
            } else {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

/*
    protected void setExit(){


        getWindow().setExitTransition(slide);

    }
*/

    protected void setShareEnter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeBounds changeClipBounds = new ChangeBounds();
            changeClipBounds.setDuration(during * 2);
//            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setSharedElementEnterTransition(changeClipBounds);
            getWindow().setSharedElementReenterTransition(changeClipBounds);
        }
    }

    protected void clearEnter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
        }
    }

    public void clearExit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(null);
        }
    }

    protected void setShareExit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeBounds changeClipBounds = new ChangeBounds();
            changeClipBounds.setDuration(during * 2);
//            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setSharedElementExitTransition(changeClipBounds);
            getWindow().setSharedElementReturnTransition(changeClipBounds);
        }
    }

    protected void setEnter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.BOTTOM);
//            slide.setInterpolator(new BounceInterpolator());
            slide.setDuration(during);
//            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setEnterTransition(slide);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registEventbus) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (registEventbus) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onGet(Message message) {
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.startActivityForResult(intent, requestCode, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        } else {
            super.startActivity(intent);
        }
    }


    public void startActivity2(Intent intent) {
        super.startActivity(intent);
    }

    /*

    public void setEnterTransition() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        }
    }


    public void startActivityForResult2(Intent intent, int rqCode) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.LEFT);
            getWindow().setExitTransition(slide);
            getWindow().setExitTransition(slide);
            startActivityForResult(intent, 1, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivityForResult(intent, rqCode);
        }
    }

    public void startActivity2(Intent intent, Transition transition) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(transition);
            startActivity(intent);
        } else {
            startActivity(intent);
        }
    }
*/

    @Override
    public void finishAfterTransition() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            super.finishAfterTransition();
        } else {
            super.finish();
        }

    }

}
