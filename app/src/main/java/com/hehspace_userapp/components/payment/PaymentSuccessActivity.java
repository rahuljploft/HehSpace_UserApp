package com.hehspace_userapp.components.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.hehspace_userapp.MainActivity;
import com.hehspace_userapp.R;
import com.hehspace_userapp.databinding.ActivityPaymentSuccessBinding;
import com.hehspace_userapp.ui.base.BaseBindingActivity;

public class PaymentSuccessActivity extends BaseBindingActivity {

    ActivityPaymentSuccessBinding activityPaymentSuccessBinding;

    @Override
    protected void setBinding() {
    activityPaymentSuccessBinding = DataBindingUtil.setContentView(this,R.layout.activity_payment_success);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
    activityPaymentSuccessBinding.layoutDone.setOnClickListener(v -> {startActivity(new Intent(this, MainActivity.class));});
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}