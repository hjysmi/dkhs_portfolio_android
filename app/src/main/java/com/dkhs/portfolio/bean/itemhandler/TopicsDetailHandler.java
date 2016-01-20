package com.dkhs.portfolio.bean.itemhandler;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.common.Spanny;
import com.dkhs.portfolio.engine.TopicsCommendEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.adapter.SpecialFundAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TopicsDetailRefreshEvent;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.portfolio.utils.WaterMarkUtil;
import com.mingle.bean.PhotoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class TopicsDetailHandler extends SimpleItemHandler<TopicsBean> implements AdapterView.OnItemSelectedListener {


    private Context mContext;
    private TopicsCommendEngineImpl.SortType mSortType;
    private CommentBean mCommentBean;

    public TopicsDetailHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_topics_detail;
    }

    public void setCommentBean(CommentBean commentBean){
        mCommentBean = commentBean;
    }


    @Override
    public void onBindView(final ViewHolder vh, final TopicsBean data, int position) {
        setClickListener(vh.get(R.id.iv_avatar), data);
//        setClickListener(vh.get(R.id.iv), data);

        setClickListener(vh.get(R.id.name), data);
        if (TextUtils.isEmpty(data.title) ) {
            vh.get(R.id.titleTV).setVisibility(View.GONE);
        } else {
            vh.get(R.id.titleTV).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.titleTV, data.title);
        }

        PeopleBean user = data.user;
        if (null != user) {
            vh.setTextView(R.id.name, user.getUsername());
        }
        if(TextUtils.isEmpty(data.text)){
            vh.get(R.id.content).setVisibility(View.GONE);
        }else {
            vh.get(R.id.content).setVisibility(View.VISIBLE);
        }
        vh.setTextView(R.id.content, data.text);
//        vh.get(R.id.iv).setVisibility(View.GONE);


        TopicsImageViewHandler topicsImageViewHandler;
        if(vh.get(R.id.content).getTag()!= null && vh.get(R.id.content).getTag() instanceof TopicsImageViewHandler){
            topicsImageViewHandler= (TopicsImageViewHandler) vh.get(R.id.content).getTag();
        }else {
            topicsImageViewHandler=new TopicsImageViewHandler();
            vh.get(R.id.content).setTag(topicsImageViewHandler);
        }
        topicsImageViewHandler.handleMedias(vh, data, true);

        if (data.state == -1) {
            vh.setTextView(R.id.tv_empty, mContext.getString(R.string.topics_already_delete));
            vh.get(R.id.main_ll).setVisibility(View.GONE);
            vh.get(R.id.emptyRl).setVisibility(View.VISIBLE);
        } else {
            vh.get(R.id.main_ll).setVisibility(View.VISIBLE);
            vh.get(R.id.emptyRl).setVisibility(View.GONE);
        }
        /**
         *  CONTENT_TYPE = (
         (0, '话题'),
         (10, '新闻'),
         (20, '公告'),
         (30, '研报'),
         )
         */
        if (data.content_type != TopicsDetailActivity.TYPE_REWARD && data.content_type != TopicsDetailActivity.TYPE_TOPIC  && data.content_type != TopicsDetailActivity.TYPE_SPECIAL) {
            setRelatedSymbols(vh.getTextView(R.id.relatedSymbolsTV), data.symbols);
            vh.setTextView(R.id.tv_time, TimeUtils.getBriefTimeString(data.publish_at) + getFromOrigin(data.source));
            if (user != null && !TextUtils.isEmpty(user.getAvatar_md())) {
                ImageLoaderUtils.setHeanderImage(user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
            } else {
                vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_user_head);
            }
//            switch (data.content_type) {
//                case 10:
//                    if (user != null && !TextUtils.isEmpty(user.getAvatar_md())) {
//                        ImageLoaderUtils.setHeanderImage(user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
//                    } else {
//                        vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_announcement);
//                    }
//                    break;
//                case 20:
//                    // FIXME: 2015/8/12 新闻图标暂缺
//                    if (user != null && !TextUtils.isEmpty(user.getAvatar_md())) {
//                        ImageLoaderUtils.setHeanderImage(user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
//                    } else {
//                        vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_announcement);
//                    }
//                    break;
//                case 30:
//                    if (user != null && !TextUtils.isEmpty(user.getAvatar_md())) {
//                        ImageLoaderUtils.setHeanderImage(user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
//                    } else {
//                        vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_user_head);
//                    }
//                    break;
//            }
        } else {
            vh.getTextView(R.id.relatedSymbolsTV).setVisibility(View.GONE);
            if(data.content_type == TopicsDetailActivity.TYPE_SPECIAL){
                vh.get(R.id.rl_header).setVisibility(View.GONE);
                vh.get(R.id.titleTV).setVisibility(View.GONE);
                vh.get(R.id.top_divider).setVisibility(View.VISIBLE);
                vh.get(R.id.content_divider).setVisibility(View.VISIBLE);
            }else{
                vh.get(R.id.top_divider).setVisibility(View.GONE);
                vh.get(R.id.content_divider).setVisibility(View.GONE);
                vh.setTextView(R.id.tv_time, TimeUtils.getBriefTimeString(data.created_at));
                if (user != null && !TextUtils.isEmpty(user.getAvatar_md())) {
                    ImageLoaderUtils.setHeanderImage(user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
                } else {
                    vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_user_head);
                }
            }

            WaterMarkUtil.calWaterMarkImage(vh.getImageView(R.id.iv_water_mark), user.verified, user.verified_type);
        }

        if(data.content_type == 40){
            vh.get(R.id.layout_reward_status).setVisibility(View.VISIBLE);
            vh.getTextView(R.id.tv_reward_state).setVisibility(View.VISIBLE);
            showRewardState(vh, data);
        }else{
            vh.get(R.id.layout_reward_status).setVisibility(View.GONE);
            vh.getTextView(R.id.tv_reward_state).setVisibility(View.GONE);
            if (data.content_type == 50 && data.symbols != null && data.symbols.size() > 0){
                //专题理财，需要显示基金
                ListView lv_funds = vh.get(R.id.lv_funds);
                SpecialFundAdapter adapter = new SpecialFundAdapter(mContext);
                lv_funds.setAdapter(new DKBaseAdapter(mContext, data.symbols).buildSingleItemView(adapter));
            }
        }
    }

    private void showRewardState(ViewHolder vh, TopicsBean data) {
        TextView stateTv = vh.getTextView(R.id.tv_reward_state);
        TextView amountTv = vh.getTextView(R.id.tv_reward_amount);
        TextView amountUnit = vh.getTextView(R.id.tv_reward_amount_unit);
        ImageView moneyIv = vh.getImageView(R.id.iv_money);
        int state;
        int amountStyle;
        int unitStyle;
        int leftDrawable;
        if(data.reward_state == 0){
            state = R.string.reward_on_going;
            amountStyle = R.style.reward_amount_on_going;
            unitStyle = R.style.reward_unit_on_going;
            leftDrawable = R.drawable.ic_money_highlight;
            stateTv.setBackgroundResource(R.drawable.bg_reward_on_going);
        }else if(data.reward_state == 1){
            state = R.string.reward_close;
            amountStyle = R.style.reward_amount_finish;
            unitStyle = R.style.reward_unit_finish;
            leftDrawable = R.drawable.ic_money_normal;
            stateTv.setBackgroundResource(R.drawable.bg_reward_finish);
        }else{
            state =  R.string.reward_finish;
            amountStyle = R.style.reward_amount_finish;
            unitStyle = R.style.reward_unit_finish;
            leftDrawable = R.drawable.ic_money_normal;
            stateTv.setBackgroundResource(R.drawable.bg_reward_finish);
        }
        stateTv.setText(state);
        amountTv.setTextAppearance(mContext, amountStyle);
        moneyIv.setImageResource(leftDrawable);
        amountUnit.setTextAppearance(mContext, unitStyle);
        amountTv.setText(data.reward_amount);
        amountUnit.setText("元");
    }


    private void setRelatedSymbols(TextView textView, List<FundQuoteBean> symbols) {

        if (symbols != null && symbols.size() > 0) {
            Spanny spany = new Spanny();
            spany.append("相关股票: ", new ForegroundColorSpan(mContext.getResources().getColor(R.color.tag_gray)));

            int size = symbols.size()>10?10:symbols.size();
            for (int i = 0; i < size; i++) {
                FundQuoteBean item = symbols.get(i);
                int star = spany.length();
                spany.append(" " + item.getAbbrName() + " ", new SymbolsClickSpan(item));
//                spany.setSpan(new RoundSpan(mContext), star, spany.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(spany);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }


    }

    private String getFromOrigin(TopicsBean.SourceBean source) {

        String origin = "";
        if (source != null) {
            origin = origin.concat(" 来自:").concat(source.title);
        }
        return origin;
    }

    public void setClickListener(View view, TopicsBean data) {
        ItemHandlerClickListenerImp<TopicsBean> itemHandlerClickListener = null;
        if (null != view.getTag() && view.getTag() instanceof ItemHandlerClickListenerImp) {
            itemHandlerClickListener = (ItemHandlerClickListenerImp<TopicsBean>) view.getTag();
        } else {
            switch (view.getId()) {
                case R.id.fl_star:
                    itemHandlerClickListener = new StarClickListenerImp();
                    break;
                case R.id.fl_commend:
                    itemHandlerClickListener = new CommendClickListenerImp();
                    break;
                case R.id.iv_avatar:
                case R.id.name:
                    itemHandlerClickListener = new AvatarClickListenerImp();
                    break;
                case R.id.iv:
                    itemHandlerClickListener = new ImageViewClickListenerImp();
                    break;
                case R.id.main_ll:
                    itemHandlerClickListener = new ItemClickListenerImp();
                    break;
                default:
                    itemHandlerClickListener = new ItemHandlerClickListenerImp<TopicsBean>();
                    break;
            }
            view.setOnClickListener(itemHandlerClickListener);
            view.setTag(itemHandlerClickListener);
        }

        itemHandlerClickListener.setDate(data);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        postRefreshEvent(position);
    }

    private void postRefreshEvent(int position) {
        TopicsDetailRefreshEvent topicsDetailRefreshEvent = new TopicsDetailRefreshEvent();
        switch (position) {
            case 0:
                topicsDetailRefreshEvent.sortType = TopicsCommendEngineImpl.SortType.latest;
                break;
            case 1:
                topicsDetailRefreshEvent.sortType = TopicsCommendEngineImpl.SortType.best;
                break;
            case 2:
                topicsDetailRefreshEvent.sortType = TopicsCommendEngineImpl.SortType.earliest;
                break;
        }

        if (mSortType != topicsDetailRefreshEvent.sortType) {
            mSortType = topicsDetailRefreshEvent.sortType;
            BusProvider.getInstance().post(topicsDetailRefreshEvent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class StarClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {
        private TopicsBean topicsBean;

        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            ImageView imageView = (ImageView) v.findViewById(R.id.iv_like);
            if (topicsBean.like) {
                imageView.setImageResource(R.drawable.praised);
            } else {
                imageView.setImageResource(R.drawable.praise);
            }
            topicsBean.like = !topicsBean.like;
        }
    }

    class CommendClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {


        private TopicsBean topicsBean;


        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {

            if (UIUtils.iStartLoginActivity(mContext)) {
                return;
            }
            UIUtils.startAnimationActivity((Activity) mContext, (PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_COMMENT_TOPIC, topicsBean.id + "", topicsBean.user.getUsername())));
        }
    }

    class AvatarClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {
        private TopicsBean topicsBean;

        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            if (topicsBean.user != null) {
                UIUtils.startAnimationActivity((Activity) mContext,
                        UserHomePageActivity.getIntent(mContext, topicsBean.user.getUsername(), topicsBean.user.getId() + ""));

            }
        }
    }

    class ImageViewClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {
        private TopicsBean topicsBean;

        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            ArrayList<PhotoBean> arrayList = new ArrayList<>();
            PhotoBean photoBean = new PhotoBean();
            photoBean.title = topicsBean.id + "";
            photoBean.loadingURl = topicsBean.medias.get(0).getImage_sm();
            photoBean.imgUrl = topicsBean.medias.get(0).getImage_md();
            arrayList.add(photoBean);
            PhotoViewActivity.startPhotoViewActivity(mContext, arrayList, v, 0);
        }
    }

    class ItemClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {

        private TopicsBean topicsBean;

        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            TopicsDetailActivity.startActivity(mContext, topicsBean);
        }
    }


    class SymbolsClickSpan extends ClickableSpan {

        FundQuoteBean symbolsBean;

        public SymbolsClickSpan(FundQuoteBean symbolsBean) {
            this.symbolsBean = symbolsBean;
        }

        @Override
        public void onClick(View widget) {
//            SelectStockBean selectStockBean = new SelectStockBean();
//            selectStockBean.setName(symbolsBean.getAbbrName());
//            selectStockBean.setId(symbolsBean.getId());
//            selectStockBean.setSymbol(symbolsBean.getSymbol());
//            //设置类型为股票
//            selectStockBean.setSymbol_type("1");
//            mContext.startActivity(StockQuotesActivity.newIntent(mContext, selectStockBean));
            new MessageHandler(mContext).handleURL(DKHSClient.getAbsoluteUrl("/s/" + symbolsBean.getSymbol() + "/"));

        }

        /**
         * Makes the text without underline.
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mContext.getResources().getColor(R.color.theme_blue));
            ds.setUnderlineText(false);
        }

    }


    public class RoundSpan extends ReplacementSpan {


        private Context mContext;

        public RoundSpan(Context context) {
            mContext = context;
        }

        @Override
        public int getSize(Paint paint,
                           CharSequence text,
                           int start, int end,
                           Paint.FontMetricsInt fm) {
            return (int) MeasureText(paint, text, start, end);
        }

        private float MeasureText(Paint paint, CharSequence text, int start, int end) {
            return paint.measureText(text, start, end);
        }

        @Override
        public void draw(Canvas canvas,
                         CharSequence text,
                         int start, int end,
                         float x, int top,
                         int y, int bottom,
                         Paint paint) {

            RectF rect = new RectF(x, top, x + MeasureText(paint, text, start, end), bottom);
            paint.setColor(mContext.getResources().getColor(R.color.activity_bg_color));
            canvas.drawRoundRect(rect, mContext.getResources().getDimensionPixelOffset(R.dimen.radius)
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.radius),
                    paint);
            paint.setColor(mContext.getResources().getColor(R.color.tag_gray));
            canvas.drawText(text, start, end, x, y, paint);
        }
    }


}
