package com.pedromassango.programmers.models;

import android.support.annotation.StringRes;

/**
 * Created by root on 05-02-2017.
 */

public class ContextMenuItem {

    private int icon;
    private
    @StringRes
    int label;

    public ContextMenuItem(int icon, @StringRes int label) {
        this.icon = icon;
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public
    @StringRes
    int getLabel() {
        return label;
    }

    public void setLabel(@StringRes int label) {
        this.label = label;
    }
}
