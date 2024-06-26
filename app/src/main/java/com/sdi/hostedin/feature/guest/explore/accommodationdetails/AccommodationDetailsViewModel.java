package com.sdi.hostedin.feature.guest.explore.accommodationdetails;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.MyApplication;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.callbacks.ReviewsCallback;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Review;
import com.sdi.hostedin.data.repositories.MultimediasRepository;
import com.sdi.hostedin.domain.GetReviewsUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class AccommodationDetailsViewModel extends AndroidViewModel {

    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Accommodation> accommodationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Review>> reviewsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Float> score = new MutableLiveData<>();
    private final ExecutorService executorService;
    private MutableLiveData<List<byte[]>> multimediasListMutableLiveData = new MutableLiveData<>();
    private final Handler mainHandler;

    public AccommodationDetailsViewModel(@NonNull Application application) {
        super(application);
        MyApplication myApplication = (MyApplication) application;
        executorService = myApplication.getExecutorService();
        mainHandler = myApplication.getMainThreadHandler();
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

    public MutableLiveData<List<byte[]>> getMultimediasListMutableLiveData() {
        return multimediasListMutableLiveData;
    }

    public void getAccommodationReviews(String accommodationId) {
        GetReviewsUseCase getReviewsUseCase = new GetReviewsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));

        String token = DataStoreAccess.accessToken(getApplication());
        getReviewsUseCase.getReviewsOfAccommodation(accommodationId, token, new ReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews, String newToken) {
                reviewsMutableLiveData.setValue(reviews);
                if (newToken != null && !newToken.equals("")) {
                    DataStoreAccess.saveToken(getApplication(), newToken);
                }
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.mssg_reviews_recovered)));
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                if (newToken != null && !newToken.equals("")) {
                    DataStoreAccess.saveToken(getApplication(), token);
                }
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

    public void loadAccommodationMultimedia(String _id) {
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
        MultimediasRepository multimediasRepository = new MultimediasRepository(executorService);
        multimediasRepository.loadAccommodationMultimedia(_id, new MultimediasRepository.LoadAccommodationMultimediaCallback() {
            @Override
            public void onSuccess(List<byte[]> multimedias) {
                //multimediasListMutableLiveData.postValue(multimedias);
            }

            @Override
            public void onSuccess(byte[] multimedia) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
                List<byte[]> currentList = multimediasListMutableLiveData.getValue();
                if (currentList == null) {
                    currentList = new ArrayList<>();
                }
                currentList.add(multimedia);
                multimediasListMutableLiveData.postValue(currentList);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_multimedia_recovered)));
            }

            @Override
            public void onError(String message) {
                List<byte[]> currentList = multimediasListMutableLiveData.getValue();
                if (currentList == null) {
                    currentList = new ArrayList<>();
                }
                byte[] genericMulti = new byte[1];
                currentList.add(genericMulti);
                multimediasListMutableLiveData.postValue(currentList);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_multimedia_not_recovered)));
            }
        }, mainHandler);
    }

    public void loadAllAccommodationMultimedia(String _id) {
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        MultimediasRepository multimediasRepository = new MultimediasRepository(executorService);
        multimediasRepository.loadAllAccommodationMultimedia(_id, new MultimediasRepository.LoadAccommodationMultimediaCallback() {
            @Override
            public void onSuccess(List<byte[]> multimedias) {
                multimediasListMutableLiveData.setValue(multimedias);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_multimedia_recovered)));
            }

            @Override
            public void onSuccess(byte[] multimedia) {

            }

            @Override
            public void onError(String message) {
                List<byte[]> currentList = multimediasListMutableLiveData.getValue();
                if (currentList == null) {
                    currentList = new ArrayList<>();
                }
                byte[] genericMulti = new byte[1];
                byte[] genericMulti2 = new byte[1];
                byte[] genericMulti3 = new byte[1];
                byte[] genericMulti4 = new byte[1];
                currentList.add(genericMulti);
                currentList.add(genericMulti2);
                currentList.add(genericMulti3);
                currentList.add(genericMulti4);
                multimediasListMutableLiveData.postValue(currentList);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, getApplication().getString(R.string.messg_multimedia_not_recovered)));
            }
        }, mainHandler);
    }
}
