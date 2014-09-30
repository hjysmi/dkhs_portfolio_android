package com.dkhs.portfolio.engine;

import android.os.Handler;
import android.os.Message;

public abstract class BaseEngine {
    /**
     * @param startTime开始时间,当网络连接时间少于两秒时,可用于延迟发送消息
     * @param handler
     * @param what
     * what标示哪个方法
     * @param content
     * obj用于传递数据
     */
    protected void sendMsg(final long startTime, final Handler handler, final int what, final Object content) {
        if (handler == null)
            return;
        if (System.currentTimeMillis() - startTime < 2000) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(2000 - (System.currentTimeMillis() - startTime));
                        sendMsg(handler, what, content);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }.start();
        }
    }

    protected void sendMsg(long startTime, Handler handler, int what) {
        sendMsg(startTime, handler, what, null);

    }

    /**
     * 
     * @param handler
     * @param what
     * what标示哪个方法
     * @param content
     * obj用于传递数据
     */
    protected void sendMsg(Handler handler, int what, Object content) {
        if (handler == null)
            return;
        boolean is = handler.hasMessages(what);
        if (is) {
            handler.removeMessages(what);
            ;
        }
        Message message = new Message();
        message.what = what;
        message.obj = content;
        handler.sendMessage(message);
    }

    protected void sendMsg(Handler handler, int what) {
        sendMsg(handler, what, null);
    }
}
