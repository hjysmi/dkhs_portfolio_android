package com.dkhs.portfolio.ui.adapter;

import android.content.Context;

import com.dkhs.portfolio.bean.MenuBean;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MultiRVAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/10.
 */
public class MultiRVAdapter extends  BaseRVAdapter {
    public MultiRVAdapter(Context context, List<List<MenuBean>> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return 0;
    }
    @Override
    public int getItemCount() {
        int count =0;

        List<List<MenuBean>> itemsList= (List<List<MenuBean>>) list;
        for (List<MenuBean>  itmes : itemsList) {

            if(itmes.size() % 4 > 0 ) {
                count+=itmes.size()/4+1;
            }else{
                count+=itmes.size()/4;
            }

        }
        return count;
    }
}
