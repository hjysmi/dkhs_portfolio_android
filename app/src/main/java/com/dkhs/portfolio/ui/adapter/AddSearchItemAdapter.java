/**
 * @Title AddSearchItemAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-8 下午2:02:32
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.widget.ChangeFollowView;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName AddSearchItemAdapter
 * @Description 搜索添加自选
 * @date 2014-10-8 下午2:02:32
 */
public class AddSearchItemAdapter extends SearchStockAdatper {


    private ChangeFollowView changeFollowView;
    private boolean isStatus;

    /**
     * @param context
     * @param datas
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    public AddSearchItemAdapter(Context context, List<SelectStockBean> datas) {
        super(context, datas, false);
        init(context);
    }

    public AddSearchItemAdapter(Context context, List<SelectStockBean> datas, boolean isStatus) {
        super(context, datas, false);
        init(context);
        this.isStatus = isStatus;
    }


    private void init(Context context) {
        changeFollowView = new ChangeFollowView(context);
        changeFollowView.setmChangeListener(changeSuccessListener);
    }

    private ChangeFollowView.IChangeSuccessListener changeSuccessListener = new ChangeFollowView.IChangeSuccessListener() {
        @Override
        public void onChange(SelectStockBean stockBean) {
            if (null != stockBean) {

                if (stockBean.isFollowed()) {
                    SelectAddOptionalActivity.mFollowList.add(stockBean);
                } else {
                    SelectAddOptionalActivity.mFollowList.remove(stockBean);
                }

                notifyDataSetChanged();
            }
        }
    };


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (isStatus) {
            ViewHodler viewHolder = (ViewHodler) view.getTag();
            if (null != viewHolder)
                viewHolder.mCheckbox.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        SelectStockBean csBean = (SelectStockBean) buttonView.getTag();


        if (!NetUtil.checkNetWork() && PortfolioApplication.hasUserLogin()) {
            buttonView.setChecked(!isChecked);
            PromptManager.showNoNetWork();
        } else if (changeFollowView != null) {
            changeFollowView.changeFollowNoDialog(csBean);
        }
    }


}
