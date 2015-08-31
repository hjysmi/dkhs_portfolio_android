package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.sortlist.CharacterParser;
import com.dkhs.portfolio.ui.widget.sortlist.PinyinComparator;
import com.dkhs.portfolio.ui.widget.sortlist.SideBar;
import com.dkhs.portfolio.ui.widget.sortlist.SortAdapter;
import com.dkhs.portfolio.ui.widget.sortlist.SortModel;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zjz on 2015/8/31.
 */
public class SortFriendFragment extends BaseFragment {
    @ViewInject(R.id.country_lvcountry)
    private ListView sortListView;


    @ViewInject(R.id.sidrbar)

    private SideBar sideBar;
    @ViewInject(R.id.dialog)
    private TextView dialog;
    private SortAdapter adapter;


    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_sort_friend;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }


    private void initViews() {
        sideBar.setTextView(dialog);

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();


        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Toast.makeText(getApplication(), (, Toast.LENGTH_SHORT).show();
                SortModel item = (SortModel) adapter.getItem(position);
                setSelectBack(item.getName());
            }
        });

        SourceDateList = filledData(getResources().getStringArray(R.array.friend_date));

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(getActivity(), SourceDateList);
        sortListView.setAdapter(adapter);
    }


    private void setSelectBack(String username) {
        Intent intent = new Intent();
        intent.putExtra("select_friend", username);
        getActivity().setResult(Activity.RESULT_OK, intent);

        getActivity().finish();
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {

        List<UserEntity> friendList;
        new UserEngineImpl().getFriendList(String.valueOf(UserEngineImpl.getUserEntity().getId()), new ParseHttpListener<MoreDataBean<UserEntity>>() {
            @Override
            protected MoreDataBean<UserEntity> parseDateTask(String jsonData) {

                MoreDataBean<UserEntity> moreBean = (MoreDataBean<UserEntity>) DataParse.parseObjectJson(new TypeToken<MoreDataBean<UserEntity>>() {
                }.getType(), jsonData);

                return moreBean;
            }

            @Override
            protected void afterParseData(MoreDataBean<UserEntity> object) {
                if (null != object) {
                    if (null != object.getResults()) {
                        for (UserEntity friendModel : object.getResults()) {
                            Log.d("GetFriend", " friendModel name:" + friendModel.getUsername());

                        }
                    }

                }
            }
        });


        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }


        //添加最近5个联系人
        for (int i = 0; i < 5; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);

            sortModel.setSortLetters("*");

            mSortList.add(sortModel);
        }

        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

}
