/**
 * @Title MyCombinationFragmnet.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-30 上午9:00:48
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.DraftBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.DraftEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.eventbus.LoadDraftEvent;
import com.dkhs.portfolio.ui.eventbus.MainThreadBus;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MyCombinationFragmnet
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-3-30 上午9:00:48
 */
public class MyDraftFragmnet extends VisiableLoadFragment implements ILoadDataBackListener {

    private MyDraftAdapter mAdapter;
    private List<DraftBean> mDataList = new ArrayList<DraftBean>();
    private DraftEngine dataEngine;
    private static final String TAG = "MyDraftFragmnet";
    public TextView tvEmptyText;

    private MainThreadBus eventBus;

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_my_draft;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = new MainThreadBus();
        dataEngine = new DraftEngine(eventBus);

//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    creatTestDraftData();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    private void creatTestDraftData() throws DbException {
        List<DraftBean> list = new ArrayList<>();
        String authorId = String.valueOf(GlobalParams.LOGIN_USER.getId());
        Log.d(TAG, "authorId:" + authorId);
        for (int i = 0; i < 30; i++) {
            DraftBean bean = new DraftBean();
            bean.setAuthorId(authorId);
            bean.setContent(i + " 的法律上的家乐福事件的佛上的浪费jam率魔法攻击的交流伺服啊上的飞机上的飞机的方式金克拉大煞风景");
            if (i % 3 == 0) {
                bean.setTitle(i + "标题很厉害的粉红色的活佛是");
            }
            bean.setLabel(1);
            if (i % 4 == 0) {
                bean.setLabel(2);
            }
            bean.setUtcTime(TimeUtils.getUTCdatetimeAsString());
            list.add(bean);
        }
        DbUtils.create(getActivity()).saveAll(list);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEmptyText = (TextView) view.findViewById(android.R.id.empty);
        SwipeMenuListView mListView = (SwipeMenuListView) view.findViewById(R.id.swipemenu_listView);
        mAdapter = new MyDraftAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(tvEmptyText);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(MyDraftFragmnet.this.getActivity());
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.tag_red)));
                deleteItem.setWidth(UIUtils.dp2px(90));
                deleteItem.setTitle(R.string.delete);
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        // open(item);
                        delCombination(position);
                        // PromptManager.showToast("删除");
                        break;
                    case 1:
                        // delete
                        // delete(item);
                        // mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });


        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PromptManager.showToast("查看草稿详情");
//                startActivity(CombinationDetailActivity.newIntent(getActivity(), mDataList.get(position)));

            }
        });
    }

    @Override
    public void requestData() {

    }

    private void delCombination(int position) {
        final DraftBean item = mDataList.get(position);
        mDataList.remove(item);
        mAdapter.notifyDataSetChanged();
        dataEngine.delDraft(item);
    }

    @Subscribe
    public void onEventMainThread(LoadDraftEvent event) {
        Log.d(TAG, "onEventMainThread LoadDraftEvent");
        if (null != event && null != event.dataList) {
            this.mDataList.clear();
            this.mDataList.addAll(event.dataList);
            mAdapter.notifyDataSetChanged();
        }
    }


    class MyDraftAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public DraftBean getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.adapter_my_draft, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();

            final DraftBean item = mDataList.get(position);
            String title = item.getTitle();
            if (TextUtils.isEmpty(title)) {
                holder.tvTitle.setVisibility(View.GONE);
            } else {
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.tvTitle.setText(title);
            }
            holder.tvEditTime.setText(TimeUtils.getBriefTimeString(item.getUtcTime()));
            String strLabel = item.getLabel() == 1 ? "主贴" : "回复";
            holder.tvLabel.setText(strLabel);

            String strContent = item.getContent();

            if (TextUtils.isEmpty(strContent)) {
                holder.tvContent.setVisibility(View.GONE);
            } else {
                holder.tvContent.setVisibility(View.VISIBLE);
                if (strContent.length() > 99) {
                    holder.tvContent.setText(strContent.substring(0, 99) + "……");
                } else {
                    holder.tvContent.setText(strContent);
                }
            }


            return convertView;
        }

        class ViewHolder {
            public TextView tvTitle;
            public TextView tvContent;
            public TextView tvEditTime;
            public TextView tvLabel;


            public ViewHolder(View row) {
                tvTitle = (TextView) row.findViewById(R.id.tv_draft_title);
                tvContent = (TextView) row.findViewById(R.id.tv_draft_content);
                tvEditTime = (TextView) row.findViewById(R.id.tv_edit_time);
                tvLabel = (TextView) row.findViewById(R.id.tv_label);
                row.setTag(this);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        eventBus.register(this);

        dataEngine.getDraftByUserId();

    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    /**
     * @param object
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadFinish(MoreDataBean object) {
        if (null != object.getResults()) {
            mDataList.clear();
            mDataList.addAll(object.getResults());

            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadFail() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
