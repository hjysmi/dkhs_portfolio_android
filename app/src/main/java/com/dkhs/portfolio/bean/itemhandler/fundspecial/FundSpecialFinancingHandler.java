package com.dkhs.portfolio.bean.itemhandler.fundspecial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.RecommendFundSpecialFinancingBean;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.Random;

/**
 * Created by zcm on 2015/12/08.
 */
public class FundSpecialFinancingHandler extends SimpleItemHandler<RecommendFundSpecialFinancingBean> {
    private Context mContext;

    public FundSpecialFinancingHandler(Context context) {
        mContext = context;
    }

    private static String[] colorRandom = new String[]{"#70aba0", "#e4b524", "#86b2f6", "#f77d7b", "#f4ad56", "#f9760b"};

    @Override
    public int getLayoutResId() {
        return R.layout.market_fund_special_financing;
    }

    @Override
    public void onBindView(final ViewHolder vh, final RecommendFundSpecialFinancingBean item, int position) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        if(item.isFirstTimeLoad){
            vh.get(R.id.iv_special_financing).postDelayed(new Runnable() {
                @Override
                public void run() {
                    int w = vh.get(R.id.iv_special_financing).getWidth();
                    int h = vh.get(R.id.iv_special_financing).getHeight();
                    ColorDrawable drawable = new ColorDrawable(Color.parseColor(colorRandom[new Random().nextInt(colorRandom.length)]));
                    Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                    Bitmap bitmap = Bitmap.createBitmap(w, h, config);
                    //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
                    Canvas canvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, w, h);
                    drawable.draw(canvas);
                    new RoundedBitmapDisplayer(300).display(bitmap, (ImageAware) (new ImageViewAware(vh.getImageView(R.id.iv_special_financing))), LoadedFrom.MEMORY_CACHE);

                }
            },200);
            item.isFirstTimeLoad = false;
        }
        String[] tags = item.getRecommend_desc().split(",");
        if(tags != null && tags.length == 3){
            vh.getTextView(R.id.tv_special_financing).setText(tags[0]);
            vh.getTextView(R.id.tv_special_financing_percent).setText(tags[1].trim());
            vh.getTextView(R.id.tv_special_financing_desc).setText(tags[2]);
        }
        vh.getTextView(R.id.tv_special_financing_title).setText(item.getRecommend_title());
        vh.get(R.id.divider).setBackgroundColor(mContext.getResources().getColor(R.color.drivi_line));

        vh.get(R.id.rl_financing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicsDetailActivity.startActivity(mContext, item.getId());
            }
        });
    }
}
