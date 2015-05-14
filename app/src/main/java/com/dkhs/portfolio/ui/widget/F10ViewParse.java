package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.bean.F10DataBean;

import java.util.List;

/**
 * Created by zjz on 2015/5/13.
 */
public class F10ViewParse {

    private F10DataBean mDataBean;
    private Context mContext;
    private static final int HEADER_TEXT_COLOR = 0XFF00ACEC;

    private static final int SUB_HEADER_TEXT_COLOR = 0XFF969696;
    private static final int SUBHEADE_FONT_SIZE = 13;
    private static final int DEFAULT_FONT_SIZE = 15;
    private static final int DEFAULT_PADDING = 18;
    private static final int DEFAULT_FONT_COLOR = 0XFF262932;

    private static final int DEFAULT_WHITE_COLOR = Color.WHITE;
    private static final int DEFAULT_BG_GRAY_COLOR = 0XFFF5F5F5;
    private static final int BLUE_BG_COLOR = 0X2000ACEC;

//    private static final int Single


    private LinearLayout mContentView;
    private int mViewHeight;


    public F10ViewParse(Context context) {
        this.mContext = context;
        mContentView = new LinearLayout(context);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        mContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        mContentView.setBackgroundColor(Color.WHITE);
    }

    public F10ViewParse(Context context, F10DataBean dataBean) {
        this.mContext = context;
        mContentView = new LinearLayout(context);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        mContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        mContentView.setBackgroundColor(Color.WHITE);
        setF10Data(dataBean);
    }

    public void setF10Data(F10DataBean bean) {
        this.mDataBean = bean;
        parseF10View();
    }

    private void parseF10View() {
        this.mContentView.addView(createTitleView(this.mDataBean.getTitle(), this.mDataBean.getSub_title()));
        createTableRow();
    }


    public LinearLayout getContentView() {
        return mContentView;
    }

    /**
     * 创建标题控件
     */
    private View createTitleView(String title, String subTitle) {
        RelativeLayout rlLayout = new RelativeLayout(mContext);
        rlLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        rlLayout.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);

        TextView tvHeader = new TextView(mContext);
        tvHeader.setTextSize(DEFAULT_FONT_SIZE);
        tvHeader.setTextColor(HEADER_TEXT_COLOR);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        tvHeader.setLayoutParams(lp);
        tvHeader.setText(title);

        TextView tvSubHeader = new TextView(mContext);
        tvSubHeader.setTextSize(SUBHEADE_FONT_SIZE);
        tvSubHeader.setTextColor(SUB_HEADER_TEXT_COLOR);
        RelativeLayout.LayoutParams tvSubHeaderlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvSubHeaderlp.addRule(RelativeLayout.CENTER_VERTICAL);
        tvSubHeaderlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        tvSubHeader.setLayoutParams(tvSubHeaderlp);
        tvSubHeader.setText(subTitle);


        rlLayout.addView(tvHeader);
        rlLayout.addView(tvSubHeader);

        return rlLayout;
    }


    private LinearLayout createTableRowView() {
        LinearLayout llLayout = new LinearLayout(mContext);
        llLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        llLayout.setOrientation(LinearLayout.HORIZONTAL);
        llLayout.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        llLayout.setWeightSum(10);

        TextView tvRowContent = null;
        for (F10DataBean.HeaderEntity header : this.mDataBean.getHeader()) {
            tvRowContent = new TextView(mContext);
            tvRowContent.setTextSize(DEFAULT_FONT_SIZE);
            tvRowContent.setTextColor(DEFAULT_FONT_COLOR);
            tvRowContent.setLineSpacing(0.0f, 1.2f);
            LinearLayout.LayoutParams tvContentlp = new LinearLayout.LayoutParams(
                    1,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tvContentlp.weight = 10.0f * header.getWidth();
            if (header.getAlign().equals(F10DataBean.ALIGN.CENTER.getType())) {
                tvRowContent.setGravity(Gravity.CENTER);
            } else if (header.getAlign().equals(F10DataBean.ALIGN.RIGHT.getType())) {
                tvRowContent.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }
            if (header.getAlign().equals(F10DataBean.ALIGN.LEFT.getType())) {
                tvRowContent.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            }

            tvRowContent.setLayoutParams(tvContentlp);
            llLayout.addView(tvRowContent);
        }

        return llLayout;
    }

    private boolean createTableHeader() {
        LinearLayout rowContent = createTableRowView();
        boolean hasTextInflate = false;
        TextView tvContent = null;
        for (int i = 0, len = rowContent.getChildCount(); i < len; i++) {
            tvContent = (TextView) rowContent.getChildAt(i);
            if (!TextUtils.isEmpty(this.mDataBean.getHeader().get(i).getTitle())) {
                hasTextInflate = true;
                tvContent.setText(this.mDataBean.getHeader().get(i).getTitle());
            }
        }
        rowContent.setBackgroundColor(BLUE_BG_COLOR);
        if (hasTextInflate) {
            this.mContentView.addView(rowContent);
            return true;

        } else {
            return false;

        }
    }

    private void createTableRow() {
        boolean hasInflateHeader = createTableHeader();
        int splitValue = hasInflateHeader ? 1 : 0;
        TextView tvContent;
        for (int rowIndex = 0, rowSize = this.mDataBean.getValues().size(); rowIndex < rowSize; rowIndex++) {
            LinearLayout rowContent = createTableRowView();
            List<String> rowValues = this.mDataBean.getValues().get(rowIndex);
            for (int i = 0, len = rowContent.getChildCount(); i < len; i++) {
                tvContent = (TextView) rowContent.getChildAt(i);
                tvContent.setText(rowValues.get(i));
            }

            if (rowIndex % 2 == splitValue) {
                rowContent.setBackgroundColor(DEFAULT_BG_GRAY_COLOR);
            } else {
                rowContent.setBackgroundColor(DEFAULT_WHITE_COLOR);
            }

            this.mContentView.addView(rowContent);
        }


    }


}