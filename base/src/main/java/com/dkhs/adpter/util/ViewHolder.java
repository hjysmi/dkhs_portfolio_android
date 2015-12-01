package com.dkhs.adpter.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.dkhs.R;

public class ViewHolder {

    private SparseArray<View> viewHolder;
    private View view;

    public static  ViewHolder newInstant(View view){
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }


    private ViewHolder(View view) {
        this.view = view;
        viewHolder = new SparseArray<>();
        view.setTag(viewHolder);
    }

    public <T extends View> T get(int id) {

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return view;
    }

    public TextView getTextView(int id) {

        return get(id);
    }
    public CheckBox getCheckBox(int id) {

        return get(id);
    }
    public TextSwitcher getTextSwitcher(int id) {

        return get(id);
    }
    public Button getButton(int id) {
        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int  id,CharSequence charSequence){

        getTextView(id).setText(charSequence);
    }

    public Context getContext(){
      return  getConvertView().getContext();
    }
}
