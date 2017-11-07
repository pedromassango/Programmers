package com.pedromassango.programmers.mvp.about;

import com.pedromassango.programmers.mvp.base.BaseContract;

/**
 * Created by Pedro Massango on 02/06/2017.
 */

class Contract {

    interface Model{

    }

    interface View{

        void startDonateActivity();

        void showTermsAndConditions();
    }

    //interface Presenter extends BaseContract.PresenterImpl{
    interface Presenter {

        void donateClicked();

        void termsAndConditionsClicked();
    }
}
