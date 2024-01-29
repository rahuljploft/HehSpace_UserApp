package com.hehspace_userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class Bitcoin_payment_model {


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
        @SerializedName("charge_code")
        public String chargeCode;
        @Expose
        @SerializedName("url")
        public String url;
    }
}
