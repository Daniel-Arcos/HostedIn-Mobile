package com.sdi.hostedin.data.datasource.remote;

import android.util.Log;

import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.model.EmailRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemotePasswordCodeDataSource {

    ApiClient.Service service = ApiClient.getInstance().getService();

    public interface SendPasswordCodeCallback{
        void onSucces(String message);
        void onError(String errorMessage);
    }

    public RemotePasswordCodeDataSource(){}

    public void sendEmailPasswordCode(String email, SendPasswordCodeCallback sendPasswordCodeCallback){
        Call<Void> call = service.sendEmailCode(new EmailRequest(email));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    sendPasswordCodeCallback.onSucces("Email sent successfully");
                }
                else{
                    Log.e("log", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sendPasswordCodeCallback.onError(t.getMessage());
            }
        });
    }

}
