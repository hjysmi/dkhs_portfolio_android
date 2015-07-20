package com.dkhs.adpter.listener;


import com.dkhs.adpter.util.ViewHolder;


public interface ItemHandler<T extends Object> {

    public  int getLayoutResId();


    public  void onBindView(ViewHolder vh, T data, int position);

    public Class<?> getDataClass();

}  