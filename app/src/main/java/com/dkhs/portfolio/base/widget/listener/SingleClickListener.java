package com.dkhs.portfolio.base.widget.listener;

import android.view.View;


/**
 * @author zwm
 * @version 2.0
 * @ClassName SingleClickListener
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/9.
 */
public class SingleClickListener implements View.OnClickListener {

    private View.OnClickListener mListener;

    private static long L_CLICK_INTERVAL = 800;
    private long preClickTime;

    public SingleClickListener(View.OnClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {


        long clickTime=System.currentTimeMillis();
        if (clickTime - preClickTime > L_CLICK_INTERVAL) {
            preClickTime= clickTime;
            mListener.onClick(v);
        }
    }
}
