/**
 * @Title RatioLayout.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-18 下午4:17:55
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import com.dkhs.portfolio.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @ClassName RatioLayout
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-18 下午4:17:55
 * @version 1.0
 */
public class RatioLayout extends RelativeLayout {

    private float mWidthRatio;
    private float mHeightRatio;

    public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatioLayout(Context context) {
        super(context);
        // init(context);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        mWidthRatio = a.getFloat(R.styleable.RatioLayout_width_ratio, 0);
        mHeightRatio = a.getFloat(R.styleable.RatioLayout_height_ratio, 0);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int defWith = getDefaultSize(0, widthMeasureSpec);
        int defHeight = getDefaultSize(0, heightMeasureSpec);

        if (1.0 == mWidthRatio && 1.0 != mHeightRatio) {
            defHeight = (int) (defWith * mHeightRatio);
        } else if (1.0 == mHeightRatio && 1.0 != mWidthRatio) {
            defWith = (int) (defHeight * mWidthRatio);
        } else if (mHeightRatio == mWidthRatio) {
            defHeight = defWith;
        }
        setMeasuredDimension(defWith, defHeight);
    }

    protected int measureDimension(int defaultSize, int measureSpec) {

        int result = defaultSize;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        // 1. layout给出了确定的值，比如：100dp
        // 2. layout使用的是match_parent，但父控件的size已经可以确定了，比如设置的是具体的值或者match_parent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize; // 建议：result直接使用确定值
        }
        // 1. layout使用的是wrap_content
        // 2. layout使用的是match_parent,但父控件使用的是确定的值或者wrap_content
        else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize); // 建议：result不能大于specSize
        }
        // UNSPECIFIED,没有任何限制，所以可以设置任何大小
        // 多半出现在自定义的父控件的情况下，期望由自控件自行决定大小
        else {
            result = defaultSize;
        }

        return result;
    }

}
