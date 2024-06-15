package com.sdi.hostedin.feature.statistics;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.MyApplication;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.AccommodationGrpc;
import com.sdi.hostedin.data.model.EarningGrpc;
import com.sdi.hostedin.data.repositories.StaticticsRepository;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class StaticticsViewModel extends AndroidViewModel {

    MutableLiveData<RequestStatus> requestBarOneStatusMutableLiveData = new MutableLiveData<>();
    MutableLiveData<RequestStatus> requestBarTwoStatusMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<AccommodationGrpc>> mostBookedAccommodationsLiveData = new MutableLiveData<>();
    MutableLiveData<List<AccommodationGrpc>> bestRatedAccommodationsLiveData = new MutableLiveData<>();
    MutableLiveData<List<AccommodationGrpc>> mostBookedAccommodationsHostLiveData = new MutableLiveData<>();
    MutableLiveData<List<EarningGrpc>> earningsHostLiveData = new MutableLiveData<>();

    private final ExecutorService executorService;
    public StaticticsViewModel(@NonNull Application application) {
        super(application);
        MyApplication myApplication = (MyApplication) application;
        executorService = myApplication.getExecutorService();
    }


    public MutableLiveData<List<AccommodationGrpc>> getMostBookedAccommodationsLiveData() {
        return mostBookedAccommodationsLiveData;
    }

    public MutableLiveData<List<AccommodationGrpc>> getBestRatedAccommodationsLiveData() {
        return bestRatedAccommodationsLiveData;
    }

    public MutableLiveData<RequestStatus> getRequestBarOneStatusMutableLiveData() {
        return requestBarOneStatusMutableLiveData;
    }

    public MutableLiveData<RequestStatus> getRequestBarTwoStatusMutableLiveData() {
        return requestBarTwoStatusMutableLiveData;
    }

    public MutableLiveData<List<AccommodationGrpc>> getMostBookedAccommodationsHostLiveData() {
        return mostBookedAccommodationsHostLiveData;
    }

    public MutableLiveData<List<EarningGrpc>> getEarningsHostLiveData() {
        return earningsHostLiveData;
    }

    public void loadMostBookedAccommodations() {
        Application app = this.getApplication();
        String token = DataStoreAccess.accessToken(app);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                requestBarOneStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
                StaticticsRepository staticticsRepository = new StaticticsRepository();
                staticticsRepository.getMostBookedAccommodations(token, new StaticticsRepository.GetAccommodations() {
                    @Override
                    public void onSuccess(List<AccommodationGrpc> accommodations) {
                        mostBookedAccommodationsLiveData.postValue(accommodations);
                        requestBarOneStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_recovered_statistics)));
                    }

                    @Override
                    public void onError(String message) {
                        requestBarOneStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.ERROR, getApplication().getString(R.string.messg_statistics_not_recovered)));
                    }
                });
            }
        });
    }

    public void loadBestRatedAccommodations() {
        Application app = this.getApplication();
        String token = DataStoreAccess.accessToken(app);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                requestBarTwoStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
                StaticticsRepository staticticsRepository = new StaticticsRepository();
                staticticsRepository.getBestRatedAccommodations(token, new StaticticsRepository.GetAccommodations() {
                    @Override
                    public void onSuccess(List<AccommodationGrpc> accommodations) {
                        bestRatedAccommodationsLiveData.postValue(accommodations);
                        requestBarTwoStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_recovered_statistics)));
                    }

                    @Override
                    public void onError(String message) {
                        requestBarTwoStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.ERROR, getApplication().getString(R.string.messg_statistics_not_recovered)));
                    }
                });
            }
        });
    }

    public void loadMostBookedHostAccommodations() {
        Application app = this.getApplication();
        String token = DataStoreAccess.accessToken(app);
        String idHost = DataStoreAccess.accessUserId(app);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                requestBarOneStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
                StaticticsRepository staticticsRepository = new StaticticsRepository();
                staticticsRepository.getMostBookedAccommodationsHost(token, idHost, new StaticticsRepository.GetAccommodations() {
                    @Override
                    public void onSuccess(List<AccommodationGrpc> accommodations) {
                        mostBookedAccommodationsHostLiveData.postValue(accommodations);
                        requestBarOneStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_recovered_statistics)));
                    }

                    @Override
                    public void onError(String message) {
                        requestBarOneStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.ERROR, getApplication().getString(R.string.messg_statistics_not_recovered)));
                    }
                });
            }
        });
    }

    public void loadHostEarnings() {
        Application app = this.getApplication();
        String token = DataStoreAccess.accessToken(app);
        String idHost = DataStoreAccess.accessUserId(app);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                requestBarTwoStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
                StaticticsRepository staticticsRepository = new StaticticsRepository();
                staticticsRepository.getEarnings(token, idHost, new StaticticsRepository.GetEarnings() {
                    @Override
                    public void onSuccess(List<EarningGrpc> earnings) {
                        earningsHostLiveData.postValue(earnings);
                        requestBarTwoStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_recovered_statistics)));
                    }

                    @Override
                    public void onError(String message) {
                        requestBarTwoStatusMutableLiveData.postValue(new RequestStatus(RequestStatusValues.ERROR, getApplication().getString(R.string.messg_statistics_not_recovered)));
                    }
                });
            }
        });
    }

}
