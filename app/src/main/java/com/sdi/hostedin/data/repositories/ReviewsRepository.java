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
            public void onSuccess(Review review, String message) {
                reviewCallback.onSuccess(review, message);
            }

            @Override
            public void onError(String errorMessage) {
                reviewCallback.onError(errorMessage);
            }
        });
    }

    public void getReviewsOfAccommodation(String accommodationId, String token, ReviewsCallback reviewsCallback) {
        remoteReviewsDataSource.getReviewsOfAccommodation(accommodationId, token, new ReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews, String token) {
                reviewsCallback.onSuccess(reviews, token);
            }

            @Override
            public void onError(String errorMessage) {
                reviewsCallback.onError(errorMessage);
            }
        });
    }
}
