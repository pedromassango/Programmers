package com.pedromassango.programmers.models;

import android.support.annotation.DrawableRes;

/**
 * Created by Pedro Massango on 07-01-2017 at 13:18.
 */

public class Category {

    @DrawableRes
    private int icon;
    private String title;

    public Category() {

    }


    public Category(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getSimpleName() {
        String icon;
        if(title.contains(" ")){
            char c1 = title.charAt(0);
            char c2 = title.charAt(title.indexOf(" ") +1);
            icon = String.format("%s%s", c1, c2);
        }else{
            char c1 = title.charAt(0);
            icon = String.valueOf(c1);
        }
        return icon.toUpperCase();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
