package com.pedromassango.programmers.presentation.profile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.Constants;
import com.pedromassango.programmers.models.Usuario;

/**
 * Created by JM on 21/09/2017.
 */

public class UserInfoFragments extends Fragment {

    private Usuario usuario;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        usuario = getArguments().getParcelable(Constants.EXTRA_USER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) view.findViewById(R.id.tv_reputation)).setText(String.valueOf(usuario.getReputation()));
        ((TextView) view.findViewById(R.id.tv_level)).setText(usuario.getCodeLevel());
        ((TextView) view.findViewById(R.id.tv_phone_number)).setText(usuario.getPhone());

        ((TextView) view.findViewById(R.id.tv_programming_language)).setText(usuario.getProgrammingLanguage());
        ((TextView) view.findViewById(R.id.tv_platform)).setText(usuario.getPlatform());
        ((TextView) view.findViewById(R.id.tv_reputation)).setText(String.valueOf(usuario.getReputation()));
        ((TextView) view.findViewById(R.id.tv_level)).setText(usuario.getCodeLevel());

        ((TextView) view.findViewById(R.id.tv_gender)).setText(usuario.getGender());
        ((TextView) view.findViewById(R.id.tv_age)).setText(usuario.getAge());
        ((TextView) view.findViewById(R.id.tv_country)).setText(usuario.getCountry());
        ((TextView) view.findViewById(R.id.tv_city)).setText(usuario.getCity());
    }
}
