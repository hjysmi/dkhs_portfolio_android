package com.dkhs.portfolio.ui.fragment;

import java.util.List;

import android.R.color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;

public class MenuRightFragment extends Fragment {
	private String[] items;
	private List<String>[] items_value;
	BaseAdapter itemAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu_right, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
	}
	
	
	class MenuItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (null == convertView) {
				convertView = View.inflate(getActivity(),
						R.layout.right_menu_item, null);
				holder = new ViewHolder();
				holder.tvItemName = (TextView) convertView
						.findViewById(R.id.right_menu_date);
				holder.layoutItemValue = (LinearLayout) convertView
						.findViewById(R.id.right_menu_layout_foradd);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			//holder.tvItemName.setText("");
			createView(holder.layoutItemValue,position);
			return convertView;
		}
		public void createView(LinearLayout layout, int position){
			for(int i = 0; i < items_value[position].size(); i++){
				LinearLayout ly = new LinearLayout(getActivity());
				ly.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				TextView tv1 = new TextView(getActivity());
				tv1.setTextColor(color.white);
				tv1.setTextSize(getResources().getDimension(R.dimen.circle_center_text));
				tv1.setText("创建了");
				ly.addView(tv1);
				TextView tv2 = new TextView(getActivity());
				tv2.setTextColor(color.holo_blue_light);
				tv2.setMaxLines(2);
				tv2.setPadding(getResources().getDimensionPixelSize(R.dimen.offset_bottom_text), 0, 0, 0);
				tv2.setTextSize(getResources().getDimension(R.dimen.circle_center_text));
				tv2.setText("创建了创建了创建了创建了创建了创建了创建了创建了创建了创建了");
				ly.addView(tv2);
				layout.addView(ly);
			}
			
		}
		class ViewHolder {
			TextView tvItemName;
			LinearLayout layoutItemValue;

		}
	}
}
