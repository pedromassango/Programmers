package com.pedromassango.programmers.mvp.adapters.users;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.interfaces.IGetDataCompleteListener;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.mvp.adapters.holders.UserVH;
import com.pedromassango.programmers.mvp.profile.profile.ProfileActivity;
import com.pedromassango.programmers.extras.ImageUtils;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;

import static com.pedromassango.programmers.extras.Constants.EXTRA_USER;

/**
 * Created by root on 22-11-2016.
 */

public class UsersAdapter extends FirebaseRecyclerAdapter<Usuario, UserVH> {

    private static final String TAG = "UsersAdapter";
    private IGetDataCompleteListener getDataCompleteListener;

    private final Activity activity;

    public UsersAdapter(Activity activity, Query usersRef, IGetDataCompleteListener getDataCompleteListener) {
        super(Usuario.class, R.layout.row_user, UserVH.class, usersRef);
        this.activity = activity;
        this.getDataCompleteListener = getDataCompleteListener;
    }


    @Override
    protected Usuario parseSnapshot(DataSnapshot snapshot) {
        Log.v(TAG, "parseSnapshot: reveived");

        if (null != getDataCompleteListener)
            getDataCompleteListener.onGetDataSuccess();

        return super.parseSnapshot(snapshot);
    }

    @Override
    protected void onCancelled(DatabaseError databaseError) {
        Log.v(TAG, "onCancelled: canceled");

        if (null != getDataCompleteListener)
            getDataCompleteListener.onGetError(Util.getError(databaseError.toException()));

        super.onCancelled(databaseError);
    }

    @Override
    protected void populateViewHolder(UserVH holder, Usuario usuario, int position) {

        holder.tvName.setText(usuario.getUsername());
        holder.tvCodelevel.setText(Util.concat(" " + usuario.getCodeLevel()));
        holder.tvReputation.setText(String.valueOf(usuario.getReputation()));
        holder.tvLanguage.setText(usuario.getProgrammingLanguage());

        ImageUtils.loadImageUser(activity, usuario.getUrlPhoto(), holder.imgUser);

        holder.itemView.findViewById(R.id.user_view).setOnClickListener(listenClick(usuario));
    }

    private View.OnClickListener listenClick(final Usuario _usuario) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = new Bundle();
                b.putParcelable(EXTRA_USER, _usuario);
                IntentUtils.startActivity(activity, b, ProfileActivity.class);
            }
        };
    }
}
