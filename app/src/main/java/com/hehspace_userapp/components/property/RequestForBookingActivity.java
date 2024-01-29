package com.hehspace_userapp.components.property;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.sidemenu.MyRequestActivity;
import com.hehspace_userapp.databinding.ActivityRequestForBookingBinding;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.PropertyDetailModel;
import com.hehspace_userapp.model.ReviewRequestModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.Arrays;
import java.util.HashMap;

public class RequestForBookingActivity extends BaseBindingActivity {

    ActivityRequestForBookingBinding activityRequestForBookingBinding;
    BookingRequestViewModel bookingRequestViewModel;
    ReviewRequestModel reviewRequestModel;

    @Override
    protected void setBinding() {
        activityRequestForBookingBinding = DataBindingUtil.setContentView(this,R.layout.activity_request_for_booking);
        bookingRequestViewModel = new BookingRequestViewModel();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initializeObject() {
        bookingRequestViewModel.livedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
         reviewRequestModel = (ReviewRequestModel)getIntent().getSerializableExtra("reviewRequestModel");
        activityRequestForBookingBinding.tvTitle.setText(reviewRequestModel.getPropertyname());
        activityRequestForBookingBinding.tvLocation.setText(reviewRequestModel.getPropertylocation());
        activityRequestForBookingBinding.tvRating.setText(reviewRequestModel.getReviewrating());
        activityRequestForBookingBinding.tvType.setText(reviewRequestModel.getPropertyType());
        activityRequestForBookingBinding.tvGuestCount.setText(reviewRequestModel.getGuestCount());
        activityRequestForBookingBinding.tvTotalAmount.setText(reviewRequestModel.getTotalPrice());
        activityRequestForBookingBinding.tvGrandTotal.setText(reviewRequestModel.getTotalPrice());
        activityRequestForBookingBinding.tvcheckindatetime.setText(reviewRequestModel.getCheckindate()+", "+reviewRequestModel.getCheckintime());
        activityRequestForBookingBinding.tvcheckoutdatetime.setText(reviewRequestModel.getCheckoutdate()+", "+reviewRequestModel.getCheckouttime());

        Glide.with(this)
                .load((reviewRequestModel.getPropertyimg()))
                .error(R.drawable.logo)
                .into(activityRequestForBookingBinding.ivImage);

        if(!reviewRequestModel.getWeeklyPrice().equals("")){
            activityRequestForBookingBinding.layoutWeekly.setVisibility(View.VISIBLE);
            activityRequestForBookingBinding.tvWeeklyPrice.setText(reviewRequestModel.getWeeklyPrice());
        }
        else activityRequestForBookingBinding.layoutWeekly.setVisibility(View.GONE);
        if(!reviewRequestModel.getDailyPrice().equals("")){
            activityRequestForBookingBinding.layoutDaily.setVisibility(View.VISIBLE);
            activityRequestForBookingBinding.tvDailyPrice.setText(reviewRequestModel.getDailyPrice());
        }
        else activityRequestForBookingBinding.layoutDaily.setVisibility(View.GONE);
        if(!reviewRequestModel.getHourlyPrice().equals("")){
            activityRequestForBookingBinding.layoutHourly.setVisibility(View.VISIBLE);
            activityRequestForBookingBinding.tvHourlyPrice.setText(reviewRequestModel.getHourlyPrice());
        }
        else activityRequestForBookingBinding.layoutHourly.setVisibility(View.GONE);
       if(!reviewRequestModel.getExtraServiceCharge().equals("")){
            activityRequestForBookingBinding.layoutExtra.setVisibility(View.VISIBLE);
            activityRequestForBookingBinding.tvextraService.setText(reviewRequestModel.getExtraServiceCharge());
        }
       else activityRequestForBookingBinding.layoutExtra.setVisibility(View.GONE);

        activityRequestForBookingBinding.tvCleaningFee.setText(reviewRequestModel.getCleaningFee());
        activityRequestForBookingBinding.tvServiceTax.setText(reviewRequestModel.getServiceTax());
    }

    @Override
    protected void setListeners() {
        activityRequestForBookingBinding.btnSubmit.setOnClickListener(this);
        activityRequestForBookingBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.btnSubmit){
            if (Uitility.isOnline(this)) {
                HashMap<String, String> reqData = new HashMap<>();
                reqData.put("property_id",reviewRequestModel.getPropertyid());
                reqData.put("category_id",reviewRequestModel.getCategoryid());

                reqData.put("first_name",sessionManager.getFNAME());
                reqData.put("last_name",sessionManager.getLNAME());
                reqData.put("email_address",sessionManager.getEMAIL());

                if(sessionManager.getPHONE().equals("")){
                    reqData.put("mobile_number",reviewRequestModel.getPhonenumber());
                }
                else reqData.put("mobile_number",sessionManager.getPHONE());

                reqData.put("number_of_guest",reviewRequestModel.getGuestCount());
                reqData.put("checkin_date",reviewRequestModel.getCheckindate());
                reqData.put("checkin_time",reviewRequestModel.getCheckintime());
                reqData.put("checkout_date",reviewRequestModel.getCheckoutdate());
                reqData.put("checkout_time",reviewRequestModel.getCheckouttime());

                reqData.put("monthly_rate",reviewRequestModel.getMonthly_rate());
                reqData.put("weekly_rate", reviewRequestModel.getWeekly_rate());
                reqData.put("daily_rate",  reviewRequestModel.getDaily_rate());
                reqData.put("hourly_rate", reviewRequestModel.getHourly_rate());

                reqData.put("stay_months",reviewRequestModel.getStay_months());
                reqData.put("stay_weeks",reviewRequestModel.getStay_weeks());
                reqData.put("stay_days",reviewRequestModel.getStay_days());
                reqData.put("stay_hours",reviewRequestModel.getStay_hours());

                reqData.put("monthly_amount",reviewRequestModel.getMonthlyPrice());
                reqData.put("weekly_amount",reviewRequestModel.getWeeklyPrice());
                reqData.put("daily_amount",reviewRequestModel.getDailyPrice());
                reqData.put("hourly_amount",reviewRequestModel.getHourlyPrice());

                reqData.put("property_amount",reviewRequestModel.getProperty_amount());

                reqData.put("services_details","");

                reqData.put("services_amount",reviewRequestModel.getExtraServiceCharge());
                reqData.put("tax_percentage",reviewRequestModel.getTax_percentage());
                reqData.put("tax_amount",reviewRequestModel.getServiceTax());
                reqData.put("total_amount",reviewRequestModel.getTotalPrice());
                reqData.put("cleaner_fee",reviewRequestModel.getCleaningFee());
                bookingRequestViewModel.createRequest(reqData);
            } else {
                Uitility.nointernetDialog(this);
            }

        }
        if (view.getId() == R.id.ivBack){
            onBackPressed();
        }
    }

    private void handleResult(ApiResponse<CommonModel> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if(result.getData().status.equals("true")){
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}