package com.example.tong.jiaowuxitong.view.custom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by TONG on 2017/1/29.
 */
public class AnimaTool {
    public interface OnAnimate {
        void onUpdate(float f);

        void onEnd();
    }

    public static void Objrotate(final View view, final OnAnimate onAnimate, float f, float t,int d) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotationX", f, t);

        if (onAnimate != null) {
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private boolean hashandled = false;

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float c = (float) animation.getAnimatedValue("rotationX");
                    if (!hashandled && c >= 90) {
                        hashandled = true;
                        onAnimate.onUpdate(c);
                    }
                }
            });
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onAnimate.onEnd();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        objectAnimator.setDuration(d);
        objectAnimator.start();

    }

    public static void rotate(Context context, View view, float angle) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillBefore(false);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(300);


//        view.setAnimation(rotateAnimation);
        view.startAnimation(rotateAnimation);
//        rotateAnimation.start();
    }
}
