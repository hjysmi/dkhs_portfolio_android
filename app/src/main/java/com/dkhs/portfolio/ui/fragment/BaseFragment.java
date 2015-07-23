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

import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
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
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public abstract int setContentLayoutId();

    // Fpublic abstract void initView(View view);

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param intent
     * @return
     */
    @Override
    public void startActivity(Intent intent) {
        System.out.println("BaseFragment startActivity");
        super.startActivity(intent);
        UIUtils.setOverridePendingAnin(getActivity());
    }

    public void startActivitySlideFormBottomAnim(Intent intent) {
        System.out.println("BaseFragment startActivity");
        super.startActivity(intent);
        UIUtils.setOverridePendingSlideFormBottomAnim(getActivity());
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param intent
     * @param requestCode
     * @return
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // TODO Auto-generated method stub
        UIUtils.setOverridePendingAnin(getActivity());
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        if(hidden){
            if(getView() != null){
                MobclickAgent.onPageEnd(this.getClass().getSimpleName());
            }
        }else{
             if(getView() !=null){
                 MobclickAgent.onPageStart(this.getClass().getSimpleName());

             }
        }
        super.onHiddenChanged(hidden);
    }
}
