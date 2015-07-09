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

    private SingClickHelper singClickhelper=new SingClickHelper();

    public SingleClickListener(View.OnClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {

        if (singClickhelper.clickEnable()) {
            mListener.onClick(v);
        }
    }
}
