package com.dkhs.portfolio.bean.itemhandler;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SafeSignBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;

/**
 * Created by xuetong on 2016/1/12.
 */
public class SafeSignHandler extends SimpleItemHandler<SafeSignBean> {
    private Context context;

    public SafeSignHandler(Context context) {
        this.context = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_safesign;
    }

    @Override
    public void onBindView(ViewHolder vh, SafeSignBean data, int position) {
        vh.get(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageHandler handler = new MessageHandler(context);
                handler.handleURL(DKHSClient.getAbsoluteUrl(DKHSClient.getAbsoluteUrl(DKHSUrl.Ads.getSafeInfo)));
            }
        });


    }


}
