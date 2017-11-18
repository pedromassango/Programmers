package com.pedromassango.programmers.presentation.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.UsersRepository;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.adapters.UsersAdapterSimple;
import com.pedromassango.programmers.presentation.base.fragment.BaseFragmentRecyclerView;

import java.util.List;

/**
 * Created by Pedro Massango on 13/06/2017 at 01:56.
 */

public class UsersFragment extends BaseFragmentRecyclerView implements Callbacks.IResultsCallback<Usuario> {

    private UsersAdapterSimple usersAdapterSimple;
    private boolean filter = false;

    @Override
    protected void setup(Bundle bundle) { }

    @Override
    protected RecyclerView.Adapter adapter() {
        usersAdapterSimple = new UsersAdapterSimple(getActivity(), this);
        return usersAdapterSimple;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pass empty to get all data, without filter
        reloadData("");
    }

    @Override
    public void reloadData(String category) {
        Util.showLog("Reloaded");
        Util.showLog("Reloaded: " +category);

        // Get the data from repository
        UsersRepository ur = RepositoryManager.getInstance().getUsersRepository();

        if(category.trim().isEmpty()) {
            filter = false;
            ur.getUsers(this);
        }else{
            filter = true;
            ur.getUsersByCategory(category, this);
        }
    }

    @Override
    public void onSuccess(List<Usuario> results) {

        usersAdapterSimple.add(results);
        showRecyclerView();
    }

    @Override
    public void onDataUnavailable() {
        if(filter){
            showToast(getString(R.string.nothing_to_show));
        }else {
            showTextError(getString(R.string.something_was_wrong));
        }
    }
}
