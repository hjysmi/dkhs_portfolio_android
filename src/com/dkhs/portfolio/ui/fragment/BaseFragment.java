/**
 * @Title BaseFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-21 下午12:38:27
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.lang.reflect.Field;

import com.lidroid.xutils.ViewUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName BaseFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-21 下午12:38:27
 * @version 1.0
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setContentLayoutId(), null);
        ViewUtils.inject(this, view); // 注入view和事件
        System.out.println("BaseFragment onCreateView()");
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves so that we can provide the initial value.
        // BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Always unregister when an object no longer should be on the bus.
        // BusProvider.getInstance().unregister(this);
    }

    public abstract int setContentLayoutId();

    // Fpublic abstract void initView(View view);

}
