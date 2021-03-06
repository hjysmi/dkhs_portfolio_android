package com.dkhs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.dkhs.R;
import com.dkhs.drawable.IndeterminateProgressDrawable;


/**
 * https://github.com/castorflex/SmoothProgressBar
 * <p/>
 * Copyright 2014 Antoine Merle
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class CircularProgress extends View {

    private static final int MAX_LEVEL = 10000;
    private static final int ANIMATION_RESOLUTION = 200;
    private static final int SMALL_SIZE = 0;
    private static final int NORMAL_SIZE = 1;
    private static final int LARGE_SIZE = 2;

    private int mMinWidth;
    private int mMaxWidth;
    private int mMinHeight;
    private int mMaxHeight;

    private int mColor;
    private int mSize;
    private boolean mIndeterminate;
    private int mBorderWidth;
    private RectF arcRectF;
    private int mMax;
    private int mProgress;
    private boolean mHasAnimation;
    private boolean mAttached;
    private long mLastDrawTime;

    private IndeterminateProgressDrawable mIndeterminateProgressDrawable;
    private DeterminateProgressDrawable mDeterminateProgressDrawable;

    public CircularProgress(Context context) {
        this(context, null);
    }

    public CircularProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularProgress);
        mColor = attributes.getColor(R.styleable.CircularProgress_circular_progress_color,
                getResources().getColor(R.color.circular_progress_color));
        mSize = attributes.getInt(R.styleable.CircularProgress_circular_progress_size, NORMAL_SIZE);
        mIndeterminate = attributes.getBoolean(R.styleable.CircularProgress_circular_progress_indeterminate,
                getResources().getBoolean(R.bool.circular_progress_indeterminate));
        mBorderWidth = attributes.getDimensionPixelSize(R.styleable.CircularProgress_circular_progress_border_width,
                getResources().getDimensionPixelSize(R.dimen.circular_progress_border_width));
       int mDuration = attributes.getInteger(R.styleable.CircularProgress_circular_progress_duration, ANIMATION_RESOLUTION);
        mMax = attributes.getInteger(R.styleable.CircularProgress_circular_progress_max,
                getResources().getInteger(R.integer.circular_progress_max));
        attributes.recycle();


        if (mIndeterminate) {


            mIndeterminateProgressDrawable = new IndeterminateProgressDrawable(mColor, mBorderWidth);
            mIndeterminateProgressDrawable.setCallback(this);


        } else {


            mDeterminateProgressDrawable = new DeterminateProgressDrawable(mColor, mBorderWidth, 20);
            mDeterminateProgressDrawable.setCallback(this);


        }
    }

    public void setColor(int color) {
        mColor = color;
        invalidate();
    }

    public void setIndeterminate(boolean indeterminate) {

        mIndeterminate = indeterminate;

        invalidate();
    }

    public void startAnimation() {

        if (getVisibility() != VISIBLE) {

            return;
        }
        mIndeterminateProgressDrawable.start();
    }

    public void stopAnimation() {
        mIndeterminateProgressDrawable.stop();
    }

    public void setProgress(int progress) {
        if (mIndeterminate || progress > mMax || progress < 0) {
            return;
        }
        mProgress = (int)(((float)progress)/mMax*360);
        mDeterminateProgressDrawable.setAngle(mProgress);



        invalidate();
    }

    public synchronized int getProgress() {
        return mIndeterminate ? 0 : mProgress;
    }

    public synchronized int getMax() {
        return mMax;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != mMax) {
            mMax = max;
            postInvalidate();

            if (mProgress > max) {
                mProgress = max;
            }
        }
    }

    private RectF getArcRectF() {
        if (arcRectF == null) {
            int size = Math.min(getWidth() - mBorderWidth * 2, getHeight() - mBorderWidth * 2);
            arcRectF = new RectF();
            arcRectF.left = (getWidth() - size) / 2;
            arcRectF.top = (getHeight() - size) / 2;
            arcRectF.right = getWidth() - (getWidth() - size) / 2;
            arcRectF.bottom = getHeight() - (getHeight() - size) / 2;
        }
        return arcRectF;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            int size = 0;
            switch (mSize) {
                case SMALL_SIZE:
                    size = getResources().getDimensionPixelSize(R.dimen.circular_progress_small_size);
                    break;
                case NORMAL_SIZE:
                    size = getResources().getDimensionPixelSize(R.dimen.circular_progress_normal_size);
                    break;
                case LARGE_SIZE:
                    size = getResources().getDimensionPixelSize(R.dimen.circular_progress_large_size);
                    break;
            }
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (mIndeterminate) {
            mIndeterminateProgressDrawable.draw(canvas);
        } else {
            mDeterminateProgressDrawable.draw(canvas);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mIndeterminate) {
            if (visibility == VISIBLE) {
                mIndeterminateProgressDrawable.start();
            } else {
                mIndeterminateProgressDrawable.stop();
            }
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable drawable) {
        if (mIndeterminate) {
            return drawable == mIndeterminateProgressDrawable || super.verifyDrawable(drawable);
        } else {
            return drawable == mDeterminateProgressDrawable || super.verifyDrawable(drawable);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (mIndeterminate) {
            mIndeterminateProgressDrawable.setBounds(0, 0, width, height);
        } else {
            mDeterminateProgressDrawable.setBounds(0, 0, width, height);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mIndeterminate) {
            startAnimation();
        }
        mAttached = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mIndeterminate) {
            stopAnimation();
        }
        super.onDetachedFromWindow();
        mAttached = false;
    }

    private class DeterminateProgressDrawable extends Drawable {

        private Paint mPaint;
        private float mBorderWidth;
        private float mEndAngle=20;
        private final RectF mDrawableBounds = new RectF();

        public DeterminateProgressDrawable(int color, int borderWidth, int angle) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(borderWidth);
            mPaint.setColor(color);



            mBorderWidth = borderWidth;


            mEndAngle = angle;
        }

        public void setAngle(float angle) {
            mEndAngle = angle;
            invalidate();
        }

        @Override
        public void draw(Canvas canvas) {

            canvas.drawArc(mDrawableBounds, 0.f, mEndAngle, false, mPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            mPaint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSPARENT;
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mDrawableBounds.left = bounds.left + mBorderWidth / 2f + .5f;
            mDrawableBounds.right = bounds.right - mBorderWidth / 2f - .5f;
            mDrawableBounds.top = bounds.top + mBorderWidth / 2f + .5f;
            mDrawableBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
        }
    }


}
