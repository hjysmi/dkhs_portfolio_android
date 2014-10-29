package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.NewsFragment;

public class FragmentSelectAdapter {
	private Context context;
	private List<String> nameList;
	private List<Fragment> fragmentList;
	private LinearLayout layout;
	private FragmentManager mFragmentManager;
	private LayoutInflater inflater;
	private int[] offsetNum;
	private int[] textWid;
	//移动的Icon
	private ImageView iv;
	//历史选中项
	private int hisPosition;
	//用于存储所有标题栏的textview,功能用于变换颜色
	private TextView[] tvList;
	//左边两边下移ICON的边距,仅当当标题栏长度小于当前屏幕宽度会自动计算多于宽度
	private int offset = 0;
	private List<Bundle> bundleList;
	/**
	 * 
	 * @param context
	 * @param nameListRTl 标题栏的名字
	 * @param fragmentList 下面Fragment界面
	 * @param layout 当前需要添加此控件的父控件
	 * @param fragmentManager fragment管理器
	 */
	public FragmentSelectAdapter(Context context,List<String> nameList,List<Fragment> fragmentList,LinearLayout layout,FragmentManager fragmentManager,List<Bundle> bundleList){
		this.context = context;
		this.nameList = nameList;
		this.fragmentList = fragmentList;
		this.layout = layout;
		this.mFragmentManager = fragmentManager;
		this.bundleList = bundleList;
		inflater = LayoutInflater.from(context);
		createView();
		changeFrament(0,fragmentList.get(0),bundleList.get(0),fragmentList.get(0).toString());
		setAnima(offset,offset);
	}
	/**
	 * 实现标题栏的代码实现
	 */
	public void createView(){
		View view = inflater.inflate(R.layout.selectadapter_layout, null);
		iv = (ImageView) view.findViewById(R.id.selectadapter_parent_icon);
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		m.getDefaultDisplay().getMetrics(dm);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.selectadapter_parent_layout);
		if(nameList.size()*context.getResources().getDimensionPixelSize(R.dimen.weight) <= dm.widthPixels){
			ll.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, LayoutParams.WRAP_CONTENT));
			offset = (dm.widthPixels/nameList.size() - context.getResources().getDimensionPixelSize(R.dimen.weight))/2;
		}else{
			ll.setLayoutParams(new LinearLayout.LayoutParams(nameList.size()*context.getResources().getDimensionPixelSize(R.dimen.weight), LayoutParams.WRAP_CONTENT));
		}
		tvList = new TextView[nameList.size()];
		for(int i = 0; i < nameList.size(); i++){
			TextView tv = new TextView(context);
			tv.setTextColor(context.getResources().getColor(R.color.black));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimensionPixelSize(R.dimen.list_text_size));
			tv.setPadding(0, 10, 0, 5);
			tv.setGravity(Gravity.CENTER);
			tv.setLayoutParams(new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.weight), LayoutParams.WRAP_CONTENT, 1.0f));
			tv.setText(nameList.get(i));
			tv.setOnClickListener(new OnItemListener(i));
			ll.addView(tv);
			tvList[i] = tv;
			if(i == 0){
				tv.setTextColor(context.getResources().getColor(R.color.red));
			}
		}
		layout.addView(view);
	}
	public void changeFrament(int levels,Fragment fragment, Bundle bundle, String tag) {
		try {
			for (int i = levels, count = mFragmentManager.getBackStackEntryCount(); i < count; i++) {
				mFragmentManager.popBackStack();
			}
				FragmentTransaction fg = mFragmentManager.beginTransaction();
				(fragment).setArguments(bundle);
				fg.replace(R.id.selectadapter_parent_child, fragment, tag);
				fg.addToBackStack(tag);
				fg.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class OnItemListener implements OnClickListener{
		private int position;
		private int wei;
		public OnItemListener(int position){
			this.position = position;
			wei = context.getResources().getDimensionPixelSize(R.dimen.weight);
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Fragment f = fragmentList.get(position);
			changeFrament(0,f,bundleList.get(position),fragmentList.get(position).toString());
			tvList[position].setTextColor(context.getResources().getColor(R.color.red));
			tvList[hisPosition].setTextColor(context.getResources().getColor(R.color.black));
			setAnima(hisPosition *wei + offset * (2 * hisPosition + 1), position * wei + offset * (2 * position + 1) );
			hisPosition = position;
		}
		
	}
	public void setAnima(int startX,int endX){
		Animation animation = null;
		animation = new TranslateAnimation(startX,endX , 0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		iv.startAnimation(animation);
	}
}
