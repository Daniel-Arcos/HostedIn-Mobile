package com.sdi.hostedin.feature.guest.explore.accommodationdetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.ReviewsCallback;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Review;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.GetReviewsUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.List;

public class AccommodationDetailsViewModel extends AndroidViewModel {

    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Accommodation> accommodationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Review>> reviewsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Float> score = new MutableLiveData<>();

    public AccommodationDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<Accommodation> getAccommodationMutableLiveData() {
        return accommodationMutableLiveData;
    }

    public MutableLiveData<List<Review>> getReviewsMutableLiveData() {
        return reviewsMutableLiveData;
    }

    public MutableLiveData<Float> getScore() {
        return score;
    }

    public void getAccommodationReviews(String accommodationId) {
        GetReviewsUseCase getReviewsUseCase = new GetReviewsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));

        String token = DataStoreAccess.accessToken(getApplication());
        getReviewsUseCase.getReviewsOfAccommodation(accommodationId, token, new ReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews, String token) {
                reviewsMutableLiveData.setValue(reviews);
                if (token != null && !token.equals("")) {
                    DataStoreAccess.saveToken(getApplication(), token);
                }
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, "Reviews recovered"));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

    public void calculateScore(List<Float> scores) {
        int scoresNumber = scores.size();

        if (scoresNumber > 0) {
            float sum = 0;
            for (Float scoreAc: scores) {
                sum += scoreAc;
            }

            float average = sum/scoresNumber;
            score.setValue(average);
        }
    }
}
