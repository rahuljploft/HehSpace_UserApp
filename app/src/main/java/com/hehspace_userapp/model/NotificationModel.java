package com.hehspace_userapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {

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
        @SerializedName("created_at")
        public String createdAt;
        @Expose
        @SerializedName("notification_details")
        public String notificationDetails;
        @Expose
        @SerializedName("notification_title")
        public String notificationTitle;
    }
}
