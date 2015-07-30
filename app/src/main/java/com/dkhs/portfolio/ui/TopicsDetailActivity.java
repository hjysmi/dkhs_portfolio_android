package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.fragment.TopicDetailFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.parceler.Parcels;


public class TopicsDetailActivity extends ModelAcitivity {


    public static final int MENU_COMMEND = 0;
    public static final int MENU_STAR = 1;
    public static final int MENU_SHARE = 2;
    public static final int MENU_MORE = 3;


    public static void startActivity(Context context, TopicsBean topicsBean) {

        Intent intent = new Intent(context, TopicsDetailActivity.class);
        intent.putExtra("topicsBean", Parcels.wrap(topicsBean));
        context.startActivity(intent);
    }

    @ViewInject(R.id.floating_action_view)
    private FloatingActionMenu mFloatingActionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideHead();
        setContentView(R.layout.activity_topics_detail);
        setTitle(R.string.title_activity_topics_detail);
        ViewUtils.inject(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new TopicDetailFragment()).commitAllowingStateLoss();
        initData();
    }


    /**
     * iniView initData
     */
    public void initData() {
        mFloatingActionMenu.addItem(MENU_COMMEND, R.string.comment, R.drawable.ic_coment);
        mFloatingActionMenu.addItem(MENU_STAR, R.string.star, R.drawable.ic_star);
        mFloatingActionMenu.addItem(MENU_SHARE, R.string.share, R.drawable.ic_coment);
        mFloatingActionMenu.addItem(MENU_MORE, R.string.more, R.drawable.ic_coment);
        mFloatingActionMenu.setOnMenuItemSelectedListener(new FloatingActionMenu.OnMenuItemSelectedListener() {
            @Override
            public boolean onMenuItemSelected(int paramInt) {
                switch (paramInt) {
                    case MENU_COMMEND:
                        break;
                    case MENU_STAR:
                        break;
                    case MENU_SHARE:
                        break;
                    case MENU_MORE:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * getData from net
     */
    public void getDataForNet() {
    }


}
