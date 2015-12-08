/**
 * @Title TabFundsFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.percent.PercentLinearLayout;
import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.SelectGeneralActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import java.util.Random;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MarketFundsFragment
 * @Description TODO(基金tab Fragment)
 * @date 2015-2-7 上午11:03:26
 * @modified zcm
 * @date 2015-12-7 上午9:40:54
 */
public class MarketFundsFragment1 extends VisiableLoadFragment implements OnClickListener {

    public static final String TAG = "MarketFundsFragment";
    //三个指数
    //上证指数
    @ViewInject(R.id.tv_shangzheng_main_value)
    private TextView tv_shangzheng_main_value;
    @ViewInject(R.id.tv_shangzheng_incease_value)
    private TextView tv_shangzheng_incease_value;
    @ViewInject(R.id.tv_shangzheng_incease_ratio)
    private TextView tv_shangzheng_incease_ratio;
    //深证指数
    @ViewInject(R.id.tv_shenzheng_main_value)
    private TextView tv_shenzheng_main_value;
    @ViewInject(R.id.tv_shenzheng_incease_value)
    private TextView tv_shenzheng_incease_value;
    @ViewInject(R.id.tv_shenzheng_incease_ratio)
    private TextView tv_shenzheng_incease_ratio;
    //创业板指
    @ViewInject(R.id.tv_chuangye_main_value)
    private TextView tv_chuangye_main_value;
    @ViewInject(R.id.tv_chuangye_incease_value)
    private TextView tv_chuangye_incease_value;
    @ViewInject(R.id.tv_chuangye_incease_ratio)
    private TextView tv_chuangye_incease_ratio;
    //三个指数

    //顶部banner
    @ViewInject(R.id.ll_fund)
    private LinearLayout ll_fund;
    //基金经理banner
    @ViewInject(R.id.ll_fund)
    private LinearLayout sv_fundmanager;

    //三个理财专线banner
    //背景图
    @ViewInject(R.id.iv_bg1)
    private TextView iv_bg1;
    @ViewInject(R.id.iv_bg2)
    private TextView iv_bg2;
    @ViewInject(R.id.iv_bg3)
    private TextView iv_bg3;
    //专线
    @ViewInject(R.id.tv_special1)
    private TextView tv_special1;
    @ViewInject(R.id.tv_special2)
    private TextView tv_special2;
    @ViewInject(R.id.tv_special3)
    private TextView tv_special3;
    //名称
    @ViewInject(R.id.tv_special_name1)
    private TextView tv_special_name1;
    @ViewInject(R.id.tv_special_name2)
    private TextView tv_special_name2;
    @ViewInject(R.id.tv_special_name3)
    private TextView tv_special_name3;
    //收益率
    @ViewInject(R.id.tv_special_percent1)
    private TextView tv_special_percent1;
    @ViewInject(R.id.tv_special_percent2)
    private TextView tv_special_percent2;
    @ViewInject(R.id.tv_special_percent3)
    private TextView tv_special_percent3;
    //标签
    @ViewInject(R.id.ll_special_tag1)
    private PercentLinearLayout ll_special_tag1;
    @ViewInject(R.id.ll_special_tag1)
    private PercentLinearLayout ll_special_tag2;
    @ViewInject(R.id.ll_special_tag1)
    private PercentLinearLayout ll_special_tag3;
    //三个理财专线banner

    //三个专题理财
    //名称
    @ViewInject(R.id.tv_special_financing1)
    private TextView tv_special_financing1;
    @ViewInject(R.id.tv_special_financing2)
    private TextView tv_special_financing2;
    @ViewInject(R.id.tv_special_financing3)
    private TextView tv_special_financing3;
    //标题
    @ViewInject(R.id.tv_special_financing_title1)
    private TextView tv_special_financing_title1;
    @ViewInject(R.id.tv_special_financing_title2)
    private TextView tv_special_financing_title2;
    @ViewInject(R.id.tv_special_financing_title3)
    private TextView tv_special_financing_title3;
    //标题
    @ViewInject(R.id.tv_special_financing_percent1)
    private TextView tv_special_financing_percent1;
    @ViewInject(R.id.tv_special_financing_percent2)
    private TextView tv_special_financing_percent2;
    @ViewInject(R.id.tv_special_financing_percent3)
    private TextView tv_special_financing_percent3;
    //标题
    @ViewInject(R.id.tv_special_financing_desc1)
    private TextView tv_special_financing_desc1;
    @ViewInject(R.id.tv_special_financing_desc2)
    private TextView tv_special_financing_desc2;
    @ViewInject(R.id.tv_special_financing_desc3)
    private TextView tv_special_financing_desc3;
    //三个专题理财

    @ViewInject(R.id.swipe_container)
    public SwipeRefreshLayout mSwipeLayout;

    private RelativeLayout.LayoutParams params;
    private static String[] colorRandom = new String[]{"#70aba0", "#e4b524", "#86b2f6", "#f77d7b", "#f4ad56", "#f9760b"};


    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_market_funds1;
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

    private void handIntent(Bundle bundle) {
        if (bundle.containsKey("fund_manager_ranking")) {
            boolean fundManagerRanking = bundle.getBoolean("fund_manager_ranking", true);
            if (fundManagerRanking) {
            } else {
            }
        }
    }


    @Subscribe
    public void newIntent(NewIntent newIntent) {
        handIntent(newIntent.bundle);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(getView());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void requestData() {

    }

    @Override
    public void onViewShow() {

        super.onViewShow();
        StatService.onPageStart(getActivity(), TAG);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && isVisible()) {
            if (getView() != null) {
                onViewShow();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    public void initView(View view) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels * 5 / 8;
        int height = width / 2;
        params = new RelativeLayout.LayoutParams(width, height);
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
//        mSwipeLayout.setRefreshing(true);
//        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                refresh();
//
//            }
//        });

        View child = View.inflate(getActivity(), R.layout.layout_special_tag_green, null);
        ((TextView) child.findViewById(R.id.tv_tag)).setText("10元起投");
        ll_special_tag1.addView(child);
        test();
    }

    private void test() {
        View leftView = new View(getActivity());
        leftView.setLayoutParams(new ViewGroup.LayoutParams(getActivity().getResources().getDimensionPixelOffset(R.dimen.widget_margin_medium),1));
        ll_fund.addView(leftView);
        for (int i = 0; i < 3; i++) {
            View bannerItem = View.inflate(getActivity(), R.layout.item_market_fund_banner, null);
            bannerItem.findViewById(R.id.ll_content).setBackgroundColor(Color.parseColor(colorRandom[new Random().nextInt(colorRandom.length)]));
            TextView tv_title = (TextView) bannerItem.findViewById(R.id.tv_title);
            TextView tv_desc = (TextView) bannerItem.findViewById(R.id.tv_desc);
            bannerItem.setLayoutParams(params);
            tv_title.setText("土豪年底收益X3");
            tv_desc.setText("大金库豪翻24月");
            ll_fund.addView(bannerItem);
        }
    }

    @OnClick({R.id.rl_fund_market, R.id.fl_sepcial_feature1, R.id.fl_sepcial_feature2, R.id.fl_sepcial_feature3})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_refresh: {
                refresh();
            }
            break;
            case R.id.btn_search:
                UIUtils.startAnimationActivity(getActivity(), new Intent(getActivity(), SelectGeneralActivity.class));
                break;
            case R.id.rl_fund_market:
                //TODO 基金超市查看全部
                break;
            case R.id.ll_fund_manager:
                //TODO 基金超市查看基金经理
                break;
            case R.id.ll_profit_rank:
                //TODO 基金超市查看收益排行
                break;
            case R.id.ll_hybrid:
                //TODO 基金超市查看混合型
                break;
            case R.id.fl_sepcial_feature1:
                //TODO 理财专线banner1
                break;
            case R.id.fl_sepcial_feature2:
                //TODO 理财专线banner2
                break;
            case R.id.fl_sepcial_feature3:
                //TODO 理财专线banner3
                break;
            case R.id.rl_fund_manager:
                //TODO 基金经理查看更多
                break;
            case R.id.rl_financing1:
                //TODO 三个理财专题1
                break;
            case R.id.rl_financing2:
                //TODO 三个理财专题2
                break;
            case R.id.rl_financing3:
                //TODO 三个理财专题3
                break;
            default:
                break;
        }

    }

    private void refresh() {
        // TODO: 2015/12/7 刷新数据
    }


    @Override
    public void onViewHide() {
        super.onViewHide();
        StatService.onPageEnd(getActivity(), TAG);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


}
