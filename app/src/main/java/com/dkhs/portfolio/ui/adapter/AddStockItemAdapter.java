/**
 * @Title AddStockItemAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-29 下午1:33:35
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
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
 * @ClassName AddStockItemAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-29 下午1:33:35
 */
public class AddStockItemAdapter extends SelectStockAdatper {

    private ChangeFollowView changeFollowView;

    /**
     * @param context
     * @param datas
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    public AddStockItemAdapter(Context context, List<SelectStockBean> datas) {
        super(context, datas);
        setAddNewStock(true);
        init(context);

    }

    public AddStockItemAdapter(Context context, boolean isDefColor) {
        super(context, isDefColor);
        setAddNewStock(true);
        init(context);
    }

    public AddStockItemAdapter(Context context) {
        super(context);
        setAddNewStock(true);
        init(context);
    }

    public AddStockItemAdapter(Context context, List<SelectStockBean> datas, boolean isDefColor) {
        super(context, datas, isDefColor);
        setAddNewStock(true);
        init(context);
    }

    private void init(Context context) {
        changeFollowView = new ChangeFollowView(context);
        changeFollowView.setmChangeListener(changeSuccessListener);
    }


    private ChangeFollowView.IChangeSuccessListener changeSuccessListener = new ChangeFollowView.IChangeSuccessListener() {
        @Override
        public void onChange(SelectStockBean stockBean) {
            if (null != stockBean) {
                if (!PortfolioApplication.hasUserLogin()) {// 如果当前是游客模式，添加自选股到本地数据库

                    if (stockBean.isFollowed()) {
                        SelectAddOptionalActivity.mFollowList.add(stockBean);
                    } else {
                        SelectAddOptionalActivity.mFollowList.remove(stockBean);
                    }
                }


            } else {


                if (stockBean.isFollowed()) {
                    int index = mDataList.indexOf(stockBean);
                    mDataList.get(index).isFollowed = true;
                } else {
                    int index = mDataList.indexOf(stockBean);
                    mDataList.get(index).isFollowed = false;
                }

            }
        }
    };


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
