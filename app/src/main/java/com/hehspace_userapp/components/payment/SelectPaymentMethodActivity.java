package com.hehspace_userapp.components.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hehspace_userapp.R;
import com.hehspace_userapp.components.sidemenu.RequestDetailsActivity;
import com.hehspace_userapp.databinding.ActivitySelectPaymentMethodBinding;
import com.hehspace_userapp.databinding.CarddetailsDailogBinding;
import com.hehspace_userapp.databinding.SupportDailogBinding;
import com.hehspace_userapp.model.Bitcoin_payment_model;
import com.hehspace_userapp.model.CommonModel;
import com.hehspace_userapp.model.RequestDetailsModel;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class SelectPaymentMethodActivity extends BaseBindingActivity {

    ActivitySelectPaymentMethodBinding activitySelectPaymentMethodBinding;
    String paymenttype = "";
    SelectPaymentView_Model selectPaymentView_model;
    File id_document = null;

    @Override
    protected void setBinding() {
        activitySelectPaymentMethodBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_payment_method);
        selectPaymentView_model = new SelectPaymentView_Model();
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        selectPaymentView_model.livedata.observe(this, propertyCategoryModelApiResponse -> handleResult(propertyCategoryModelApiResponse));
        selectPaymentView_model.livedatabitcoin.observe(this, propertyCategoryModelApiResponse -> handleResultbitcoin(propertyCategoryModelApiResponse));
        activitySelectPaymentMethodBinding.layoutBitcoin.setOnClickListener(v -> {
            paymenttype = "bitcoin";
            activitySelectPaymentMethodBinding.layoutBitcoin.setBackground(this.getResources().getDrawable(R.drawable.payment_border_green));
            activitySelectPaymentMethodBinding.layoutStripe.setBackground(this.getResources().getDrawable(R.drawable.payment_border_grey));
        });

        activitySelectPaymentMethodBinding.layoutStripe.setOnClickListener(v -> {
            paymenttype = "stripe";
            activitySelectPaymentMethodBinding.layoutStripe.setBackground(this.getResources().getDrawable(R.drawable.payment_border_green));
            activitySelectPaymentMethodBinding.layoutBitcoin.setBackground(this.getResources().getDrawable(R.drawable.payment_border_grey));
        });

        activitySelectPaymentMethodBinding.layoutProceed.setOnClickListener(v -> {
            if (paymenttype.equals("")){
                Toast.makeText(this, "Select Payment Method", Toast.LENGTH_SHORT).show();
            }else {
                if (paymenttype.equals("bitcoin")) {
                    if (Uitility.isOnline(this)) {
                        selectPaymentView_model.createBooking_bitcoin(getIntent().getStringExtra("fname"),getIntent().getStringExtra("lname"),getIntent().getStringExtra("email")
                                ,getIntent().getStringExtra("phone"),getIntent().getStringExtra("bookfor"),getIntent().getStringExtra("reqid"),id_document
                                );
                    } else {
                        Uitility.nointernetDialog(this);
                    }

                } else{
                    getCardDialog();
                }
            }

        });

        activitySelectPaymentMethodBinding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    @Override
    protected void setListeners() {

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
                    if (result.getData().status.equals("true")) {
                        startActivity(new Intent(this,PaymentSuccessActivity.class));
                    } else {
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void handleResultbitcoin(ApiResponse<Bitcoin_payment_model> result) {
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
                    if (result.getData().status.equals("true")) {
                        startActivity(new Intent(SelectPaymentMethodActivity.this,Bitcoin_Payment.class)
                        .putExtra("url",result.getData().data.url)
                        .putExtra("orderid",result.getData().data.chargeCode));
                        //Toast.makeText(this, "This time is not available for booking payment!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getCardDialog() {
        Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        CarddetailsDailogBinding carddetailsDailogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mActivity),
                R.layout.carddetails_dailog,
                null,
                false
        );
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialogBinding.tvTitle.setText(resources.getString(R.string.internet_issue))
//        dialogBinding.tvMessage.setText(resources.getString(R.string.please_check_your_internet))
        carddetailsDailogBinding.layoutProceed.setOnClickListener(v -> {
            if(TextUtils.isEmpty(carddetailsDailogBinding.etCardNo.getText().toString())){
                Toast.makeText(this, "Please Enter Card Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(carddetailsDailogBinding.etMM.getText().toString())){
                Toast.makeText(this, "Please Enter Month", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(carddetailsDailogBinding.etYYYY.getText().toString())){
                Toast.makeText(this, "Please Enter Year", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(carddetailsDailogBinding.etCVV.getText().toString())){
                Toast.makeText(this, "Please Enter CVV", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Uitility.isOnline(this)) {
                selectPaymentView_model.createBooking(getIntent().getStringExtra("fname"),getIntent().getStringExtra("lname"),getIntent().getStringExtra("email")
                ,getIntent().getStringExtra("phone"),getIntent().getStringExtra("bookfor"),getIntent().getStringExtra("reqid"),id_document,
                        carddetailsDailogBinding.etCardNo.getText().toString(),carddetailsDailogBinding.etMM.getText().toString(),carddetailsDailogBinding.etYYYY.getText().toString(),
                        carddetailsDailogBinding.etCVV.getText().toString());
            } else {
                Uitility.nointernetDialog(this);
            }
        });
        carddetailsDailogBinding.ivClose.setOnClickListener(v ->
                dialog.dismiss());
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(carddetailsDailogBinding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


}