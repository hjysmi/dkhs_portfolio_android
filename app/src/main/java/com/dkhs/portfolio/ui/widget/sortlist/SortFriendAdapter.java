package com.dkhs.portfolio.ui.widget.sortlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageView;
import com.dkhs.portfolio.bean.SortUserEntity;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.utils.WaterMarkUtil;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

public class SortFriendAdapter extends BaseAdapter implements SectionIndexer {

    private List<SortUserEntity> list = null;

    private Context mContext;
    private final BitmapUtils bitmapUtils;

    public SortFriendAdapter(Context mContext, List<SortUserEntity> list) {
        this.mContext = mContext;
        this.list = list;
        bitmapUtils = new BitmapUtils(mContext);
    }

    public void updateListView(List<SortUserEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        final SortUserEntity mContent = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_friend, null);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            viewHolder.ivAvater = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.ivWaterMark = (ImageView) convertView.findViewById(R.id.iv_water_mark);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            if (mContent.getSortLetters().equals("*")) {
                viewHolder.tvLetter.setText(R.string.last_contact_friend);
            } else {

                viewHolder.tvLetter.setText(mContent.getSortLetters());
            }
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvUserName.setText(mContent.getUsername());

        if (null != mContent.getAvatar_md() && mContent.getAvatar_md().length() > 35) {
            bitmapUtils.display(viewHolder.ivAvater, mContent.getAvatar_md(), R.drawable.ic_user_head, R.drawable.ic_user_head);
        } else {
            viewHolder.ivAvater.setImageResource(R.drawable.ic_user_head);
        }
        WaterMarkUtil.calWaterMarkImage(viewHolder.ivWaterMark, mContent.verified, mContent.verified_type == UserEntity.VERIFIEDTYPE.EXPERT.getTypeid() ? WaterMarkUtil.TYPE_RED : WaterMarkUtil.TYPE_BLUE);

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvUserName;
        ImageView ivAvater;
        ImageView ivWaterMark;
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
