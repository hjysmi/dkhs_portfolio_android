/**
 * @Title AnimationHelper.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-23 下午1:28:02
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import static android.view.View.VISIBLE;


/**
 * @author zjz
 * @version 1.0
 * @ClassName AnimationHelper
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-3-23 下午1:28:02
 */
public class AnimationHelper {

    /**
     * 动画时长
     */
    public static int ANIM_DURATION = 500;

    // 生成自定义动画
    public static void setupCustomAnimations(LayoutTransition transittioner, Object taget) {
        // 动画：CHANGE_APPEARING
        // Changing while Adding
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);

        final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(taget, pvhLeft, pvhTop, pvhRight,
                pvhBottom, pvhScaleX, pvhScaleY).setDuration(
                transittioner.getDuration(LayoutTransition.CHANGE_APPEARING));
        transittioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
        changeIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        });

        // 动画：CHANGE_DISAPPEARING
        // Changing while Removing
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
        final ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(taget, pvhLeft, pvhTop, pvhRight,
                pvhBottom, pvhRotation).setDuration(transittioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
        transittioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
        changeOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotation(0f);
            }
        });

        // 动画：APPEARING
        // Adding
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).setDuration(
                transittioner.getDuration(LayoutTransition.APPEARING));
        transittioner.setAnimator(LayoutTransition.APPEARING, animIn);
        animIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationY(0f);
            }
        });

        // 动画：DISAPPEARING
        // Removing
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).setDuration(
                transittioner.getDuration(LayoutTransition.DISAPPEARING));
        transittioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        animOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                view.setRotationX(0f);
            }
        });

        transittioner.setDuration(300);

    }

    public static void showScale(View view) {
        if (view.getVisibility() == VISIBLE) {
            return;
        }
        view.setVisibility(VISIBLE);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1, 0.5f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(ANIM_DURATION);

        view.startAnimation(animationSet);


    }

    public static void dismissScale(final View view) {
        if (view.getVisibility() == View.GONE) {
            return;
        }
        view.setVisibility(VISIBLE);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.5f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(ANIM_DURATION / 2);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animationSet);


    }


    public static void translationToTopDismiss(final View view, Animator.AnimatorListener animatorListener) {

        if (view.getVisibility() == View.GONE) {
            return;
        }

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, -view.getMeasuredHeight());
        objectAnimator.setDuration(ANIM_DURATION - 100);
        objectAnimator.addListener(animatorListener);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();

    }

    public static void translationFromTopShow(final View view, Animator.AnimatorListener animatorListener) {

        view.setVisibility(VISIBLE);

        if (view.getMeasuredHeight() == 0) {
            view.measure(0, 0);
        }

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", -view.getMeasuredHeight(), 0);
        objectAnimator.setDuration(ANIM_DURATION - 100);
        objectAnimator.addListener(animatorListener);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();

    }

    public static void alphaShow(final View view) {


        AlphaAnimation alphaAnimation = new AlphaAnimation(0.01f, 1f);
        alphaAnimation.setDuration(ANIM_DURATION - 100);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alphaAnimation);


    }

    public static void alphaDismiss(final View view) {
        if (view.getVisibility() == View.GONE) {
            return;
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(ANIM_DURATION - 100);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alphaAnimation);

    }

    /**
     * 拉下来
     *
     * @param v
     * @param height
     * @param listener
     */
    public static void expandView(final View v, int height, boolean anim, com.nineoldandroids.animation.Animator.AnimatorListener listener) {

        if (anim) {
            ValueAnimator vA = expandViewAnim(v, height);
            vA.addListener(listener);
            vA.start();
        } else {
            v.getLayoutParams().height = height;
            v.requestLayout();
        }

    }


    public static void expandView(final View v, boolean anim) {
        int height = v.getMeasuredHeight();
        if (height == 0) {
            v.measure(0, 0);
            height = v.getMeasuredHeight();
        }
        expandViewAnim(v, height).start();
    }


    private static ValueAnimator expandViewAnim(final View v, int height) {
        final int initialHeight = height;
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float interpolatedTime = animation.getAnimatedFraction();
                if (interpolatedTime == 1) {
                    v.setVisibility(View.VISIBLE);
                } else {
                    v.getLayoutParams().height = (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                    v.setVisibility(View.VISIBLE);
                }
            }
        });
        anim.setDuration(ANIM_DURATION);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setTarget(v);
        return anim;
    }

    public static void collapseView(View view, boolean isAnim,com.nineoldandroids.animation.Animator.AnimatorListener listener) {
        if (isAnim) {
            ValueAnimator vA = collapseAnim(view);
            vA.addListener(listener);
            vA.start();
        } else {
            view.getLayoutParams().height = 0;
            view.requestLayout();
        }
    }

    private static ValueAnimator collapseAnim(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float interpolatedTime = animation.getAnimatedFraction();
                v.getLayoutParams().height = initialHeight
                        - (int) (initialHeight * interpolatedTime);
                v.requestLayout();
            }
        });
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(ANIM_DURATION);
        anim.setTarget(v);
        return anim;
    }


}
