package com.dkhs.portfolio.bean.itemhandler;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by xuetong on 2016/1/13.
 */
public class StockNewsHandler extends SimpleItemHandler<OptionNewsBean> {

    Context context;
    DisplayMetrics dm;
    public StockNewsHandler(Context context) {
        this.context = context;
        dm = UIUtils.getDisplayMetrics();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_opition_list;
    }

    @Override
    public void onBindView(ViewHolder vh, OptionNewsBean data, int position) {
        super.onBindView(vh, data, position);
        TextView tvTextName = (TextView) vh.get(R.id.adapter_market_title);
        TextView tvTextNameNum = (TextView) vh.get(R.id.adapter_market_title_num);
        TextView tvTextDate = (TextView) vh.get(R.id.option_news_text_date);
        TextView zhengquan = (TextView) vh.get(R.id.zhengquan);
        Paint p = new Paint();
        Rect rect = new Rect();
        p.setTextSize(context.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
        p.getTextBounds(data.getTitle(), 0, data.getTitle().length(), rect);
        if (dm.widthPixels * 3 / 2 - 50 < rect.width()) {
            int le = (int) (data.getTitle().length() - data.getTitle().length() * (rect.width() - dm.widthPixels * 3 / 2 + 50) / rect.width() - 3);
            String text = data.getTitle().substring(0, le);
            tvTextName.setText(text + "...");
        } else {
            tvTextName.setText(data.getTitle());
        }
        tvTextNameNum.setText(data.getSymbols().get(0).getAbbrName());
        if (null != data.getSource()) {
            zhengquan.setText(data.getSource().getTitle());
        }

        if (TimeUtils.isSameDay(data.getPublish())) {
            tvTextDate.setText(TimeUtils.getTimeString(data.getPublish()));
        } else {
            tvTextDate.setText(TimeUtils.getMMDDString(data.getPublish()));
        }
    }
}
