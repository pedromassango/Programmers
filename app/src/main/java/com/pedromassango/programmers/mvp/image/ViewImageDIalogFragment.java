package com.pedromassango.programmers.mvp.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.extras.ImageUtils;

/**
 * Created by Pedro Massango on 21/06/2017 at 23:31.
 */

public class ViewImageDIalogFragment extends AppCompatDialogFragment implements Contract.View {

    private Presenter presenter;
    //  private TouchImageView imageView;
    private ImageView imageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view_mage, container, false);
        imageView = (ImageView) view.findViewById(R.id.img_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new Presenter(this);
        presenter.initialize(getArguments(), savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public Bitmap getBitmapImage() {

        return imageView.getDrawingCache();
    }

    @Override
    public void showImage(Bitmap bitmap) {

        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void showImage(String imgURL) {

        ImageUtils.loadImage(getActivity(), imgURL, imageView);
    }
}
