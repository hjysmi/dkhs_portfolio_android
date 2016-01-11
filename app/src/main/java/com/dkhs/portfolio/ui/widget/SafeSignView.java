package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.dkhs.portfolio.R;

/**
 * Created by xuetong on 2016/1/11.
 */
public class SafeSignView extends View {
    View view;
    private Context context;

    public SafeSignView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    public SafeSignView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_safesign, null);


    }

}
