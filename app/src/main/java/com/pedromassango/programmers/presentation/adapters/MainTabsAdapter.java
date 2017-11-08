package com.pedromassango.programmers.presentation.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.presentation.main.fragments.ChatsFragment;
import com.pedromassango.programmers.presentation.main.fragments.JobsFragment;
import com.pedromassango.programmers.presentation.main.fragments.LinksFragment;
import com.pedromassango.programmers.presentation.main.fragments.PostsFragment;
import com.pedromassango.programmers.presentation.main.fragments.UsersFragment;

import static com.pedromassango.programmers.extras.Constants.EXTRA_CATEGORY;

/**
 * Created by Pedro Massango on 13/06/2017 at 00:46.
 */

public class MainTabsAdapter extends FragmentStatePagerAdapter {
//public class MainTabsAdapter extends FragmentPagerAdapter {

    // The tab itens titles
    private String[] titles;
    private final String category;

    public MainTabsAdapter(Activity activity, String category, FragmentManager fm) {
        super(fm);
        this.category = category;
        this.titles = activity.getResources().getStringArray(R.array.array_tabs_titles);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;
        switch (position) {
            case 0: // Posts
                f = new PostsFragment();
                break;
            case 1: // users
                f = new UsersFragment();
                break;
            case 2:
                f = new JobsFragment();
                break;
            case 3:
                f = new LinksFragment();
                break;
            case 4:
                f = new ChatsFragment();
                break;
        }

        Bundle b = new Bundle();
        b.putString(EXTRA_CATEGORY, category);
        f.setArguments(b);
        return f;
    }

    @Override
    public int getCount() {

        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (titles[position]);
    }
}
