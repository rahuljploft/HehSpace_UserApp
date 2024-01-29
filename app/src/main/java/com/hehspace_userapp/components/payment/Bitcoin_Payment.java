package com.hehspace_userapp.components.payment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.hehspace_userapp.R;
import com.hehspace_userapp.databinding.ActivityBitcoinPaymentBinding;
import com.hehspace_userapp.databinding.BitcoinBackAlertDailogBinding;
import com.hehspace_userapp.databinding.BitcoinPaymentFailDailogBinding;
import com.hehspace_userapp.model.Bitcoin_Payment_Check_Model;
import com.hehspace_userapp.model.Bitcoin_payment_model;
import com.hehspace_userapp.network.ApiResponse;
import com.hehspace_userapp.ui.base.BaseBindingActivity;
import com.hehspace_userapp.util.Constant;
import com.hehspace_userapp.util.ProgressDialog;
import com.hehspace_userapp.util.Uitility;

import java.util.HashMap;

public class Bitcoin_Payment extends BaseBindingActivity {

    ActivityBitcoinPaymentBinding activityBitcoinPaymentBinding;
    SelectPaymentView_Model selectPaymentView_model;
    @Override
    protected void setBinding() {
        activityBitcoinPaymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_bitcoin_payment);
        selectPaymentView_model = new SelectPaymentView_Model();
        initView();

    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        selectPaymentView_model.livedatabitcoinsuccess.observe(this, propertyCategoryModelApiResponse -> handleResultbitcoincheckpayment(propertyCategoryModelApiResponse));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 20 seconds
                handler.postDelayed(this, 10000);
                if (Uitility.isOnline(Bitcoin_Payment.this)) {
                    HashMap<String, String> reqData = new HashMap<>();
                    reqData.put("charge_code", getIntent().getStringExtra("orderid"));
                    selectPaymentView_model.checkpayment(reqData);
                } else {
                    Uitility.nointernetDialog(Bitcoin_Payment.this);
                }
            }
        }, 20000);  //the time is in miliseconds
    }

    @Override
    protected void setListeners() {

    }
    private void handleResultbitcoincheckpayment(ApiResponse<Bitcoin_Payment_Check_Model> result) {
        switch (result.getStatus()) {
            case ERROR:
               // ProgressDialog.hideProgressDialog();
                Log.e("qwerty", "error - " + result.getError().getMessage());
                break;
            case LOADING:
               // ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
               // ProgressDialog.hideProgressDialog();
                if (result.getData().responsecode.equals("200")) {
                    if (result.getData().status.equals("true")) {
                      if (result.getData().message.equals("BOOKING CONFIRMED SUCCESSFULLY")){
                          startActivity(new Intent(this,PaymentSuccessActivity.class));
                      }else {
                          getAlertdialogpaymentcancel();
                      }
                    }
                    else {
                        Toast.makeText(this, result.getData().message, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
//                    if (Uitility.isOnline(this)) {
//                        HashMap<String, String> reqData = new HashMap<>();
//                        reqData.put("charge_code", getIntent().getStringExtra("orderid"));
//                        selectPaymentView_model.checkpayment(reqData);
//                    } else {
//                        Uitility.nointernetDialog(this);
//                    }
                }
                break;
        }
    }
    public void initView() {
        activityBitcoinPaymentBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String uas = "AppleWebKit/537.36" + "(KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
        activityBitcoinPaymentBinding.webview.getSettings().setBlockNetworkLoads(false);
        activityBitcoinPaymentBinding.webview.getSettings().setAppCacheEnabled(true);
        activityBitcoinPaymentBinding.webview.getSettings().setJavaScriptEnabled(true);
        activityBitcoinPaymentBinding.webview.getSettings().setAllowContentAccess(true);
        activityBitcoinPaymentBinding.webview.getSettings().setAllowFileAccess(true);
        activityBitcoinPaymentBinding.webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        activityBitcoinPaymentBinding.webview.getSettings().setGeolocationEnabled(true);
        activityBitcoinPaymentBinding.webview.getSettings().setUserAgentString(uas);
        activityBitcoinPaymentBinding.webview.getSettings().setAllowContentAccess(true);
        activityBitcoinPaymentBinding.webview.getSettings().setDatabaseEnabled(true);
        activityBitcoinPaymentBinding.webview.getSettings().setLoadsImagesAutomatically(true);
        activityBitcoinPaymentBinding.webview.getSettings().setLoadWithOverviewMode(true);
        activityBitcoinPaymentBinding.webview.getSettings().setUseWideViewPort(true);
        activityBitcoinPaymentBinding.webview.getSettings().setDatabaseEnabled(true);
        activityBitcoinPaymentBinding.webview.getSettings().setDomStorageEnabled(true);
        activityBitcoinPaymentBinding.webview.loadUrl(getIntent().getStringExtra("url"));

    }

    @Override
    public void onBackPressed() {
        getAlertdialog();
    }

    private void getAlertdialog() {
        Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        BitcoinBackAlertDailogBinding carddetailsDailogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mActivity),
                R.layout.bitcoin_back_alert_dailog,
                null,
                false
        );
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        carddetailsDailogBinding.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        carddetailsDailogBinding.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
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

    private void getAlertdialogpaymentcancel() {
        Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        BitcoinPaymentFailDailogBinding carddetailsDailogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mActivity),
                R.layout.bitcoin_payment_fail_dailog,
                null,
                false
        );
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        carddetailsDailogBinding.layoutDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
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