package com.dkhs.portfolio.ui.fragment;

import java.lang.reflect.Type;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.OptionalStockEngineImpl;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.FundsOrderActivity;
import com.dkhs.portfolio.ui.MarketCenterActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.OptionMarketNewsActivity;
import com.dkhs.portfolio.ui.OptionalStockListActivity;
import com.dkhs.portfolio.ui.SettingActivity;
import com.dkhs.portfolio.ui.YanBaoActivity;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;

public class MenuLeftFragment extends Fragment implements OnClickListener {
    private String[] items;
    private String[] icon_items;
    BaseAdapter itemAdapter;
    private RelativeLayout menuSetting;
    private TextView tvCombin;
    private TextView tvStock;
    private TextView tvUserName;
    LoadSelectDataEngine mLoadDataEngine;
    private ImageView ivUserheader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = getResources().getStringArray(R.array.left_menu_img_items);
        icon_items = getResources().getStringArray(R.array.left_menu_img);
        itemAdapter = new MenuItemAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_left, null);
        menuSetting = (RelativeLayout) view.findViewById(R.id.menu_setting);
        menuSetting.setOnClickListener(this);
        initView(view);
        loadCombinationData();
        /*
         * mLoadDataEngine = new FundDataEngine(mSelectStockBackListener, FundDataEngine.TYPE_MAININDEX);
         * mLoadDataEngine.loadData();
         */
        return view;
    }

    private void initView(View view) {
        tvCombin = (TextView) view.findViewById(R.id.tv_combin);
        // tvCombin.setText(getString(R.string.combin, 3));
        tvStock = (TextView) view.findViewById(R.id.tv_stock);
        tvUserName = (TextView) view.findViewById(R.id.tv_username);
        ivUserheader = (ImageView) view.findViewById(R.id.iv_userheader);
        tvUserName.setText(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME));
        // tvStock.setText(getString(R.string.optional_stock_format, 12));
        // view.findViewById(R.id.btn_setting).setOnClickListener(this);
        ListView lvItem = (ListView) view.findViewById(R.id.menu_list);
        lvItem.setAdapter(itemAdapter);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_setting: {
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
                convertView = View.inflate(getActivity(), R.layout.left_menu_item, null);
                holder = new ViewHolder();
                holder.tvItemName = (TextView) convertView.findViewById(R.id.left_menu_item_text);
                holder.ivItemIcon = (ImageView) convertView.findViewById(R.id.left_menu_item_img);
                holder.layoutLine = (LinearLayout) convertView.findViewById(R.id.left_menu_layout_line);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 1 || position == 3) {
                holder.layoutLine.setVisibility(View.VISIBLE);
            }
            holder.tvItemName.setText(items[position]);
            int id = getResources().getIdentifier(getActivity().getPackageName() + ":drawable/" + icon_items[position],
                    null, null);
            holder.ivItemIcon.setImageResource(id);
            convertView.setOnClickListener(new OnMyItemListener(position));
            return convertView;
        }

        class ViewHolder {
            TextView tvItemName;
            ImageView ivItemIcon;
            LinearLayout layoutLine;
        }
    }

    /**
     * 选项监听
     * 用于跳转至各个界面
     * 
     * @author weiting
     * 
     */
    class OnMyItemListener implements OnClickListener {
        int position;

        public OnMyItemListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = null;
            switch (position) {
                case 0:
                    intent = new Intent(getActivity(), MyCombinationActivity.class);
                    break;
                case 1:
                    intent = new Intent(getActivity(), FundsOrderActivity.class);
                    break;
                case 2:
                    intent = new Intent(getActivity(), OptionalStockListActivity.class);

                    break;
                case 3:
                    intent = new Intent(getActivity(), MarketCenterActivity.class);
                    break;
                case 4:
                    intent = new Intent(getActivity(), OptionMarketNewsActivity.class);
                    break;
                case 5:
                    intent = new Intent(getActivity(), YanBaoActivity.class);
                    break;
                case 6:

                    break;
                case 7:

                    break;
                default:
                    break;
            }
            if (null != intent) {
                startActivity(intent);
            }
        }

    }

    private void loadCombinationData() {
        new MyCombinationEngineImpl().getCombinationList(new ParseHttpListener<List<CombinationBean>>() {

            @Override
            protected List<CombinationBean> parseDateTask(String jsonData) {
                Type listType = new TypeToken<List<CombinationBean>>() {
                }.getType();
                List<CombinationBean> combinationList = DataParse.parseJsonList(jsonData, listType);

                return combinationList;
            }

            @Override
            protected void afterParseData(List<CombinationBean> dataList) {
                tvCombin.setText(dataList.size() + "");
                String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
                if (!TextUtils.isEmpty(url)) {
                    url = DKHSUrl.BASE_DEV_URL + url;
                    BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
                    bitmapUtils.display(ivUserheader, url);
                    //b = UIUtils.toRoundBitmap(b);
                    //ivUserheader.setImageBitmap(b);
                }else{
        	        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.ic_user_head);
        	        b = UIUtils.toRoundBitmap(b);
        	        ivUserheader.setImageBitmap(b);
                }
            }

        });
        new OptionalStockEngineImpl(mSelectStockBackListener).loadData();
    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            tvStock.setText(dataList.size() + "");

        }

    };
}
