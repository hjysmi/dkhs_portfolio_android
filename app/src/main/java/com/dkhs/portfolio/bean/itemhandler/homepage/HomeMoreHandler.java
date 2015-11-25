package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.HomeMoreBean;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class HomeMoreHandler extends SimpleItemHandler<HomeMoreBean> {
    private Context mContext;
    public HomeMoreHandler(Context context){
        mContext = context;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.layout_more_data;
    }

    @Override
    public void onBindView(ViewHolder vh, HomeMoreBean data, int position) {
        vh.get(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        vh.getTextView(R.id.title).setText(data.getTitle());
    }
}
