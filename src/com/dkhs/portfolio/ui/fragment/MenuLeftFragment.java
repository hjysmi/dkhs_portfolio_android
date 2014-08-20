package com.dkhs.portfolio.ui.fragment;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.SettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuLeftFragment extends Fragment implements OnClickListener {
	private String[] items;
	private String[] icon_items;
	BaseAdapter itemAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		items = getResources().getStringArray(R.array.menu_items);
		icon_items = getResources().getStringArray(R.array.menu_img_items);
		itemAdapter = new MenuItemAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu_left, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		TextView tvCombin = (TextView) view.findViewById(R.id.tv_combin);
		tvCombin.setText(getString(R.string.combin, 3));
		TextView tvStock = (TextView) view.findViewById(R.id.tv_stock);
		tvStock.setText(getString(R.string.stock, 12));
		view.findViewById(R.id.btn_setting).setOnClickListener(this);
		ListView lvItem = (ListView) view.findViewById(R.id.menu_list);
		lvItem.setAdapter(itemAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_setting: {
			// Toast.makeText(getActivity(), "点击设置按钮",
			// Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getActivity(), SettingActivity.class);
			getActivity().startActivity(intent);
		}
			break;

		default:
			break;
		}
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
						.findViewById(R.id.item_name);
				holder.ivItemIcon = (ImageView) convertView
						.findViewById(R.id.item_icon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvItemName.setText(items[position]);
			int id = getResources().getIdentifier(
					getActivity().getPackageName() + ":drawable/"
							+ icon_items[position], null, null);
			holder.ivItemIcon.setImageResource(id);

			return convertView;
		}

		class ViewHolder {
			TextView tvItemName;
			ImageView ivItemIcon;

		}
	}

}
