package com.sdi.hostedin.data.datasource.remote;

import android.util.Log;

import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseSignupObject;
import com.sdi.hostedin.data.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteUsersDataSource {

    ApiClient.Service service = ApiClient.getInstance().getService();
    public interface CreateUserCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    public RemoteUsersDataSource() {

    }

    public void createUserAccount(User user, CreateUserCallback createUserCallback){
        Call<ResponseSignupObject> call = service.signUp(user);
        call.enqueue(new Callback<ResponseSignupObject>() {
            @Override
            public void onResponse(Call<ResponseSignupObject> call, Response<ResponseSignupObject> response) {
                if (response.isSuccessful()) {
                    ResponseSignupObject responseSignupObject = response.body();
                    User userSaved = new User();
                    userSaved.setEmail(responseSignupObject.getUser().getEmail());
                    userSaved.setFullName(responseSignupObject.getUser().getFullName());
                    userSaved.setPhoneNumber(responseSignupObject.getUser().getPhoneNumber());
                    userSaved.setId(responseSignupObject.getUser().getPhoneNumber());
                    createUserCallback.onSuccess(userSaved, "");
                } else {
                    Log.e("log", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseSignupObject> call, Throwable t) {
                createUserCallback.onError(t.getMessage());
            }
        });
    }
}
