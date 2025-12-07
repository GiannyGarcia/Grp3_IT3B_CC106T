package com.example.sanisidropharmacy;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    Call<AuthResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<AuthResponse> register(
            @Field("fullname") String fullname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("contact") String contact,
            @Field("address") String address,
            @Field("birthdate") String birthdate,
            @Field("role") String role
    );


    // Add other endpoints later (createOrder, getUserOrders, updateLoyalty etc.)
}
