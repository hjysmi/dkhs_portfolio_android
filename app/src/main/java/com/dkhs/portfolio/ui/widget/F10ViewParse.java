package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.percent.PercentRelativeLayout;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.F10DataBean;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.bean.ManagersEntity;
import com.dkhs.portfolio.utils.TimeUtils;

import java.util.List;

/**
 * Created by zjz on 2015/5/13.
 */
public class F10ViewParse {

    private F10DataBean mDataBean;
    private Context mContext;
    private static final int HEADER_TEXT_COLOR = PortfolioApplication.getInstance().getResources().getColor(R.color.theme_blue);

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


    public View parseFundProfileView(FundQuoteBean fundQuoteBean) {
        this.mContentView.addView(createTitleView("基金概况", ""));
        createFundRow(fundQuoteBean);

        return mContentView;
    }

    private void createFundRow(FundQuoteBean fundQuoteBean) {
        String[] profileTitles = mContext.getResources().getStringArray(R.array.fund_profile_title);
        int splitValue = 0;
        int rowIndex = 0;
        for (String profileTitle : profileTitles) {
            String rowText = getFundRowText(rowIndex, fundQuoteBean);
            PercentRelativeLayout rowContent = createFundRowView(profileTitle, rowText);
            if (rowIndex % 2 == splitValue) {
                rowContent.setBackgroundColor(DEFAULT_BG_GRAY_COLOR);
            } else {
                rowContent.setBackgroundColor(DEFAULT_WHITE_COLOR);
            }

            this.mContentView.addView(rowContent);
            rowIndex++;
        }
    }

    private String getFundRowText(int row, FundQuoteBean fundQuoteBean) {
        String rowText = "";
        switch (row) {
            case 0: {
                rowText = fundQuoteBean.getName();
            }
            break;
            case 1: {
                StringBuilder sbFundManager = new StringBuilder();
                for (ManagersEntity managersEntity : fundQuoteBean.getManagers()) {
                    sbFundManager.append(managersEntity.getName());
                    sbFundManager.append("  ");
                }
                rowText = sbFundManager.toString();
            }
            break;
            case 2: {
                rowText = fundQuoteBean.getMana_name();
            }
            break;
            case 3: {
                rowText = fundQuoteBean.getStypeText();
            }
            break;
            case 4: {
                rowText = TimeUtils.getSimpleDay(fundQuoteBean.getEstab_date());
            }
            break;
            case 5: {
                rowText = fundQuoteBean.getEnd_shares();
            }
            break;
        }
        return rowText;
    }

    private PercentRelativeLayout createFundRowView(String title, String content) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_funddetail, null);
        PercentRelativeLayout rl_root = (PercentRelativeLayout) view.findViewById(R.id.rl_root);
        TextView tvTitleContent = (TextView) view.findViewById(R.id.tvTitleContent);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        tvContent.setText(content);
        tvTitleContent.setText(title);
        return rl_root;
    }


    public View parseFundProfileViewPurchase(FundQuoteBean mFundQuoteBean) {
        this.mContentView.addView(createTitleView("购买须知", ""));
        createFundRowPurchase(mFundQuoteBean);

        return mContentView;
    }

    private void createFundRowPurchase(FundQuoteBean fundQuoteBean) {
        String[] profileTitles = mContext.getResources().getStringArray(R.array.fund_profile_title_purchase);
        int splitValue = 0;
        int rowIndex = 0;
        for (String profileTitle : profileTitles) {
            String rowText = getFundRowTextPurchase(rowIndex, fundQuoteBean);
            PercentRelativeLayout rowContent = createFundRowView(profileTitle, rowText);
            TextView tvPrice = (TextView) rowContent.findViewById(R.id.tvprice);
            if (rowIndex == 1) {
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
                if (null != fundQuoteBean) {
                    tvPrice.setText(String.valueOf(fundQuoteBean.getFare_ratio_buy()));
                }

            } else {
                tvPrice.setVisibility(View.GONE);
            }

            if (rowIndex % 2 == splitValue) {
                rowContent.setBackgroundColor(DEFAULT_BG_GRAY_COLOR);
            } else {
                rowContent.setBackgroundColor(DEFAULT_WHITE_COLOR);
            }

            this.mContentView.addView(rowContent);
            rowIndex++;
        }
    }

    private String getFundRowTextPurchase(int row, FundQuoteBean fundQuoteBean) {
        String rowText = "";
        switch (row) {
            case 0: {
                //收费方式
                rowText = "";
            }
            break;
            case 1:
                //申购费率
                if (null != fundQuoteBean) {
                    rowText = String.valueOf(fundQuoteBean.getDiscount_rate_buy());
                }
                //  rowText = "0.5%";
                break;
            case 2:
                //起购金额
                rowText = fundQuoteBean.getAmount_min_buy();
                break;
            case 3:
                //赎回到账时间
                rowText = "";
                break;
            case 4: {
                //shares_min最低赎回份额
                if (null != fundQuoteBean) {
                    rowText = String.valueOf(fundQuoteBean.getShares_min_sell());
                }

            }
            break;
            case 5: {
                rowText = getRiskValue(fundQuoteBean.getInvestment_risk());
            }
            break;
        }
        return rowText;
    }

    private String getRiskValue(int index) {

        switch (index) {
            case TYPE_RISK_UNKNOWN:
                return "未知";
            case TYPE_RISK_LOW:
                return "低";
            case TYPE_RISK_LOW_MIDDLE:
                return "中低";
            case TYPE_RISK_MIDDLE:
                return "中";
            case TYPE_RISK_MIDDLE_HIGH:
                return "中高";
            case TYPE_RISK_HIGH:
                return "高";
            default:

        }
        return "未知";
    }

    public static final int TYPE_RISK_UNKNOWN = 0;//未知
    public static final int TYPE_RISK_LOW = 1;//风险低
    public static final int TYPE_RISK_LOW_MIDDLE = 2;//风险中低
    public static final int TYPE_RISK_MIDDLE = 3;//风险中
    public static final int TYPE_RISK_MIDDLE_HIGH = 4;//风险中高
    public static final int TYPE_RISK_HIGH = 5;//风险高

}
