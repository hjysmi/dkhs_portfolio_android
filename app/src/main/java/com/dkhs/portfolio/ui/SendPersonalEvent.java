package com.dkhs.portfolio.ui;

/**
 * Created by xuetong on 2015/12/15.
 */
public class SendPersonalEvent {
    private boolean success;

    public SendPersonalEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /* String msg;

    public SendPersonalEvent(String msg) {
        this.msg = msg;
    }*/
}
