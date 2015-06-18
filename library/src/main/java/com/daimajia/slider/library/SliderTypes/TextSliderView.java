package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.slider.library.R;
import com.dkhs.drawable.IndeterminateProgressDrawable;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView{
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(R.id.description);

        ProgressBar progressBar= (ProgressBar) v.findViewById(R.id.loading_bar);
        progressBar .setVisibility(View.VISIBLE);
        progressBar.setProgressDrawable(new IndeterminateProgressDrawable(getContext().getResources().getColor(R.color.tag_red),4));
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
