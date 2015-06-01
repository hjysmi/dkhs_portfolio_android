package com.dkhs.portfolio.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
/**
 * @author zwm
 * @version 2.0
 * @ClassName RVHolder
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/1.
 */
public class RVHolder  extends RecyclerView.ViewHolder {


    private ViewHolderUtils.ViewHolder viewHolder;

    public RVHolder(View itemView) {
        super(itemView);
        viewHolder=ViewHolderUtils.get(itemView);
    }


    public ViewHolderUtils.ViewHolder getViewHolder() {
        return viewHolder;
    }
}
