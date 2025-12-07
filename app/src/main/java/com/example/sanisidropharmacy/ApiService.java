package com.example.sanisidropharmacy;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // ---------------- AUTH: REGISTER (JSON BODY) ----------------
    @Headers({"Content-Type: application/json"})
    @POST("register.php")
    Call<AuthResponse> register(@Body RequestBody body);

    // ---------------- AUTH: LOGIN (FORM DATA OK) ----------------
    @Headers({"Content-Type: application/x-www-form-urlencoded; charset=UTF-8"})
    @FormUrlEncoded
    @POST("login.php")
    Call<AuthResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    // ---------------- CRM: ORDERS ----------------
    @POST("createOrder.php")
    Call<OrderResponse> createOrder(@Body RequestBody body);

    @GET("getUserOrders.php")
    Call<OrderHistoryResponse> getUserOrders(@Query("user_id") int userId);

    // ---------------- CRM: LOYALTY ----------------
    @Headers({"Content-Type: application/x-www-form-urlencoded; charset=UTF-8"})
    @FormUrlEncoded
    @POST("updateLoyalty.php")
    Call<LoyaltyResponse> updateLoyalty(
            @Field("user_id") int userId,
            @Field("points") int points
    );

    @GET("getLoyalty.php")
    Call<LoyaltyResponse> getLoyalty(@Query("user_id") int userId);
}
