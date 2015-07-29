package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;


/**
 * @author zwm
 * @version 2.0
 * @ClassName CommendHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class CommentHandler implements ItemHandler<CommentBean> {
    @Override
    public int getLayoutResId() {
        return  R.layout.item_reply;
    }

    @Override
    public void onBindView(ViewHolder vh, CommentBean comment, int position) {

        UserEntity user = comment.getUser();
        if(!TextUtils.isEmpty(user.getAvatar_sm())){
            ImageLoaderUtils.setHeanderImage(comment.getUser().getAvatar_sm(),vh.getImageView(R.id.iv_head));
        }
        vh.getTextView(R.id.tv_username).setText(user.getUsername());
        vh.getTextView(R.id.tv_text).setText(comment.getText());
        vh.get(R.id.iv_praise).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(final View v) {
                  //TODO 点赞处理
                  v.setBackgroundResource(R.drawable.praised);
                  ScaleAnimation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                          Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                  animation.setDuration(500);//设置动画持续时间
                  animation.setRepeatMode(Animation.REVERSE);
                  animation.setRepeatCount(1);
                  animation.setFillAfter(false);
                  v.startAnimation(animation);
                  animation.setAnimationListener(new Animation.AnimationListener() {
                      @Override
                      public void onAnimationStart(Animation animation) {
                      }

                      @Override
                      public void onAnimationEnd(Animation animation) {
                          v.setBackgroundResource(R.drawable.praise);
                      }

                      @Override
                      public void onAnimationRepeat(Animation animation) {

                      }
                  });
              }
          });

    }
}
