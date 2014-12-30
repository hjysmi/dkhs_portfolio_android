package com.dkhs.portfolio.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.utils.UIUtils;

public class FragmentSelectAdapter {
	private Context context;
	private String[] nameList;
	private List<Fragment> fragmentList;
	private LinearLayout layout;
	private FragmentManager mFragmentManager;
	private LayoutInflater inflater;
	private int[] offsetNum;
	private int[] textWid;
	private int[] textLayout;
	//移动的Icon
	private ImageView iv;
	//历史选中项
	private int hisPosition;
	//用于存储所有标题栏的textview,功能用于变换颜色
	private TextView[] tvList;
	//左边两边下移ICON的边距,仅当当标题栏长度小于当前屏幕宽度会自动计算多于宽度
	private int offset = 0;
	private int oneTextSize = 0;
	private int imageAddSize = 0;
	private ScrollViewPager pager;
	private DisplayMetrics dm;
	private int totalLength = 0;
	private StockQuotesActivity OutLaoyout;
	private HorizontalScrollView selectScroll;
	/**
	 * 
	 * @param context
	 * @param nameListRTl 标题栏的名字
	 * @param fragmentList 下面Fragment界面
	 * @param layout 当前需要添加此控件的父控件
	 * @param fragmentManager fragment管理器
	 */
	public FragmentSelectAdapter(Context context,String[] nameList,List<Fragment> fragmentList,LinearLayout layout,FragmentManager fragmentManager){
		this.context = context;
		this.nameList = nameList;
		this.fragmentList = fragmentList;
		this.layout = layout;
		this.mFragmentManager = fragmentManager;
		inflater = LayoutInflater.from(context);
		offset = context.getResources().getDimensionPixelSize(R.dimen.select_offset);
		oneTextSize = UIUtils.getTextWidth("正", context.getResources().getDimensionPixelSize(R.dimen.list_text_size));
		imageAddSize = context.getResources().getDimensionPixelSize(R.dimen.select_text);
		initDate();
		createView();
		//changeFrament(0,fragmentList.get(0),bundleList.get(0),fragmentList.get(0).toString());
		setAnima(offset,offset);
		pager.setCurrentItem(0);
	}
	private void initDate(){
		dm = new DisplayMetrics();
		WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		m.getDefaultDisplay().getMetrics(dm);
		textWid = new int[nameList.length];
		textLayout = new int[nameList.length];
		for(int i =0; i < nameList.length; i++){
			textWid[i] = nameList[i].length() * oneTextSize;
			textLayout[i] = nameList[i].length() * oneTextSize + imageAddSize * 2 + offset * 2;
			totalLength  = totalLength + textLayout[i];
		}
		if(totalLength < dm.widthPixels){
			totalLength = dm.widthPixels;
			int imageLength = 0;
			for(int k = 0; k < textLayout.length; k++){
				imageLength = imageLength +textWid[k] + imageAddSize * 2;
			}
			offset = (dm.widthPixels - imageLength)/nameList.length/2;
			for(int k = 0; k < textLayout.length; k++){
				textLayout[k] = textWid[k]  + imageAddSize * 2 + offset * 2;
			}
		}
	}
	public void setScrollAble(boolean isScorll){
		pager.setCanScroll(isScorll);
	}
	/**
	 * 实现标题栏的代码实现
	 */
	public void createView(){
		View view = inflater.inflate(R.layout.selectadapter_layout, null);
		pager = (ScrollViewPager) view.findViewById(R.id.selectadapter_pager);
        pager.setAdapter(new OrderFragmentAdapter(mFragmentManager, fragmentList));
        pager.setOnPageChangeListener(pageChangeListener);
		iv = (ImageView) view.findViewById(R.id.selectadapter_parent_icon);
		//selectScroll = (HorizontalScrollView) view.findViewById(R.id.select_scroll);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.selectadapter_parent_layout);
		ll.setLayoutParams(new LinearLayout.LayoutParams(totalLength, LayoutParams.WRAP_CONTENT));
		//ll.setOnTouchListener(new OnmyLayout());
		tvList = new TextView[nameList.length];
		for(int i = 0; i < nameList.length; i++){
			TextView tv = new TextView(context);
			tv.setTextColor(context.getResources().getColor(R.color.black));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimensionPixelSize(R.dimen.list_text_size));
			tv.setPadding(0, 10, 0, 5);
			
			tv.setLayoutParams(new LinearLayout.LayoutParams(textLayout[i] , LayoutParams.WRAP_CONTENT));
			tv.setGravity(Gravity.CENTER);
			tv.setText(nameList[i]);
			tv.setOnClickListener(new OnItemListener(i));
			//tv.setOnTouchListener(new OnmyLayout(i));
			ll.addView(tv);
			tvList[i] = tv;
			if(i == 0){
				tv.setTextColor(context.getResources().getColor(R.color.red));
				iv.getLayoutParams().width = textWid[i] + imageAddSize * 2;
			}
		}
		layout.addView(view);
		
	}
	OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
        	//mHandler.sendEmptyMessage(arg0);
        	scroll(arg0);
        	
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };
	class OnItemListener implements OnClickListener{
		private int position;
		public OnItemListener(int position){
			this.position = position;
			
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pager.setCurrentItem(position);
			/*if(position % 2 == 1){
				OutLaoyout.chartTounching();
            	
			}else{
				OutLaoyout.loseTouching();
			}*/
			//scroll(position);
		}
		
	}
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			scroll(msg.what);
			super.handleMessage(msg);
		}
		
	};
	public void scroll(int position){
		iv.getLayoutParams().width = textWid[position] + imageAddSize * 2;
		
		int end = offset;
		int start = offset;
		/*Fragment f = fragmentList.get(position);
		changeFrament(0,f,bundleList.get(position),fragmentList.get(position).toString());*/
		for(int i = 0; i < position ; i++){
			end = end + textLayout[i];
			if(i <= textLayout.length - 1){
			    end = end + (textLayout[i + 1] - textLayout[i])/2;
			}
		}
		for(int i = 0; i < hisPosition ; i++){
			start = start + textLayout[i];
		}
		tvList[position].setTextColor(context.getResources().getColor(R.color.red));
		tvList[hisPosition].setTextColor(context.getResources().getColor(R.color.black));
		setAnima(start, end);
		hisPosition = position;
		//layout.getLayoutParams().height = 2000;
	}
	public void setAnima(int startX,int endX){
		Animation animation = null;
		animation = new TranslateAnimation(startX,endX , 0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		iv.startAnimation(animation);
	}
	private class OrderFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public OrderFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList2) {
            super(fm);
            this.fragmentList = fragmentList2;

        }

        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }
	public StockQuotesActivity getOutLaoyout() {
		return OutLaoyout;
	}
	public void setOutLaoyout(StockQuotesActivity outLaoyout) {
		OutLaoyout = outLaoyout;
	}
	class OnmyLayout implements OnTouchListener{
		int position;
		public OnmyLayout(int position){
			this.position = position;
		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(null != OutLaoyout){
			switch (event.getAction()) {
			
            case MotionEvent.ACTION_DOWN:
            	
            	OutLaoyout.loseTouching();
                break;
            case MotionEvent.ACTION_UP:
            	pager.setCurrentItem(position);
            	//OutLaoyout.chartTounching();
                break;
            default:
                break;
			}
        }else{
        	pager.setCurrentItem(position);
        }
        return true;
		}
		
	}
}
