package com.pedromassango.programmers.presentation.main.drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.CategoriesUtils;
import com.pedromassango.programmers.extras.ImageUtils;
import com.pedromassango.programmers.interfaces.IRecyclerViewCategoryClickListener;
import com.pedromassango.programmers.models.Category;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.adapters.CategoryAdapter;
import com.pedromassango.programmers.presentation.main.activity.MainPresenter;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pedromassango.programmers.extras.Constants.EXTRA_USER;

/**
 * Created by Pedro Massango on 15-11-2016 at 16:13.
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener, IRecyclerViewCategoryClickListener {

    // KEYs
    private final String TAG = "DrawerFragment";
    private static int LAST_CATEGORY_POSITION = 0;

    private Usuario currentUser;
    private MainPresenter mainPresenter;

    // Views
    private CategoryAdapter adapter;
    //private CategoryAdapterListView adapter;


    // Header views
    private CircleImageView userImage;
    private TextView tvUsername, tvEmail, tvLanguage, tvSkill, tvLevel;

    // The recyclerView
    private RecyclerView recyclerCategories;

    // Toolbar and Drawer
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLAyout;
    private Toolbar mToolbar;
    private View containerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_drawer_layout, container, false);
        recyclerCategories = view.findViewById(R.id.recycler_category);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerCategories.setHasFixedSize(true);

        tvUsername = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvLanguage = view.findViewById(R.id.tv_language);
        tvSkill = view.findViewById(R.id.tv_energy);
        tvLevel = view.findViewById(R.id.tv_level);

        userImage = view.findViewById(R.id.img_user);
        userImage.setOnClickListener(this);

        // Quit click
        TextView tvQuit = view.findViewById(R.id.tv_quit);
        tvQuit.setText(tvQuit.getText().toString().toUpperCase());
        tvQuit.setOnClickListener(this);
        return view;
    }

    public void fillHeader(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        Log.v(TAG, "fillHeader - setup");
        containerView = getActivity().findViewById(fragmentId);

        mDrawerLAyout = drawerLayout;
        mToolbar = toolbar;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLAyout.addDrawerListener(mDrawerToggle);
        mDrawerLAyout.post(new Runnable() {
            @Override
            public void run() {

                mDrawerToggle.syncState();
            }
        });
    }

    public void fillHeader(Usuario usuario, MainPresenter activity) {
        Log.v(TAG, "fillHeader - bind views");
        Log.v(TAG, "fillHeader - check ususario is null: " + (usuario == null));

        this.currentUser = usuario;
        this.mainPresenter = activity;

        if (usuario == null)
            return;

        tvUsername.setText(usuario.getUsername());
        tvEmail.setText(usuario.getEmail());
        tvLanguage.setText(usuario.getProgrammingLanguage());

        String reputation = mainPresenter.getSkill(usuario.getReputation());
        tvSkill.setText(reputation);

        tvLevel.setText(usuario.getCodeLevel());

        ImageUtils.loadImageUser(getContext(), usuario.getUrlPhoto(), userImage);

        try {
            adapter = new CategoryAdapter(getContext(), usuario, CategoriesUtils.getCategories(getResources()), this);
            recyclerCategories.setAdapter(adapter);
        }catch(IllegalStateException ex ){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_user:
                Bundle b = new Bundle();
                b.putParcelable(EXTRA_USER, currentUser);
                mainPresenter.getView().gotoProfile(b);
                break;

            case R.id.tv_quit:
                mainPresenter.onLogoutClicked();
                break;
        }

        mDrawerLAyout.closeDrawers();
    }

    @Override
    public void onCategoryClicked(int position, Category category) {
        mDrawerLAyout.closeDrawers();

        // Check if the positioon is the last tha was clicked
        if (LAST_CATEGORY_POSITION == position) {
            return;
        }

        // Change the toolbar title, to the selected category
        mToolbar.setTitle(category.getTitle());

        // If position in one, so, it is the home option
        // so, let pass empty string, to retrieve all
        // default data
        if (position == 0) {
            category.setTitle("");
        }

        // Update the last position tha was clicked
        LAST_CATEGORY_POSITION = position;

        // Will remove all special charachters tha Firebase do not allow
        String query = CategoriesUtils.getCategory(category.getTitle());

        mainPresenter.setFragmentByCategory(query);
    }
}
