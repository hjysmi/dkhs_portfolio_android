package com.dkhs.portfolio.ui.selectfriend.store;

import com.dkhs.portfolio.ui.eventbus.Dispatcher;
import com.dkhs.portfolio.ui.selectfriend.actions.EventAction;


/**
 * Created by lgvalle on 02/08/15.
 */
public abstract class Store {

    final Dispatcher dispatcher;

    protected Store(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    void postStoreChange(String type) {
        dispatcher.postChange(changeEvent(type));
    }

    abstract StoreChangeEvent changeEvent(String type);

    public abstract void onAction(EventAction action);

    public interface StoreChangeEvent {
    }
}
