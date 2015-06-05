package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.melnykov.fab.ObservableScrollView;

import org.parceler.Parcels;

/**
 * 基金详情页
 * Created by zjz on 2015/6/4.
 */
public class FundDetailActivity extends ModelAcitivity {

    public static final String EXTRA_FUND = "extra_fund";
    private SelectStockBean mFundBean;


    @ViewInject(R.id.hs_title)
    private HScrollTitleView hsTitleTab;

    @ViewInject(R.id.layout_sepfund_header)
    private ViewStub vsSepFundHeader;
    @ViewInject(R.id.layout_noramlfund_header)
    private ViewStub vsNormalFundHeader;

    @ViewInject(R.id.floating_action_view)
    private FloatingActionMenu mFloatMenu;

    public static Intent newIntent(Context context, SelectStockBean bean) {
        Intent intent = new Intent(context, FundDetailActivity.class);
        intent.putExtra(EXTRA_FUND, Parcels.wrap(bean));

        return intent;
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_fund_detail);
        ViewUtils.inject(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        updataTitle();
        initView();
    }

    private void handleExtras(Bundle extras) {
        if (extras != null) {
            mFundBean = Parcels.unwrap(extras.getParcelable(EXTRA_FUND));
        }

    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private void updataTitle() {
        if (null != mFundBean) {
            setTitle(mFundBean.getName());
            setTitleTipString(
                    mFundBean.tradeDay);
        }

    }


    private void initView() {

        String[] stockListTiles = getResources().getStringArray(R.array.fund_tab_titles);
        hsTitleTab.setTitleList(stockListTiles, getResources().getDimensionPixelSize(R.dimen.title_2text_length));

        mFloatMenu.attachToScrollView((ObservableScrollView) findViewById(R.id.sv_combinations));
        mFloatMenu.setOnMenuItemSelectedListener(mFloatMenuSelectListner);
        initFloatingActionMenu();
//        vsSepFundHeader.inflate();
        vsNormalFundHeader.inflate();

//        replaceTrendView();
//        replacePostionView();
//        replaceCompareView();
//
//        mChangeFollowView = new ChangeFollowView(this);

    }

    private final int MENU_FOLLOW = 1;
    private final int MENU_DELFOLLOW = 2;
    private final int MENU_REMIND = 3;
    private final int MENU_SHARE = 6;

    FloatingActionMenu.OnMenuItemSelectedListener mFloatMenuSelectListner = new FloatingActionMenu.OnMenuItemSelectedListener() {

        @Override
        public boolean onMenuItemSelected(int selectIndex) {

            switch (selectIndex) {

                case MENU_FOLLOW: { // 调整仓位

                }
                break;

                case MENU_DELFOLLOW: {

                }
                break;
                case MENU_REMIND: {

                }
                break;
                case MENU_SHARE: {

                }
                break;
                default:
                    break;
            }
            return false;
        }
    };

//    @Subscribe
//    public void updateCombination(UpdateCombinationEvent updateCombinationEvent) {
//        if (null != updateCombinationEvent && null != updateCombinationEvent.mCombinationBean) {
//            initFloatingActionMenu();
//        }
//    }

    private void initFloatingActionMenu() {
        mFloatMenu.removeAllItems();

//        if (mCombinationBean.isFollowed()) {
//            mFloatMenu.addItem(MENU_DELFOLLOW, R.string.float_menu_delfollow,
//                    R.drawable.btn_del_item_normal);
//            mFloatMenu.addItem(MENU_REMIND, R.string.float_menu_remind, R.drawable.ic_fm_remind);
//            mFloatMenu.addItem(MENU_SHARE, R.string.float_menu_share, R.drawable.ic_fm_share);
//        } else {
        mFloatMenu.addItem(MENU_FOLLOW, R.string.float_menu_follow, R.drawable.ic_add);
        mFloatMenu.addItem(MENU_SHARE, R.string.float_menu_share, R.drawable.ic_fm_share);

//        }

    }


}
