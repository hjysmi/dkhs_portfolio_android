package com.dkhs.portfolio.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.dkhs.portfolio.R;

/**
 * Created by wuyongsen on 2015/10/9.
 */
public class PopupWinUtis {
    private PopupWinUtis(){

    }
    public static PopupWindow getPopupWindow(Context context,String[] items,AdapterView.OnItemClickListener itemClickListener){
            View view = View.inflate(context, R.layout.layout_popupwin, null);
            final PopupWindow pw = new PopupWindow(view,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            view.findViewById(R.id.im_bg).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pw.isShowing())
                        pw.dismiss();
                }
            });
            ListView listview = (ListView)view.findViewById(R.id.listview);
            listview.setAdapter(new ArrayAdapter<String>(context,R.layout.item_menu,R.id.textView,items));
            listview.setOnItemClickListener(itemClickListener);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.setOutsideTouchable(true);
            pw.setAnimationStyle(R.style.popwin_anim_style);
        return pw;
    }


}
