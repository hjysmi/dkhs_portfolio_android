package com.dkhs.portfolio.bean.itemhandler.searchmoredetail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UpdateCombinationEvent;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;


/**
 * Created by zhangcm on 2015/11/20.
 */
public class SearchMoreCombinationHandler extends SimpleItemHandler<CombinationBean>{

    private final Context context;

    public SearchMoreCombinationHandler(Context context){
        this.context = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_select_combination;
    }
    private ViewHolder vh;
    private int position;
    @Override
    public void onBindView(final ViewHolder vh, final CombinationBean mCombinationBean, int position) {
        this.vh = vh;
        this.position = position;
        final View itemView = vh.getConvertView();
        TextView tv_combination_name = vh.getTextView(R.id.tv_combination_name);
        TextView tv_combination_builder = vh.getTextView(R.id.tv_combination_builder);;
        CheckBox cb_select_combin= vh.getCheckBox(R.id.cb_select_combin);
        tv_combination_name.setText(mCombinationBean.getName());
        tv_combination_builder.setText(mCombinationBean.getUser().getUsername());
        cb_select_combin.setOnCheckedChangeListener(null);
        cb_select_combin.setTag(mCombinationBean);
        cb_select_combin.setChecked(mCombinationBean.isFollowed());
        cb_select_combin.setOnCheckedChangeListener(checkedChangeListener);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startAnimationActivity((Activity) itemView.getContext(), CombinationDetailActivity.newIntent(itemView.getContext(), mCombinationBean));

            }
        });
    }
    private CombinationBean mCombinationBean;

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CombinationBean csBean = (CombinationBean) buttonView.getTag();
            if (!isChecked) {
//                buttonView.setChecked(true);
                csBean.setFollowed(false);
                onBindView(vh,csBean,position);
                delFollowCombinatio(csBean);
//                showDelDialog(buttonView, isChecked);
            } else {
                csBean.setFollowed(isChecked);
                onBindView(vh,csBean,position);
                delFollowCombinatio(csBean);
            }
        }
    };
    public void showDelDialog(final CompoundButton buttonView, final boolean isChecked) {

        MAlertDialog builder = PromptManager.getAlertDialog(context);
        builder.setMessage(R.string.dialog_message_delfollow_combination);
        // builder.setTitle(R.string.tips);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CombinationBean csBean = (CombinationBean) buttonView.getTag();
                csBean.setFollowed(false);
                onBindView(vh,csBean,position);
                delFollowCombinatio(csBean);
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
    private void delFollowCombinatio(CombinationBean mCombinationBean) {
        if (PortfolioApplication.hasUserLogin()) {
            this.mCombinationBean = mCombinationBean;
            if (mCombinationBean.isFollowed()) {
                new FollowComEngineImpl().followCombinations(mCombinationBean.getId(), followComListener);
            } else {
                new FollowComEngineImpl().defFollowCombinations(mCombinationBean.getId(), delFollowComListener);

            }
        } else {
            if (mCombinationBean.isFollowed()) {
                new VisitorDataEngine().saveCombination(mCombinationBean);
                PromptManager.showFollowToast();
            } else {
                new VisitorDataEngine().delCombinationBean(mCombinationBean);
                PromptManager.showDelFollowToast();
            }
            BusProvider.getInstance().post(new UpdateCombinationEvent(mCombinationBean));
        }
    }

    private ParseHttpListener delFollowComListener = new ParseHttpListener() {
        @Override
        protected Object parseDateTask(String jsonData) {
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            BusProvider.getInstance().post(new UpdateCombinationEvent(mCombinationBean));
            PromptManager.showDelFollowToast();
        }
    };
    private ParseHttpListener followComListener = new ParseHttpListener() {
        @Override
        protected Object parseDateTask(String jsonData) {
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            BusProvider.getInstance().post(new UpdateCombinationEvent(mCombinationBean));
            PromptManager.showFollowToast();
        }
    };
}
