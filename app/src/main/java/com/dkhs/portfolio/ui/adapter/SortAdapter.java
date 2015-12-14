package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.percent.PercentLinearLayout;
import com.dkhs.adpter.adapter.SingleItemAdapter;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.SortModel;

import java.util.List;

public class SortAdapter extends SingleItemAdapter<SortModel> implements SectionIndexer {
    List<SortModel> list;

    public SortAdapter(Context context, List<SortModel> data) {
        super(context, data);
        this.list = data;

    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_organization;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public void onBindView(ViewHolder vh, SortModel data, int position) {
        super.onBindView(vh, data, position);
        TextView tvTitle = vh.get(R.id.tv_organization);
        TextView tvLetter = vh.get(R.id.catalog);
        PercentLinearLayout ll_top = vh.get(R.id.ll_top);
        View line = vh.get(R.id.line);
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        // viewHolder.tvLetter.setPadding((int) (0.02 * width), 0, 0, 0);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {

            ll_top.setVisibility(View.VISIBLE);
            tvLetter.setText(list.get(position).getSortLetters());
        } else {
            ll_top.setVisibility(View.GONE);
        }

        if (position == getLastPositionForSection(section)) {
            //字母的最后一行
            line.setVisibility(View.INVISIBLE);
        } else {
            line.setVisibility(View.VISIBLE);
        }

        tvTitle.setText(this.list.get(position).getName());

      /*  vh.get(R.id.fl_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }


    // 当ListView数据发生变化时,调用此方法来更新ListView

    /* public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();

    }*/


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其最后一次出现该首字母的位置
     */

    public int getLastPositionForSection(int section) {
        for (int i = getCount() - 1; i < getCount(); i--) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                char x = (char) section;
                Log.e("xue", "firstChar = " + firstChar + " " + "section = " + x + " name= " + list.get(i).getName());
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }


}