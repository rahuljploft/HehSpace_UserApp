package com.hehspace_userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class RequestDetailsModel {

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
        @SerializedName("property_images")
        public List<PropertyImagesEntity> propertyImages;
        @Expose
        @SerializedName("price_calculation")
        public PriceCalculationEntity priceCalculation;
        @Expose
        @SerializedName("time_calculation")
        public TimeCalculationEntity timeCalculation;
        @Expose
        @SerializedName("property_rate")
        public PropertyRateEntity propertyRate;
        @Expose
        @SerializedName("addon_services")
        public List<AddonServicesEntity> addonServices;
        @Expose
        @SerializedName("host_details")
        public HostDetailsEntity hostDetails;
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
        @SerializedName("property_review")
        public String propertyReview;
        @Expose
        @SerializedName("property_ratting")
        public String propertyRatting;
        @Expose
        @SerializedName("property_location")
        public String propertyLocation;
        @Expose
        @SerializedName("property_title")
        public String propertyTitle;
        @Expose
        @SerializedName("property_id")
        public String propertyId;
        @Expose
        @SerializedName("number_of_guest")
        public String numberOfGuest;
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
        @SerializedName("checkout_date")
        public String checkoutDate;
        @Expose
        @SerializedName("checkin_date")
        public String checkinDate;
        @Expose
        @SerializedName("request_id")
        public String requestId;
    }

    public static class PropertyImagesEntity {
        @Expose
        @SerializedName("property_image_url")
        public String propertyImageUrl;
    }

    public static class PriceCalculationEntity {
        @Expose
        @SerializedName("cleaner_fee")
        public String cleanerFee;
        @Expose
        @SerializedName("total_amount")
        public String totalAmount;
        @Expose
        @SerializedName("tax_amount")
        public String taxAmount;
        @Expose
        @SerializedName("services_amount")
        public String servicesAmount;
        @Expose
        @SerializedName("hourly_amount")
        public String hourlyAmount;
        @Expose
        @SerializedName("daily_amount")
        public String dailyAmount;
        @Expose
        @SerializedName("weekly_amount")
        public String weeklyAmount;
        @Expose
        @SerializedName("monthly_amount")
        public String monthlyAmount;
    }

    public static class TimeCalculationEntity {
        @Expose
        @SerializedName("stay_hours")
        public String stayHours;
        @Expose
        @SerializedName("stay_days")
        public String stayDays;
        @Expose
        @SerializedName("stay_weeks")
        public String stayWeeks;
        @Expose
        @SerializedName("stay_months")
        public String stayMonths;
    }

    public static class PropertyRateEntity {
        @Expose
        @SerializedName("hourly_rate")
        public String hourlyRate;
        @Expose
        @SerializedName("daily_rate")
        public String dailyRate;
        @Expose
        @SerializedName("weekly_rate")
        public String weeklyRate;
        @Expose
        @SerializedName("monthly_rate")
        public String monthlyRate;
    }

    public static class AddonServicesEntity {
        @Expose
        @SerializedName("services_rate")
        public String servicesRate;
        @Expose
        @SerializedName("services_title")
        public String servicesTitle;
    }

    public static class HostDetailsEntity {
        @Expose
        @SerializedName("user_image")
        public String userImage;
        @Expose
        @SerializedName("full_name")
        public String fullName;
    }
}
