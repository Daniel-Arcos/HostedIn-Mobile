package com.sdi.hostedin.data.datasource.apiclient.responseobjects;

import com.sdi.hostedin.data.model.Review;

public class ReponseReviewObject {
    private String message;
    private Review review;

    public ReponseReviewObject() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
