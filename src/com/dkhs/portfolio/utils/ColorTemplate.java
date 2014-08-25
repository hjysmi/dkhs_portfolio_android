package com.dkhs.portfolio.utils;

import java.util.Random;

import android.graphics.Color;
import android.graphics.Path;

public class ColorTemplate {

    // public static final int[] DEFAULTCOLORS = { R.color.line_1, R.color.line_2, R.color.line_3, R.color.line_4,
    // R.color.line_5, R.color.line_6, R.color.line_7 };

    public static int getRaddomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

}
