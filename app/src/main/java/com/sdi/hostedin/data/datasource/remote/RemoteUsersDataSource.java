package com.sdi.hostedin.data.datasource.remote;

import android.util.Log;

import com.sdi.hostedin.data.datasource.apiclient.ApiClient;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseEditAccountObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetUserObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseSignupObject;
import com.sdi.hostedin.data.model.ProfilePhoto;
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

    public interface EditAccountCallback {
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

    public void editUserAccount(User user, EditAccountCallback editAccountCallback) {
        Call<ResponseEditAccountObject> call = service.updateUserById(user.getId(), user);

        call.enqueue(new Callback<ResponseEditAccountObject>() {
            @Override
            public void onResponse(Call<ResponseEditAccountObject> call, Response<ResponseEditAccountObject> response) {
                if (response.isSuccessful()) {
                    ResponseEditAccountObject responseEditAccountObject = response.body();
                    User editedUser = new User();
                    editAccountCallback.onSuccess(editedUser, "");
                }
            }

            @Override
            public void onFailure(Call<ResponseEditAccountObject> call, Throwable t) {
                editAccountCallback.onError(t.getMessage());
            }
        });
    }

    public void getUserById(String userId, EditAccountCallback editAccountCallback) {
        Call<ResponseGetUserObject> call = service.getUserById(userId);

        call.enqueue(new Callback<ResponseGetUserObject>() {
            @Override
            public void onResponse(Call<ResponseGetUserObject> call, Response<ResponseGetUserObject> response) {
                if (response.isSuccessful()) {
                    ResponseGetUserObject responseGetUserObject = response.body();
                    User userFound = responseGetUserObject.getUser();

                    editAccountCallback.onSuccess(userFound, "");
                }
            }

            @Override
            public void onFailure(Call<ResponseGetUserObject> call, Throwable t) {
                editAccountCallback.onError(t.getMessage());
            }
        });
    }
}
