package com.dkhs.portfolio.utils;

import java.util.Random;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;

import android.graphics.Color;
import android.graphics.Path;

public class ColorTemplate {

    public static final int[] DEFAULTCOLORS = { R.color.def_1, R.color.def_2, R.color.def_3, R.color.def_4,
            R.color.def_5 };
    public static final int MY_COMBINATION_LINE = R.color.blue_line;
    public static final int DEF_RED =0xFFE73535;
    public static final int DEF_GREEN = Color.GREEN;

    public static int getRaddomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    public static int getDefaultColor(int position) {
        if (position < DEFAULTCOLORS.length) {

            return PortfolioApplication.getInstance().getResources().getColor(ColorTemplate.DEFAULTCOLORS[position]);
        } else {
            return getRaddomColor();
        }
    }

}
