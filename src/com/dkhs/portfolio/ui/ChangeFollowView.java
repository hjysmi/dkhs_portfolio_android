/**
 * @Title FollowView.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-5-6 下午3:16:21
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.DialogInterface;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UpdateCombinationEvent;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;

/**
 * @ClassName FollowView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-5-6 下午3:16:21
 * @version 1.0
 */
public class ChangeFollowView {
    private Context mContext;
    private CombinationBean mCombinationBean;

    public ChangeFollowView(Context context) {
        this.mContext = context;
    }

    public void changeFollow(CombinationBean comBean) {
        this.mCombinationBean = comBean;
        if (mCombinationBean.isFollowed()) {
            showDelDialog();
        } else {
            followCombination();
        }
    }

    private void followCombination() {
        if (PortfolioApplication.getInstance().hasUserLogin()) {

            if (mCombinationBean.isFollowed()) {
                new FollowComEngineImpl().defFollowCombinations(mCombinationBean.getId(), followComListener);
            } else {

                new FollowComEngineImpl().followCombinations(mCombinationBean.getId(), followComListener);
            }
        } else {
            if (mCombinationBean.isFollowed()) {
                mCombinationBean.setFollowed(false);
                new VisitorDataEngine().delCombinationBean(mCombinationBean);
                delFollowSuccess();
            } else {
                mCombinationBean.setFollowed(true);
                new VisitorDataEngine().saveCombination(mCombinationBean);
                addFollowSuccess();
            }
            // btnAddOptional.setEnabled(true);
            // addOptionalButton(mCombinationBean.isFollowed());
        }
    }

    private void delFollowSuccess() {
        PromptManager.showToast(R.string.msg_def_follow_success);

        mCombinationBean.setFollowerCount(mCombinationBean.getFollowerCount() - 1);
    }

    private void addFollowSuccess() {
        PromptManager.showToast(R.string.msg_follow_success);
        mCombinationBean.setFollowerCount(mCombinationBean.getFollowerCount() + 1);
    }

    public void showDelDialog() {

        MAlertDialog builder = PromptManager.getAlertDialog(mContext);
        builder.setMessage(R.string.dialog_message_delfollow_combination);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                followCombination();
            }

        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // btnAddOptional.setEnabled(true);
            }
        });
        builder.create().show();

    }

    ParseHttpListener followComListener = new ParseHttpListener<Object>() {

        @Override
        public void requestCallBack() {
            super.requestCallBack();
            // btnAddOptional.setEnabled(true);
        };

        @Override
        protected Object parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            mCombinationBean.setFollowed(!mCombinationBean.isFollowed());
            BusProvider.getInstance().post(new UpdateCombinationEvent(mCombinationBean));
            if (mCombinationBean.isFollowed()) {
                addFollowSuccess();
            } else {
                delFollowSuccess();
            }
        }
    };

}
