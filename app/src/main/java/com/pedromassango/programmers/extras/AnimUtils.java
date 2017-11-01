package com.pedromassango.programmers.extras;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

public class AnimUtils {

    public static Animation blink() {

        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(550);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(10);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    public static Animation transOut(Context context) {

        return AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
    }

    public static Animation transIn(Context context) {

        return AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
    }

}
