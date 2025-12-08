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

    // ---------------- AUTH: LOGIN ----------------
    @Headers({"Content-Type: application/x-www-form-urlencoded; charset=UTF-8"})
    @FormUrlEncoded
    @POST("login.php")
    Call<AuthResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    // ---------------- CRM: CREATE ORDER ----------------
    @POST("createOrder.php")
    Call<OrderResponse> createOrder(@Body RequestBody body);

    // ---------------- CRM: GET ORDER HISTORY (User + Admin) ----------------
    // user_id = -1 → return ALL orders (admin mode)
    @GET("getUserOrders.php")
    Call<OrderHistoryResponse> getUserOrders(
            @Query("user_id") int userId
    );

    // ---------------- CRM: LOYALTY ----------------
    @Headers({"Content-Type: application/x-www-form-urlencoded; charset=UTF-8"})
    @FormUrlEncoded
    @POST("updateLoyalty.php")
    Call<LoyaltyResponse> updateLoyalty(
            @Field("user_id") int userId,
            @Field("points") int points
    );

    @GET("getLoyalty.php")
    Call<LoyaltyResponse> getLoyalty(
            @Query("user_id") int userId
    );

    // Admin - get all orders
    @GET("getAllOrders.php")
    Call<OrderHistoryResponse> getAllOrders();

    // Admin - update order status
    @FormUrlEncoded
    @POST("updateOrderStatus.php")
    Call<BasicResponse> updateOrderStatus(
            @Field("order_id") int orderId,
            @Field("status") String status
    );

    // Admin - get loyalty summary
    @GET("adminGetLoyaltySummary.php")
    Call<AdminLoyaltySummaryResponse> getAdminLoyaltySummary();

    // Rewards - list available rewards
    @GET("getRewards.php")
    Call<RewardsResponse> getRewards();

    // Rewards - redeem
    @FormUrlEncoded
    @POST("redeemReward.php")
    Call<BasicResponse> redeemReward(
            @Field("user_id") int userId,
            @Field("reward_id") int rewardId
    );


}
