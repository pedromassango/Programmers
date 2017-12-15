package com.pedromassango.programmers.presentation.post._new;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.pedromassango.programmers.R;
import com.pedromassango.programmers.data.PostsRepository;
import com.pedromassango.programmers.data.RepositoryManager;
import com.pedromassango.programmers.data.prefs.PrefsHelper;
import com.pedromassango.programmers.extras.TextUtils;
import com.pedromassango.programmers.interfaces.Callbacks;
import com.pedromassango.programmers.models.Post;

import static com.pedromassango.programmers.extras.Util.showLog;

/**
 * Created by Pedro Massango on 23-02-2017 11:50.
 */

class Presenter implements Contract.Presenter, Callbacks.IResultCallback<Post> {

    private static final int RESULT_OK = -1;
    private Contract.View view;
    private PostsRepository postModel;
    private Uri imageUri = null;

    Presenter(Contract.View view) {
        this.view = view;
        this.postModel = RepositoryManager.getInstance().getPostsRepository();
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
        post.setAuthorId(PrefsHelper.getId());
        post.setAuthor(PrefsHelper.getName());
        post.setCategory(category);
        post.setCommentsActive(true);
        post.setTitle(title);
        post.setBody(description);
        post.setTimestamp(System.currentTimeMillis());

        view.showProgress();
        postModel.save(post, this);
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
    public void onSuccess(Post result) {

        view.dismissProgress();
        view.onSuccess();
    }

    @Override
    public void onDataUnavailable() {

        view.dismissProgress();
        view.onError();
    }
}
