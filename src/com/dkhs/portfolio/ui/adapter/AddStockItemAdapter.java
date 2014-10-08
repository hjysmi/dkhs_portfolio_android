/**
 * @Title AddStockItemAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-29 下午1:33:35
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.CompoundButton;

import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;

/**
 * @ClassName AddStockItemAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-29 下午1:33:35
 * @version 1.0
 */
public class AddStockItemAdapter extends SelectStockAdatper {

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param context
     * @param datas
     */
    public AddStockItemAdapter(Context context, List<SelectStockBean> datas) {
        super(context, datas);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        SelectStockBean csBean = (SelectStockBean) buttonView.getTag();
        if (null != csBean) {

            if (isChecked) {
                new QuotesEngineImpl().symbolfollow(csBean.id, null);
            } else {
                new QuotesEngineImpl().delfollow(csBean.id, null);
                System.out.println("remove optional :" + csBean.name + " id:" + csBean.id);
            }
        }

        // System.out.println("remove mSelectIdList lenght:" + BaseSelectActivity.mSelectList.size());

    }

}
