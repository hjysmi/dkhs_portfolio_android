package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zhangcm on 2015/11/18.
 */
public class SelectCombinationViewBean extends ViewBean{
    private static final int TYPE = 5;
    private CombinationBean mCombinationBean;
    public SelectCombinationViewBean(){}
    public SelectCombinationViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }
    public SelectCombinationViewBean(CombinationBean mCombinationBean) {
        this.mCombinationBean = mCombinationBean;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_select_combination));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(mCombinationBean);
        ViewUitls.fullSpanView(itemHolder);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        TextView tv_combination_name;
        TextView tv_combination_desc;
        CheckBox cb_select_combin;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_combination_name = (TextView) itemView.findViewById(R.id.tv_combination_name);
            tv_combination_desc = (TextView) itemView.findViewById(R.id.tv_combination_desc);
            cb_select_combin = (CheckBox) itemView.findViewById(R.id.cb_select_combin);

        }

        public void bindView(final CombinationBean mCombinationBean) {
            tv_combination_name.setText(mCombinationBean.getName());
            String desc = mCombinationBean.getDescription();
            if(!TextUtils.isEmpty(desc)){
                tv_combination_desc.setText(desc);
            }
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
        private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CombinationBean csBean = (CombinationBean) buttonView.getTag();
                if (!isChecked) {
                    buttonView.setChecked(true);
//                    showDelDialog(buttonView, isChecked);
                    csBean.setFollowed(false);
                    bindView(csBean);
                    delFollowCombinatio(csBean);
                } else {
                    csBean.setFollowed(isChecked);
                    bindView(csBean);
                    delFollowCombinatio(csBean);
                }
            }
        };
        public void showDelDialog(final CompoundButton buttonView, final boolean isChecked) {

            MAlertDialog builder = PromptManager.getAlertDialog(itemView.getContext());
            builder.setMessage(R.string.dialog_message_delfollow_combination);
            // builder.setTitle(R.string.tips);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CombinationBean csBean = (CombinationBean) buttonView.getTag();
                    csBean.setFollowed(false);
                    bindView(csBean);
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
            }
        }

        private ParseHttpListener delFollowComListener = new ParseHttpListener() {
            @Override
            protected Object parseDateTask(String jsonData) {
                return null;
            }

            @Override
            protected void afterParseData(Object object) {
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
                PromptManager.showFollowToast();
            }
        };
    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
