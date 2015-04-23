package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;



@SuppressWarnings({"unused,ambiguous"})
public abstract class MBaseAdapter extends BaseAdapter {

    public LayoutInflater inflater;
    public Context context;
    public List<?> list;

    public MBaseAdapter(Context context, List<?> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    public MBaseAdapter(LayoutInflater inflater, List<?> list) {
        this.context = inflater.getContext();
        this.list = list;
        this.inflater = inflater;

    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getView33(position, convertView, parent);
    }

    /**
     * 与BaseAdapter 中的getView() 一样
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public abstract View getView33(int position, View convertView,
                                   ViewGroup parent);


//    public AnimationAdapter getAnmAdapter(ListView listView) {
//        adapter = new SwingBottomInAnimationAdapter(this);
//        adapter.setAbsListView(listView);
//        return adapter;
//    }
//
//    public AnimationAdapter getAnmAdapter(ListView listView, AnimationAdapter adapter) {
//        adapter.setAbsListView(listView);
//        return adapter;
//
//    }

//    private AnimationAdapter adapter;


//    public void resetAnimation() {
//
//
//        if (adapter != null) {
//            adapter.reset();
//        }
//
//    }


}
