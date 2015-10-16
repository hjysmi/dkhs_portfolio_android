package com.dkhs.portfolio.bean.itemhandler.combinationdetail;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.TopicsCommendEngineImpl;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TopicsDetailRefreshEvent;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class RewardReplyBarHandler extends SimpleItemHandler<TopicsBean> implements AdapterView.OnItemSelectedListener {


    private Context mContext;
    private TopicsCommendEngineImpl.SortType mSortType;

    public RewardReplyBarHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_reward_reply_bar;
    }


    @Override
    public void onBindView(final ViewHolder vh, final TopicsBean data, int position) {
        vh.setTextView(R.id.tv_like, mContext.getString(R.string.like) + " " + data.attitudes_count);
        vh.setTextView(R.id.comment, mContext.getString(R.string.answer) + " " + data.comments_count);

        final Spinner spinner = vh.get(R.id.spinner);
        if (spinner.getAdapter() == null) {
            spinner.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_spinner, mContext.getResources().getStringArray(R.array.topics_commend_sort)));
            spinner.setOnItemSelectedListener(this);
        }

        if (mSortType != null) {
            switch (mSortType) {
                case latest:
                    spinner.setSelection(0);
                    break;
                case best:
                    spinner.setSelection(1);
                    break;
                case earliest:
                    spinner.setSelection(2);
                    break;
            }
        }
        vh.getTextView(R.id.tv_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.getTextView(R.id.tv_like).setTextColor(v.getResources().getColor(R.color.theme_color));
                vh.getTextView(R.id.comment).setTextColor(v.getResources().getColor(R.color.tag_gray));
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(vh.get(R.id.indicate), "translationX", (v.getLeft() + v.getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2));
                objectAnimator.setDuration(200);
                objectAnimator.start();
                TopicsDetailRefreshEvent topicsDetailRefreshEvent = new TopicsDetailRefreshEvent();
                mSortType = TopicsCommendEngineImpl.SortType.like;
                topicsDetailRefreshEvent.sortType = mSortType;
                BusProvider.getInstance().post(topicsDetailRefreshEvent);
                spinner.setVisibility(View.INVISIBLE);
            }
        });
        vh.getTextView(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                vh.getTextView(R.id.comment).setTextColor(v.getResources().getColor(R.color.theme_color));
                vh.getTextView(R.id.tv_like).setTextColor(v.getResources().getColor(R.color.tag_gray));

                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(vh.get(R.id.indicate), "translationX", (v.getLeft() + v.getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2));
                objectAnimator.setDuration(200);
                objectAnimator.start();
                postRefreshEvent(spinner.getSelectedItemPosition());
                spinner.setVisibility(View.VISIBLE);


            }
        });
        if (mSortType == TopicsCommendEngineImpl.SortType.like) {

            vh.getTextView(R.id.tv_like).setTextColor(vh.getConvertView().getResources().getColor(R.color.theme_color));
            vh.getTextView(R.id.comment).setTextColor(vh.getConvertView().getResources().getColor(R.color.tag_gray));
            vh.getTextView(R.id.comment).post(new Runnable() {
                @Override
                public void run() {
                    ViewHelper.setTranslationX(vh.get(R.id.indicate), vh.getTextView(R.id.tv_like).getLeft() + vh.getTextView(R.id.tv_like).getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2);

                }
            });
        } else {
            vh.getTextView(R.id.tv_like).setTextColor(vh.getConvertView().getResources().getColor(R.color.tag_gray));
            vh.getTextView(R.id.comment).setTextColor(vh.getConvertView().getResources().getColor(R.color.theme_color));

            vh.getTextView(R.id.comment).post(new Runnable() {
                @Override
                public void run() {
                    ViewHelper.setTranslationX(vh.get(R.id.indicate), vh.getTextView(R.id.comment).getLeft() + vh.getTextView(R.id.comment).getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2);
                }
            });
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        postRefreshEvent(position);
    }

    private void postRefreshEvent(int position) {
        TopicsDetailRefreshEvent topicsDetailRefreshEvent = new TopicsDetailRefreshEvent();
        switch (position) {
            case 0:
                topicsDetailRefreshEvent.sortType = TopicsCommendEngineImpl.SortType.latest;
                break;
            case 1:
                topicsDetailRefreshEvent.sortType = TopicsCommendEngineImpl.SortType.best;
                break;
            case 2:
                topicsDetailRefreshEvent.sortType = TopicsCommendEngineImpl.SortType.earliest;
                break;
        }

        if (mSortType != topicsDetailRefreshEvent.sortType) {
            mSortType = topicsDetailRefreshEvent.sortType;
            BusProvider.getInstance().post(topicsDetailRefreshEvent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
