package com.dkhs.portfolio.ui.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.AnimationHelper;

import java.util.ArrayList;
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

    private List<CharSequence>  data=new ArrayList<>();

    private Adapter adapter;
    private ImageView imageView;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View view=   LayoutInflater.from(getContext()).inflate(R.layout.layout_menu_float,null);

        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);
        imageView= (ImageView) view.findViewById(R.id.im_bg);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3, GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter=new Adapter();
        recyclerView.setAdapter(adapter );
        this.addView(view);
    }


    public void setData(List<CharSequence>  data){

        this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();

    }

    /**
     * 出现
     */
    public void  show(){

        this.setVisibility(VISIBLE);
        AnimationHelper.translationFromTopShow(recyclerView,new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimationHelper.alphaShow(imageView);
    }

    /**
     * 消失
     */
    public  void dissmiss(){
        AnimationHelper.translationToTopDismiss(recyclerView,new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
               MenuFloat.this.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimationHelper.alphaDismiss(imageView);

    }

    //fixme 待优化,使用通用的适配器
    class Adapter   extends RecyclerView.Adapter<Holder>{

        public Adapter(){
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_menu_float,null);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {


            CharSequence  text=data.get(position);
            holder.textView.setText(text);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


    }
    class  Holder extends RecyclerView.ViewHolder{

        TextView textView;

        public Holder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.textView);
        }
    }


}
