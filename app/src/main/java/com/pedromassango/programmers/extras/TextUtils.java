package com.pedromassango.programmers.extras;

/**
 * Created by Pedro Massango on 19/06/2017 at 23:19.
 */

public class TextUtils {
    public static boolean isEmpty(String text) {
        if (text == null)
            return true;
        return text.trim().length() <= 0;

    }

    public static boolean lessOrEqualTwoDigits(String text) {
        return text.trim().length() <= 2;
    }

    public static boolean lessOrEqualFiveDigits(String text) {
        return text.trim().length() <= 5;
    }

}
