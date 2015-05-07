package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.widget.BaseAdapter;

public abstract class DKHSBaseAdapter extends BaseAdapter {

    private Object[] data;

    public void notifyDataChanged(List<?> data) {
        notifyDataChanged(data.toArray());
    }

    public void notifyDataChanged(Object[] data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void appendData(List<?> newData) {
        appendData(newData.toArray());
    }

    public void appendData(Object[] newData) {
        int size1 = newData == null ? 0 : newData.length;
        if (size1 == 0)
            return;// 传入得数据为空，不需要通知界面刷新

        if (data == null) {
            this.data = newData;
        } else {
            int size2 = data.length;

            Object[] dd = new Object[size1 + size2];
            System.arraycopy(data, 0, dd, 0, data.length);
            System.arraycopy(newData, 0, dd, data.length, newData.length);
            data = dd;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.length;
    }

    @Override
    public Object getItem(int arg0) {
        return data == null ? null : data[arg0];
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

}
