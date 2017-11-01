package com.pedromassango.programmers.mvp.base.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IGetDataCompleteListener;
import com.pedromassango.programmers.interfaces.IRecyclerScrollListener;
import com.pedromassango.programmers.mvp.main.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragmentRecyclerView extends Fragment implements IGetDataCompleteListener {

    private static final String TAG = "fragment_recycler";

    private IRecyclerScrollListener iRecyclerScrollListener;
    public RecyclerView recyclerView;
    private TextView tvEmpty;

    protected abstract void setup(Bundle bundle);

    protected abstract RecyclerView.Adapter adapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Saving fragment state
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (iRecyclerScrollListener != null)
                    iRecyclerScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        });

        tvEmpty = (TextView) rootView.findViewById(R.id.tv_no_data);
        //tvEmpty.startAnimation( Util.Anim.blink());

        recyclerView.setAdapter(adapter());
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MainActivity) {
            iRecyclerScrollListener = (IRecyclerScrollListener) getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (iRecyclerScrollListener != null)
            iRecyclerScrollListener = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Post views crated implementations
        setup(savedInstanceState);
    }

    private void showRecyclerView() {
        if (tvEmpty.getVisibility() == View.VISIBLE) {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showTextError(String error) {
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
        tvEmpty.setText(error);
    }

    protected void showToast(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetDataSuccess() {
        Log.v(TAG, "onGetDataSuccess");
        showRecyclerView();
    }

    @Override
    public void onGetError(String error) {
        Log.v(TAG, "onGetError");
        showTextError(error);
    }

}
