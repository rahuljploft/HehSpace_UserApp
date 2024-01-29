package com.hehspace_userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class CityModel {

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
        @SerializedName("city_name")
        public String cityName;
        @Expose
        @SerializedName("country_name")
        public String countryName;
    }
}
