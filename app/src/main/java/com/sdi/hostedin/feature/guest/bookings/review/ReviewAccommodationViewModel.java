package com.sdi.hostedin.feature.guest.bookings.review;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.ReviewCallback;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Review;
import com.sdi.hostedin.domain.CreateReviewUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class ReviewAccommodationViewModel extends AndroidViewModel {
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();

    public ReviewAccommodationViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public void saveNewReview(Review review){
        CreateReviewUseCase createReviewUseCase = new CreateReviewUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        String token = DataStoreAccess.accessToken(getApplication());
        createReviewUseCase.saveNewReview(review, token, new ReviewCallback() {
            @Override
            public void onSuccess(Review review, String newToken) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, newToken));
                if(newToken != null && !newToken.isEmpty()){
                    DataStoreAccess.saveToken(getApplication(), newToken);
                }
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                if(newToken != null && !newToken.isEmpty()){
                    DataStoreAccess.saveToken(getApplication(), newToken);
                }
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }
}
