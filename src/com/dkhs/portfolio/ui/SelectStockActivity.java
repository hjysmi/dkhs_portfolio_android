/**
 * @Title AddConbinationStockActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.ui.adapter.SelectFundAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;
import com.dkhs.portfolio.utils.ColorTemplate;

/**
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version 1.0
 */
public class SelectStockActivity extends BaseSelectActivity implements OnClickListener {

    @Override
    protected boolean isLoadBySelectFund() {
        return false;
    }

    @Override
    protected void setTabViewPage(ArrayList<String> titleList, List<FragmentSelectStockFund> fragmenList) {
        String[] tArray = getResources().getStringArray(R.array.select_stock);
        int titleLenght = tArray.length;
        for (int i = 0; i < titleLenght; i++) {
            titleList.add(tArray[i]);

        }
        FragmentSelectStockFund mOptionalFragment = FragmentSelectStockFund.getStockFragment(ViewType.STOCK_OPTIONAL);
        FragmentSelectStockFund mIncreaseFragment = FragmentSelectStockFund.getStockFragment(ViewType.STOCK_INCREASE);
        FragmentSelectStockFund mDownFragment = FragmentSelectStockFund.getStockFragment(ViewType.STOCK_DRAWDOWN);
        FragmentSelectStockFund mHandoverFragment = FragmentSelectStockFund.getStockFragment(ViewType.STOCK_HANDOVER);

        fragmenList.add(mOptionalFragment);
        fragmenList.add(mIncreaseFragment);
        fragmenList.add(mDownFragment);
        fragmenList.add(mHandoverFragment);

    }

    @Override
    protected FragmentSelectStockFund setSearchFragment() {
        return FragmentSelectStockFund.getStockFragment(ViewType.SEARCH);
    }

}
