/**
 * @Title PositionItemView.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-1-8 下午2:29:10
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PositionAdjustBean;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName PositionItemView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-1-8 下午2:29:10
 * @version 1.0
 */
public class PositionItemView {

    private View contentView;

    public PositionItemView(Context context, PositionAdjustBean position) {
        initView(context, position);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param context
     * @return void
     */
    private void initView(Context context, PositionAdjustBean position) {
        contentView = View.inflate(context, R.layout.item_stock_position, null);
        TextView tvStockName = (TextView) contentView.findViewById(R.id.tv_stockname);
        TextView tvPostion = (TextView) contentView.findViewById(R.id.tv_position_fromto);
        tvStockName.setText(position.getStockName());
        tvPostion.setText(context.getString(R.string.position_from_to,
                StringFromatUtils.get2PointPercent(position.getFromPercent()),
                StringFromatUtils.get2PointPercent(position.getToPercent())));

    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

}
