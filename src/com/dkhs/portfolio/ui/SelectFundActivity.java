/**
 * @Title AddConbinationStockActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.FundDataEngine;
import com.dkhs.portfolio.engine.FundDataEngine.OrderType;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;
import com.lidroid.xutils.cache.MD5FileNameGenerator;

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

    // public static OrderType mOrderType = OrderType.DAY;

    @Override
    protected void setTabViewPage(ArrayList<String> titleList, List<FragmentSelectStockFund> fragmenList) {
        String[] tArray = getResources().getStringArray(R.array.refer_funds);
        int titleLenght = tArray.length;
        for (int i = 0; i < titleLenght; i++) {
            titleList.add(tArray[i]);
        }
        FragmentSelectStockFund mPagerFragment = FragmentSelectStockFund.getFundFragment(ViewType.FUND_MAININDEX);
        FragmentSelectStockFund mPagerFragment2 = FragmentSelectStockFund.getFundFragment(ViewType.FUND_INDEX);
        FragmentSelectStockFund mPagerFragment3 = FragmentSelectStockFund.getFundFragment(ViewType.FUND_STOCK);
        fragmenList.add(mPagerFragment);
        fragmenList.add(mPagerFragment2);
        fragmenList.add(mPagerFragment3);

    }

    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);

        btnOrder = getOrderButton();

        btnOrder.setOnClickListener(orderClickLisenter);

        orderTitle = getResources().getStringArray(R.array.order_type);
        drawable_up = getResources().getDrawable(R.drawable.ic_select_up);
        drawable_down = getResources().getDrawable(R.drawable.ic_select_down);
        drawable_up.setBounds(0, 0, drawable_up.getMinimumWidth(), drawable_up.getMinimumHeight());
        drawable_down.setBounds(0, 0, drawable_down.getMinimumWidth(), drawable_down.getMinimumHeight());
        btnOrder.setCompoundDrawables(null, null, drawable_down, null);
    }

    OnClickListener orderClickLisenter = new OnClickListener() {

        @Override
        public void onClick(View v) {
        	btnOrder.setCompoundDrawables(null, null, drawable_up, null);
            showPopWindow();

        }
    };
    OnClickListener popMoreClickLisenter = new OnClickListener() {

        @Override
        public void onClick(View v) {
            OrderType mOrderType = OrderType.MONTH;
            int id = v.getId();
            switch (id) {
//                case R.id.tv_day_order: {
//                    btnOrder.setText(orderTitle[0]);
//                    mOrderType = OrderType.MONTH;
//                }
//
//                    break;
//                case R.id.tv_month_order: {
//                    btnOrder.setText(orderTitle[1]);
//                    mOrderType = OrderType.YEAR;
//
//                }
//
//                    break;
//                case R.id.tv_quarter_order: {
//                    btnOrder.setText(orderTitle[2]);
//                    mOrderType = OrderType.TYEAR;
//                }
//                    break;

                default:
                    break;
            }

            mPopMoreWindow.dismiss();
            for (FragmentSelectStockFund fragment : fragmentList) {
                fragment.setOrderType(mOrderType);
            }
            btnOrder.setCompoundDrawables(null, null, drawable_down, null);
        }
    };
	private Drawable drawable_up;
	private Drawable drawable_down;

    @Override
    protected FragmentSearchStockFund getSearchFragment() {
        return FragmentSearchStockFund.getFundFragment();
    }

    private void showPopWindow() {
        View view;
        view = this.getLayoutInflater().inflate(R.layout.layout_popview_order, null);
        ListView lv_profit_loss = (ListView) view.findViewById(R.id.lv_more);
        lv_profit_loss.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	OrderType mOrderType = OrderType.MONTH;
            	switch (position) {
				case 0:
					btnOrder.setText(orderTitle[0]);
	                mOrderType = OrderType.MONTH;

					break;
				case 1:
					btnOrder.setText(orderTitle[1]);
					mOrderType = OrderType.YEAR;
					break;
				case 2:
					btnOrder.setText(orderTitle[2]);
					mOrderType = OrderType.TYEAR;
					break;
				default:
					break;
				}
            	mPopMoreWindow.dismiss();
            	for (FragmentSelectStockFund fragment : fragmentList) {
                    fragment.setOrderType(mOrderType);
                }
            }
        });
        lv_profit_loss.setAdapter(new ArrayAdapter<String>(this, R.layout.item_btn_more, orderTitle));
        mPopMoreWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopMoreWindow.setOutsideTouchable(true); // 不能在没有焦点的时候使用
        mPopMoreWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopMoreWindow.setFocusable(true);
        mPopMoreWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				 btnOrder.setCompoundDrawables(null, null, drawable_down, null);
			}
		});
//        TextView tvDay = (TextView) view.findViewById(R.id.tv_day_order);
//        TextView tvMonth = (TextView) view.findViewById(R.id.tv_month_order);
//        TextView tvQuarter = (TextView) view.findViewById(R.id.tv_quarter_order);
//        tvDay.setOnClickListener(popMoreClickLisenter);
//        tvMonth.setOnClickListener(popMoreClickLisenter);
//        tvQuarter.setOnClickListener(popMoreClickLisenter);

//        mPopMoreWindow.setWidth(btnOrder.getWidth());

//        mPopMoreWindow.showAsDropDown(btnOrder);
        mPopMoreWindow.showAsDropDown(btnOrder,  0, 5);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    protected ListViewType getLoadByType() {

        return ListViewType.FUND;
    }

}
