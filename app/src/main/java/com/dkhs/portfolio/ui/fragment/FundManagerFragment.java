package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ManagersEntity;
import com.dkhs.portfolio.ui.FundManagerActivity;
import com.dkhs.portfolio.ui.adapter.FundManagerAdapter;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by zjz on 2015/6/8.
 */
public class FundManagerFragment extends BaseFragment {
    public static final String EXTRA_MANAGER_LIST = "extra_manager_list";
    @ViewInject(R.id.lv_manager)
    private ListViewEx lvManger;
    @ViewInject(R.id.tv_empty)
    private TextView tvEmpty;
    private List<ManagersEntity> mListManager;

    public static FundManagerFragment newInstance(List<ManagersEntity> managersEntities) {
        FundManagerFragment fragment = new FundManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MANAGER_LIST, Parcels.wrap(managersEntities));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mListManager = Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_MANAGER_LIST));
        } else {
            Bundle bundle = getArguments();
            mListManager = Parcels.unwrap(bundle.getParcelable(EXTRA_MANAGER_LIST));
        }
        setRetainInstance(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_MANAGER_LIST, Parcels.wrap(mListManager));
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_fund_manager;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != mListManager) {
            tvEmpty.setVisibility(View.GONE);
            FundManagerAdapter mManagerAdapter = new FundManagerAdapter(getActivity(), mListManager);
            lvManger.setAdapter(mManagerAdapter);
            lvManger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(FundManagerActivity.newIntent(getActivity(), mListManager.get(position).getId() + ""));

                }
            });
        } else {

            tvEmpty.setVisibility(View.VISIBLE);
        }


    }
}
