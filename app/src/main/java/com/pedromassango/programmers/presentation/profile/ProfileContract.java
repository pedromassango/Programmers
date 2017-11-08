package com.pedromassango.programmers.presentation.profile;

import android.net.Uri;

import com.pedromassango.programmers.interfaces.IGetUserListener;
import com.pedromassango.programmers.models.Usuario;
import com.pedromassango.programmers.presentation.base.BaseContract;
import com.pedromassango.programmers.presentation.profile.edit.Contract;

/**
 * Created by Pedro Massango on 27/06/2017 at 14:04.
 */

public class ProfileContract {

    public interface ModelImpl extends BaseContract.ModelImpl {
        void saveProfile(Contract.Presenter presenter, Contract.OnEditListener listener, Uri imagePicked, Usuario usuario);

        void updateProfile(Usuario mUsuario);

        void getUser(String userId, IGetUserListener getUserListener);
    }

    public interface ProfilePresenter extends BaseContract.PresenterImpl{

        boolean isFirsttime();
    }
}
