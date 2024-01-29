package com.hehspace_userapp.model;

import java.io.Serializable;
import java.util.List;

public class ReviewRequestModel implements Serializable {
    String propertyimg;

    public String getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(String propertyid) {
        this.propertyid = propertyid;
    }

    String propertyid;

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    String categoryid;
    String propertyname;
    String propertylocation;
    String reviewrating;
    String checkindate;
    String checkintime;
    String checkoutdate;
    String checkouttime;
    String propertyType;
    String guestCount;

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    String phonenumber;
    String TotalPrice;
    String DailyPrice;
    String HourlyPrice;
    String WeeklyPrice;
    List<AdditionalSeviceModel> list;

    public List<AdditionalSeviceModel> getList() {
        return list;
    }

    public void setList(List<AdditionalSeviceModel> list) {
        this.list = list;
    }

    public String getHourlyPrice() {
        return HourlyPrice;
    }

    public void setHourlyPrice(String hourlyPrice) {
        HourlyPrice = hourlyPrice;
    }

    String MonthlyPrice;
    String CleaningFee;
    String ExtraServiceCharge;
    String ServiceTax;

    String monthly_rate;
    String weekly_rate;
    String daily_rate;
    String hourly_rate;
    String stay_months;
    String stay_weeks;
    String stay_days;
    String stay_hours;
    String property_amount;
    String tax_percentage;

    public String getMonthly_rate() {
        return monthly_rate;
    }

    public void setMonthly_rate(String monthly_rate) {
        this.monthly_rate = monthly_rate;
    }

    public String getWeekly_rate() {
        return weekly_rate;
    }

    public void setWeekly_rate(String weekly_rate) {
        this.weekly_rate = weekly_rate;
    }

    public String getDaily_rate() {
        return daily_rate;
    }

    public void setDaily_rate(String daily_rate) {
        this.daily_rate = daily_rate;
    }

    public String getHourly_rate() {
        return hourly_rate;
    }

    public void setHourly_rate(String hourly_rate) {
        this.hourly_rate = hourly_rate;
    }

    public String getStay_months() {
        return stay_months;
    }

    public void setStay_months(String stay_months) {
        this.stay_months = stay_months;
    }

    public String getStay_weeks() {
        return stay_weeks;
    }

    public void setStay_weeks(String stay_weeks) {
        this.stay_weeks = stay_weeks;
    }

    public String getStay_days() {
        return stay_days;
    }

    public void setStay_days(String stay_days) {
        this.stay_days = stay_days;
    }

    public String getStay_hours() {
        return stay_hours;
    }

    public void setStay_hours(String stay_hours) {
        this.stay_hours = stay_hours;
    }

    public String getProperty_amount() {
        return property_amount;
    }

    public void setProperty_amount(String property_amount) {
        this.property_amount = property_amount;
    }

    public String getTax_percentage() {
        return tax_percentage;
    }

    public void setTax_percentage(String tax_percentage) {
        this.tax_percentage = tax_percentage;
    }

    public String getPropertyimg() {
        return propertyimg;
    }

    public void setPropertyimg(String propertyimg) {
        this.propertyimg = propertyimg;
    }

    public String getPropertyname() {
        return propertyname;
    }

    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }

    public String getPropertylocation() {
        return propertylocation;
    }

    public void setPropertylocation(String propertylocation) {
        this.propertylocation = propertylocation;
    }

    public String getReviewrating() {
        return reviewrating;
    }

    public void setReviewrating(String reviewrating) {
        this.reviewrating = reviewrating;
    }

    public String getCheckindate() {
        return checkindate;
    }

    public void setCheckindate(String checkindate) {
        this.checkindate = checkindate;
    }

    public String getCheckintime() {
        return checkintime;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }

    public String getCheckoutdate() {
        return checkoutdate;
    }

    public void setCheckoutdate(String checkoutdate) {
        this.checkoutdate = checkoutdate;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public void setCheckouttime(String checkouttime) {
        this.checkouttime = checkouttime;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(String guestCount) {
        this.guestCount = guestCount;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getDailyPrice() {
        return DailyPrice;
    }

    public void setDailyPrice(String dailyPrice) {
        DailyPrice = dailyPrice;
    }

    public String getWeeklyPrice() {
        return WeeklyPrice;
    }

    public void setWeeklyPrice(String weeklyPrice) {
        WeeklyPrice = weeklyPrice;
    }

    public String getMonthlyPrice() {
        return MonthlyPrice;
    }

    public void setMonthlyPrice(String monthlyPrice) {
        MonthlyPrice = monthlyPrice;
    }

    public String getCleaningFee() {
        return CleaningFee;
    }

    public void setCleaningFee(String cleaningFee) {
        CleaningFee = cleaningFee;
    }

    public String getExtraServiceCharge() {
        return ExtraServiceCharge;
    }

    public void setExtraServiceCharge(String extraServiceCharge) {
        ExtraServiceCharge = extraServiceCharge;
    }

    public String getServiceTax() {
        return ServiceTax;
    }

    public void setServiceTax(String serviceTax) {
        ServiceTax = serviceTax;
    }

    @Override
    public String toString() {
        return "ReviewRequestModel{" +
                "propertyimg='" + propertyimg + '\'' +
                ", propertyname='" + propertyname + '\'' +
                ", propertylocation='" + propertylocation + '\'' +
                ", reviewrating='" + reviewrating + '\'' +
                ", checkindate='" + checkindate + '\'' +
                ", checkintime='" + checkintime + '\'' +
                ", checkoutdate='" + checkoutdate + '\'' +
                ", checkouttime='" + checkouttime + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", guestCount='" + guestCount + '\'' +
                ", TotalPrice='" + TotalPrice + '\'' +
                ", DailyPrice='" + DailyPrice + '\'' +
                ", WeeklyPrice='" + WeeklyPrice + '\'' +
                ", MonthlyPrice='" + MonthlyPrice + '\'' +
                ", CleaningFee='" + CleaningFee + '\'' +
                ", ExtraServiceCharge='" + ExtraServiceCharge + '\'' +
                ", ServiceTax='" + ServiceTax + '\'' +
                '}';
    }
}
