package com.dkhs.portfolio.ui.messagecenter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.utils.StringFromatUtils;
import com.lidroid.xutils.BitmapUtils;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.UnknownMessageItemProvider;
import io.rong.imlib.model.MessageContent;

import static com.dkhs.portfolio.R.id;
import static com.dkhs.portfolio.R.layout;

/**
 * @author zwm
 * @version 1.0
 * @ClassName MessageProvider
 * @date 2015/5/6.10:45
 * @Description TODO(自定义消息)
 */

@ProviderTag(messageContent = DKImgTextMsg.class, showPortrait = false)
public class DKImgTextMsgProvider extends UnknownMessageItemProvider {


    @Override
    public View newView(Context context, ViewGroup group) {

        return LayoutInflater.from(context).inflate(layout.layout_messaage, null);
    }


    @Override
    public Spannable getContentSummary(MessageContent data) {


        String contentSummary = "";

        if (data instanceof DKImgTextMsg) {
            DKImgTextMsg dkImgTextMsg = (DKImgTextMsg) data;
            if (!TextUtils.isEmpty(dkImgTextMsg.getTitle())) {

//                if()
                contentSummary = dkImgTextMsg.getTitle();
            }
        }
        return new SpannableString(contentSummary);


    }


    //    @Override
//    public Spannable getSummary(Message data) {
//        return super.getSummary(data);
//    }


    //

    @Override
    public void bindView(View v, int position, MessageContent content, UIMessage message) {

        TextView titleTV = (TextView) v.findViewById(id.tv_title);
        TextView contentTV = (TextView) v.findViewById(id.tv_content);
        TextView dateLineTV = (TextView) v.findViewById(id.tv_date_line);
        ImageView imageView = (ImageView) v.findViewById(id.im_content);
        View scaleLayout =  v.findViewById(id.scaleLayout);


        if (content instanceof DKImgTextMsg) {
            DKImgTextMsg dkImgTextMsg = (DKImgTextMsg) content;
            titleTV.setText(dkImgTextMsg.getTitle());
            contentTV.setText(dkImgTextMsg.getContent());
            if (TextUtils.isEmpty(dkImgTextMsg.getImageUri())) {
                scaleLayout.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
            } else {
                scaleLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                BitmapUtils.displayNoEffect(imageView, dkImgTextMsg.getImageUri());
            }
            long timeLineLong = message.getReceivedTime();

            if (timeLineLong <= 0) {
                dateLineTV.setVisibility(View.GONE);
            } else {
                String timeLine = StringFromatUtils.dateFormat(timeLineLong);
                dateLineTV.setVisibility(View.VISIBLE);
                dateLineTV.setText(timeLine);
            }


        }


    }


}
