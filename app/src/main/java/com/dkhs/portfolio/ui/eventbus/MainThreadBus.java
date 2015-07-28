package com.dkhs.portfolio.ui.eventbus;

import android.os.Looper;

import com.dkhs.portfolio.common.WeakHandler;
import com.squareup.otto.Bus;

/**
 * Created by zjz on 2015/7/27.
 */
public class MainThreadBus extends Bus {
    private final WeakHandler mHandler = new WeakHandler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }
}
