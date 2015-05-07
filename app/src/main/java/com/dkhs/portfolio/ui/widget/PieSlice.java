package com.dkhs.portfolio.ui.widget;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Region;

public class PieSlice {
    private int color = Color.BLACK;
    private float value;
    private String title;
    private Path path;
    private Region region;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
