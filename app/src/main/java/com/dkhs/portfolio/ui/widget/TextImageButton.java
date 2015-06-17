/**
 * @Title TextImageButton.java
 * @Package com.example.aminationtest
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-23 下午3:30:47
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TextImageButton
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-4-23 下午3:30:47
 */
public class TextImageButton extends FrameLayout {

    private ImageView imageView;
    private TextView textView;

    /* Constructors */
    public TextImageButton(Context context) {
        this(context, null);
    }

    public TextImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextImageButton(Context context, AttributeSet attrs, int defaultStyle) {
        // Initialize the parent layout with the system's button style
        // This sets the clickable attributes and button background
        // to match the current theme.
        // super(context, attrs, android.R.attr.textAppearance);
        super(context, attrs, android.R.attr.buttonStyle);
        // Create the child views
        imageView = new ImageView(context, attrs, defaultStyle);
        textView = new TextView(context, attrs, defaultStyle);
        // textView.setTextColor(context.getResources().getColor(R.color.white_textselector);
        // Create LayoutParams for children to wrap content and center
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        // Add the views
        this.addView(imageView, params);
        this.addView(textView, params);

        // If an image is present, switch to image mode
        if (imageView.getDrawable() != null) {
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }
    }

    /* Accessors */
    public void setText(CharSequence text) {
        // Switch to text
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        // Apply text
        textView.setText(text);
    }

    public void setText(int resId) {
        // Switch to text
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        // Apply text
        textView.setText(resId);
    }

    public void setImageResource(int resId) {
        // Switch to image
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        // Apply image
        imageView.setImageResource(resId);
    }

    public void setImageDrawable(Drawable drawable) {
        // Switch to image
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        // Apply image
        imageView.setImageDrawable(drawable);
    }

}
