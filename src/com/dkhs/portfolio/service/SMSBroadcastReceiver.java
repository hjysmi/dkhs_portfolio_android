/**   
 * @Title SMSBroadcastReceiver.java 
 * @Package com.dkhs.portfolio.service 
 * @Description TODO(用一句话描述该文件做什么) 
 * @author zjz  
 * @date 2014-10-31 下午1:55:30 
 * @version V1.0   
 */
package com.dkhs.portfolio.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/** 
 * @ClassName SMSBroadcastReceiver 
 * @Description TODO(这里用一句话描述这个类的作用) 
 * @author zjz 
 * @date 2014-10-31 下午1:55:30 
 * @version 1.0 
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static MessageListener mMessageListener;  
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";  
      
    public SMSBroadcastReceiver() {  
        super();  
    }  
  
    @Override  
    public void onReceive(Context context, Intent intent) {  
            if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {  
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");  
                for(Object pdu:pdus) {  
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte [])pdu);  
                    String sender = smsMessage.getDisplayOriginatingAddress();  
                    //短信内容  
                    String content = smsMessage.getDisplayMessageBody();  
//                    long date = smsMessage.getTimestampMillis();  
//                    Date tiemDate = new Date(date);  
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//                    String time = simpleDateFormat.format(tiemDate);  
                    //过滤不需要读取的短信的发送号码  
                    System.out.println("sender:"+sender);
                    if ("10690506324434".equals(sender)||"10690251381100".equals(sender)) {  
                        System.out.println("content:"+content);
                        mMessageListener.onReceived(content);  
                        abortBroadcast();  
                    }  
                }  
            }  
          
    }  
      
    //回调接口  
    public interface MessageListener {  
        public void onReceived(String message);  
    }  
      
    public void setOnReceivedMessageListener(MessageListener messageListener) {  
        this.mMessageListener = messageListener;  
    }  

}
