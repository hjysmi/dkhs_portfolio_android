/**
 * @Title: MenuItemFragment.java
 * @Package com.yulebaby
 * @Description: TODO(用一句话描述该文件做什么)
 * @author think4
 * @date 2014-5-7 下午1:18:45
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.NewMainActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * @ClassName: MenuItemFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhoujunzhou
 * @date 2014-5-7 下午1:18:45
 * @version 1.0
 */
public class MenuItemFragment extends BaseFragment implements OnClickListener {

    public static final int TABINDEX_1 = R.id.tab_1;
    public static final int TABINDEX_2 = R.id.tab_2;
    public static final int TABINDEX_3 = R.id.tab_3;
    public static final int TABINDEX_4 = R.id.tab_4;

    @ViewInject(R.id.btn_tab1)
    private Button btnTab1;
    @ViewInject(R.id.tv_tab1)
    private TextView tvTab1;

    @ViewInject(R.id.btn_tab2)
    private Button btnTab2;
    @ViewInject(R.id.tv_tab2)
    private TextView tvTab2;

    @ViewInject(R.id.btn_tab3)
    private Button btnTab3;
    @ViewInject(R.id.tv_tab3)
    private TextView tvTab3;

    @ViewInject(R.id.btn_tab4)
    private Button btnTab4;
    @ViewInject(R.id.tv_tab4)
    private TextView tvTab4;

    @ViewInject(R.id.tab_1)
    private View tabLayout1;
    @ViewInject(R.id.tab_2)
    private View tabLayout2;
    @ViewInject(R.id.tab_3)
    private View tabLayout3;
    @ViewInject(R.id.tab_4)
    private View tabLayout4;

    private int mIndex = TABINDEX_1;

    public static final String KEY_TABINDEX = "key_tabindex";

    public static MenuItemFragment newInstance(int value) {
        MenuItemFragment fragment = new MenuItemFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(KEY_TABINDEX, value);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        // if (arguments != null) {
        // mIndex = arguments.getInt(KEY_TABINDEX);
        // }
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.layout_bottom;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        // clickTab(mIndex);
    }

    public void clickTab(int index) {
        mIndex = index;
        setupView();

        // BusProvider.getInstance().post(new TabSelectEvent(index));
        ((NewMainActivity) getActivity()).showContentIndex(index);
        // App.getInstance().mTabIndex = index;

    }

    private void setupView() {
        setViewDefStatus();
        if (mIndex == TABINDEX_1) {
            setSelectView(btnTab1);
            setSelectView(tabLayout1);
            setSelectText(tvTab1);

        } else if (mIndex == TABINDEX_2) {
            setSelectText(tvTab2);
            setSelectView(btnTab2);
            setSelectView(tabLayout2);
        } else if (mIndex == TABINDEX_3) {
            setSelectText(tvTab3);
            setSelectView(btnTab3);
            setSelectView(tabLayout3);
        } else if (mIndex == TABINDEX_4) {
            setSelectText(tvTab4);
            setSelectView(btnTab4);
            setSelectView(tabLayout4);
        }

    }

    private void setViewDefStatus() {
        btnTab1.setEnabled(true);
        btnTab2.setEnabled(true);
        btnTab3.setEnabled(true);
        btnTab4.setEnabled(true);
        tabLayout1.setEnabled(true);
        tabLayout2.setEnabled(true);
        tabLayout3.setEnabled(true);
        tabLayout4.setEnabled(true);
        ColorStateList cls = getResources().getColorStateList(R.color.compare_select_gray);
        tvTab1.setTextColor(cls);
        tvTab2.setTextColor(cls);
        tvTab3.setTextColor(cls);
        tvTab4.setTextColor(cls);
    }

    private void setSelectView(View rButton) {
        rButton.setEnabled(false);
    }

    private void setSelectText(TextView tvSelect) {
        tvSelect.setTextColor(getResources().getColorStateList(R.color.title_color));
    }

    @OnClick({ R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4 })
    public void onClick(View v) {
        int id = v.getId();

        clickTab(id);
    }

}
