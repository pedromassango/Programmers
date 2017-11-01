package com.pedromassango.programmers.extras;

import com.pedromassango.programmers.BuildConfig;

/**
 * Created by Pedro Massango on 21-11-2016.
 */

public class Constants {

    //public static final boolean _DEVELOP_MODE = BuildConfig.DEBUG;
    public static final boolean _DEVELOP_MODE = false;

    public static final String _FIRST_TIME = "com.pedromassango.programmers.first_time";
    public static final String EXTRA_USER = "com.pedromassango.programmers._user";
    public static final String EXTRA_FRIEND_CONTACT = "com.pedromassango.programmers.EXTRA_FRIEND_CONTACT";
    public static final String EXTRA_POST = "com.pedromassango.programmers._post_extra";
    public static final String EXTRA_POST_ID = "com.pedromassango.programmers._post_id";
    public static final String EXTRA_COMMENT = "com.pedromassango.programmers._comment_extra";
    public static final String EXTRA_IMAGE_URL = "com.pedromassango.programmers._image_extra_url";
    public static final String EXTRA_IMAGE = "com.pedromassango.programmers._bitmap_extra";
    public static final String EXTRA_USER_ID = "com.pedromassango.programmers._extra_user_id";
    public static final String EXTRA_CATEGORY = "com.pedromassango.programmers._category";
    public static final String EXTRA_LINK = "com.pedromassango.programmers.key_edit_link";
    public static final String EXTRA_JOB = "com.pedromassango.programmers.estra_job";
    public static final String LAST_EMAIL = "com.pedromassango.programmers._last_email";

    public class AcountStatus {
        public final static int COMPLETE = 1;
        public final static int INCOMPLETE = 0;
    }

    public class NotificationType {
       public static final String POST = "com.pedromassango.programmers.notify.post";
       public static final String COMMENT = "com.pedromassango.programmers.notify.sendComment";
    }
}
