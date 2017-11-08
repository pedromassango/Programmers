package com.pedromassango.programmers.presentation.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.profile.fragments.UserInfoFragments;
import com.pedromassango.programmers.presentation.profile.fragments.UserPostsFragment;

/**
 * Created by JM on 21/09/2017.
 */

public class ProfileTabsAdapter extends FragmentPagerAdapter {

    private final String[] titles = {"Sobre", "Posts"};
    private final Usuario usuario;

    public ProfileTabsAdapter(FragmentManager fm, Usuario usuario) {
        super(fm);
        this.usuario = usuario;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle b = new Bundle();
                b.putParcelable(Constants.EXTRA_USER, usuario);
                UserInfoFragments uif = new UserInfoFragments();
                uif.setArguments(b);
                return (uif);
            case 1:
                UserPostsFragment upf = new UserPostsFragment();
                Bundle b1 = new Bundle();
                b1.putString(Constants.EXTRA_USER_ID, usuario.getId());
                upf.setArguments(b1);
                return (upf);
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
