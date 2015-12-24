package com.dkhs.portfolio.ui.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MenuBean;
import com.dkhs.portfolio.ui.adapter.AutoRVAdapter;
import com.dkhs.portfolio.ui.adapter.RVHolder;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.fragment.MarketFundsFragment;
import com.dkhs.portfolio.utils.AnimationHelper;

import java.util.ArrayList;
import java.util.List;
/**
 * @author zwm
 * @version 2.0
 * @ClassName MenuFloat
 * @Description TODO()
 * @date 2015/5/28.
 */
public class MultiChooserRelativeLayout extends RelativeLayout {


    // FIXME: 2015/7/10   待优化
    public MultiChooserRelativeLayout(Context context) {
        super(context);
    }

    public MultiChooserRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiChooserRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiChooserRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }



    public RecyclerView recyclerView;
    public RecyclerView recyclerView2;
    public SwitchCompat switchCompat;
    private int allowTrade;


    private List<MenuBean> data = new ArrayList<>();
    private List<MenuBean> data2 = new ArrayList<>();

    private FloatMenuAdapter floatMenuAdapter;
    private FloatMenuAdapter floatMenuAdapter2;
    private ImageView imageView;
    private View menuLL;

    private MenuBean selectItem;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void inflater() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_multi_menu_relativelayout, null);

        imageView = (ImageView) view.findViewById(R.id.im_bg);
        menuLL = view.findViewById(R.id.ll_menu);
        this.addView(view);
        this.setVisibility(GONE);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new WrapGridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        floatMenuAdapter = new FloatMenuAdapter(getContext(), data);

        floatMenuAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                floatMenuAdapter.setSelectIndex(data.get(position));
                floatMenuAdapter2.setSelectIndex(data.get(position));
                selectItem = data.get(position);
                notifyDataSetChanged();
                BusProvider.getInstance().post(selectItem);
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggle();
                    }
                }, 500);
            }
        });
        recyclerView.setAdapter(floatMenuAdapter);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler_view2);
        GridLayoutManager gridLayoutManager2 = new WrapGridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(gridLayoutManager2);
        floatMenuAdapter2 = new FloatMenuAdapter(getContext(), data2);

        floatMenuAdapter2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                floatMenuAdapter.setSelectIndex(data2.get(position));
                floatMenuAdapter2.setSelectIndex(data2.get(position));
                selectItem=data2.get(position);
                notifyDataSetChanged();
                BusProvider.getInstance().post(selectItem);
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggle();
                    }
                }, 500);
            }
        });
        recyclerView2.setAdapter(floatMenuAdapter2);
        switchCompat = (SwitchCompat) view.findViewById(R.id.sw_fund_can_buy);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectItem.setAllowTrade(isChecked ? MarketFundsFragment.SHOW_ONLY_ALLOW_TRADE : MarketFundsFragment.SHOW_ALL);
                BusProvider.getInstance().post(selectItem);
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggle();
                    }
                }, 500);
            }
        });
    }


    public MenuBean getSelectItem() {
        return selectItem;
    }

    public void notifyDataSetChanged() {


//        int prePosition = floatMenuAdapter.getSelectIndex();

        floatMenuAdapter.notifyDataSetChanged();
//        selectItem = data.get(prePosition);
        floatMenuAdapter2.notifyDataSetChanged();
//        selectItem = data.get(prePosition);

    }

    private int selectIndex;

    public int getSelectIndex() {
        return selectIndex;
    }


    public void setData(List<MenuBean> data, List<MenuBean> data2) {
        inflater();
        this.data.clear();
        this.data.addAll(data);
        selectItem = data2.get(0);
        floatMenuAdapter.setSelectIndex(selectItem);
        floatMenuAdapter.notifyDataSetChanged();
        this.data2.clear();
        this.data2.addAll(data2);
        floatMenuAdapter2.setSelectIndex(selectItem);
        floatMenuAdapter2.notifyDataSetChanged();
    }

    public void setAllowTrade(int allowTrade){
        this.allowTrade = allowTrade;
    }

    private ViewGroup parentView;

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
        if (this.getParentView() != null) {
            ViewGroup viewGroup = this.getParentView();
            viewGroup.removeView(this);
        }
        this.parentView.addView(this);
        AnimationHelper.translationFromTopShow(menuLL, new Animator.AnimatorListener() {
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
    public void dismiss(boolean anim) {

        if (anim) {
            if (null == imageView.getAnimation() || imageView.getAnimation().hasEnded()) {
                AnimationHelper.translationToTopDismiss(menuLL, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        BusProvider.getInstance().post(MultiChooserRelativeLayout.this);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        MultiChooserRelativeLayout.this.setVisibility(GONE);
                        menuLL.setVisibility(GONE);
                        MultiChooserRelativeLayout.this.parentView.removeView(MultiChooserRelativeLayout.this);
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
        } else {

            MultiChooserRelativeLayout.this.setVisibility(GONE);
            menuLL.setVisibility(GONE);
            MultiChooserRelativeLayout.this.parentView.removeView(MultiChooserRelativeLayout.this);
        }

    }

    public void dismiss() {
        dismiss(true);
    }

    public void setFundManagerRanking() {
        floatMenuAdapter.setSelectIndex(data2.get(0));
        floatMenuAdapter2.setSelectIndex(data2.get(0));
        selectItem=data2.get(0);
        notifyDataSetChanged();
        if(selectItem != null)
            BusProvider.getInstance().post(selectItem);

    }

    public void setFundsAllRanking() {
        floatMenuAdapter.setSelectIndex(data.get(0));
        floatMenuAdapter2.setSelectIndex(data.get(0));
        selectItem=data.get(0);
        notifyDataSetChanged();
        BusProvider.getInstance().post(selectItem);

    }
    public void setFundsMixedRanking() {
        floatMenuAdapter.setSelectIndex(data.get(2));
        floatMenuAdapter2.setSelectIndex(data.get(2));
        selectItem=data.get(2);
        notifyDataSetChanged();
        BusProvider.getInstance().post(selectItem);

    }


    class FloatMenuAdapter extends AutoRVAdapter {

        private MenuBean selectIndex=new MenuBean();

        public FloatMenuAdapter(Context context, List<?> list) {
            super(context, list);
        }


        @Override
        public int onCreateViewLayoutID(int viewType) {

            return R.layout.item_menu;
        }

        @Override
        public void onBindViewHolder(RVHolder holder, int position) {

            super.onBindViewHolder(holder, position);
            MenuBean item = (MenuBean) list.get(position);
            holder.getViewHolder().setTextView(R.id.textView, item.getKey());

            View view =  holder.getViewHolder().get(R.id.textView);
            holder.getViewHolder().getConvertView().setClickable(item.isEnable());
            if (item.isEnable()) {
                view.setEnabled(true);

                if (item.getValue() .equals( selectIndex.getValue())) {
                    view.setSelected(true);
                } else {
                    view.setSelected(false);
                }
            } else {
                view.setEnabled(false);
            }
        }

        public MenuBean getSelectIndex() {
            return selectIndex;
        }

        public void setSelectIndex(MenuBean selectIndex) {
            this.selectIndex = selectIndex;
        }
    }


    public class WrapGridLayoutManager extends GridLayoutManager {

        private int[] mMeasuredDimension = new int[2];

        public WrapGridLayoutManager(Context context) {
            super(context);
        }

        public WrapGridLayoutManager(Context context, int columns, int orientation) {
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


//            for (int i = 0; i < getItemCount(); i++) {
//                measureScrapChild(recycler, i,
//                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//                        mMeasuredDimension);
//
//                if (getOrientation() == HORIZONTAL) {
//                    if (i % getColumns() == 0) {
//                        width = width + mMeasuredDimension[0];
//                    }
//                    if (i == 0) {
//                        height = mMeasuredDimension[1];
//                    }
//                } else {
//                    if (i % getColumns() == 0) {
//                        height = height + mMeasuredDimension[1];
//                    }
//                    if (i == 0) {
//                        width = mMeasuredDimension[0];
//                    }
//                }
//            }
            if (getItemCount() > 0) {

                int row = getItemCount() / getColumns();

                if (getItemCount() % getColumns() != 0) {
                    row++;
                }

                measureScrapChild(recycler, 0,
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);

                if (getOrientation() == HORIZONTAL) {

                    width = mMeasuredDimension[0] * row;


                } else {
                    height = height + mMeasuredDimension[1] * row;

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
//
            setMeasuredDimension(width, height);
//            super.onMeasure(recycler,state,widthSpec,heightSpec);
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


    public ViewGroup getParentView() {
        return parentView;
    }

    public void setParentView(ViewGroup parentView) {
        this.parentView = parentView;
    }
}
