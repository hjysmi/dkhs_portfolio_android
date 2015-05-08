package com.dkhs.portfolio.ui.eventbus;

/**
 * Created by zjz on 2015/5/7.
 */
public class UpdateComDescEvent {
    public String comName;
    public String comDesc;

    public UpdateComDescEvent(String name, String desc) {
        this.comDesc = desc;
        this.comName = name;
    }
}
