/**
 * @Title AdatperSelectConbinStock.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-29 上午10:25:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.BaseSelectActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName AdatperSelectConbinStock
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-29 上午10:25:51
 */
public class BaseAdatperSelectStockFund extends BaseAdapter implements OnCheckedChangeListener {
    public Context mContext;
    public List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();

    public BaseAdatperSelectStockFund(Context context) {
        this.mContext = context;
    }

    public BaseAdatperSelectStockFund(Context context, List<SelectStockBean> datas) {
        this.mContext = context;
        this.mDataList = datas;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setData(List<SelectStockBean> dataList) {
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    /**
     * @param buttonView
     * @param isChecked
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SelectStockBean csBean = (SelectStockBean) buttonView.getTag();

        if (this instanceof SelectCompareFundAdatper && isChecked) {
            if (BaseSelectActivity.mSelectList.size() == 5) {
                Toast.makeText(mContext, mContext.getString(R.string.max_fund_select_tip), Toast.LENGTH_SHORT).show();
                buttonView.setChecked(false);
                return;
            }
        }
        if (isChecked && !BaseSelectActivity.mSelectList.contains(csBean)) {
            if (BaseSelectActivity.mSelectList.size() == 20) {
                Toast.makeText(mContext, "最多只能添加20只股票", Toast.LENGTH_SHORT).show();
                buttonView.setChecked(false);
                return;
            }
            BaseSelectActivity.mSelectList.add(csBean);

        } else {
            boolean isRmove = BaseSelectActivity.mSelectList.remove(csBean);

            if (isRmove) {

                // System.out.println("remove mSelectIdList lenght:" + BaseSelectActivity.mSelectList.size());
            }
        }
        if (null != mSelectLisenter) {
            mSelectLisenter.onCheckedChanged(buttonView, isChecked);
        }

    }

    public ISelectChangeListener mSelectLisenter;

    public interface ISelectChangeListener {
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
    }

    public void setCheckChangeListener(ISelectChangeListener listener) {
        this.mSelectLisenter = listener;
    }



}
