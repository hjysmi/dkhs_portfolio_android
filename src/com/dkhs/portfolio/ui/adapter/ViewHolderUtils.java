package com.dkhs.portfolio.ui.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderUtils {


    public ViewHolder get(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        return viewHolder;
    }


    public class ViewHolder {

        private SparseArray<View> viewHolder;
        private View view;

        public ViewHolder(View view) {
            this.view = view;
            viewHolder = new SparseArray<View>();
        }

        public <T extends View> T get(int id) {

            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;

        }


        public TextView getTextView(int id) {

            return get(id);
        }

        public ImageView getImageView(int id) {

            return get(id);
        }



        public void setTextView(int  id,CharSequence charSequence){
            

            getTextView(id).setText(charSequence);
        }

    }


}
