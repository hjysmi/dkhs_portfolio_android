package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName AutoAdapter
 * @date 2015/4/23.14:07
 * @Description TODO(简化BaseAdapter, 内部实现ViewHodler 模式)
 */
public abstract class AutoAdapter extends MBaseAdapter {


    public AutoAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public View getViewAgent(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(setLayoutID(), parent, false);
        }
        onViewCreated(position, convertView, ViewHolderUtils.get(convertView));
        return convertView;
    }


    /**
     * 设置Adapter布局layoutId
     */
    public abstract int setLayoutID();

    /**
     * Adapter view的创建，用于初始化item view
     * <p/>
     * 与{@linkplain android.widget.BaseAdapter }的getview用法一致
     *
     * @param vh:处理过的viewHolder，用于直接返回需要设置的view
     */
    public abstract void onViewCreated(int position, View v, ViewHolderUtils.ViewHolder vh);

}
