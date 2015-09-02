package com.dkhs.adpter.handler;


import com.dkhs.adpter.util.ViewHolder;


public interface ItemHandler<T> {


    int getLayoutResId();

    void onBindView(ViewHolder vh, T data, int position);


}
