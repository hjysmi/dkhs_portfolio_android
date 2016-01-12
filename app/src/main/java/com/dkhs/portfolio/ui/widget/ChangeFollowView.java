package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.content.DialogInterface;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.utils.PromptManager;

/**
 * Created by zjz on 2015/6/9.
 */
public class ChangeFollowView {

    private Context mContext;
    private SelectStockBean mStockBean;
    private VisitorDataEngine mVisitorDataEngine;

    public ChangeFollowView(Context context) {
        this.mContext = context;
        mVisitorDataEngine = new VisitorDataEngine();
    }

    public void changeFollow(SelectStockBean stockBean) {
        this.mStockBean = stockBean;
        if (mStockBean.isFollowed()) {
            showDelDialog();
        } else {
            followStockChange();
        }
    }


    public void changeFollowNoDialog(SelectStockBean stockBean) {
        this.mStockBean = stockBean;
        followStockChange();
    }


    public void showDelDialog() {

        MAlertDialog builder = PromptManager.getAlertDialog(mContext);
        builder.setTitle(R.string.tips);
        builder.setMessage(R.string.dialog_messag_delfollow);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                followStockChange();
            }

        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();


    }

    private void followStockChange() {
        int type;
        if (!PortfolioApplication.hasUserLogin()) {

            if (mStockBean.isFollowed()) {
                mStockBean.setFollowed(false);
                mVisitorDataEngine.delOptionalStock(mStockBean);
            } else {
                mStockBean.isFollowed = true;
                mStockBean.sortId = 0;
                mVisitorDataEngine.saveOptionalStock(mStockBean);
            }

            afterChange();
        } else {
            if (mStockBean.isFollowed()) {
                new QuotesEngineImpl().delfollow(mStockBean.id, baseListener);
            } else {
                new QuotesEngineImpl().symbolfollow(mStockBean.id, baseListener);
            }
        }
    }


    private void afterChange() {
        if (mStockBean.isFollowed()) {
            PromptManager.showFollowToast();
        } else {
            PromptManager.showDelFollowToast();
        }
        if (null != mChangeListener) {
            mChangeListener.onChange(mStockBean);
        }
    }

    IHttpListener baseListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {

            mStockBean.setFollowed(!mStockBean.isFollowed());
            afterChange();
        }
    };


    private IChangeSuccessListener mChangeListener;

    public void setmChangeListener(IChangeSuccessListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public interface IChangeSuccessListener {
        public void onChange(SelectStockBean stockBean);
    }


}
