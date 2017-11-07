package com.pedromassango.programmers.mvp.policy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.pedromassango.programmers.R;

/**
 * Created by Pedro Massango on 21/06/2017 at 19:09.
 */

public class PrivacyPolicyDialogFragment extends AppCompatDialogFragment {

    private WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_privacy_policy, container, false);
        webView = view.findViewById(R.id.webview);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String privacyPolicyUrl = "file:///android_asset/privacy.html";
        webView.loadUrl(privacyPolicyUrl);
    }
}
