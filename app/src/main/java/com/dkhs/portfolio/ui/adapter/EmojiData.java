package com.dkhs.portfolio.ui.adapter;

import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcm on 2015/7/21.
 */
public class EmojiData {
    public static  List<Emojicon> getData(int page){
        if(page > 5)
            return null;
        List<Emojicon> emojis = new ArrayList<Emojicon>();
        if(page == 0){
            emojis.add(Emojicon.fromCodePoint(0x1f603));
            emojis.add(Emojicon.fromCodePoint(0x1f60d));
            emojis.add(Emojicon.fromCodePoint(0x1f612));
            emojis.add(Emojicon.fromCodePoint(0x1f633));
            emojis.add(Emojicon.fromCodePoint(0x1f601));
            emojis.add(Emojicon.fromCodePoint(0x1f618));
            emojis.add(Emojicon.fromCodePoint(0x1f609));
            emojis.add(Emojicon.fromCodePoint(0x1f620));
            emojis.add(Emojicon.fromCodePoint(0x1f61e));
            emojis.add(Emojicon.fromCodePoint(0x1f625));
            emojis.add(Emojicon.fromCodePoint(0x1f62d));
            emojis.add(Emojicon.fromCodePoint(0x1f61d));
            emojis.add(Emojicon.fromCodePoint(0x1f621));
            emojis.add(Emojicon.fromCodePoint(0x1f623));
            emojis.add(Emojicon.fromCodePoint(0x1f614));
            emojis.add(Emojicon.fromCodePoint(0x1f604));
            emojis.add(Emojicon.fromCodePoint(0x1f637));
            emojis.add(Emojicon.fromCodePoint(0x1f61a));
            emojis.add(Emojicon.fromCodePoint(0x1f613));
            emojis.add(Emojicon.fromCodePoint(0x1f602));
            emojis.add(Emojicon.fromCodePoint(0x2f888));
        }else if(page == 1){
            emojis.add(Emojicon.fromCodePoint(0x1f60a));
            emojis.add(Emojicon.fromCodePoint(0x1f622));
            emojis.add(Emojicon.fromCodePoint(0x1f61c));
            emojis.add(Emojicon.fromCodePoint(0x1f628));
            emojis.add(Emojicon.fromCodePoint(0x1f630));
            emojis.add(Emojicon.fromCodePoint(0x1f632));
            emojis.add(Emojicon.fromCodePoint(0x1f60f));
            emojis.add(Emojicon.fromCodePoint(0x1f631));
            emojis.add(Emojicon.fromCodePoint(0x1f62a));
            emojis.add(Emojicon.fromCodePoint(0x1f616));
            emojis.add(Emojicon.fromCodePoint(0x1f60c));
            emojis.add(Emojicon.fromCodePoint(0x1f47f));
            emojis.add(Emojicon.fromCodePoint(0x1f47b));
            emojis.add(Emojicon.fromCodePoint(0x1f385));
            emojis.add(Emojicon.fromCodePoint(0x1f467));
            emojis.add(Emojicon.fromCodePoint(0x1f466));
            emojis.add(Emojicon.fromCodePoint(0x1f469));
            emojis.add(Emojicon.fromCodePoint(0x1f468));
            emojis.add(Emojicon.fromCodePoint(0x1f436));
            emojis.add(Emojicon.fromCodePoint(0x1f431));
            emojis.add(Emojicon.fromCodePoint(0x2f888));
        }else if(page == 2){
            emojis.add(Emojicon.fromCodePoint(0x1f44d));
            emojis.add(Emojicon.fromCodePoint(0x1f44e));
            emojis.add(Emojicon.fromCodePoint(0x1f44a));
            emojis.add(Emojicon.fromChar((char) 0x270a));
            emojis.add(Emojicon.fromChar((char) 0x270c));
            emojis.add(Emojicon.fromCodePoint(0x1f4aa));
            emojis.add(Emojicon.fromCodePoint(0x1f44f));
            emojis.add(Emojicon.fromCodePoint(0x1f448));
            emojis.add(Emojicon.fromCodePoint(0x1f446));
            emojis.add(Emojicon.fromCodePoint(0x1f449));
            emojis.add(Emojicon.fromCodePoint(0x1f447));
            emojis.add(Emojicon.fromCodePoint(0x1f44c));
            emojis.add(Emojicon.fromChar((char) 0x2764));
            emojis.add(Emojicon.fromCodePoint(0x1f494));
            emojis.add(Emojicon.fromCodePoint(0x1f64f));
            emojis.add(Emojicon.fromChar((char) 0x2600));
            emojis.add(Emojicon.fromCodePoint(0x1f319));
            emojis.add(Emojicon.fromCodePoint(0x1f31f));
            emojis.add(Emojicon.fromChar((char) 0x26a1));
            emojis.add(Emojicon.fromChar((char) 0x2601));
            emojis.add(Emojicon.fromCodePoint(0x2f888));
        }else if(page == 3){
            emojis.add(Emojicon.fromChar((char) 0x2614));
            emojis.add(Emojicon.fromCodePoint(0x1f341));
            emojis.add(Emojicon.fromCodePoint(0x1f33b));
            emojis.add(Emojicon.fromCodePoint(0x1f343));
            emojis.add(Emojicon.fromCodePoint(0x1f457));
            emojis.add(Emojicon.fromCodePoint(0x1f380));
            emojis.add(Emojicon.fromCodePoint(0x1f444));
            emojis.add(Emojicon.fromCodePoint(0x1f339));
            emojis.add(Emojicon.fromChar((char) 0x2615));
            emojis.add(Emojicon.fromCodePoint(0x1f382));
            emojis.add(Emojicon.fromCodePoint(0x1f559));
            emojis.add(Emojicon.fromCodePoint(0x1f37a));
            emojis.add(Emojicon.fromCodePoint(0x1f50d));
            emojis.add(Emojicon.fromCodePoint(0x1f4f1));
            emojis.add(Emojicon.fromCodePoint(0x1f3e0));
            emojis.add(Emojicon.fromCodePoint(0x1f697));
            emojis.add(Emojicon.fromCodePoint(0x1f381));
            emojis.add(Emojicon.fromChar((char) 0x26bd));
            emojis.add(Emojicon.fromCodePoint(0x1f4a3));
            emojis.add(Emojicon.fromCodePoint(0x1f48e));
            emojis.add(Emojicon.fromCodePoint(0x2f888));
        }
        return  emojis;
    }
}