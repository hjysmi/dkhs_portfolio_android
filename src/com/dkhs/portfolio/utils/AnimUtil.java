package com.dkhs.portfolio.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import com.dkhs.portfolio.config.APPConfig;

/**
 * @author useradmin
 * @version 1.0
 * @ClassName zwm
 * @Description TODO(基础动画集)
 * @date 2015/4/16.16:31
 */
public class AnimUtil {



    public static void  showScale(View view){
        if(view.getVisibility() == View.VISIBLE){
            return;
        }
        view.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation=new ScaleAnimation(0.5f,1,0.5f,1, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.5f,1f);

        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(APPConfig.ANIM_DURATION);

        view.startAnimation(animationSet);


    }
    public static void  dismissScale(final View view){
        if(view.getVisibility() == View.GONE){
            return;
        }
        view.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation=new ScaleAnimation(1,0.5f,1,0.5f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1f,0.5f);

        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(APPConfig.ANIM_DURATION/2);
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

}
