package com.dkhs.adpter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.dkhs.adpter.itemhandler.LoadMoreHandler;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ClassMap;
import com.dkhs.adpter.util.ViewHolder;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AutoLoadMoreRVAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/17.
 */
public class AutoLoadMoreRVAdapter extends  AutoRVAdapter {

    private AutoRVAdapter mAutoRVAdapter;
    private boolean showLoadFootView =true;

    private AutoLoadMoreRVAdapter(Context context, List<?> data, AutoRVAdapter adapter) {
        super(context, data);
        mAutoRVAdapter=adapter;
        mAutoRVAdapter.initHandlers(mClassMap);

    }


    public  static AutoLoadMoreRVAdapter  warp (AutoRVAdapter adapter){
        if(adapter instanceof  AutoLoadMoreRVAdapter){
            return (AutoLoadMoreRVAdapter)adapter;
        }

        return new AutoLoadMoreRVAdapter(adapter.mContext,adapter.mData,adapter);
    }

    @Override
    public int getItemViewType(int position) {

        String type=null;
        if(position == mData.size()) {
            type = "default";
        }else{
            type= mData.get(position).getClass().toString();
        }
            return mClassMap.getViewType(type);
    }

    @Override
    protected void initHandlers(ClassMap mAdapterItemMap) {
        mAdapterItemMap.add("default", new  LoadMoreHandler());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ItemHandler itemHandler;
        if(position == mData.size()){
             itemHandler = getItemHandler("default");
            itemHandler.onBindView((ViewHolder) holder.itemView.getTag(), null, position);
        }else {
             itemHandler = getItemHandler(mData.get(position).getClass());
            itemHandler.onBindView((ViewHolder) holder.itemView.getTag(), mData.get(position), position);
        }

    }

    @Override
    public int getItemCount() {

        if(showLoadFootView){
            return super.getItemCount() + 1;
        }else{
            return super.getItemCount();
        }
    }

    public boolean isShowLoadFootView() {
        return showLoadFootView;
    }

    public void setShowLoadFootView(boolean showLoadFootView) {
        this.showLoadFootView = showLoadFootView;
    }
}
