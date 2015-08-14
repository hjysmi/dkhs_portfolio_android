/**
 * @Title MyCombinationFragmnet.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-30 上午9:00:48
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.DraftBean;
import com.dkhs.portfolio.engine.DraftEngine;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.ui.eventbus.LoadDraftEvent;
import com.dkhs.portfolio.ui.eventbus.MainThreadBus;
import com.dkhs.portfolio.ui.widget.DKHSTextView;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MyCombinationFragmnet
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-3-30 上午9:00:48
 */
public class MyDraftFragmnet extends VisiableLoadFragment {

    private MyDraftAdapter mAdapter;
    private List<DraftBean> mDataList = new ArrayList<DraftBean>();
    private DraftEngine dataEngine;
    private static final String TAG = "MyDraftFragmnet";
    public TextView tvEmptyText;
    public View loadingView;

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

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEmptyText = (TextView) view.findViewById(android.R.id.empty);

        loadingView = view.findViewById(android.R.id.progress);
        SwipeMenuListView mListView = (SwipeMenuListView) view.findViewById(R.id.swipemenu_listView);
        mAdapter = new MyDraftAdapter();
        mListView.setAdapter(mAdapter);

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
                DraftBean draftBean = mDataList.get(position);
                if (UIUtils.iStartLoginActivity(getActivity())) {
                    return;
                }
                Intent intent = PostTopicActivity.getIntent(getActivity(), draftBean.getLabel(), draftBean.getStatusId(), draftBean.getReplyUserName());
                intent.putExtra(PostTopicActivity.ARGUMENT_DRAFT, Parcels.wrap(draftBean));
                startActivity(intent);

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
        if (mDataList.isEmpty()) {
            tvEmptyText.setVisibility(View.VISIBLE);
        } else {
            tvEmptyText.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEventMainThread(LoadDraftEvent event) {
        Log.d(TAG, "onEventMainThread LoadDraftEvent");
        loadingView.setVisibility(View.GONE);
        if (null != event) {
            this.mDataList.clear();
            if (null != event.dataList) {
                this.mDataList.addAll(event.dataList);
            }
            if (this.mDataList.isEmpty()) {
                tvEmptyText.setVisibility(View.VISIBLE);
            } else {
                tvEmptyText.setVisibility(View.GONE);
            }
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
//            holder.tvEditTime.setText(TimeUtils.getBriefTimeString(item.getUtcTime()));
            holder.tvEditTime.setText(TimeUtils.getBriefTimeString(item.getEdittime()));
            String strLabel = item.getLabel() == 1 ? "主贴" : "回复";
            holder.tvLabel.setText(strLabel);

            String strContent = item.getContent();


            if (!TextUtils.isEmpty(item.getImageUri())) {
                ImageLoaderUtils.setImage(item.getImageUri(), holder.ivImage);
                holder.ivImage.setVisibility(View.VISIBLE);
            } else {
                holder.ivImage.setVisibility(View.GONE);

            }

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

            String strFailReason = item.getFailReason();
            if (TextUtils.isEmpty(strFailReason)) {
                holder.tvFailReason.setVisibility(View.GONE);
            } else {
                holder.tvFailReason.setVisibility(View.VISIBLE);
                holder.tvFailReason.setText(strFailReason);
            }

            return convertView;
        }

        class ViewHolder {
            public DKHSTextView tvTitle;
            public DKHSTextView tvContent;
            public TextView tvEditTime;
            public TextView tvLabel;
            public TextView tvFailReason;
            public ImageView ivImage;


            public ViewHolder(View row) {
                tvTitle = (DKHSTextView) row.findViewById(R.id.tv_draft_title);
                tvContent = (DKHSTextView) row.findViewById(R.id.tv_draft_content);
                tvEditTime = (TextView) row.findViewById(R.id.tv_edit_time);
                tvLabel = (TextView) row.findViewById(R.id.tv_label);
                tvFailReason = (TextView) row.findViewById(R.id.tv_fail_reason);
                ivImage = (ImageView) row.findViewById(R.id.iv_image);
                row.setTag(this);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        eventBus.register(this);

        dataEngine.getDraftByUserId();
        loadingView.setVisibility(View.VISIBLE);

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


}
