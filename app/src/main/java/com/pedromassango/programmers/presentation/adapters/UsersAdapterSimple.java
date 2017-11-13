package com.pedromassango.programmers.presentation.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.ImageUtils;
import com.pedromassango.programmers.extras.IntentUtils;
import com.pedromassango.programmers.extras.Util;
import com.pedromassango.programmers.interfaces.IGetDataCompleteListener;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.adapters.holders.UserVH;
import com.pedromassango.programmers.presentation.profile.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

import static com.pedromassango.programmers.extras.Constants.EXTRA_USER;

/**
 * Created by root on 22-11-2016.
 */

public class UsersAdapterSimple extends RecyclerView.Adapter<UserVH> {

    private static final String TAG = "UsersAdapter";
    private IGetDataCompleteListener getDataCompleteListener;
    private List<Usuario> usuarios;

    private final Activity activity;

    public UsersAdapterSimple(Activity activity, IGetDataCompleteListener getDataCompleteListener) {
        this.activity = activity;
        this.usuarios = new ArrayList<>();
        this.getDataCompleteListener = getDataCompleteListener;
    }

    public UsersAdapterSimple(Activity activity, List<Usuario> usuarios, IGetDataCompleteListener getDataCompleteListener) {
        this.activity = activity;
        this.usuarios = usuarios;
        this.getDataCompleteListener = getDataCompleteListener;
    }

    public void add(List<Usuario> usuarios){
        this.usuarios = (usuarios);
        notifyDataSetChanged();
    }

    @Override
    public UserVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_user, viewGroup, false);
        return new UserVH(v);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    @Override
    public void onBindViewHolder(UserVH holder, int position) {
        Usuario usuario = usuarios.get(position);

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
