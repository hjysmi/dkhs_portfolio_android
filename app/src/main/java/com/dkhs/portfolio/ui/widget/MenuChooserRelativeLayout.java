package com.dkhs.portfolio.ui.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
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
import com.dkhs.portfolio.bean.MenuTypeBean;
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
public class MenuChooserRelativeLayout extends RelativeLayout {
    public MenuChooserRelativeLayout(Context context) {
        super(context);
    }

    public MenuChooserRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuChooserRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MenuChooserRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public RecyclerView recyclerView;

    private List<MenuTypeBean> data = new ArrayList<>();

    private Adapter adapter;
    private ImageView imageView;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_menu_float, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        imageView = (ImageView) view.findViewById(R.id.im_bg);
        GridLayoutManager gridLayoutManager = new MyLinearLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        this.addView(view);
        this.setVisibility(GONE);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void setData(List<MenuTypeBean> data) {

        this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
    }


    public void toggle() {

        if (this.getVisibility() == VISIBLE) {
            dismiss();
        } else {
            show();
        }


    }

    /**
     * 出现
     */
    public void show() {

        this.setVisibility(VISIBLE);
        AnimationHelper.translationFromTopShow(recyclerView, new Animator.AnimatorListener() {
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
    public void dismiss() {
        AnimationHelper.translationToTopDismiss(recyclerView, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                MenuChooserRelativeLayout.this.setVisibility(GONE);
                recyclerView.setVisibility(GONE);

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
    class Adapter extends RecyclerView.Adapter<Holder> {

        private int selectIndex;

        public Adapter() {
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_menu, null);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

            MenuTypeBean item = data.get(position);
            holder.textView.setText(item.getKey());
            if (item.isEnable()) {
                holder.view.setEnabled(true);
                if (position == selectIndex) {
                    holder.view.setSelected(true);
                } else {
                    holder.view.setSelected(false);
                }
            } else {
                holder.view.setEnabled(false);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setSelectIndex(int selectIndex) {
            this.selectIndex = selectIndex;
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView textView;
        View view;

        public Holder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }


    public class MyLinearLayoutManager extends GridLayoutManager {

        private int[] mMeasuredDimension = new int[2];

        public MyLinearLayoutManager(Context context) {
            super(context);
        }

        public MyLinearLayoutManager(Context context, int columns, int orientation) {
            super(context, columns, orientation);
        }

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                              int widthSpec, int heightSpec) {
            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);
            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i++) {
                measureScrapChild(recycler, i,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);

                if (getOrientation() == HORIZONTAL) {
                    if (i % getColumns() == 0) {
                        width = width + mMeasuredDimension[0];
                    }
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    if (i % getColumns() == 0) {
                        height = height + mMeasuredDimension[1];
                    }
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                       int heightSpec, int[] measuredDimension) {
            View view = recycler.getViewForPosition(position);
            if (view != null) {
                RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                        getPaddingLeft() + getPaddingRight(), p.width);
                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                        getPaddingTop() + getPaddingBottom(), p.height);
                view.measure(childWidthSpec, childHeightSpec);
                measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                recycler.recycleView(view);
            }
        }
    }

}
