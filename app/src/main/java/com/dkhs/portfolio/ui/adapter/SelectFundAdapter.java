/**
 * @Title SelectFundAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName SelectFundAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-28 下午3:36:46
 */
public class SelectFundAdapter extends BaseAdapter {

    private Context mContext;
    // private int dataLenght = 5;

    private List<SelectStockBean> mDataSet;
    private BaseSelectActivity mActivity;

    public SelectFundAdapter(Context context, List<SelectStockBean> mSelectIdList) {
        this.mContext = context;
        mActivity = (BaseSelectActivity) context;
        this.mDataSet = mSelectIdList;

    }

    @Override
    public int getCount() {

        return this.mDataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_select_refer_fund, null);

            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_item_select_fund);
            viewHolder.ivDelIcon = (ImageView) convertView.findViewById(R.id.iv_item_del);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        final SelectStockBean item = mDataSet.get(position);
        viewHolder.tvName.setText(item.name);

        viewHolder.ivDelIcon.setOnClickListener(new OndelClickListener(item));

        return convertView;
    }

    public class OndelClickListener implements OnClickListener {
        private SelectStockBean stockBean;

        public OndelClickListener(SelectStockBean stockBean) {
            this.stockBean = stockBean;
        }

        @Override
        public void onClick(View v) {
            if (stockBean.isStop()) {
                showDelDialog();
            } else {
                mDataSet.remove(stockBean);
                mActivity.notifySelectDataChange(true);
            }
        }
    }

    private void showDelDialog() {
        MAlertDialog builder = PromptManager.getAlertDialog(mContext);
        builder.setMessage(R.string.dialog_message_no_del_stock);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        builder.create().show();
    }


    final static class ViewHodler {
        TextView tvName;
        ImageView ivDelIcon;
    }
}
