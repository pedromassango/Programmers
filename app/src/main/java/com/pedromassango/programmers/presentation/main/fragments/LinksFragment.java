package com.pedromassango.programmers.presentation.main.fragments;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.pedromassango.programmers.presentation.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.presentation.link.views.LinkAdapter;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.extras.TextUtils;

/**
 * Created by Pedro Massango on 14/06/2017 at 18:57.
 */

public class LinksFragment extends BaseFragmentRecyclerView {

    @Override
    protected void setup(Bundle bundle) {

    }

    @Override
    protected LinkAdapter adapter() {

        String category = getArguments().getString(Constants.EXTRA_CATEGORY, "");
        DatabaseReference linksRef = TextUtils.isEmpty(category) ?
                Library.getLinksRef()
                :
                Library.getLinksByCategoryRef(category);
        return (new LinkAdapter(getActivity(), linksRef, this));
    }

}
