package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.MyDraftActivity;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.ui.adapter.BasePagerFragmentAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
import com.dkhs.portfolio.ui.eventbus.SendTopicEvent;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainRewardFragment extends VisiableLoadFragment {

    @ViewInject(R.id.ll)
    LinearLayout mll;
    @ViewInject(R.id.rl_send_fail_tip)
    View viewSendFail;
    @ViewInject(R.id.vp)
    ViewPager mVp;
    private BasePagerFragmentAdapter mAdpter;
    @ViewInject(R.id.btn_titletab_left)
    Button mLeftBtn;
    @ViewInject(R.id.btn_titletab_right)
    Button mRightBtn;
    private int mCurFragment;
    public MainRewardFragment() {
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_reward;
    }


    @Override
    public void requestData() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }


    @Subscribe
    public void topicSendFail(SendTopicEvent event) {
        if (null != event && null != viewSendFail) {
            viewSendFail.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<>();
        fragments.add(new RewardsFragment());
        fragments.add(new HotTopicsFragment());

        //超级夸张
//        mAdpter = new FragmentSelectAdapter(mActivity, getResources().getStringArray(R.array.reward_title_list), fragments, mll, getChildFragmentManager());
        mAdpter = new BasePagerFragmentAdapter(getChildFragmentManager(), fragments);
        mVp.setAdapter(new BasePagerFragmentAdapter(getChildFragmentManager(), fragments));
        mVp.setOnPageChangeListener(new OnPagerListener());
        mVp.setCurrentItem(0);
        setSelectTextSize(mLeftBtn);
        super.onViewCreated(view, savedInstanceState);

        viewSendFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSendFail.setVisibility(View.GONE);
                UIUtils.startAnimationActivity(getActivity(), new Intent(getActivity(), MyDraftActivity.class));
            }
        });
        if(getActivity() instanceof MainActivity){
            Bundle bundle=((MainActivity)getActivity()).mBundle;
            if(bundle !=null)
                handIntent(bundle);
        }

        view.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (UIUtils.iStartLoginActivity(getActivity())) {
                    return;
                }
                if(mCurFragment == 0){
                    //TODO
                }else if(mCurFragment == 1){
                    getActivity().startActivity(PostTopicActivity.getIntent(getActivity(), PostTopicActivity.TYPE_POST, "", ""));
                }
            }
        });
    }



    @Subscribe
    public void newIntent(NewIntent newIntent){
        handIntent(newIntent.bundle);
    }
    private void handIntent(Bundle bundle) {
        if (bundle.containsKey("bbs_index")) {
            int index = bundle.getInt("bbs_index", 0);
            mVp.setCurrentItem(index);
        }
    }

    private void setSelectTextSize(Button button) {
        int smallSize = button.getResources().getDimensionPixelSize(R.dimen.text_tab_normal);
        LogUtils.d("smallSize:" + smallSize);
        mLeftBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mRightBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }

    @OnClick({R.id.btn_titletab_right,R.id.btn_titletab_left})
    public void switchTab(View v){
        switch (v.getId()){
            case R.id.btn_titletab_left:
                mVp.setCurrentItem(0);
                mCurFragment = 0;
                setSelectTextSize(mLeftBtn);
                mLeftBtn.setEnabled(false);
                mRightBtn.setEnabled(true);
                break;
            case R.id.btn_titletab_right:
                mCurFragment = 1;
                mVp.setCurrentItem(1);
                setSelectTextSize(mRightBtn);
                mLeftBtn.setEnabled(true);
                mRightBtn.setEnabled(false);
                break;
        }
    }

    class OnPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            switch (i) {
                case 0:
                    mLeftBtn.performClick();
                    break;
                case 1:
                    mRightBtn.performClick();
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }
}
