package com.pedromassango.programmers.presentation.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.Query;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.UsersRepository;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.adapters.users.UsersAdapter;
import com.pedromassango.programmers.presentation.adapters.users.UsersNoFirebaseAdapter;
import com.pedromassango.programmers.presentation.base.fragment.BaseFragmentRecyclerView;
import com.pedromassango.programmers.server.Library;
import com.pedromassango.programmers.extras.TextUtils;

import java.util.List;

import static com.pedromassango.programmers.extras.Constants.EXTRA_CATEGORY;

/**
 * Created by Pedro Massango on 13/06/2017 at 01:56.
 */

public class UsersFragment extends BaseFragmentRecyclerView {

    private UsersNoFirebaseAdapter usersNoFirebaseAdapter;

    @Override
    protected void setup(Bundle bundle) {

    }

    @Override
    protected RecyclerView.Adapter adapter() {

        usersNoFirebaseAdapter = new UsersNoFirebaseAdapter(getActivity(), this);
        return usersNoFirebaseAdapter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the data from repository
        RepositoryManager.getInstance()
                .getUsersRepository()
                .getUsers(new Callbacks.IResultsCallback<Usuario>() {
                    @Override
                    public void onSuccess(List<Usuario> results) {

                        usersNoFirebaseAdapter.add(results);
                        showRecyclerView();
                    }

                    @Override
                    public void onDataUnavailable() {

                        showTextError("Data Unavailable");
                    }
                });
    }
}
