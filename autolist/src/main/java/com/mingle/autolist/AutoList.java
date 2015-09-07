package com.mingle.autolist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;

import com.mingle.utils.BusProvider;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AutoList<T extends Object> extends ArrayList<T> implements DataObserver {

    public static final String Tag = "AutoList";
    private BaseAdapter mAdapter;
    private RecyclerView.Adapter mRvAdapter;
    private ActionHandler mActionHandler;

    private List<Class> actionFs = new ArrayList<>();

    public AutoList(int capacity, Class actionF) {
        super(capacity);
        actionFs.add(actionF);
    }
    public AutoList( ) {
        super();
    }

    public AutoList(Class actionF) {
        actionFs.add(actionF);
    }

    public AutoList(Collection<? extends T> collection, Class actionF) {
        super(collection);
        actionFs.add(actionF);
    }

    private void register() {

        Log.d(Tag, "register  Bus");
        BusProvider.getInstance().register(this);
    }

    private void unRegister() {
        Log.d(Tag, "unRegister Bus");
        BusProvider.getInstance().unregister(this);
    }

    public void setup(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().add(new DataFragment(),
                this.toString()).commitAllowingStateLoss();
    }

    public void setup(FragmentActivity activity) {

        setup(activity.getSupportFragmentManager());
    }


    public void setup(Fragment fragment) {

        if (fragment instanceof DataObserverEnable) {
            ((DataObserverEnable) fragment).addDataObserver(this);

        } else {
            Log.e(Tag, " your Fragment must implement DataObserverEnable");
        }
    }


    public AutoList applyAction(Class actionF) {
        actionFs.add(actionF);
        return this;
    }

    @Subscribe
    public void action(T o) {


        boolean response = false;
        for (Class item : actionFs) {
            if (item.isInstance(o)) {
                response = true;
                break;
            }
        }
        if (!response) {
            return;
        }


        if (o instanceof AutoData) {
            if (TextUtils.isEmpty(((AutoData) o).getIdentifies())) {
                return;
            }
            AutoData autoData = (AutoData) o;

            boolean handled = false;
            if (mActionHandler != null) {
                handled = mActionHandler.beforeHandleAction(autoData);
            }

            if (!handled) {
                switch (autoData.action) {
                    case Add:
                        addT(0, autoData);
                        break;
                    case Delete:
                        deleteT(autoData);
                        break;
                    case Update:
                        updateT(autoData);
                        break;
                    case Custom:
                        break;
                }
            }
            if (mActionHandler != null) {
                mActionHandler.afterHandleAction(autoData);
            }

            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
            if (mRvAdapter != null) {
                mRvAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRegister() {
        register();
    }

    @Override
    public void onUnRegister() {
        unRegister();

    }


    public interface ActionHandler<T extends AutoData> {
        boolean beforeHandleAction(T a);

        void afterHandleAction(T a);


    }

    public void setActionHandler(ActionHandler<AutoData> actionHandler) {

        mActionHandler = actionHandler;
    }


    public void updateT(AutoData o) {


        int index = indexOf(o);
        if (index != -1) {
            this.remove(index);
            this.add(index, (T) o);
        }


    }

    public void deleteT(AutoData o) {

        int index = indexOf(o);
        if (index != -1) {
            this.remove(index);
        }

    }

    public void addT(int addIndex, AutoData o) {

        if (!contains(o)) {
            this.add(addIndex, (T) o);
        }

    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
    }

    public void setRVAdapter(RecyclerView.Adapter rvAdapter) {
        mRvAdapter = rvAdapter;
    }


    @SuppressLint("ValidFragment")
    class DataFragment extends Fragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            register();

        }

        @Override
        public void onDestroy() {
            unRegister();
            super.onDestroy();
        }

    }
}
