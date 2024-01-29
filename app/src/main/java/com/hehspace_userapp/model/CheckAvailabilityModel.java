package com.hehspace_userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class CheckAvailabilityModel {

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
        @SerializedName("calculate_price")
        public CalculatePriceEntity calculatePrice;
        @Expose
        @SerializedName("property_price")
        public PropertyPriceEntity propertyPrice;
    }

    public static class CalculatePriceEntity {
        @Expose
        @SerializedName("total_amount")
        public String totalAmount;
        @Expose
        @SerializedName("cleaner_fee")
        public String cleanerFee;
        @Expose
        @SerializedName("tax_amount")
        public String taxAmount;
        @Expose
        @SerializedName("tax_percentage")
        public String taxPercentage;
        @Expose
        @SerializedName("services_amount")
        public String servicesAmount;
        @Expose
        @SerializedName("property_amount")
        public String propertyAmount;
        @Expose
        @SerializedName("hourly_amount")
        public String hourlyAmount;
        @Expose
        @SerializedName("stay_hours")
        public String stayHours;
        @Expose
        @SerializedName("daily_amount")
        public String dailyAmount;
        @Expose
        @SerializedName("stay_days")
        public String stayDays;
        @Expose
        @SerializedName("weekly_amount")
        public String weeklyAmount;
        @Expose
        @SerializedName("stay_weeks")
        public String stayWeeks;
        @Expose
        @SerializedName("monthly_amount")
        public String monthlyAmount;
        @Expose
        @SerializedName("stay_months")
        public String stayMonths;
    }

    public static class PropertyPriceEntity {
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
}
