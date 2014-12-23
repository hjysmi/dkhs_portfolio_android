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

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentMarkerCenter;

/**
 * @ClassName MarketListActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-22 下午5:47:35
 * @version 1.0
 */
public class MarketListActivity extends ModelAcitivity {

    public enum LoadViewType {
        StockIncease, StockDown, IndexUp, IndexDown, StockTurnOver, StockAmplit, PlateHot;
    }

    private LoadViewType mLoadType;

    public static Intent newIntent(Context context, LoadViewType loadType) {
        Intent intent = new Intent(context, MarketListActivity.class);

        intent.putExtra("load_type", loadType);

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
        mLoadType = (LoadViewType) extras.getSerializable("load_type");
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        processExtraData();
        setContentView(R.layout.activity_market);
        setTitleByType(mLoadType);
        initView();
    }

    private void replaceContentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_content, fragment).commit();

    }

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

            default:
                break;
        }
    }

}
