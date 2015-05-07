/**
 * @Title FragmentLifecycle.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-9 下午6:36:17
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

/**
 * @ClassName FragmentLifecycle
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-9 下午6:36:17
 * @version 1.0
 */
public interface FragmentLifecycle {
    public void onPauseFragment();

    public void onResumeFragment();
}
