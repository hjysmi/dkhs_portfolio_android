package com.dkhs.portfolio.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageView;

/**
 * @author zwm
 * @version 2.0
 * @ClassName ScaleRelativeLayout
 * @Description TODO(按照比例显示的RelativeLayout,主要是以 android:layout_width 的宽度为基准.忽视了android:layout_height 的属性)
 * @date 2015/6/18.F
 */
public class ScaleImageView extends ImageView {

        public float mScale;

        public ScaleImageView(Context context) {
            super(context);
        }

        public ScaleImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(attrs);
        }

        private void init(AttributeSet attrs) {
            TypedArray typedArray=getContext().obtainStyledAttributes(attrs, R.styleable.ScaleLayout);
            mScale= typedArray.getFloat(R.styleable.ScaleLayout_scale,-1);
            typedArray.recycle();
        }

        public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(attrs);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init(attrs);
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            if(mScale != -1){
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthMeasureSpec) * mScale), MeasureSpec.getMode(widthMeasureSpec)));
            }else{
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }



    private boolean mInterceptTouch;

    public boolean isInterceptTouch() {
        return mInterceptTouch;
    }

    public void setInterceptTouch(boolean interceptTouch) {
        mInterceptTouch = interceptTouch;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mInterceptTouch)
        getParent().requestDisallowInterceptTouchEvent(true);//这句话的作用 告诉父view，我的单击事件我自行处理，不要阻碍我。
        return super.dispatchTouchEvent(ev);
    }


}
