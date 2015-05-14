/**
 * @Title MarketListActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-22 下午5:47:35
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentMarkerCenter;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MarketListActivity
 * @Description 由行情中心入口的列表Activity, 包含：国内行情、个股涨跌幅、个股振幅、个股换手率、热门板块界面；
 * @date 2014-12-22 下午5:47:35
 */
public class MarketListActivity extends RefreshModelActivity {

    public enum LoadViewType {
        /**
         * 个股涨幅排序
         */
        StockIncease,
        /**
         * 个股跌幅排序
         */
        StockDown,
        /**
         * 国内指数涨幅排序
         */
        IndexUp,
        /**
         * 国内指数跌幅幅排序
         */
        IndexDown,
        /**
         * 个股换手率排序
         */
        StockTurnOver,
        /**
         * 个股振幅
         */
        StockAmplit,
        /**
         * 热门行业
         */
        PlateHot,
        /**
         * 行业列表
         */
        PlateList;
    }

    private LoadViewType mLoadType;
    private String mPlateName;
    private String mPlateId;

    public static final String EXTRA_PLATE_NAME = "plate_name";
    public static final String EXTRA_PLATE_ID = "plate_id";
    public static final String EXTRA_LOAD_TYPE = "load_type";

    public static Intent newIntent(Context context, LoadViewType loadType) {
        Intent intent = new Intent(context, MarketListActivity.class);

        intent.putExtra(EXTRA_LOAD_TYPE, loadType);

        return intent;
    }

    public static Intent newIntent(Context context, LoadViewType loadType, String plateId, String platename) {
        Intent intent = new Intent(context, MarketListActivity.class);

        intent.putExtra(EXTRA_LOAD_TYPE, loadType);
        intent.putExtra(EXTRA_PLATE_NAME, platename);
        intent.putExtra(EXTRA_PLATE_ID, plateId);

        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);// must store the new intent unless getIntent() will return the old one
        processExtraData();

    }

    private void processExtraData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

    }

    private void handleExtras(Bundle extras) {
        mLoadType = (LoadViewType) extras.getSerializable(EXTRA_LOAD_TYPE);
        mPlateId = extras.getString(EXTRA_PLATE_ID);
        mPlateName = extras.getString(EXTRA_PLATE_NAME);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        processExtraData();
        setContentView(R.layout.activity_market);
        setTitleByType(mLoadType);
        initView();

        setRefreshButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_content);
                if (null != fragment && fragment instanceof FragmentMarkerCenter) {
                    ((FragmentMarkerCenter) fragment).refreshData();
                }
            }
        });

    }

//    private void replaceContentFragment(Fragment fragment) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, fragment).commit();
//
//    }

    private void initView() {
        switch (mLoadType) {
            case StockIncease: {
                replaceContentFragment(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.TYPE_STOCK_UP));
            }
            break;
            case IndexUp: {
                replaceContentFragment(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.TYPE_INLAND_INDEX_UP));
            }
            break;
            case StockDown: {
                replaceContentFragment(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.TYPE_STOCK_DOWN));
            }
            break;
            case StockAmplit: {
                replaceContentFragment(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.TYPE_STOCK_AMPLI_UP));
            }
            break;
            case StockTurnOver: {
                replaceContentFragment(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.TYPE_STOCK_TURN_UP));
            }
            break;
            case PlateHot: {
                replaceContentFragment(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.TYPE_PLATE_UP));
            }
            break;
            case PlateList: {
                replaceContentFragment(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.TYPE_PLATEDETAIL_UP));
            }
            break;

            default:
                break;
        }
    }

    public void setTitleByType(LoadViewType type) {
        mLoadType = type;

        switch (mLoadType) {
            case StockIncease: {
                setTitle(R.string.market_title_up);
            }
            break;
            case IndexUp: {
                setTitle(R.string.market_title_index);
            }
            break;
            case StockDown: {
                setTitle(R.string.market_title_down);
            }
            break;
            case StockAmplit: {
                setTitle(R.string.market_title_ampli);
            }
            break;
            case StockTurnOver: {
                setTitle(R.string.market_title_turnover);
            }
            break;
            case PlateHot: {
                setTitle(R.string.market_title_hot);
            }
            break;
            case PlateList: {
                setTitle(mPlateName);
            }
            break;

            default:
                break;
        }
    }

    public interface ILoadingFinishListener {
        void loadingFinish();

        void startLoadingData();
    }

}