package com.sdi.hostedin.data.datasource.apiclient;

import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseAuthObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseEditAccountObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetUserObject;
import com.sdi.hostedin.data.model.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ApiClient {

    public interface  Service {

        @GET("users/{userId}")
        Call<ResponseGetUserObject> getUserById(@Path("userId") String userId);

        @POST("auth/signup")
        Call<ResponseAuthObject> signUp(@Body User user);

        @POST("auth/signin")
        Call<ResponseAuthObject> signIn(@Body User user);

        @PUT("users/{userId}")
        Call<ResponseEditAccountObject> updateUserById(@Path("userId") String userId, @Body User user);

        @DELETE("users/{userId}")
        Call<ResponseEditAccountObject> deleteUserById(@Path("userId") String userId);

    }

    Retrofit retrofit = new Retrofit.Builder()
            //Modificar con la URL de su computadora - red
            .baseUrl("http://192.168.100.7:3000/api/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

    private Service service;

    private  static  final ApiClient apiClient = new ApiClient();

    public  static ApiClient getInstance() {
        return apiClient;
    }

    public Service getService() {
        if (service == null) {
            service = retrofit.create(Service.class);
        }
        return service;
    }
}
