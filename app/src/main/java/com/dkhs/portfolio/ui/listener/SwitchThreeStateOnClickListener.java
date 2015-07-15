package com.dkhs.portfolio.ui.listener;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.Action1;

import static com.dkhs.portfolio.ui.listener.SwitchThreeStateOnClickListener.Status.*;

/**
 * @author zwm
 * @version 2.0
 * @ClassName SwitchThreeStateOnCllickListener
 * @Description TODO(三种状态切换)
 * @date 2015/7/15.
 */
public class SwitchThreeStateOnClickListener implements View.OnClickListener {


    private TextView textView;
    private Action1<Status> action1;
    Status state = NORMAL;

    public SwitchThreeStateOnClickListener(TextView textView, Action1<Status> action1) {
        this.textView = textView;
        this.action1 = action1;
    }

    @Override
    public void onClick(View v) {
        switch (state) {
            case NORMAL:
                setDrawableDown(textView);
                state = DOWN;
                break;
            case DOWN:
                state = UP;
                setDrawableUp(textView);
                break;
            case UP:
                state = NORMAL;
                setDrawableNoraml(textView);
                break;
        }
        action1.call(state);
    }


    public void updateState(Status status, boolean action) {

        switch (state) {
            case NORMAL:
                setDrawableNoraml(textView);
                break;
            case DOWN:
                setDrawableDown(textView);
                break;
            case UP:
                setDrawableUp(textView);
                break;
        }
        if (action) {
            action1.call(state);
        }
    }

    private void setDrawableNoraml(TextView view) {
        view.setCompoundDrawables(null, null, null, null);
        view.setCompoundDrawablePadding(textView.getContext().getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }


    private void setDrawableUp(TextView view) {
        Drawable drawable = textView.getContext().getResources().getDrawable(R.drawable.market_icon_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(textView.getContext().getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private void setDrawableDown(TextView view) {

        Drawable drawable = textView.getContext().getResources().getDrawable(R.drawable.market_icon_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(textView.getContext().getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }


    public enum Status {
        NORMAL(0),
        UP(1), DOWN(2);
        int value;

        Status(int value) {
            this.value = value;
        }

    }


}
