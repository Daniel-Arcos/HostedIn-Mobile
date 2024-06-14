package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.callbacks.ReviewCallback;
import com.sdi.hostedin.data.callbacks.ReviewsCallback;
import com.sdi.hostedin.data.datasource.remote.RemoteReviewsDataSource;
import com.sdi.hostedin.data.model.Review;

import java.util.List;

public class ReviewsRepository {
    private RemoteReviewsDataSource remoteReviewsDataSource = new RemoteReviewsDataSource();

    public void saveNewReview(Review review, String token, ReviewCallback reviewCallback){
        remoteReviewsDataSource.saveNewReview(review, token, new ReviewCallback() {
            @Override
            public void onSuccess(Review review, String newToken) {
                reviewCallback.onSuccess(review, newToken);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                reviewCallback.onError(errorMessage, newToken);
            }
        });
    }

    public void getReviewsOfAccommodation(String accommodationId, String token, ReviewsCallback reviewsCallback) {
        remoteReviewsDataSource.getReviewsOfAccommodation(accommodationId, token, new ReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews, String newToken) {
                reviewsCallback.onSuccess(reviews, newToken);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                reviewsCallback.onError(errorMessage, newToken);
            }
        });
    }
}
