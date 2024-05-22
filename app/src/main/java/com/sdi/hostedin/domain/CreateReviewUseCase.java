package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.callbacks.ReviewCallback;
import com.sdi.hostedin.data.model.Review;
import com.sdi.hostedin.data.repositories.ReviewsRepository;

public class CreateReviewUseCase {
    private ReviewsRepository reviewsRepository = new ReviewsRepository();

    public CreateReviewUseCase() {
    }

    public void saveNewReview(Review review, String token, ReviewCallback reviewCallback){
        reviewsRepository.saveNewReview(review, token, new ReviewCallback() {
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
