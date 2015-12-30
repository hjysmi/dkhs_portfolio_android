package com.dkhs.portfolio.ui.messagecenter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.RichContentMessageItemProvider;
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

@ProviderTag(messageContent = RichContentMessage.class,showPortrait=false)
public class MessageProvider extends RichContentMessageItemProvider {

    @Override
    public View newView(Context context, ViewGroup group) {
        return   LayoutInflater.from(context).inflate(layout.layout_messaage   , null);
    }

    @Override
    public Spannable getContentSummary(RichContentMessage data) {

        return new SpannableString(data.getTitle());
    }


    @Override
    public void bindView(View v, int position, RichContentMessage content, UIMessage message) {

        TextView titleTV= (TextView) v.findViewById(R.id.tv_title);
        TextView contentTV= (TextView) v.findViewById(id.tv_content);
        titleTV.setText(content.getTitle());
        contentTV.setText(content.getContent());


    }
}
