package com.dkhs.portfolio.base.widget.listener;

import android.view.View;
import android.widget.AdapterView;

/**
 * @author zwm
 * @version 2.0
 * @ClassName OnItemClickListener
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/9.
 */
public class SingleItemClickListener implements AdapterView.OnItemClickListener {

    private AdapterView.OnItemClickListener singleItemClickListener;

    private SingClickhelper singClickhelper=new SingClickhelper();

    public SingleItemClickListener(AdapterView.OnItemClickListener singleItemClickListener) {
        this.singleItemClickListener = singleItemClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(singClickhelper.clickEnable()){
            singleItemClickListener.onItemClick(parent, view, position, id);
        }
    }
}
