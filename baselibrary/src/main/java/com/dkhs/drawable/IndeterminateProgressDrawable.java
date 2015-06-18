package com.dkhs.drawable;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * @author zwm
 * @version 2.0
 * @ClassName IndeterminateProgressDrawable
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/18.
 */

    public class IndeterminateProgressDrawable extends Drawable implements Animatable {

        private final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
        private final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
        private static final int ANGLE_ANIMATOR_DURATION = 2000;
        private static final int SWEEP_ANIMATOR_DURATION = 600;
        private static final int MIN_SWEEP_ANGLE = 30;
        private final RectF mDrawableBounds = new RectF();

        private ObjectAnimator mObjectAnimatorSweep;
        private ObjectAnimator mObjectAnimatorAngle;
        private boolean mModeAppearing;
        private Paint mPaint;
        private float mCurrentGlobalAngleOffset;
        private float mCurrentGlobalAngle;
        private float mCurrentSweepAngle;
        private float mBorderWidth;
        private boolean mRunning;

        public IndeterminateProgressDrawable(int color, float borderWidth) {
            mBorderWidth = borderWidth;

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(borderWidth);
            mPaint.setColor(color);

            setupAnimations();
        }

        @Override
        public void draw(Canvas canvas) {
            float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
            float sweepAngle = mCurrentSweepAngle;
            if (!mModeAppearing) {
                startAngle = startAngle + sweepAngle;
                sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
            } else {
                sweepAngle += MIN_SWEEP_ANGLE;
            }
            canvas.drawArc(mDrawableBounds, startAngle, sweepAngle, false, mPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSPARENT;
        }

        private void toggleAppearingMode() {
            mModeAppearing = !mModeAppearing;
            if (mModeAppearing) {
                mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
            }
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mDrawableBounds.left = bounds.left + mBorderWidth / 2f + .5f;
            mDrawableBounds.right = bounds.right - mBorderWidth / 2f - .5f;
            mDrawableBounds.top = bounds.top + mBorderWidth / 2f + .5f;
            mDrawableBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
        }

        ///////////////////////////////////////// Animation /////////////////////////////////////////

        private Property<IndeterminateProgressDrawable, Float> mAngleProperty
                = new Property<IndeterminateProgressDrawable, Float>(Float.class, "angle") {
            @Override
            public Float get(IndeterminateProgressDrawable object) {
                return object.getCurrentGlobalAngle();
            }

            @Override
            public void set(IndeterminateProgressDrawable object, Float value) {
                object.setCurrentGlobalAngle(value);
            }
        };

        private Property<IndeterminateProgressDrawable, Float> mSweepProperty
                = new Property<IndeterminateProgressDrawable, Float>(Float.class, "arc") {
            @Override
            public Float get(IndeterminateProgressDrawable object) {
                return object.getCurrentSweepAngle();
            }

            @Override
            public void set(IndeterminateProgressDrawable object, Float value) {
                object.setCurrentSweepAngle(value);
            }
        };

        private void setupAnimations() {
            mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f);
            mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
            mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
            mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);
            mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);

            mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty, 360f - MIN_SWEEP_ANGLE * 2);
            mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
            mObjectAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
            mObjectAnimatorSweep.setRepeatMode(ValueAnimator.RESTART);
            mObjectAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
            mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    toggleAppearingMode();
                }
            });
        }

        @Override
        public void start() {
            if (isRunning()) {
                return;
            }
            mRunning = true;
            mObjectAnimatorAngle.start();
            mObjectAnimatorSweep.start();
            invalidateSelf();
        }

        @Override
        public void stop() {
            if (!isRunning()) {
                return;
            }
            mRunning = false;
            mObjectAnimatorAngle.cancel();
            mObjectAnimatorSweep.cancel();
            invalidateSelf();
        }

        @Override
        public boolean isRunning() {
            return mRunning;
        }

        public void setCurrentGlobalAngle(float currentGlobalAngle) {
            mCurrentGlobalAngle = currentGlobalAngle;
            invalidateSelf();
        }

        public float getCurrentGlobalAngle() {
            return mCurrentGlobalAngle;
        }

        public void setCurrentSweepAngle(float currentSweepAngle) {
            mCurrentSweepAngle = currentSweepAngle;
            invalidateSelf();
        }

        public float getCurrentSweepAngle() {
            return mCurrentSweepAngle;
        }

    }
