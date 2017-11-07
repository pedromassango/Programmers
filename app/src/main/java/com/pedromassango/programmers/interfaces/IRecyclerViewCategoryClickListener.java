package com.pedromassango.programmers.interfaces;

import com.pedromassango.programmers.models.Category;

/**
 * Created by Pedro Massango on 22/06/2017 at 18:21.
 */

public interface IRecyclerViewCategoryClickListener {

    void onCategoryClicked(int position, Category category);
}
