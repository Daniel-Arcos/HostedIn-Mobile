package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.callbacks.ReviewCallback;
import com.sdi.hostedin.data.datasource.remote.RemoteReviewsDataSource;
import com.sdi.hostedin.data.model.Review;

public class ReviewsRepository {
    private RemoteReviewsDataSource remoteReviewsDataSource = new RemoteReviewsDataSource();

    public void saveNewReview(Review review, String token, ReviewCallback reviewCallback){
        remoteReviewsDataSource.saveNewReview(review, token, new ReviewCallback() {
            @Override
            public void onSuccess(Review review, String message) {
                reviewCallback.onSuccess(review, message);
            }

            @Override
            public void onError(String errorMessage) {
                reviewCallback.onError(errorMessage);
            }
        });

    }
}
