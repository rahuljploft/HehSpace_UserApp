package com.hehspace_userapp.components.property;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.payment.SelectPaymentMethodActivity;
import com.hehspace_userapp.databinding.ActivityBookNowBinding;
import com.hehspace_userapp.model.ReviewRequestModel;
import com.hehspace_userapp.ui.base.BaseBindingActivity;

public class BookNowActivity extends BaseBindingActivity {

    ActivityBookNowBinding activityBookNowBinding;
    String book_for="SELF";
    String reqid="";
    ReviewRequestModel reviewRequestModel;
    @Override
    protected void setBinding() {
        activityBookNowBinding = DataBindingUtil.setContentView(this,R.layout.activity_book_now);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
    mActivity = this;
    }

    @Override
    protected void initializeObject() {
        reviewRequestModel = (ReviewRequestModel)getIntent().getSerializableExtra("reviewRequestModel");
        reqid = getIntent().getStringExtra("reqid");

        activityBookNowBinding.cbOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                activityBookNowBinding.layoutOther.setVisibility(View.VISIBLE);
                activityBookNowBinding.cbSelf.setChecked(false);
                book_for = "OTHER";
            }
        });

        activityBookNowBinding.cbSelf.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                activityBookNowBinding.layoutOther.setVisibility(View.GONE);
                activityBookNowBinding.cbOther.setChecked(false);
                book_for = "SELF";
            }
        });
        activityBookNowBinding.tvTitle.setText(reviewRequestModel.getPropertyname());
        activityBookNowBinding.tvLocation.setText(reviewRequestModel.getPropertylocation());
        activityBookNowBinding.tvRating.setText(reviewRequestModel.getReviewrating());
        activityBookNowBinding.tvType.setText(reviewRequestModel.getPropertyType());
        activityBookNowBinding.tvGuestCount.setText(reviewRequestModel.getGuestCount());
        activityBookNowBinding.tvTotalAmount.setText(reviewRequestModel.getTotalPrice());
        activityBookNowBinding.tvGrandTotal.setText(reviewRequestModel.getTotalPrice());
        activityBookNowBinding.tvcheckindatetime.setText(reviewRequestModel.getCheckindate()+" ,"+reviewRequestModel.getCheckintime());
        activityBookNowBinding.tvcheckoutdatetime.setText(reviewRequestModel.getCheckoutdate()+" ,"+reviewRequestModel.getCheckouttime());

        Glide.with(this)
                .load((reviewRequestModel.getPropertyimg()))
                .error(R.drawable.logo)
                .into(activityBookNowBinding.ivImage);

        if(!reviewRequestModel.getWeeklyPrice().equals("")){
            activityBookNowBinding.layoutWeekly.setVisibility(View.VISIBLE);
            activityBookNowBinding.tvWeeklyPrice.setText(reviewRequestModel.getWeeklyPrice());
        }
        else activityBookNowBinding.layoutWeekly.setVisibility(View.GONE);
        if(!reviewRequestModel.getDailyPrice().equals("")){
            activityBookNowBinding.layoutDaily.setVisibility(View.VISIBLE);
            activityBookNowBinding.tvDailyPrice.setText(reviewRequestModel.getDailyPrice());
        }
        else activityBookNowBinding.layoutDaily.setVisibility(View.GONE);
        if(!reviewRequestModel.getHourlyPrice().equals("")){
            activityBookNowBinding.layoutHourly.setVisibility(View.VISIBLE);
            activityBookNowBinding.tvHourlyPrice.setText(reviewRequestModel.getHourlyPrice());
        }
        else activityBookNowBinding.layoutHourly.setVisibility(View.GONE);
        if(!reviewRequestModel.getExtraServiceCharge().equals("")){
            activityBookNowBinding.layoutExtra.setVisibility(View.VISIBLE);
            activityBookNowBinding.tvextraService.setText(reviewRequestModel.getExtraServiceCharge());
        }
        else activityBookNowBinding.layoutExtra.setVisibility(View.GONE);

        activityBookNowBinding.tvCleaningFee.setText(reviewRequestModel.getCleaningFee());
        activityBookNowBinding.tvServiceTax.setText(reviewRequestModel.getServiceTax());

        activityBookNowBinding.ivBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void setListeners() {
        activityBookNowBinding.layoutProceed.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.layoutProceed){
            if(book_for.equals("OTHER")){
                if(TextUtils.isEmpty(activityBookNowBinding.etFirstName.getText().toString())){
                    Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(activityBookNowBinding.etLastName.getText().toString())){
                    Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(activityBookNowBinding.etEmail.getText().toString())){
                    Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(activityBookNowBinding.etPhone.getText().toString())){
                    Toast.makeText(this, "Please Enter Phone", Toast.LENGTH_SHORT).show();
                    return;
                }
               startActivity(new Intent(mActivity, SelectPaymentMethodActivity.class)
               .putExtra("bookfor",book_for)
               .putExtra("reqid",reqid)
               .putExtra("fname",activityBookNowBinding.etFirstName.getText().toString())
               .putExtra("lname",activityBookNowBinding.etLastName.getText().toString())
               .putExtra("email",activityBookNowBinding.etEmail.getText().toString())
               .putExtra("phone",activityBookNowBinding.etPhone.getText().toString())
               );
            }
            else {
                if(sessionManager.getPHONE().equals("")){
                    Toast.makeText(mActivity, "Please update Mobile Number in profile section to proceed.", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(mActivity, SelectPaymentMethodActivity.class)
                        .putExtra("bookfor",book_for)
                        .putExtra("reqid",reqid)
                        .putExtra("fname",sessionManager.getFNAME())
                        .putExtra("lname",sessionManager.getLNAME())
                        .putExtra("email",sessionManager.getEMAIL())
                        .putExtra("phone",sessionManager.getPHONE())
                );

            }
        }
    }
}