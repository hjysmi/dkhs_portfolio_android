package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.utils.PromptManager;

/**
 * Created by zjz on 2015/6/18.
 */
public class FlowPackAdapter extends BaseAdapter {


    String[] titleTexts = PortfolioApplication.getInstance().getResources().getStringArray(R.array.flow_pack_lvtitle);
    String[] tipTexts = PortfolioApplication.getInstance().getResources().getStringArray(R.array.flow_pack_lvtitle_tip);
    //    int[] iconRes = PortfolioApplication.getInstance().getResources().getIntArray(R.array.flow_pack_lv_icon);
    String[] btnTexts = PortfolioApplication.getInstance().getResources().getStringArray(R.array.flow_pack_btn_text);
    int[] iconRes;

    private Context mContext;

    public FlowPackAdapter(Context context) {
        this.mContext = context;
        initIconResource();
    }

    private void initIconResource() {
        TypedArray ar = mContext.getResources().obtainTypedArray(R.array.flow_pack_lv_icon);
        int len = ar.length();
        iconRes = new int[len];
        for (int i = 0; i < len; i++) iconRes[i] = ar.getResourceId(i, 0);
        ar.recycle();
    }

    @Override
    public int getCount() {
        return titleTexts.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_flow_input, null);
        }
//        onViewCreated(position, convertView, ViewHolderUtils.get(convertView));
        ViewHolderUtils.ViewHolder vholder = ViewHolderUtils.get(convertView);
        Button btnAction = vholder.get(R.id.btn_action);
        ImageView ivIcon = vholder.get(R.id.iv_flow_icon);
        TextView tvTitle = vholder.get(R.id.tv_flow_title);
        TextView tvTip = vholder.get(R.id.tv_flow_tip);
        tvTitle.setText(titleTexts[position]);
        tvTip.setText(tipTexts[position]);
        btnAction.setText(btnTexts[position]);
        ivIcon.setImageResource(iconRes[position]);


        btnAction.setOnClickListener(new ButtonListener(position));
        return convertView;
    }


    class ButtonListener implements View.OnClickListener {
        private int position;

        ButtonListener(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
//            int vid = v.getId();
//            if (vid == holder.buttonClose.getId())
//                removeItem(position);

            PromptManager.showToastTest("click postion:" + position);
        }
    }


}
