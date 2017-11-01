package com.pedromassango.programmers.extras;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pedro Massango on 26/05/2017.
 */

public class CategoriesUtils {
    private static String TAG = "output";
    private static List<Category> categories;

    public static List<Category> getCategories(Resources resources) {
        if (categories == null) {
            categories = new ArrayList<>();
            categories.add( (new Category(0, resources.getString(R.string.home_page))));

            String[] stringArray = resources.getStringArray(R.array.array_languages);
            for (String aStringArray : stringArray) {
                categories.add(new Category( 0, aStringArray));
            }

            Log.v(TAG, "getCategories: " + categories.size());
        }
        return categories;
    }

    /**
     *  This method is usae d retrieve the position of the giving category
     * @param context to obtain the array of categories
     * @param category the category to find it position
     * @return the position of that category
     */
    public static int getCPosition(Context context, String category) {
        String[] cs = context.getResources().getStringArray(R.array.array_languages);
        for (int x = 0, y = cs.length; x <= y; x++) {
            if (cs[x].equalsIgnoreCase(category)) {
                return x;
            }
        }
        return 0;
    }

    @NonNull
    public static String getCategory(String category) {
        category = category.replace("#", "");
        category = category.replace(".", "");
        category = category.replace("$", "");
        category = category.replace("[", "");
        category = category.replace("]", "");
        return category.toLowerCase();
    }

    public static String getCategoryTopic(String category) {
        if(category == null) return "";

        category = category.replace(" ", "_");
        category = category.replace("#", "");
        category = category.replace(".", "");
        category = category.replace("$", "");
        category = category.replace("[", "");
        category = category.replace("]", "");
        return category;
    }
}
