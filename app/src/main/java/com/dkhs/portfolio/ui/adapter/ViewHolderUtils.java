package com.dkhs.portfolio.ui.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author zwm
 * @version 1.0
 * @ClassName ViewHolderUtils
 * @date 2015/4/23.14:07
 * @Description TODO(ViewHolder 实现工具类)
 */
public class ViewHolderUtils {


    public static ViewHolder get(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        return viewHolder;
    }


    public static class ViewHolder {

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


        public View getRootView() {
            return view;
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
