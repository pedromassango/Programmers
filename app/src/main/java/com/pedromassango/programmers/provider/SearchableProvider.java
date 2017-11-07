package com.pedromassango.programmers.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Pedro Massango on 24-03-2017 13:12.
 */

public class SearchableProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.pedromassango.programmers.provider.SearchableProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchableProvider(){

        setupSuggestions(AUTHORITY, MODE);
    }
}
