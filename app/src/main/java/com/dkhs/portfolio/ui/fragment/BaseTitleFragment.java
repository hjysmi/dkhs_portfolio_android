/**
 * @Title BaseFragmentTitle.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-5 下午12:48:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.TextImageButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zjz
 * @version 1.0
 * @ClassName BaseFragmentTitle
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-5 下午12:48:51
 */
public abstract class BaseTitleFragment extends BaseFragment {

    public final int RIGHTBUTTON_ID = R.id.btn_right;
    public final int BACKBUTTON_ID = R.id.btn_back;
    public final int SECONDRIGHTBUTTON_ID = R.id.btn_right_second;
    @ViewInject(R.id.btn_back)
    private TextImageButton btnBack;

    @ViewInject(RIGHTBUTTON_ID)
    private Button btnRight;

    @ViewInject(SECONDRIGHTBUTTON_ID)
    private Button btnSecondRight;

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
//    @ViewInject(R.id.includeHead)
    protected RelativeLayout titleRL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View wrapper = inflater.inflate(R.layout.layout_model, null);
        wrapper.setBackgroundResource(R.color.person_setting_backgroud);
        RelativeLayout layoutContent = (RelativeLayout) wrapper.findViewById(R.id.layoutContent);
        // LinearLayout wrapper = new LinearLayout(getActivity()); // for example
        // inflater.inflate(setContentLayoutId(), wrapper, true);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layoutContent.addView(View.inflate(getActivity(), setContentLayoutId(), null), params);

        System.out.println("BaseTitleFragment onCreateView()");
        return wrapper;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view); // 注入view和事件
        btnBack = (TextImageButton) view.findViewById(BACKBUTTON_ID);
        titleRL = (RelativeLayout) view.findViewById(R.id.includeHead);
        btnRight = (Button) view.findViewById(RIGHTBUTTON_ID);
        btnSecondRight = (Button) view.findViewById(SECONDRIGHTBUTTON_ID);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        hideBackButton();
        // btnBac.(setVisibilityView.GONE);
    }

    public void hideBackButton() {
        if (null != btnBack) {
            btnBack.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title) {
        if (null != tvTitle) {
            tvTitle.setText(title);
        }
    }

    public void setTitle(int titleIds) {
        if (null != tvTitle) {
            tvTitle.setText(titleIds);
        }
    }

    public Button getRightButton() {
        // Button btnRight = (Button) findViewById(RIGHTBUTTON_ID);
        btnRight.setVisibility(View.VISIBLE);
        // btnRight.setTextColor(Color.WHITE);
        return btnRight;
    }

    public Button getSecondRightButton() {
        btnSecondRight.setVisibility(View.VISIBLE);
        return btnSecondRight;
    }

}
