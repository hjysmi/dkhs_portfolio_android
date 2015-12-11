package com.dkhs.adpter.handler;

import com.dkhs.adpter.util.ViewHolder;

/**
 * @author zwm
 * @version 2.0
 * @ClassName SimpleItemHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/29.
 */
public abstract class SimpleItemHandler<T> implements ItemHandler<T> {


//    protected Context mContext;
    public boolean isRefreshed;

    @Override
    public void onBindView(ViewHolder vh, T data, int position) {
//        if (mContext == null) {
//            mContext = vh.getContext();
//        }

    }

}
