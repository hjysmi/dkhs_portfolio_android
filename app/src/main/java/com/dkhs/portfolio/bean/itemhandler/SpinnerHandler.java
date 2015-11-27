package com.dkhs.portfolio.bean.itemhandler;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TopicSortTypeEvent;

/**
 * Created by wuyongsen on 2015/11/5.
 */
public class SpinnerHandler extends SimpleItemHandler<TopicsBean>{
    private Context mContext;
    private int mSortType;
    private Spinner mSpinner;
    public SpinnerHandler(Context context,int sortType){
        mContext = context;
        mSortType = sortType;
    }

    public void setSelection(int pos){
        if(mSpinner != null && mSpinner.getAdapter() != null){
            mSpinner.setSelection(pos);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_spinner_rewards;
    }

    @Override
    public void onBindView(ViewHolder vh, TopicsBean data, int position) {
        mSpinner = vh.get(R.id.spinner);
        if(mSpinner.getAdapter() == null){
            mSpinner.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_spinner, mContext.getResources().getStringArray(R.array.choices_sort_type)));
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    postRefreshEvent(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void postRefreshEvent(int sortType){
        if(mSortType != sortType){
            mSortType = sortType;
            TopicSortTypeEvent topicSortTypeEvent = new TopicSortTypeEvent();
            topicSortTypeEvent.sortType = mSortType;
            BusProvider.getInstance().post(topicSortTypeEvent);
        }
    }

}
