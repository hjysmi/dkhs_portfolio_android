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
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
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
public class SelectFundActivity extends BaseSelectActivity implements OnClickListener {

    private Button btnOrder;
    private PopupWindow mPopMoreWindow;
    private String[] orderTitle;

    @Override
    protected boolean isLoadBySelectFund() {
        return true;
    }

    @Override
    protected void setTabViewPage(ArrayList<String> titleList, List<FragmentSelectStockFund> fragmenList) {
        String[] tArray = getResources().getStringArray(R.array.refer_funds);
        int titleLenght = tArray.length;
        for (int i = 0; i < titleLenght; i++) {
            titleList.add(tArray[i]);
            FragmentSelectStockFund mPagerFragment = FragmentSelectStockFund.getFundFragment(ViewType.FUND_MAININDEX);
            fragmenList.add(mPagerFragment);
        }

    }

    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);

        btnOrder = getOrderButton();

        btnOrder.setOnClickListener(orderClickLisenter);

        orderTitle = getResources().getStringArray(R.array.order_type);

    }

    OnClickListener orderClickLisenter = new OnClickListener() {

        @Override
        public void onClick(View v) {
            showPopWindow();

        }
    };
    OnClickListener popMoreClickLisenter = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_day_order: {
                    btnOrder.setText(orderTitle[0]);
                }

                    break;
                case R.id.tv_month_order: {
                    btnOrder.setText(orderTitle[1]);

                }

                    break;
                case R.id.tv_quarter_order: {
                    btnOrder.setText(orderTitle[2]);

                }

                    break;

                default:
                    break;
            }
            mPopMoreWindow.dismiss();
        }
    };

    @Override
    protected FragmentSelectStockFund setSearchFragment() {
        return FragmentSelectStockFund.getFundFragment(ViewType.SEARCH);
    }

    private void showPopWindow() {
        View view;
        view = this.getLayoutInflater().inflate(R.layout.layout_popview_order, null);
        mPopMoreWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopMoreWindow.setOutsideTouchable(true); // 不能在没有焦点的时候使用
        mPopMoreWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopMoreWindow.setFocusable(true);

        TextView tvDay = (TextView) view.findViewById(R.id.tv_day_order);
        TextView tvMonth = (TextView) view.findViewById(R.id.tv_month_order);
        TextView tvQuarter = (TextView) view.findViewById(R.id.tv_quarter_order);
        tvDay.setOnClickListener(popMoreClickLisenter);
        tvMonth.setOnClickListener(popMoreClickLisenter);
        tvQuarter.setOnClickListener(popMoreClickLisenter);

        System.out.println("btnOrder width:" + btnOrder.getWidth());
        mPopMoreWindow.setWidth(btnOrder.getWidth());

        mPopMoreWindow.showAsDropDown(btnOrder);

    }

}
