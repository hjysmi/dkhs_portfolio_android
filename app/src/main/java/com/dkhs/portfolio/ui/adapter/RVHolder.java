package com.dkhs.portfolio.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dkhs.adpter.util.ViewHolder;

/**
 * @author zwm
 * @version 2.0
 * @ClassName RVHolder
 * @Description TODO()
 * @date 2015/6/1.
 */
public class RVHolder  extends RecyclerView.ViewHolder {


    private ViewHolder viewHolder;

    public RVHolder(View itemView) {
        super(itemView);
        viewHolder=ViewHolder.newInstant(itemView);
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

}
