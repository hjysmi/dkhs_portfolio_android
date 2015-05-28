package com.dkhs.portfolio.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.dkhs.portfolio.R;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MenuFloat
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/5/28.
 */
public class MenuFloat  extends RelativeLayout {
    public MenuFloat(Context context) {
        super(context);
    }

    public MenuFloat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuFloat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MenuFloat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public RecyclerView recyclerView;

    private List  data;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View view=   LayoutInflater.from(getContext()).inflate(R.layout.layout_menu_float,null);

        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);

    }


    public void setData(List data){
        this.data=data;





    }

    /**
     * 出现
     */
    public void  show(){

    }

    /**
     * 消失
     */
    public  void dissmiss(){

    }





}
