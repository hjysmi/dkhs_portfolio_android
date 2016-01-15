/**
 * @Title BaseFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-21 下午12:38:27
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;

/**
 * @author zjz
 * @version 1.0
 * @ClassName BaseFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-11-21 下午12:38:27
 */
public abstract class BaseFragment extends Fragment {


//    private boolean mVisibleToUser = false;

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
        View view = (ViewGroup) inflater.inflate(setContentLayoutId(), container, false);
        ViewUtils.inject(this, view); // 注入view和事件
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (iStrictVisible()) {
//            onVisibleHintChanged(true);
//        }
        if (getPageStatisticsStringId() != 0) {
            StatService.onPageStart(getActivity(), UIUtils.getResString(getActivity(), getPageStatisticsStringId()));
            MobclickAgent.onPageStart(UIUtils.getResString(getActivity(), getPageStatisticsStringId()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        onVisibleHintChanged(false);
        if (getPageStatisticsStringId() != 0) {
            StatService.onPageEnd(getActivity(), UIUtils.getResString(getActivity(), getPageStatisticsStringId()));
            MobclickAgent.onPageEnd(UIUtils.getResString(getActivity(), getPageStatisticsStringId()));
        }
    }

    public int getPageStatisticsStringId(){
        return 0;
    }

    public abstract int setContentLayoutId();


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        UIUtils.setOverridePendingAnin(getActivity());
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        UIUtils.setOverridePendingAnin(getActivity());
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * fix me
     */
//    public void onVisibleHintChanged(boolean isVisibleToUser) {
//
//        if (mVisibleToUser != isVisibleToUser) {
//            if (isVisibleToUser) {
//                if (getView() != null) {
//                    onViewShow();
//                    mVisibleToUser = isVisibleToUser;
//                    MobclickAgent.onPageStart(this.getClass().getSimpleName());
//                }
//            } else {
//                if (getView() != null) {
//                    MobclickAgent.onPageEnd(this.getClass().getSimpleName());
//                    onViewHide();
//                    mVisibleToUser = isVisibleToUser;
//                }
//            }
//
//        }
//    }
//
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        onVisibleHintChanged(isVisibleToUser);
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        onVisibleHintChanged(!hidden);
//    }

    /**
     * fix me
     */
//    public void onViewShow() {
//        List<Fragment> fragments = getChildFragmentManager().getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                if (fragment != null && fragment instanceof InstanceVisibilityStateI) {
//                    InstanceVisibilityStateI instanceVisibilityStateI = (InstanceVisibilityStateI) fragment;
//                    instanceVisibilityStateI.restoreStats();
//                }
//            }
//        }
//    }

    /**
     * fix me
     */
//    public void onViewHide() {
//        List<Fragment> fragments = getChildFragmentManager().getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                if (fragment != null && fragment instanceof InstanceVisibilityStateI) {
//                    InstanceVisibilityStateI instanceVisibilityStateI = (InstanceVisibilityStateI) fragment;
//                    instanceVisibilityStateI.saveStats();
//
//                }
//            }
//        }
//    }
//
//    protected boolean iStrictVisible() {
//        if (getParentFragment() != null) {
//            return getUserVisibleHint() && isVisible() && getParentFragment().isVisible();
//        } else {
//            return getUserVisibleHint() && isVisible();
//        }
//    }
//
//    public boolean mSaveVisibleToUser = false;
//
//    public void saveStats() {
//        mSaveVisibleToUser = mVisibleToUser;
//        onVisibleHintChanged(false);
//    }
//
//    public void restoreStats() {
//        onVisibleHintChanged(mSaveVisibleToUser);
//    }
//
    @Override
    public void onHiddenChanged(boolean hidden) {

        if (hidden) {
            if (getView() != null) {
                MobclickAgent.onPageEnd(this.getClass().getSimpleName());
            }
        } else {
            if (getView() != null) {
                MobclickAgent.onPageStart(this.getClass().getSimpleName());

            }
        }
        super.onHiddenChanged(hidden);
    }

}
