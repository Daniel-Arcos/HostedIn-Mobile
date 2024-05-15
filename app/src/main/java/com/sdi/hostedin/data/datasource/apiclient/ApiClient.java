package com.sdi.hostedin.data.datasource.apiclient;

//import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseSignupObject;

import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseAuthObject;
import com.sdi.hostedin.data.model.GenericSingleString;
import com.sdi.hostedin.data.model.NewPasswordRecovery;
import com.sdi.hostedin.data.model.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class ApiClient {

    public interface  Service {

        @POST("auth/signup")
        Call<ResponseAuthObject> signUp(@Body User user);

        @POST("auth/signin")
        Call<ResponseAuthObject> signIn(@Body User user);

        @POST("passwords/sendemailcode")
        Call<Void> sendEmailCode(@Body GenericSingleString email);

        @POST("passwords/verifycode")
        Call<Void> verifyEmailCode(@Body GenericSingleString code);

        @POST("passwords/changepasswithcode")
        Call<Void> changePasswordByCode(@Header("authorization")GenericSingleString token, @Body NewPasswordRecovery newPassword);

    }

    Retrofit retrofit = new Retrofit.Builder()
            //Modificar con la URL de su computadora - red 192.168.50.7
            .baseUrl("http://192.168.1.75:3000/api/v1/")
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
