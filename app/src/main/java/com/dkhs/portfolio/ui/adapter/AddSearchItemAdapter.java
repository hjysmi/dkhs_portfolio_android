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
