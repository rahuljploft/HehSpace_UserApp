package com.hehspace_userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public  class PropertyDetailModel {

    @Expose
    @SerializedName("Data")
    public DataEntity data;
    @Expose
    @SerializedName("Message")
    public String message;
    @Expose
    @SerializedName("Status")
    public String status;
    @Expose
    @SerializedName("ResponseCode")
    public String responsecode;

    public static class DataEntity {
        @Expose
        @SerializedName("property_review_list")
        public ArrayList<PropertyReviewList> propertyReviewList;
        @Expose
        @SerializedName("property_images")
        public ArrayList<PropertyImagesEntity> propertyImages;
        @Expose
        @SerializedName("host_details")
        public HostDetailsEntity hostDetails;
        @Expose
        @SerializedName("ratting_by_me")
        public String rattingByMe;
        @Expose
        @SerializedName("wishlist_status")
        public String wishlistStatus;
        @Expose
        @SerializedName("property_services")
        public List<PropertyService> propertyServices;
        @Expose
        @SerializedName("property_details")
        public String propertyDetails;
        @Expose
        @SerializedName("property_higlights")
        public String propertyHiglights;
        @Expose
        @SerializedName("property_anenities")
        public String propertyAnenities;
        @Expose
        @SerializedName("property_category")
        public String propertyCategory;
        @Expose
        @SerializedName("five_start_ratio")
        public String fiveStartRatio;
        @Expose
        @SerializedName("property_review")
        public String propertyReview;
        @Expose
        @SerializedName("property_ratting")
        public String propertyRatting;
        @Expose
        @SerializedName("property_status")
        public String propertyStatus;
        @Expose
        @SerializedName("cleaner_fee")
        public String cleanerFee;
        @Expose
        @SerializedName("hourly_rate")
        public String hourlyRate;
        @Expose
        @SerializedName("property_location")
        public String propertyLocation;
        @Expose
        @SerializedName("no_of_guest")
        public String noOfGuest;
        @Expose
        @SerializedName("available_check_out_time")
        public List<String> availableCheckOutTime;
        @Expose
        @SerializedName("available_check_in_time")
        public List<String> availableCheckInTime;
        @Expose
        @SerializedName("property_time")
        public String propertyTime;
        @Expose
        @SerializedName("checkout_time")
        public String checkoutTime;
        @Expose
        @SerializedName("checkin_time")
        public String checkinTime;
        @Expose
        @SerializedName("property_title")
        public String propertyTitle;
        @Expose
        @SerializedName("property_id")
        public String propertyId;
        @Expose
        @SerializedName("category_id")
        public String category_id;
    }

    public static class PropertyImagesEntity {
        @Expose
        @SerializedName("property_image_url")
        public String propertyImageUrl;
    }

    public static class HostDetailsEntity {
        @Expose
        @SerializedName("user_image")
        public String userImage;
        @Expose
        @SerializedName("full_name")
        public String fullName;
    }

    public static class PropertyService {
        @Expose
        @SerializedName("service_id")
        public String service_id;
        @Expose
        @SerializedName("services_title")
        public String services_title;
        @Expose
        @SerializedName("services_rate")
        public String services_rate;

        public boolean isselected = false;
    }

    public static class PropertyReviewList {
        @Expose
        @SerializedName("full_name")
        public String full_name;
        @Expose
        @SerializedName("user_image")
        public String user_image;
        @Expose
        @SerializedName("ratting_comment")
        public String ratting_comment;
        @Expose
        @SerializedName("ratting_star")
        public String ratting_star;
        @Expose
        @SerializedName("created_at")
        public String created_at;
    }
}
