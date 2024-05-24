package com.sdi.hostedin.data.datasource.apiclient;

import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetReviewsObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseReviewObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseAccommodationObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseAuthObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseBookedAccommodation;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseBookingObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseBookingsListObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseEditAccountObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetAccommodationsObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetUserObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGuestBookedAccommodations;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.model.GenericSingleString;
import com.sdi.hostedin.data.model.NewPasswordRecovery;
import com.sdi.hostedin.data.model.Review;
import com.sdi.hostedin.data.model.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiClient {

    public interface Service {
        // Users
        @GET("users/{userId}")
        Call<ResponseGetUserObject> getUserById(@Path("userId") String userId, @Header("Authorization") String authToken);

        @POST("auth/signup")
        Call<ResponseAuthObject> signUp(@Body User user);

        @POST("auth/signin")
        Call<ResponseAuthObject> signIn(@Body User user);

        @POST("users/passwords")
        Call<Void> createPasswordCode(@Body GenericSingleString email);

        @POST("users/passwords/code")
        Call<Void> createCodeToken(@Body GenericSingleString code);

        @PATCH("users/passwords")
        Call<Void> updateUserPassword(@Header("authorization")String token, @Body NewPasswordRecovery newPassword);

        @PUT("users/{userId}")
        Call<ResponseEditAccountObject> updateUserById(@Path("userId") String userId, @Body User user, @Header("Authorization") String authToken);

        @DELETE("users/{userId}")
        Call<ResponseEditAccountObject> deleteUserById(@Path("userId") String userId, @Header("Authorization") String authToken);

        // Accommodations
        @POST("accommodations")
        Call<ResponseAccommodationObject> createAccommodation(@Body Accommodation accommodation, @Header("Authorization") String authToken);

        @GET("accommodations/{accommodationId}/bookings/")
        Call<ResponseBookingsListObject> getBookingsOfSpecificAccommodation(@Path("accommodationId") String accommodationId, @Header("Authorization") String authToken);

        @GET("accommodations")
        Call<ResponseGetAccommodationsObject> getAllAccommodations(@Header("Authorization") String authToken);

        @GET("accommodations")
        Call<ResponseGetAccommodationsObject> getAllAccommodationsExceptUserAccommodations(@Query("id") String id, @Header("Authorization") String authToken);

        @GET("accommodations")
        Call<ResponseGetAccommodationsObject> getAccommodationsByLocationExceptUserAccommodations(
                @Query("lat") double lat,
                @Query("long") double lng,
                @Query("id") String id,
                @Header("Authorization") String authToken
        );
        @GET("users/{userId}/accommodations")
        Call<ResponseBookedAccommodation> getHostBookedAccommodations(@Path("userId") String userId, @Query("atLeastOneBooking") boolean atLeastOneBooking);

        @GET("users/{userId}/accommodations")
        Call<ResponseGetAccommodationsObject> getAllHostAccommodations(@Header("Authorization") String authToken, @Path("userId") String userId);

        @GET("users/{userId}/accommodations")
        Call<ResponseGuestBookedAccommodations> getGuestBookedAccommodations(@Path("userId") String userId, @Query("status") String status);

        // Bookings
        @POST("bookings")
        Call<ResponseBookingObject> createBooking(@Body Booking booking, @Header("Authorization") String authToken);

        @POST("reviews")
        Call<ResponseReviewObject> createReview(@Header("Authorization") String authToken, @Body Review review);

        @GET("accommodations/{accommodationId}/reviews")
        Call<ResponseGetReviewsObject> getReviewsOfAccommodation(@Header("Authorization") String authToken, @Path("accommodationId") String accommodationId);

        @PUT("accommodations/{accommodationId}")
        Call<ResponseAccommodationObject> updateAccommodation(@Header("Authorization") String token, @Path("accommodationId") String accommodationId, @Body Accommodation accommodation);

    }



    Retrofit retrofit = new Retrofit.Builder()
            //Modificar con la URL de su computadora - red
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
