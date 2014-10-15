package com.dkhs.portfolio.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.MenuLeftFragment.MenuItemAdapter;

public class MenuRightFragment extends Fragment implements OnClickListener{
	private ListVO[] items_value;
	BaseAdapter itemAdapter;
	private ImageView rightImagePoint;
	private LinearLayout[] ly;
	private ImageView[] im;
	private ListView rightListview;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTestDate();
		ly = new LinearLayout[items_value.length];
		im = new ImageView[items_value.length];
		itemAdapter = new MenuItemAdapter();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu_right, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		rightImagePoint = (ImageView) view.findViewById(R.id.right_image_point);
		rightListview = (ListView) view.findViewById(R.id.right_listview);
		rightListview.setAdapter(itemAdapter);
		rightImagePoint.setOnClickListener(this);
		
	}
	
	
	class MenuItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return items_value.length;
		}

		@Override
		public Object getItem(int position) {
			return items_value[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			//if (null == convertView) {
				convertView = View.inflate(getActivity(),
						R.layout.right_menu_item, null);
				holder = new ViewHolder();
				holder.tvItemName = (TextView) convertView
						.findViewById(R.id.right_menu_date);
				holder.layoutItemValue = (LinearLayout) convertView
						.findViewById(R.id.right_menu_layout_foradd);
				holder.imgItemIcon = (ImageView) convertView.findViewById(R.id.right_menu_line_first);
				holder.imgItemine = (ImageView) convertView.findViewById(R.id.right_menu_line_all);
				//holder.layoutLine = (LinearLayout) convertView.findViewById(R.id.right_menu_line_layouts);
				convertView.setTag(holder);
			//} else {
				//holder = (ViewHolder) convertView.getTag();
			//}
			//holder.tvItemName.setText("");
			if(position == 0){
				holder.imgItemIcon.setVisibility(View.GONE);
			}
			createView(holder.layoutItemValue,items_value[position]);
			
			//holder.layoutLine.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			/*Bundle b = new Bundle();
			b.putSerializable("holder",  holder);
			Message m = new Message();
			m.setData(b);
			mHander.sendMessage(m);*/
			ly[position] = holder.layoutItemValue;
			im[position] = holder.imgItemine;
				/*Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mHander.sendEmptyMessage(0);
					}
				});
				t.start();*/
			return convertView;
		}
		Handler mHander = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				for(int i =0; i < items_value.length; i++){
					if(im[i] != null){
						LayoutParams lp = (LayoutParams) im[i].getLayoutParams();
						lp.height = ly[i].getHeight();
						im[i].setLayoutParams(lp);
					}
				}
				
				super.handleMessage(msg);
			}
			
		};
		public void createView(LinearLayout layout, ListVO vo){
			for(int i = 0; i < vo.list.size(); i++){
				LinearLayout ly = new LinearLayout(getActivity());
				ly.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				ly.setOrientation(LinearLayout.HORIZONTAL);
				TextView tv1 = new TextView(getActivity());
				tv1.setTextColor(getResources().getColor(R.color.white));
				tv1.setTextSize(getResources().getDimension(R.dimen.text_size));
				tv1.setText("创建了");
				ly.addView(tv1);
				TextView tv2 = new TextView(getActivity());
				tv2.setTextColor(getResources().getColor(R.color.blue));
				tv2.setMaxLines(2);
				tv2.setTextSize(getResources().getDimension(R.dimen.text_size));
				tv2.setEllipsize(TruncateAt.END);
				tv2.setPadding((int)getResources().getDimension(R.dimen.offset_bottom_text), 0, 0, 0);
				tv2.setText("创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了");
				ly.addView(tv2);
				layout.addView(ly);
			}
			
		}
		class ViewHolder implements Serializable{
			TextView tvItemName;
			LinearLayout layoutItemValue;
			ImageView imgItemIcon;
			ImageView imgItemine;
			LinearLayout layoutLine;
		}
	}
	public void setTestDate(){
		ListVO vo = new ListVO();
		List<VO> list= new ArrayList<VO>();
		VO v = new VO();
		v.title = "创建了";
		v.value = "创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了";
		list.add(v);
		list.add(v);
		list.add(v);
		list.add(v);
		vo.list = list;
		vo.date = ("10月14日  20:23:36");
		items_value = new ListVO[]{vo,vo,vo,vo};
	}
	class VO{
		String title;
		String value;
	}
	class ListVO{
		List<VO> list;
		String date;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.right_image_point:
			
			break;

		default:
			break;
		}
	}
}
