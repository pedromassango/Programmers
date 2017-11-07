package com.pedromassango.programmers.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Pedro Massango on 26/05/2017.
 * @Author Pedro Pindali Massango
 *
 * This class is used to listen the Recycler view state
 * and to controll the FAB visibility
 */

public interface IRecyclerScrollListener {

    void onScrollStateChanged(RecyclerView recyclerView, int newState);
}
