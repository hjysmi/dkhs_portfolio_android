/**
 * @Title RVMainFunctionAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-1-19 上午10:53:44
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.GettingUrlForAPPActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter.IDelButtonListener;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter.ViewHolder;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * @ClassName RVMainFunctionAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-1-19 上午10:53:44
 * @version 1.0
 */
public class RVMyCombinationAdapter extends RecyclerView.Adapter<RVMyCombinationAdapter.ViewHolder> implements
        OnClickListener {

    private Context mContext;
    private List<CombinationBean> mDataList;
    private boolean isDelStatus;

    public RVMyCombinationAdapter(Context context, List<CombinationBean> datas) {
        this.mContext = context;
        this.mDataList = datas;
    }

    @Override
    public int getItemCount() {
        if (isDelStatus) {
            return this.mDataList.size();
        }
        return this.mDataList.size() + 1;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (position == mDataList.size()) {
            viewHolder.ivAdd.setVisibility(View.VISIBLE);
            viewHolder.viewInfo.setVisibility(View.INVISIBLE);
            viewHolder.tvDesc.setVisibility(View.INVISIBLE);
            viewHolder.ivDel.setVisibility(View.GONE);
        } else {
            viewHolder.ivAdd.setVisibility(View.GONE);
            viewHolder.viewInfo.setVisibility(View.VISIBLE);
            viewHolder.tvDesc.setVisibility(View.VISIBLE);

            CombinationBean item = mDataList.get(position);
            final ViewHolder viewhold = viewHolder;
            viewHolder.tvTitle.setText(item.getName());
            if (position >= 9) {
                viewHolder.tvIndex.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.two_index_padding),
                        0, 0, 0);
            } else {
                viewHolder.tvIndex.setPadding(
                        mContext.getResources().getDimensionPixelSize(R.dimen.single_index_padding), 0, 0, 0);

            }
            viewHolder.tvIndex.setText((position + 1) + "");

            float currenValue = item.getChng_pct_day();
            viewHolder.tvCurrent.setTextColor(ColorTemplate.getUpOrDrownCSL(currenValue));
            viewHolder.tvCurrent.setText(StringFromatUtils.get2PointPercentPlus(currenValue));

            float addValue = item.getAddUpValue();
            viewHolder.tvAddup.setTextColor(ColorTemplate.getUpOrDrownCSL(addValue));
            viewHolder.tvAddup.setText(StringFromatUtils.get2PointPercentPlus(addValue));

            viewHolder.ivDel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (UIUtils.isFastDoubleClick()) {
                        return;
                    }
                    if (null != mItemClickListener) {
                        mItemClickListener.onClickDeleteButton(position);
                    }
                }
            });
            if (isDelStatus) {
                viewHolder.ivDel.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivDel.setVisibility(View.GONE);

            }
            if (TextUtils.isEmpty(item.getDescription().trim())) {
                viewHolder.tvDesc.setText(mContext.getString(R.string.desc_format,
                        mContext.getString(R.string.desc_def_text)));
            } else {

                viewHolder.tvDesc.setText(mContext.getString(R.string.desc_format, item.getDescription()));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_my_combination, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvTitle;
        TextView tvCurrent;
        TextView tvAddup;
        TextView tvIndex;
        TextView tvDesc;
        ImageView ivAdd;
        View viewInfo;

        ImageButton ivDel;

        public ViewHolder(View row) {
            super(row);

            tvTitle = (TextView) row.findViewById(R.id.tv_combin_title);
            tvCurrent = (TextView) row.findViewById(R.id.tv_mycob_curren_value);
            tvAddup = (TextView) row.findViewById(R.id.tv_mycob_add_value);
            tvIndex = (TextView) row.findViewById(R.id.tv_combination_index);
            tvDesc = (TextView) row.findViewById(R.id.tv_combination_desc);
            ivDel = (ImageButton) row.findViewById(R.id.ib_del_conbin);
            ivAdd = (ImageView) row.findViewById(R.id.iv_additem);
            viewInfo = row.findViewById(R.id.rl_combina_title);
            row.setOnClickListener(this);
            row.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getPosition() == mDataList.size()) {
                addItem();
            } else if (null != mItemClickListener && !isDelStatus) {
                mItemClickListener.onItemClick(v, getPosition());
            }

        }

        @Override
        public boolean onLongClick(View v) {
            if (getPosition() != mDataList.size() && null != mItemClickListener) {
                mItemClickListener.onLongItemClick(v, getPosition());
            }
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        addItem();
    }

    public void addItem() {
        if (mDataList.size() >= 20) {
            PromptManager.showShortToast(R.string.more_combination_tip);
        } else {
            mContext.startActivity(PositionAdjustActivity.newIntent(mContext, null));
        }

    }

    public void setDelStatus(boolean isDelStatus) {
        this.isDelStatus = isDelStatus;
        notifyDataSetChanged();
    }

    OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

        void onClickDeleteButton(int position);

        void onLongItemClick(View view, int position);
    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
