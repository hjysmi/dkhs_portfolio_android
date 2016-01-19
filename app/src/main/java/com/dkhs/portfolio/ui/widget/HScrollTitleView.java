/**
 * @Title HScrollTitleView.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午1:13:32
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.lidroid.xutils.util.LogUtils;

/**
 * @author zjz
 * @version 1.0
 * @ClassName HScrollTitleView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-29 下午1:13:32
 */
public class HScrollTitleView extends FrameLayout implements AnimationListener {

    // private Context context;
    private String[] nameList;
    // private List<Fragment> fragmentList;
    // private LinearLayout layout;
    // private FragmentManager mFragmentManager;
    // private LayoutInflater inflater;
    private int[] offsetNum;
    private int[] textWid;
    // 移动的Icon
    private ImageView iv;
    // 历史选中项
    private int hisPosition = -1;
    // 用于存储所有标题栏的textview,功能用于变换颜色
    private TextView[] tvList;
    // 左边两边下移ICON的边距,仅当当标题栏长度小于当前屏幕宽度会自动计算多于宽度
    private int offset = 0;

    private int indiatorWidth;

    /**
     *
     * @param context
     * @param nameListRTl 标题栏的名字
     * @param fragmentList 下面Fragment界面
     * @param layout 当前需要添加此控件的父控件
     * @param fragmentManager fragment管理器
     */
    // public HScrollTitleView(Context context, List<String> nameList) {
    // this.context = context;
    // this.nameList = nameList;
    // this.layout = layout;
    // inflater = LayoutInflater.from(context);
    // createView();
    // setAnima(offset, offset);
    // }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    public HScrollTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * @param context
     * @param attrs
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    public HScrollTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * @param context
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    public HScrollTitleView(Context context) {
        super(context);
        init();
    }

    /**
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void init() {
        // nameList = new ArrayList<String>();
        // nameList.add("新闻资讯");
        // nameList.add("个股公告");
        // nameList.add("F10");
        indiatorWidth = getContext().getResources().getDimensionPixelSize(R.dimen.weight);
        if (null != nameList && nameList.length > 0) {

            createView();

        }

    }

    /**
     * 实现标题栏的代码实现
     */
    public void createView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_horizontal_scroll_title, null);
        iv = (ImageView) view.findViewById(R.id.selectadapter_parent_icon);
        iv.getLayoutParams().width = indiatorWidth;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.selectadapter_parent_layout);
        if (nameList.length * indiatorWidth <= dm.widthPixels) {
            ll.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, getContext().getResources()
                    .getDimensionPixelSize(R.dimen.gray_tab_height) - 7));
            offset = (dm.widthPixels / nameList.length - indiatorWidth) / 2;
        } else {
            ll.setLayoutParams(new LinearLayout.LayoutParams(nameList.length * indiatorWidth, getContext()
                    .getResources().getDimensionPixelSize(R.dimen.gray_tab_height) - 7));
        }
        tvList = new TextView[nameList.length];
        for (int i = 0; i < nameList.length; i++) {
            TextView tv = new TextView(getContext());
            tv.setTextColor(getContext().getResources().getColor(R.color.black));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getContext().getResources().getDimensionPixelSize(R.dimen.title_tab_text_size));

            // tv.setPadding(0, 5, 0, 5);
            tv.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            tv.setLayoutParams(new LinearLayout.LayoutParams(indiatorWidth, LayoutParams.MATCH_PARENT, 1.0f));
            tv.setText(nameList[i]);
            tv.setOnClickListener(new OnItemListener(i));
            ll.addView(tv);
            tvList[i] = tv;
            if (i == 0) {
                tv.setTextColor(getContext().getResources().getColor(R.color.theme_blue));
            }
        }
        addView(view);
        setAnima(offset, offset);
    }

    public void setLayoutWidth(int widthPixels) {
        removeAllViews();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_horizontal_scroll_title, null);
        iv = (ImageView) view.findViewById(R.id.selectadapter_parent_icon);
        iv.getLayoutParams().width = indiatorWidth;
        // DisplayMetrics dm = new DisplayMetrics();
        // WindowManager m = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        // m.getDefaultDisplay().getMetrics(dm);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.selectadapter_parent_layout);
        if (nameList.length * indiatorWidth <= widthPixels) {
            ll.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, getContext().getResources()
                    .getDimensionPixelSize(R.dimen.gray_tab_height) - 7));
            offset = (widthPixels / nameList.length - indiatorWidth) / 2;
        } else {
            ll.setLayoutParams(new LinearLayout.LayoutParams(nameList.length * indiatorWidth, getContext()
                    .getResources().getDimensionPixelSize(R.dimen.gray_tab_height) - 7));
        }
        tvList = new TextView[nameList.length];
        for (int i = 0; i < nameList.length; i++) {
            TextView tv = new TextView(getContext());
            tv.setTextColor(getContext().getResources().getColor(R.color.black));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getContext().getResources().getDimensionPixelSize(R.dimen.title_tab_text_size));

            // tv.setPadding(0, 5, 0, 5);
            tv.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            tv.setLayoutParams(new LinearLayout.LayoutParams(indiatorWidth, LayoutParams.MATCH_PARENT, 1.0f));
            tv.setText(nameList[i]);
            tv.setOnClickListener(new OnItemListener(i));
            ll.addView(tv);
            tvList[i] = tv;
            if (i == 0) {
                tv.setTextColor(getContext().getResources().getColor(R.color.theme_blue));
            }
        }
        addView(view);
        setAnima(offset, offset);

    }

    private int currentPosition = 0;

    class OnItemListener implements OnClickListener {
        private int position;

        // private int wei;

        public OnItemListener(int position) {
            this.position = position;
            // wei = getContext().getResources().getDimensionPixelSize(R.dimen.weight);
        }

        @Override
        public void onClick(View v) {

            currentPosition = position;
            if (currentPosition == hisPosition) {
                return;
            }

            setAnima(hisPosition * indiatorWidth + offset * (2 * hisPosition + 1), position * indiatorWidth + offset
                    * (2 * position + 1));
            if (getVisibility() == View.GONE || getVisibility() == View.INVISIBLE) {
                Animation animation = iv.getAnimation();
                if (animation != null) {
                    animation.setDuration(0);
                }
                tvList[currentPosition].setTextColor(getContext().getResources().getColor(R.color.theme_blue));
                if (hisPosition != -1) {
                    tvList[hisPosition].setTextColor(getContext().getResources().getColor(R.color.black));
                }
                hisPosition = currentPosition;
            }

        }

    }

    public void setAnima(int startX, int endX) {
        Animation animation = null;
        animation = new TranslateAnimation(startX, endX, 0, 0);
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(250);
        animation.setAnimationListener(this);
        iv.startAnimation(animation);
    }

    public void setTitleList(String[] nameList, int width) {
        this.nameList = nameList;
        this.indiatorWidth = width;
        createView();
        // invalidate();
    }

    public void setTitleList(String[] nameList) {
        this.nameList = nameList;
        createView();
        // invalidate();
    }

    public void setSelectIndex(int index) {
        // tvList[currentPosition].setTextColor(getContext().getResources().getColor(R.color.red));
        // if(hisPosition != -1){
        // tvList[hisPosition].setTextColor(getContext().getResources().getColor(R.color.black));
        // }
        if (hisPosition == -1) {
            hisPosition = 0;
        }

        if (index >= 0 && index < tvList.length) {
            tvList[index].performClick();
        }
    }

    public void setSelectIndexNoCallBack(int index) {

        if (hisPosition == -1) {
            hisPosition = 0;
        }

        if (index >= 0 && index < tvList.length) {
            tvList[index].performClick();
        }
    }

    public void setIndicatorWidth(int width) {
        invalidate();
    }

    private ISelectPostionListener mSelectListener;

    public void setSelectPositionListener(ISelectPostionListener selectListener) {
        this.mSelectListener = selectListener;
    }

    public interface ISelectPostionListener {
        public void onSelectPosition(int position);
    }

    @Override
    public void onAnimationEnd(Animation arg0) {
        if (null != mSelectListener) {
            mSelectListener.onSelectPosition(currentPosition);
        }
        // PromptManager.closeProgressDialog();
    }

    @Override
    public void onAnimationRepeat(Animation arg0) {

    }

    @Override
    public void onAnimationStart(Animation arg0) {
        // PromptManager.showProgressDialog(getContext(), "");
        System.out.println("currentPosition:" + currentPosition);
        System.out.println("hisPosition:" + hisPosition);


        if (hisPosition != -1) {
            tvList[hisPosition].setTextColor(getContext().getResources().getColor(R.color.black));
        }
        tvList[currentPosition].setTextColor(getContext().getResources().getColor(R.color.theme_blue));
        hisPosition = currentPosition;

    }

    public int getCurrentPosition() {
        return currentPosition;
    }


    // @Override
    // protected void onLayout(boolean changed, int l, int t, int r, int b) {
    // View v = getChildAt(0);
    // v.layout(l, t, r, b);
    // }
}
