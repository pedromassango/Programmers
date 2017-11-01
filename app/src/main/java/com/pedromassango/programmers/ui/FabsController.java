package com.pedromassango.programmers.ui;

import android.app.Activity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.mvp.job.activity.JobActivity;
import com.pedromassango.programmers.mvp.main.activity.Contract;

/**
 * Created by Pedro Massango on 16/06/2017 at 15:29.
 */

public class FabsController implements View.OnClickListener {

    // The FABs
    private FloatingActionMenu fabMenu;

    // Presenter -> MVP
    private Contract.View mainView;

    public FabsController(Contract.View mainView, View mView) {
        this.mainView = mainView;

        fabMenu = (FloatingActionMenu) mView.findViewById(R.id.main_fab);
        FloatingActionButton fab1 = (FloatingActionButton) fabMenu.findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) fabMenu.findViewById(R.id.fab2);
        FloatingActionButton fab3 = (FloatingActionButton) fabMenu.findViewById(R.id.fab3);
        FloatingActionButton fab4 = (FloatingActionButton) fabMenu.findViewById(R.id.fab4);

        fabMenu.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab1: // Fab share Clicked
                IntentUtils.startShareApp(((Activity) mainView));
                break;

            case R.id.fab2: // Fab new Link Clicked
                colapseFab();
                mainView.startNewLinkActivity();
                break;
            case R.id.fab3:
                colapseFab();
                mainView.startNewPostActivity();
                break;

            case R.id.fab4:
                colapseFab();
                IntentUtils.startActivity(((Activity) mainView), JobActivity.class);
                break;
        }
    }

    public void colapseFab() {
        fabMenu.close(true);
    }

    public void setMainFABVisibility(boolean visibility) {
        if (visibility)
            fabMenu.showMenuButton(true);
        else
            fabMenu.hideMenuButton(true);
    }
}
