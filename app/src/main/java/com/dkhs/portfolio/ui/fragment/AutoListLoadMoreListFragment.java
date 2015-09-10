package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.mingle.autolist.DataObserver;
import com.mingle.autolist.DataObserverEnable;
import com.mingle.autolist.DataObserverHelper;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AutoListLoadMoreListFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/8/31.
 */
public abstract class AutoListLoadMoreListFragment extends LoadMoreListFragment implements DataObserverEnable {

    DataObserverHelper mDataObserverHelper = new DataObserverHelper(this);


    public void addDataObserver(DataObserver dataObserver) {
        this.mDataObserverHelper.add(dataObserver);
    }


    @Override
    public void onDestroyView() {
        this.mDataObserverHelper.unRegister();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDataObserverHelper.register();
    }

}
