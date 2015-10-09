package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.wallets.RechargeFragment;

/**
 * Created by zjz on 2015/10/9.
 */
public class RechargeActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_recharge);
        replaceContentFragment(new RechargeFragment());
    }
}
