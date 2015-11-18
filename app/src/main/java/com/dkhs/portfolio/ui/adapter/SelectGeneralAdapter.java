package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.QuotesBean;

import java.util.List;

/**
 * Created by xuetong on 2015/11/16.
 */
public class SelectGeneralAdapter extends BaseAdapter {
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_TYPE = 1;
    private static final int TYPE_MANAGER = 2;
    private static final int TYPE_MORE = 3;
    private Context context;
    private List<Object> list;
    private LayoutInflater inflater;

    public SelectGeneralAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolderTitle holderTitle = null;
        ViewHolderType holderType = null;
        if (convertView == null) {
            switch (type) {
                //标题
                case TYPE_TITLE:
                    holderTitle = new ViewHolderTitle();
                    convertView = inflater.inflate(R.layout.layout_search_title, parent, false);
                    holderTitle.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                    convertView.setTag(R.id.Tag_type_title, holderTitle);
                    break;
                //股票，基金，指数，组合
                case TYPE_TYPE:
                    holderType = new ViewHolderType();
                    convertView = inflater.inflate(R.layout.layout_search_type, parent, false);
                    holderType.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
                    holderType.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    convertView.setTag(R.id.Tag_type_type, holderType);
                    break;
                //基金经理，用户
                case TYPE_MANAGER:

                    break;
                //查看更多
                case TYPE_MORE:

                    break;
            }
        } else {
            switch (type) {
                case TYPE_TITLE:
                    holderTitle = (ViewHolderTitle) convertView.getTag(R.id.Tag_type_title);
                    break;
                case TYPE_TYPE:
                    holderType = (ViewHolderType) convertView.getTag(R.id.Tag_type_type);
                    break;
            }
        }
        //
        Object item = getItem(position);
        switch (type) {
            case TYPE_TITLE:
                String title = (String) item;
                if (null != title) {
                    holderTitle.tv_title.setText(title);
                }
                break;
            case TYPE_TYPE:
                QuotesBean quotesBean = (QuotesBean) item;
                holderType.tv_code.setText(quotesBean.getSymbol());
                holderType.tv_name.setText(quotesBean.getAbbrName());
                break;

        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (item instanceof String) {
            return TYPE_TITLE;
        } else if (item instanceof QuotesBean) {
            return TYPE_TYPE;
        } else if (item instanceof FundManagerBean) {
            return TYPE_MANAGER;
        } else {
            return super.getItemViewType(position);
        }
    }

    /**
     * 股票，基金，指数，组合
     */
    private static class ViewHolderType {
        TextView tv_code, tv_name;
    }

    /**
     * 标题
     */
    private static class ViewHolderTitle {
        TextView tv_title;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void bindDatas(List<Object> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
