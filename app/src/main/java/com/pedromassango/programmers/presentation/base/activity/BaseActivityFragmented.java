package com.pedromassango.programmers.presentation.base.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.pedromassango.programmers.R;

/**
 * Created by Pedro Massango on 25/05/2017.
 */

public abstract class BaseActivityFragmented extends BaseActivity {

    private FragmentManager fragmentManager;
    private android.app.FragmentManager androidFragmentManager;

    protected int layoutResource() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void initializeViews() {
        fragmentManager = getSupportFragmentManager();
        androidFragmentManager = getFragmentManager();

    }


    /**
     * Will replace de current Fragment on the layout
     *
     * @Param fragment The fragment to show/replace.
     */
    public final void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            String tag = fragment.getTag() != null && !fragment.getTag().isEmpty() ? fragment.getTag() : fragment.getClass().getSimpleName();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment, tag)
                    .commit();
        }
    }

    public final void replaceFragment(android.app.Fragment fragment) {
        if (fragment != null) {
            String tag = fragment.getTag() != null && !fragment.getTag().isEmpty() ? fragment.getTag() : fragment.getClass().getSimpleName();
            androidFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment, tag)
                    .commit();
        }
    }

    public final void showFragment(android.app.Fragment fragment) {
        if (fragment != null) {
            androidFragmentManager
                    .beginTransaction()
                    .show(fragment)
                    .commit();
        }
    }

    public final void showFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .show(fragment)
                    .commit();
        }
    }

    private boolean fragmentManagerHaveBackStackEntry() {

        return fragmentManager.getBackStackEntryCount() > 0 || androidFragmentManager.getBackStackEntryCount() > 0;
    }

    private void fragmentManagerPopBack() {
        if (fragmentManagerHaveBackStackEntry()) {

            //fragmentManager.popBackStackImmediate();
            fragmentManager.popBackStack();
            androidFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {

        fragmentManagerPopBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                fragmentManagerPopBack();
                return true;

        }
        return false;
    }
}
