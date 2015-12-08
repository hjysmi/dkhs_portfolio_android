package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.dkhs.portfolio.R;

/**
 * Created by wuyongsen on 2015/12/8.
 */
public class WaterMarkImageView extends android.widget.FrameLayout {
    public enum TypeEnum {
        nothing, red, blue;
    }

    private Context mContext;
    private ImageView mAvatar;
    private ImageView mWaterMark;
    private TypeEnum mType = TypeEnum.nothing;


    public WaterMarkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_water_mark,this,true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        mAvatar = (ImageView)findViewById(R.id.iv_avatar);
        mWaterMark = (ImageView)findViewById(R.id.iv_water_mark);
        setType(mType);
    }

    public ImageView getImageView() {
        return mAvatar;
    }

    public void setType(TypeEnum type) {
        if (mType != type) {
            mType = type;
            updateWaterMark(mType);
        }
    }

    private void updateWaterMark(TypeEnum type) {
        switch (type) {
            case nothing:
                mWaterMark.setVisibility(GONE);
                break;
            case blue:
                mWaterMark.setVisibility(VISIBLE);
                mWaterMark.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.water_mark_blue));
                break;
            case red:
                mWaterMark.setVisibility(VISIBLE);
                mWaterMark.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.water_mark_red));
                break;
        }
    }
}
