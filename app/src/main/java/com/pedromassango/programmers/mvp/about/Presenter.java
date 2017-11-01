package com.pedromassango.programmers.mvp.about;

/**
 * Created by Pedro Massango on 02/06/2017.
 */

class Presenter implements Contract.Presenter {

    private Contract.View view;

    public Presenter(Contract.View view) {
        this.view = view;
    }

    @Override
    public void donateClicked() {

        view.startDonateActivity();
    }

    @Override
    public void termsAndConditionsClicked() {

        view.showTermsAndConditions();
    }
}
