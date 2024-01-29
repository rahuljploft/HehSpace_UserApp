package com.hehspace_userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class PropertySearchModel {

    @Expose
    @SerializedName("Data")
    public List<DataEntity> data;
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
        @SerializedName("wishlist_status")
        public String wishlistStatus;
        @Expose
        @SerializedName("property_ratting")
        public String propertyRatting;
        @Expose
        @SerializedName("property_image_url")
        public String propertyImageUrl;
        @Expose
        @SerializedName("hourly_rate")
        public String hourlyRate;
        @Expose
        @SerializedName("property_time")
        public String propertyTime;
        @Expose
        @SerializedName("property_title")
        public String propertyTitle;
        @Expose
        @SerializedName("category_id")
        public String category_id;
        @Expose
        @SerializedName("property_id")
        public String propertyId;
    }
}
