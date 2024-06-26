package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.callbacks.ReviewsCallback;
import com.sdi.hostedin.data.model.Review;
import com.sdi.hostedin.data.repositories.ReviewsRepository;

import java.util.List;

public class GetReviewsUseCase {
    ReviewsRepository reviewsRepository = new ReviewsRepository();

    public void getReviewsOfAccommodation(String accommodationId, String token, ReviewsCallback reviewsCallback) {
        reviewsRepository.getReviewsOfAccommodation(accommodationId, token, new ReviewsCallback() {
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
