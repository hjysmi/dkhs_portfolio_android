package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.PayResEvent;
import com.dkhs.portfolio.ui.wallets.RechargeFragment;
import com.squareup.otto.Subscribe;

/**
 * Created by zjz on 2015/10/9.
 */
public class RechargeActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_recharge);
        BusProvider.getInstance().register(this);
        float chargeAmount = getIntent().getFloatExtra(RechargeFragment.CHARGE_AMOUNT,0);
        replaceContentFragment(RechargeFragment.newInstance(chargeAmount));
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
