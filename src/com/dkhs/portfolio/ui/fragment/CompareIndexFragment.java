/**
 * @Title CompareFundFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-5-5 下午4:18:02
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @ClassName CompareFundFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-5-5 下午4:18:02
 * @version 1.0
 */
public class CompareIndexFragment extends BaseFragment {

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_compare_index;
    }

    @OnClick({ R.id.tv_more_funds })
    public void onClick(View v) {
        if (v.getId() == R.id.tv_more_funds) {
            PromptManager.showToast("更多基金比较");
        }
    }

}
