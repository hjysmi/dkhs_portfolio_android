/**
 * @Title FloatingActionMenu.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-24 上午10:16:00
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.melnykov.fab.FloatingActionView;

/**
 * @ClassName FloatingActionMenu
 * @Description 下拉隐藏，上拉出现的浮动菜单控件
 * @author zjz
 * @date 2015-4-24 上午10:16:00
 * @version 1.0
 */
public class FloatingActionMenu extends FloatingActionView {

    private LinearLayout containerView;
    private OnMenuItemSelectedListener menuItemSelectedListener;

    public FloatingActionMenu(Context paramContext) {
        this(paramContext, null);
    }

    public FloatingActionMenu(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.menuItemSelectedListener = null;
        init();
    }

    public FloatingActionMenu(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.menuItemSelectedListener = null;
        init();
    }

    private void addBorderInTop() {
        View localView = new View(getContext());
        localView.setLayoutParams(new ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1));
        localView.setBackgroundColor(getResources().getColor(R.color.drivi_line));
        addView(localView);
        this.containerView = new LinearLayout(getContext());
        this.containerView.setLayoutParams(new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        this.containerView.setOrientation(0);
        addView(this.containerView);
    }

    private void addDivider() {
        FrameLayout divView = new FrameLayout(getContext());
        View localView = new View(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        divView.setLayoutParams(params);
        LinearLayout.LayoutParams divParams = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        divView.setPadding(0, getResources().getDimensionPixelOffset(R.dimen.float_menu_div_padding), 0, getResources()
                .getDimensionPixelOffset(R.dimen.float_menu_div_padding));
        localView.setLayoutParams(divParams);
        localView.setBackgroundColor(getResources().getColor(R.color.drivi_line));
        divView.setBackgroundColor(getResources().getColor(R.color.white));
        divView.addView(localView);
        this.containerView.addView(divView);
    }

    private View addItemView(int paramInt1, String tvText, int iconResId, boolean isAddMenu) {

        View flaotMenu = View.inflate(getContext(), R.layout.item_float_menu, null);
        // RelativeLayout localRelativeLayout = new RelativeLayout(getContext());
        flaotMenu.setTag(Integer.valueOf(paramInt1));
        flaotMenu.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelOffset(R.dimen.floating_action_menu_item_height), 1.0F));
        TextView tvConntent = (TextView) flaotMenu.findViewById(R.id.tv_floatmenu);
        if (!TextUtils.isEmpty(tvText)) {
            tvConntent.setText(tvText);
        }
        if (iconResId > 0) {
            tvConntent.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        }

        flaotMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = ((Integer) v.getTag()).intValue();
                if (null != menuItemSelectedListener) {
                    menuItemSelectedListener.onMenuItemSelected(index);
                }
            }
        });
        if (this.containerView.getChildCount() > 0) {
            addDivider();
        }
        if (isAddMenu) {

            this.containerView.addView(flaotMenu);
        }
        return flaotMenu;
    }

    private void init() {
        setOrientation(1);
        addBorderInTop();
    }

    public void addItem(int viewIndex, int textResId, int iconResId) {
        addItem(viewIndex, getResources().getString(textResId), iconResId);
    }

    public void addItem(int viewIndex, String textString, int iconResId) {
        addItemView(viewIndex, textString, iconResId, true);
    }

    public void addItem(int viewIndex, String textString, int iconResId, boolean isAddMenu) {
        addItemView(viewIndex, textString, iconResId, isAddMenu);
    }

    public void removeAllItems() {
        this.containerView.removeAllViews();
    }

    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener paramOnMenuItemSelectedListener) {
        this.menuItemSelectedListener = paramOnMenuItemSelectedListener;
    }

    public static abstract interface OnMenuItemSelectedListener {
        public abstract boolean onMenuItemSelected(int paramInt);
    }

    public MoreMenuItemBuilder addMoreItem(int viewIndex, String textString, int iconResId) {
        return addMoreItem(viewIndex, textString, iconResId, true);
    }

    public MoreMenuItemBuilder addMoreItem(int viewIndex, String textString, int paramInt2, boolean isAddMenu) {
        View localView = addItemView(viewIndex, textString, 0, isAddMenu);
        final ListPopupWindow morePopupWindow = new ListPopupWindow(getContext());
        // morePopupWindow.setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(
        // android.R.color.transparent)));
        ArrayAdapter localArrayAdapter = new ArrayAdapter(getContext(), R.layout.item_btn_more);
        final MoreMenuItemBuilder moreMenuItemBuilder = new MoreMenuItemBuilder(localArrayAdapter);

        morePopupWindow.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Animation fadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
                // fadeInAnimation.setDuration(10);
                // view.startAnimation(fadeInAnimation);

                morePopupWindow.dismiss();
                if (moreMenuItemBuilder.positionItemIdMap.get(position) != null && null != menuItemSelectedListener) {
                    menuItemSelectedListener.onMenuItemSelected(moreMenuItemBuilder.positionItemIdMap.get(position));
                }
            }
        });
        morePopupWindow.setAdapter(localArrayAdapter);
        morePopupWindow.setAnchorView(localView);
        morePopupWindow.setModal(true);
        localView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                morePopupWindow.show();

            }
        });
        return moreMenuItemBuilder;
    }

    public static class MoreMenuItemBuilder {
        private ArrayAdapter adapter;
        private SparseArray<Integer> positionItemIdMap = new SparseArray();

        public MoreMenuItemBuilder(ArrayAdapter paramArrayAdapter) {
            this.adapter = paramArrayAdapter;
        }

        public MoreMenuItemBuilder addItem(int positionIndex, String titleString) {
            this.positionItemIdMap.put(this.adapter.getCount(), Integer.valueOf(positionIndex));
            this.adapter.add(titleString);
            this.adapter.notifyDataSetChanged();
            return this;
        }
    }
}
