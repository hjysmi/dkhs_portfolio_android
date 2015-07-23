package com.dkhs.portfolio.net;

/**
 * Created by zjz on 2015/7/22.
 */
public abstract class SecurityParseHttpLister extends ParseHttpListener {


    public SecurityParseHttpLister() {
        this.isEncry = true;
    }

//    @Override
//    protected Object parseDateTask(String jsonData) {
//        try {
//            jsonData = new SecurityUtils().encryptResponeJsonData(jsonData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (listener instanceof BasicHttpListener) {
//            listener.onSuccess(jsonData);
//        } else if (listener instanceof ParseHttpListener) {
//            listener.onSuccess(jsonData);
//        }
//        return null;
//    }
//
//    @Override
//    protected void afterParseData(Object object) {
//
//    }

}
