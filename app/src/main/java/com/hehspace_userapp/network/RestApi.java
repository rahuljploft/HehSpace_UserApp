package com.hehspace_userapp.network;


import com.hehspace_userapp.model.Bitcoin_Payment_Check_Model;
import com.hehspace_userapp.model.Bitcoin_payment_model;
import com.hehspace_userapp.model.BookingDetailsModel;
import com.hehspace_userapp.model.BookingListModel;
import com.hehspace_userapp.model.ChatModel;
import com.hehspace_userapp.model.ChatUserListModel;
import com.hehspace_userapp.model.CheckAvailabilityModel;
import com.hehspace_userapp.model.CityModel;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.DateListModel;
import com.hehspace_userapp.model.ForgotPasswordModel;
import com.hehspace_userapp.model.LoginModel;
import com.hehspace_userapp.model.NotificationModel;
import com.hehspace_userapp.model.PageModel;
import com.hehspace_userapp.model.ProfileModel;
import com.hehspace_userapp.model.PropertyAmenitiesModel;
import com.hehspace_userapp.model.PropertyCategoryModel;
import com.hehspace_userapp.model.PropertyDetailModel;
import com.hehspace_userapp.model.PropertyListModel;
import com.hehspace_userapp.model.PropertySearchModel;
import com.hehspace_userapp.model.RequestDetailsModel;
import com.hehspace_userapp.model.RequestListModel;
import com.hehspace_userapp.model.VerifyOtpModel;
import com.hehspace_userapp.model.WishListModel;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestApi {

    @Multipart
    @POST("/api/create_account")
    Observable<LoginModel> createAccount(
            @Part MultipartBody.Part first_name,
            @Part MultipartBody.Part last_name,
            @Part MultipartBody.Part email_address,
            @Part MultipartBody.Part mobile_number,
            @Part MultipartBody.Part gender_id,
            @Part MultipartBody.Part user_password,
            @Part MultipartBody.Part social_media_id,
            @Part MultipartBody.Part user_type,
            @Part MultipartBody.Part id_document,
            @Part MultipartBody.Part device_token
    );


    @FormUrlEncoded
    @POST("/api/sent_otp")
    Observable<VerifyOtpModel> verifyOTP(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/login_account")
    Observable<LoginModel> loginApi(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/login_social_media")
    Observable<LoginModel> socialloginApi(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/forgot_password")
    Observable<ForgotPasswordModel> forgotPassword(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/reset_password")
    Observable<CommonModel> resetPassword(@FieldMap HashMap<String, String> reqData);

    @GET("/api/notification/list")
    Observable<NotificationModel> notificationList();

    @GET("/api/notification/delete")
    Observable<CommonModel> deleteNotification();

    @GET("/api/notification/seen")
    Observable<CommonModel> seenNotification();

    @GET("/api/profile")
    Observable<ProfileModel> profileDetails();

    @Multipart
    @POST("/api/update_profile")
    Observable<ProfileModel> updateprofileDetails(
            @Part MultipartBody.Part first_name,
            @Part MultipartBody.Part last_name,
            @Part MultipartBody.Part email_address,
            @Part MultipartBody.Part mobile_number,
            @Part MultipartBody.Part gender_id,
            @Part MultipartBody.Part user_type,
            @Part MultipartBody.Part user_photo,
            @Part MultipartBody.Part id_document,
            @Part MultipartBody.Part _method
    );

    @GET("/api/property/category")
    Observable<PropertyCategoryModel> propertyCategory();

    @FormUrlEncoded
    @POST("/api/user/property/list")
    Observable<PropertyListModel> propertyList(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/user/property/details")
    Observable<PropertyDetailModel> propertyDetails(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/user/wishlist/create")
    Observable<CommonModel> AddToFav(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/user/booking/check_bitcoin_payment_response")
    Observable<Bitcoin_Payment_Check_Model> checkpayment(@FieldMap HashMap<String, String> reqData);

    @GET("/api/user/wishlist/list")
    Observable<WishListModel> getWishList();

    @GET("/api/get_city")
    Observable<CityModel> getCity();

    @GET("/api/property/anenities")
    Observable<PropertyAmenitiesModel> getPropertyAmenities();

    @FormUrlEncoded
    @POST("/api/user/property/search")
    Observable<PropertySearchModel> searchProperty(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/check_availability")
    Observable<CheckAvailabilityModel> checAvailability(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/user/request/create")
    Observable<CommonModel> createRequest(@FieldMap HashMap<String, String> reqData);

    @GET("/api/user/request/list")
    Observable<RequestListModel> getRequesList();

    @GET("/api/user/request/details/{request_id}")
    Observable<RequestDetailsModel> getRequestDetails(@Path("request_id") int itemId);

    @GET("/api/booked_dates/{property_id}")
    Observable<DateListModel> getDateList(@Path("property_id") int itemId);

    @GET("/api/user/booking/list/")
    Observable<BookingListModel> getBookingList();

    @GET("/api/user/booking/details/{booking_id}")
    Observable<BookingDetailsModel> getBookingDetails(@Path("booking_id") int itemId);

    @FormUrlEncoded
    @POST("/api/user/booking/feedback")
    Observable<CommonModel> addFeedback(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/user/booking/cancel/{booking_id}")
    Observable<CommonModel> cancelBooking(@Path("booking_id") int itemId,@FieldMap HashMap<String, String> reqData);

    @Multipart
    @POST("/api/user/booking/create")
    Observable<CommonModel> createBooking(
            @Part MultipartBody.Part book_for,
            @Part MultipartBody.Part request_id,
            @Part MultipartBody.Part card_token,
            @Part MultipartBody.Part first_name,
            @Part MultipartBody.Part last_name,
            @Part MultipartBody.Part email_address,
            @Part MultipartBody.Part mobile_number,
            @Part MultipartBody.Part id_document,
            @Part MultipartBody.Part card_no,
            @Part MultipartBody.Part exp_month,
            @Part MultipartBody.Part exp_year,
            @Part MultipartBody.Part cvc
    );
    @Multipart
    @POST("/api/user/booking/create_booking_bitcoin")
    Observable<Bitcoin_payment_model> create_booking_bitcoin(
            @Part MultipartBody.Part book_for,
            @Part MultipartBody.Part request_id,
            @Part MultipartBody.Part card_token,
            @Part MultipartBody.Part first_name,
            @Part MultipartBody.Part last_name,
            @Part MultipartBody.Part email_address,
            @Part MultipartBody.Part mobile_number,
            @Part MultipartBody.Part id_document
    );
    @GET("/api/chat_user_list")
    Observable<ChatUserListModel> getChatUserList();

    @GET("/api/get_pages")
    Observable<PageModel> getPages();

    @FormUrlEncoded
    @POST("/api/chat_list")
    Observable<ChatModel> chatList(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/create_chat")
    Observable<CommonModel> sendMessage(@FieldMap HashMap<String, String> reqData);

    @FormUrlEncoded
    @POST("/api/delete_chat")
    Observable<CommonModel> deleteChat(@FieldMap HashMap<String, String> reqData);
}
