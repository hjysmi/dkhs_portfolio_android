package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import com.dkhs.portfolio.R;
/**
 * @author zwm
 * @version 1.0
 * @ClassName AutoAdapter
 * @date 2015/4/23.14:07
 * @Description TODO(简化BaseAdapter,内部实现ViewHodler 模式)
 */
public abstract class AutoAdapter extends MBaseAdapter {


    private int layoutID;
    protected ViewHolderUtils vh;

    public AutoAdapter(Context context, List<?> list, int layoutID) {
        super(context, list);
        this.layoutID = layoutID;
        vh = new ViewHolderUtils();
    }

    @Override
    public View getView33(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(layoutID, parent, false);
        }
        getView33(position, convertView, vh.get(convertView));
        return convertView;
    }


    public abstract void getView33(int position, View v, ViewHolderUtils.ViewHolder vh);


}
