package com.pedromassango.programmers.presentation.main.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.about.AboutActivity;
import com.pedromassango.programmers.presentation.adapters.MainTabsAdapter;
import com.pedromassango.programmers.presentation.base.activity.BaseActivity;
import com.pedromassango.programmers.presentation.donate.DonateActivity;
import com.pedromassango.programmers.presentation.link.views.LinkActivity;
import com.pedromassango.programmers.presentation.main.drawer.NavigationDrawerFragment;
import com.pedromassango.programmers.presentation.post._new.NewPostActivity;
import com.pedromassango.programmers.presentation.profile.profile.ProfileActivity;
import com.pedromassango.programmers.presentation.repport.BugActivity;
import com.pedromassango.programmers.presentation.settings.activity.SettingsActivity;
import com.pedromassango.programmers.server.logout.LogoutHadler;
import com.pedromassango.programmers.services.GoogleServices;
import com.pedromassango.programmers.ui.FabsController;
import com.pedromassango.programmers.extras.IntentUtils;

public class MainActivity extends BaseActivity implements Contract.View {

    // To log monitor
    private static final String TAG = "MainActivity";
    
    // Views
    private MainTabsAdapter mainTabsAdapter;
    private ViewPager mViewPager;
    private NavigationDrawerFragment drawerFragment;

    private DrawerLayout drawerLayout;

    // FAB controller
    private FabsController fabsController;

    // Presenter - MVP
    private MainPresenter mainPresenter;

    @Override
    protected int layoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);

        // Set up the ViewPager
        mViewPager = drawerLayout.findViewById(R.id.container);

        // Set up the TabLayout
        TabLayout tabLayout = drawerLayout.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Set up the NavigationDrawer
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.fillHeader(drawerFragment.getId(), drawerLayout, toolbar);

        // Setting up Fabs buttons controll
        fabsController = new FabsController(this, drawerLayout);
    }


    @Override
    public void showHeaderInfo(Usuario usuario) {

        drawerFragment.fillHeader(usuario, mainPresenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPresenter = new MainPresenter(this, RepositoryManager.getInstance()
        .getUsersRepository());
        mainPresenter.initialize(getIntent(), savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mainPresenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // This will check if the device have
        // the apropriated Play Service version
        // If not, the app will be closed
        new GoogleServices(this).isGooglePlayServicesAvailable();
        this.makeMeOnline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // this will hide the fab button if it is showing
        // when the user leave de MainActivity
        fabsController.colapseFab();
    }

    @Override
    public void showLogoutDialog() {
        LogoutHadler logoutHadler = new LogoutHadler(this);
        logoutHadler.showAlertDialogLogout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem itemSearch = menu.findItem(R.id.action_search_post);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;

        searchView = (SearchView) itemSearch.getActionView();

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            searchView = (SearchView) itemSearch.getActionView();
        } else {
            searchView = (SearchView) MenuItem.getActionView(itemSearch);
        }*/

        if(searchView == null){
            return super.onCreateOptionsMenu(menu);
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fabsController.colapseFab();
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        fabsController.colapseFab();
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mainPresenter.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Hadle click of ImageUser in Header
     *
     * @param b the bundle containing the user data
     */
    @Override
    public void gotoProfile(Bundle b) {

        IntentUtils.startActivity(this, b, ProfileActivity.class);
    }

    @Override
    public void startNewPostActivity() {

        IntentUtils.startActivity(this, NewPostActivity.class);
    }

    @Override
    public void startRateApp() {

        IntentUtils.startPlaystoreAppPage(this);
    }

    @Override
    public void startNewLinkActivity() {

        IntentUtils.startActivity(this, LinkActivity.class);
    }

    @Override
    public void startReportBugActivity() {

        IntentUtils.startActivity(this, BugActivity.class);
    }

    @Override
    public void startSettingsActivity() {

        IntentUtils.startActivity(this, SettingsActivity.class);
    }

    @Override
    public void startAboutActivity() {

        IntentUtils.startActivity(this, AboutActivity.class);
    }

    @Override
    public void startDonateActivity() {

        IntentUtils.startActivity(this, DonateActivity.class);
    }

    @Override
    public void setFragmentByCategory(String category) {
        Log.v(TAG, "setFragmentByCategory: " + category);

        mainTabsAdapter = new MainTabsAdapter(this, category, getSupportFragmentManager());
        mViewPager.setAdapter(mainTabsAdapter);
    }

    @Override
    public void vibrate(int intensity) {

        super.vibratePhone(intensity);
    }

    @Override
    public void showDialogGetUserInfoError(String string, final Contract.OnDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle(R.string.internet_connection_error)
                .setMessage(R.string.get_user_info_error)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        listener.onRetry();
                    }
                })
                .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        listener.onQuit();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        vibrate(30);
    }

    @Override
    public void setFABVisibility(boolean b) {

        fabsController.setMainFABVisibility( b);
    }

    /**
     * Hadle recyclerView scroll status
     * to hide/show FAB
     *
     * @param recyclerView the current recyclerView
     * @param newState     the new state of the recyclerView
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        mainPresenter.onScrollStateChanged(newState);
    }

    @Override
    public void showProgress(@StringRes int message) {

        IntentUtils.showProgressFragmentDialog(this, true);
    }

    @Override
    public void dismissprogess() {

        IntentUtils.showProgressFragmentDialog(this, false);
    }

    @Override
    public void showToast(String error) {

        super.showToastMessage(error);
    }

    @Override
    public void quit() {

        this.onBackPressed();
        this.finish();
    }

}

