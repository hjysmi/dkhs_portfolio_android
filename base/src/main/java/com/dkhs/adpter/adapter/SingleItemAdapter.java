package com.dkhs.adpter.adapter;

import android.content.Context;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;

import java.util.List;

/**
 * Created by zjz on 2015/9/15.
 */
public abstract class SingleItemAdapter<T> extends DKBaseAdapter implements ItemHandler<T> {
    public SingleItemAdapter(Context context, List<?> data) {
        super(context, data);
        buildSingleItemView(this);
    }

    @Override
    public void onBindView(ViewHolder vh, T data, int position) {

    }
}
