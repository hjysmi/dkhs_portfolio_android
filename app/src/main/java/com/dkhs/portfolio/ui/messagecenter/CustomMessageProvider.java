package com.dkhs.portfolio.ui.messagecenter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.widget.provider.RichContentMessageItemProvider;
import io.rong.imkit.widget.provider.UnknownMessageItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.RichContentMessage;

import static com.dkhs.portfolio.R.id;
import static com.dkhs.portfolio.R.layout;

/**
 * @author zwm
 * @version 1.0
 * @ClassName MessageProvider
 * @date 2015/5/6.10:45
 * @Description TODO(自定义消息)
 */

@ProviderTag(messageContent = CustomMessage.class,showPortrait=false)
public class CustomMessageProvider extends UnknownMessageItemProvider {

    @Override
    public View newView(Context context, ViewGroup group) {
        return   LayoutInflater.from(context).inflate(layout.layout_messaage   , null);
    }



    @Override
    public Spannable getContentSummary(MessageContent data) {


        String contentSummary="";

        if(data instanceof  CustomMessage){
            CustomMessage customMessage=(CustomMessage)data;
            contentSummary=customMessage.getTitle();

        }
        return new SpannableString(contentSummary);

    }

//    @Override
//    public Spannable getSummary(Message data) {
//        return super.getSummary(data);
//    }


    //
    @Override
    public void bindView(View v, int position, MessageContent content, Message message) {

        TextView titleTV= (TextView) v.findViewById(id.tv_title);
        TextView contentTV= (TextView) v.findViewById(id.tv_content);


        if(content instanceof  CustomMessage){
            CustomMessage customMessage=(CustomMessage)content;
            titleTV.setText(customMessage.getTitle());
            contentTV.setText(customMessage.getContent());
        }


    }



}
