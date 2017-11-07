package com.pedromassango.programmers.mvp.post._new;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.models.Post;
import com.pedromassango.programmers.mvp.post.PostModel;
import com.pedromassango.programmers.extras.TextUtils;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 23-02-2017 11:50.
 */

class Presenter implements Contract.Presenter {

    private static final int RESULT_OK = -1;
    private Contract.View view;
    private PostModel postModel;
    private Uri imageUri = null;

    Presenter(Contract.View view) {
        this.view = view;
        this.postModel = new PostModel(this);
    }

    @Override
    public Context getContext() {
        return (Context) view;
    }

    @Override
    public void initialize(Intent intent) {

        view.setImagePickedLayoutVisibility(View.GONE);
    }

    @Override
    public void pickImageClicked() {

        Intent iPickImage = new Intent(Intent.ACTION_GET_CONTENT);
        iPickImage.setType("image/*");

        view.onPickImage(iPickImage);
    }

    @Override
    public void publishPostClicked() {
        String title = view.getPostTitle();
        String description = view.getDescription();
        String category = view.getCategory();

        if (title.isEmpty()) {
            view.setTitleError(R.string.empty_title);
            return;
        }

        if (TextUtils.lessOrEqualFiveDigits(title)) {
            view.setTitleError(R.string.title_too_short);
            return;
        }

        if (imageUri == null) {
            if (TextUtils.isEmpty(description)) {
                view.setDescriptionError(R.string.empty_description);
                return;
            }

            if (TextUtils.lessOrEqualFiveDigits(description)) {
                view.setTitleError(R.string.description_too_short);
                return;
            }
        }

        Post post = new Post();
        post.setAuthorId(postModel.getUserId());
        post.setAuthor(postModel.getUsername());
        post.setCategory(category);
        post.setCommentsActive(true);
        post.setTitle(title);
        post.setBody(description);
        post.setTimestamp(System.currentTimeMillis());

        view.showProgress();
        postModel.publishPost(getContext(), this, imageUri, post);
    }

    @Override
    public void clearCurrentSelectedImageClicked() {

        // Remove data of the selected Image
        imageUri = null;

        // Hide Image picked layout
        view.setImagePickedLayoutVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (null != data) {
                try {

                    imageUri = data.getData();

                    view.showImagePicked(imageUri);
                    view.setImagePickedLayoutVisibility(View.VISIBLE);
                } catch (Exception e) {
                    showLog("GET IMAGE ERROR: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onSuccess() {

        view.dismissProgress();
        view.onSuccess();
    }

    @Override
    public void onError(String error) {

        view.dismissProgress();
        view.onError(error);
    }
}
