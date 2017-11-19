package com.pedromassango.programmers.ui;

import android.app.Activity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.presentation.main.activity.Contract;

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

        fabMenu = mView.findViewById(R.id.main_fab);
        FloatingActionButton fab1 = fabMenu.findViewById(R.id.fab1);
        FloatingActionButton fab3 = fabMenu.findViewById(R.id.fab3);

        fabMenu.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab1: // Fab share Clicked
                IntentUtils.startShareApp(((Activity) mainView));

                break;
            case R.id.fab3:
                colapseFab();
                mainView.startNewPostActivity();
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
