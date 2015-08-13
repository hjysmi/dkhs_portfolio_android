package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.dkhs.portfolio.R;

import java.util.ArrayList;

public class PieGraph extends View {

    private ArrayList<PieSlice> slices = new ArrayList<PieSlice>();
    private Paint paint = new Paint();
    private Path path = new Path();

    private int indexSelected = -1;
    private int circleWidth;
    private OnSliceClickedListener listener;
    private int maxValue = 100;
    private int defalutColor = Color.parseColor("#AA66CC");

    private float sweepAngle = 0;

    public PieGraph(Context context) {
        super(context);
        init(context);
    }

    public PieGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        circleWidth = (int) context.getResources().getDimensionPixelSize(R.dimen.arc_width);

    }

    float midX, midY, radius, innerRadius;

    public void onDraw(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT);

        // int sum = 0;
        // for (PieSlice pieS : slices) {
        // System.out.println("" + pieS.getValue());
        // sum += pieS.getValue();
        // }
        // System.out.println("sum:" + sum);

        paint.reset();
        paint.setAntiAlias(true);

        path.reset();

        float circlePadding = 10;

        midX = (getWidth() - circlePadding) / 2;
        midY = (getHeight() - circlePadding) / 2;
        drawCircle(canvas);

        drawCenterText(canvas);

    }

    private void drawCircle(Canvas canvas) {
        float currentAngle = 270;
        float currentSweep;
        int totalValue = 0;
        float arcPadding = 0;
        float paddingBorder = 20;
        float paddingLeft = 20;
        if (midX < midY) {
            radius = midX;
        } else {
            radius = midY;
        }
        radius -= paddingBorder;
        paddingLeft = this.getWidth() / 2 - radius;
        innerRadius = radius - circleWidth;

        for (PieSlice slice : slices) {
            totalValue += slice.getValue();
        }
        // if (totalValue < maxValue) {
        // int surpusValue = maxValue - totalValue;
        // addEmptyPieSlice(surpusValue > 0 ? surpusValue : 0);
        // }
        if (getSweepAngle() <= 359) {
            handler.postDelayed(updateRunnable, 1);
        } else {
            setSweepAngle(359.99f);
            handler.removeCallbacks(updateRunnable);
        }
        int count = 0;
        Path p = new Path();
        for (PieSlice slice : slices) {

            p.reset();
            paint.setColor(slice.getColor());
            RectF rectF;
            currentSweep = (slice.getValue() / maxValue) * (360);
            float endAngle = currentAngle + currentSweep;
            float maxAngle = getSweepAngle() + 270;
            float maxSweepAngle = maxAngle > endAngle ? currentSweep : getSweepAngle() + 270 - currentAngle;
            // currentSweep = currentSweep == 360 ? getSweepAngle() : currentSweep;
            // currentSweep = currentSweep == 360 ? 359.99f : currentSweep;
            rectF = new RectF(paddingLeft, midY - radius, paddingLeft + 2 * radius, midY + radius);
            p.arcTo(rectF, currentAngle + arcPadding, maxSweepAngle - arcPadding);
            RectF innerRectF = new RectF(paddingLeft + circleWidth, midY - innerRadius, paddingLeft + circleWidth
                    + 2.0f * innerRadius, midY + innerRadius);
            float arcAngle = (currentAngle + arcPadding) + (maxSweepAngle - arcPadding);
            // arcAngle = arcAngle>getSweepAngle()+270?getSweepAngle()
            p.arcTo(innerRectF, arcAngle, -(maxSweepAngle - arcPadding));
            p.close();

            slice.setPath(p);
            Region region = new Region((int) (0), (int) (midY - radius), (int) (radius), (int) (midY + radius));
            slice.setRegion(region);
            canvas.drawPath(p, paint);
            Rect tempRect = region.getBounds();
            int left = tempRect.left + (tempRect.right - tempRect.left) / 2;
            int right = left + 30;
            int bottom = tempRect.bottom;
            int top = bottom + 50;
            float startPY = midY - circleWidth;
            RectF rectPain = new RectF(2 * (paddingLeft + radius) + paddingLeft, startPY + (circleWidth * count + 20),
                    2 * (paddingLeft + radius) + paddingLeft + circleWidth, startPY + circleWidth * (count + 1));

            paint.setTextSize(25);

            if (indexSelected == count && listener != null) {
                path.reset();
                paint.setColor(slice.getColor());
                paint.setColor(Color.parseColor("#33B5E5"));
                paint.setAlpha(100);

                if (slices.size() > 1) {
                    path.close();
                } else {
                    path.addCircle(midX, midY, radius + arcPadding, Direction.CW);
                }

                canvas.drawPath(path, paint);
                paint.setAlpha(255);
            }

            currentAngle = currentAngle + currentSweep;
            if (currentAngle > getSweepAngle() + 270) {
                break;
            }

            count++;
        }
    }

   protected Handler handler = new Handler();
    Runnable updateRunnable = new Runnable() {

        @Override
        public void run() {
            sweepAngle += 12;
            postInvalidate();
        }
    };

    private void drawCenterText(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.circle_center_text));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Align.CENTER);

        int xPos = (this.getWidth() / 2);
        int yPos = (int) ((this.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        // ((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
        canvas.drawText("净值占比", xPos, yPos, textPaint);

    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    //
    // Point point = new Point();
    // point.x = (int) event.getX();
    // point.y = (int) event.getY();
    //
    // int count = 0;
    // for (PieSlice slice : slices) {
    // Region r = new Region();
    // r.setPath(slice.getPath(), slice.getRegion());
    // if (r.contains(point.x, point.y) && event.getAction() == MotionEvent.ACTION_DOWN) {
    // indexSelected = count;
    // } else if (event.getAction() == MotionEvent.ACTION_UP) {
    // if (r.contains(point.x, point.y) && listener != null) {
    // if (indexSelected > -1) {
    // listener.onClick(indexSelected);
    // }
    // indexSelected = -1;
    // }
    //
    // }
    // count++;
    // }
    //
    // if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
    // postInvalidate();
    // }
    //
    // return true;
    // }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

//    private void addEmptyPieSlice(float value) {
//        PieSlice slice = new PieSlice();
//        slice.setColor(Color.RED);
//        slice.setValue(value);
//        this.slices.add(slice);
//    }

//    public ArrayList<PieSlice> getSlices() {
//        return slices;
//    }

    public void setSlices(ArrayList<PieSlice> slices) {
        this.slices.clear();
        this.slices.addAll(slices);
        setSweepAngle(0);
        postInvalidate();

    }
//
//    public void updateSlices(int position, int value) {
//        // this.slices.remove(slices.size() - 1);
//        this.slices.get(position).setValue(value);
//        postInvalidate();
//    }

//    public PieSlice getSlice(int index) {
//        return slices.get(index);
//    }
//
//    public void addSlice(PieSlice slice) {
//        this.slices.add(slice);
//        postInvalidate();
//    }

    public void setOnSliceClickedListener(OnSliceClickedListener listener) {
        this.listener = listener;
    }

    public int getThickness() {
        return circleWidth;
    }

    public void setThickness(int thickness) {
        this.circleWidth = thickness;
        postInvalidate();
    }

//    public void removeSlices() {
//        for (int i = slices.size() - 1; i >= 0; i--) {
//            slices.remove(i);
//        }
//        postInvalidate();
//    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(updateRunnable);
    }

    public static interface OnSliceClickedListener {
        public abstract void onClick(int index);
    }

}
