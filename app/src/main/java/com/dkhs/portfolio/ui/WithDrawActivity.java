package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.PayResEvent;
import com.dkhs.portfolio.ui.wallets.WithDrawFragment;
import com.squareup.otto.Subscribe;

/**
 * Created by zjz on 2015/10/9.
 */
public class WithDrawActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.withdraw);
        BusProvider.getInstance().register(this);
        Intent intent = getIntent();
        if(intent != null){
            float avail = intent.getFloatExtra(MyPurseActivity.AVAIL_AMOUNT,0);
            Bundle bundle = new Bundle();
            bundle.putFloat(MyPurseActivity.AVAIL_AMOUNT,avail);
            replaceContentFragment(WithDrawFragment.newInstance(bundle));
        }
    }

    @Subscribe
    public void updateData(PayResEvent event){
        if(event.errCode == 0){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
}