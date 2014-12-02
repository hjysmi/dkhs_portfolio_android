package com.dkhs.portfolio.utils;

import java.util.Random;

import android.content.res.ColorStateList;
import android.graphics.Color;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;

public class ColorTemplate {

    public static final int[] DEFAULTCOLORS = { R.color.def_1, R.color.def_2, R.color.def_3, R.color.def_4,
            R.color.def_5, R.color.def_6, R.color.def_7, R.color.def_8, R.color.def_9, R.color.def_10, R.color.def_11,
            R.color.def_12, R.color.def_13, R.color.def_14, R.color.def_15, R.color.def_16, R.color.def_17,
            R.color.def_18, R.color.def_19, R.color.def_20 };
    public static final int[] COMPARE = { R.color.def_1, R.color.def_3, R.color.title_color, R.color.def_4,
        R.color.def_5,R.color.def_5 };
    public static final int MY_COMBINATION_LINE = PortfolioApplication.getInstance().getResources()
            .getColor(R.color.blue_line);
    // public static final int DEF_RED =0xFFE73535;
    // public static final int DEF_GREEN = Color.GREEN;
    public static final int DEF_RED = PortfolioApplication.getInstance().getResources().getColor(R.color.def_red);
    public static final int DEF_GREEN = PortfolioApplication.getInstance().getResources().getColor(R.color.def_green);
    public static final int DEF_GRAY = PortfolioApplication.getInstance().getResources().getColor(R.color.def_gray);
    public static final int THEME_COLOR = PortfolioApplication.getInstance().getResources()
            .getColor(R.color.theme_color);

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
    public static int getDefaultColors(int position) {
        if (position < COMPARE.length) {

            return PortfolioApplication.getInstance().getResources().getColor(ColorTemplate.COMPARE[position]);
        } else {
            return getRaddomColor();
        }
    }
    public static ColorStateList getTextColor(float value1, float value2) {

        if (value1 < value2) {

            return (ColorStateList) PortfolioApplication.getInstance().getResources()
                    .getColorStateList(R.color.def_green);

        } else if (value1 > value2) {

            return (ColorStateList) PortfolioApplication.getInstance().getResources()
                    .getColorStateList(R.color.def_red);
        }
        return (ColorStateList) PortfolioApplication.getInstance().getResources()
                .getColorStateList(R.color.theme_color);
    }

    public static ColorStateList getTextColor(int resId) {

        return (ColorStateList) PortfolioApplication.getInstance().getResources().getColorStateList(resId);

    }

    public static ColorStateList getUpOrDrownCSL(float value) {
        ColorStateList colorStateList = (ColorStateList) PortfolioApplication.getInstance().getResources()
                .getColorStateList(R.color.theme_color);
        if (value > 0) {
            colorStateList = (ColorStateList) PortfolioApplication.getInstance().getResources()
                    .getColorStateList(R.color.def_red);

        } else if (value < 0) {
            colorStateList = (ColorStateList) PortfolioApplication.getInstance().getResources()
                    .getColorStateList(R.color.def_green);
        }
        return colorStateList;

    }

}
